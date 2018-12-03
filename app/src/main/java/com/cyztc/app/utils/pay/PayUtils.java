package com.cyztc.app.utils.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cyztc.app.CyApplication;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.pay.alipay.PayResult;
import com.cyztc.app.utils.pay.alipay.SignUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by ywl on 2017/1/5.
 */

public class PayUtils {


    // 商户PID
    public static final String PARTNER = "2088021705683631";
    // 商户收款账号
    public static final String SELLER = "2097680497@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMbjSNnFDWKsvXMupgc/K78NCpGfRxuA2moeWoG7WQAvmvcOzLtoOS6audyDQ7QLGRX4LYykCk+jVSK0Mk6KxFjm4gL7sMfxfBybMdK+FAvPcuzDKR/cS1BOxwOHFlMrXvhyLAET6TRI8Qnlry+wbqmBr5myL7f49mQplaNqtn9NAgMBAAECgYAp7AKRFH4LzIlqW+zXHjOkY6VUqJQecfyX3aoKKmlKJr6tUMtKvV9AOZDvsCN3Mdepz9QaDwG1Y6IwXHc2Iv3buAfI6Xq2aAFIeuimfPz8I33ZoI9xDWc0CNqhbl1XCvl4j6enVWBG1Z/FWnENYkGMw7WPQla6hTLEPzC5b+ynaQJBAPFb34StfwvG9tP8pxJ+cYVmo+ogr017RhMZGr6h4q/WnqwUQQ4dAzLOr1dwREQ4JlKCNvL3B8VNf7Yyh+jqJgMCQQDS893720uD+dreWgXyKelM8G3K90lKpcKy5sKNP+TR9Lvl47svbXPgmk+xJwREr4HKIp6IZ3vn85YZOtgv4qxvAkEAlTvjRpwn89ZAA+8yNoMsGfnO3BL9kYDpYSGiocBi86pWTbvwRZO2yJF3ZS8rZqr5NwcAhtYrXdl/X956no84lQJAAR+VVgsD6u8AsGBwWarJeERrgUIx3LjnQYajEJd/g3K3MXE2BPmjPVX1CK95gVgh686lY3qRmVmdugKrNg4R9wJADCRrZvv9o3OrHgTFSeTss9/BdkyUZD+IPZNle6G7xJG1XNL/ESPPcxTrafKLdOd5HlJu78RbSd5kZxpH7Xt3Tg==";

    private static final int SDK_PAY_FLAG = 1;

    public static void alipay(final Activity activity, String name, String desrc, String amount, String orderId)
    {

        String orderInfo = getOrderInfo(name, desrc, amount, orderId);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
                        {
                            CyApplication.getInstance().getOnPayListener().onPayResult(0, "支付成功");
                        }
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
                            {
                                CyApplication.getInstance().getOnPayListener().onPayResult(-1, "支付结果确认中");
                            }

                        }
                        else if (TextUtils.equals(resultStatus, "6001")) {
                            if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
                            {
                                CyApplication.getInstance().getOnPayListener().onPayResult(-2, "操作已取消");
                            }

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            if(CyApplication.getInstance() != null && CyApplication.getInstance().getOnPayListener() != null)
                            {
                                CyApplication.getInstance().getOnPayListener().onPayResult(-2, "支付失败");
                            }
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * create the order info. 创建订单信息
     *
     */
    private static String getOrderInfo(String subject, String body, String price, String orderId) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + HttpMethod.BASE_URL + "alipayNotify.do" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }

//    /**
//     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
//     *
//     */
//    private static String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
//        return key;
//    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * 微信支付
     * @param context
     * @param wxOrderBean
     */
    public static void wxPay(Context context, WxOrderBean wxOrderBean)
    {
        IWXAPI api = WXAPIFactory.createWXAPI(context, wxOrderBean.getAppid());
        String nonceStr = CommonUtil.genNonceStr();
        String timeStamp = String.valueOf(CommonUtil.genTimeStamp());

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", wxOrderBean.getAppid());
        parameters.put("partnerid", wxOrderBean.getPartnerid());
        parameters.put("prepayid", wxOrderBean.getPrepayid());
        parameters.put("package", "Sign=WXPay");
        parameters.put("noncestr", nonceStr);
        parameters.put("timestamp", timeStamp);

        PayReq request = new PayReq();
        request.appId = wxOrderBean.getAppid();
        request.partnerId = wxOrderBean.getPartnerid();
        request.prepayId = wxOrderBean.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = wxOrderBean.getNoncestr();
        request.timeStamp = wxOrderBean.getTimestamp();
//        request.sign = CommonUtil.createSign("UTF-8", parameters, "");
        request.sign = wxOrderBean.getSign();
        api.sendReq(request);
    }

}

package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CheckPayBean;
import com.cyztc.app.bean.TrainingInfoBean;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.dialog.PayDialog;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.pay.PayUtils;
import com.google.gson.Gson;

import butterknife.OnClick;

/**
 * Created by ywl on 2017/1/9.
 */

public class TrainMainInfoActivity extends BaseActivity{

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_name)
    TextView tvNmae;
    @BindView(R.id.tv_cardnum)
    TextView tvCardNum;
    @BindView(R.id.tv_phonenum)
    TextView tvPhoneNum;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_corse)
    TextView tvCorse;
    @BindView(R.id.tv_pay)
    TextView tvPay;

    private String orderId;
    private int pType = -1;
    private CheckPayBean checkPayBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_traininfo_layout);
        setBackView();
        setTitle("主课程报名");
//        setTextMenuView("报名");
        //initDatas(CyApplication.getInstance().getTrainingInfoBean());
        showDataLoadMsg("数据加载中");
        checkPay(CyApplication.getInstance().getUserBean().getStudentId());
        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(new CyApplication.OnPayListener() {
                @Override
                public void onPayResult(int code, String msg) {
                    if(code == 0)
                    {
                        if(pType == 1)//支付宝
                        {
                            UserApi.getInstance().payForTrain(orderId, 1, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener() {
                                @Override
                                public void onSucceed(Object data) {
                                    showToast("报名成功");
                                    CyApplication.getInstance().getUserBean().setIsAmount(1);
                                    TrainMainInfoActivity.this.finish();
                                }

                                @Override
                                public void onError(int code, String msg) {
                                    showToast(msg);
                                }
                            }, TrainMainInfoActivity.this));
                        }
                        else
                        {
                            showToast("报名成功");
                            CyApplication.getInstance().getUserBean().setIsAmount(1);
                            TrainMainInfoActivity.this.finish();
                        }
                    }
                    else
                    {
                        showToast(msg);
                    }
                }
            });
        }
    }

    public void initDatas(CheckPayBean checkPayBean)
    {
        tvTime.setText(checkPayBean.getStartDate() + " 到 " + checkPayBean.getEndDate());
        tvNmae.setText("姓名：" + checkPayBean.getStudentName());
        tvCardNum.setText("身份证：" + checkPayBean.getIdCard());
        tvPhoneNum.setText("电话号码：" + checkPayBean.getPhoneNum());
        tvCost.setText("费用：" + checkPayBean.getTrainingCost());
        tvCorse.setText(checkPayBean.getTrainingInfoName());
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_pay)
    public void onClickPay(View view)
    {
        if(checkPayBean.getTrainingCost() == 0)
        {
            UserApi.getInstance().createTrainOrder(CyApplication.getInstance().getUserBean().getAccountId(), CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), 1, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
                @Override
                public void onSucceed(String data) {
                    orderId = data;
                    tvPay.setText("付款");
                    hideLoadDialog();
                    UserApi.getInstance().payForTrain(orderId, 1, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener() {
                        @Override
                        public void onSucceed(Object data) {
                            showToast("报名成功");
                            CyApplication.getInstance().getUserBean().setIsAmount(1);
                            TrainMainInfoActivity.this.finish();
                        }

                        @Override
                        public void onError(int code, String msg) {
                            showToast(msg);
                        }
                    }, TrainMainInfoActivity.this));
                }

                @Override
                public void onError(int code, String msg) {
                    hideLoadDialog();
                    showToast(msg);
                }
            }, this));
        }
        else {
            if (TextUtils.isEmpty(orderId)) {
                showLoadDialog("创建订单");
                UserApi.getInstance().createTrainOrder(CyApplication.getInstance().getUserBean().getAccountId(), CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), 1, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        orderId = data;
                        tvPay.setText("付款");
                        hideLoadDialog();
                        showPayDialot();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        hideLoadDialog();
                        showToast(msg);
                    }
                }, this));
            } else {
                showPayDialot();
            }
        }
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, TrainMainInfoActivity.class);
        context.startActivity(intent);
    }

    public void showPayDialot()
    {
        Toast.makeText(this, "暂不能支付", Toast.LENGTH_SHORT).show();
        /* add by jjyuan
        final PayDialog payDialog = new PayDialog(TrainMainInfoActivity.this);
        Window window = payDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        payDialog.show();
        payDialog.setPaylistener(new PayDialog.PayListener() {
            @Override
            public void onPay(int paytype) {
                if(paytype == 1)//支付宝
                {
                    pType = 1;
                    String amount = CommonUtil.forMatPrice(checkPayBean.getTrainingCost());
                    PayUtils.alipay(TrainMainInfoActivity.this, "主课报名", "主课报名", amount, orderId);
                }
                else if(paytype == 2)//微信
                {
                    pType = 2;
                    payOrder(orderId);
                }
            }
        });
        */
    }

    public void payOrder(String orderId)
    {
        UserApi.getInstance().payForTrain(orderId, 2, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
            @Override
            public void onSucceed(WxOrderBean data) {
                if(data != null)
                {
                    MyLog.d(data);
                    PayUtils.wxPay(TrainMainInfoActivity.this, data);
                }
                else
                {
                    showToast("创建订单失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
            }
        }, this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(null);
        }
    }

    public void checkPay(String studentId)
    {
        UserApi.getInstance().checkPay(studentId, new HttpSubscriber<CheckPayBean>(new SubscriberOnListener<CheckPayBean>() {
            @Override
            public void onSucceed(CheckPayBean data) {
                checkPayBean = data;
                if(checkPayBean != null)
                {
                    initDatas(checkPayBean);
                }
                hideDataLoadMsg();
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadMsg(msg);
            }
        }, this));
    }
}

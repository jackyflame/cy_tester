package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.cyztc.app.bean.TrainEnlistBean;
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
 * Created by ywl on 2016/12/18.
 */

public class PayForActivity extends BaseActivity{

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
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_pay)
    TextView tvPay;


    private TrainEnlistBean trainEnlistBean;
    private int pType = -1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trainEnlistBean = (TrainEnlistBean) getIntent().getExtras().get("trainenlistbean");
        setContentView(R.layout.activity_payfor_layout);
        setTitle("在线报名");
        setBackView();
        initDatas(trainEnlistBean);
//        showDataLoadMsg("数据加载中");
//        getCheckPayinfo(CyApplication.getInstance().getUserBean().getStudentId());
        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(new CyApplication.OnPayListener() {
                @Override
                public void onPayResult(int code, String msg) {
                    if(code == 0)
                    {
                        if(pType == 1)//支付宝
                        {
                            showLoadDialog("支付中");
                            UserApi.getInstance().payForTrain(trainEnlistBean.getOrderId(), 1, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener() {
                                @Override
                                public void onSucceed(Object data) {
                                    showToast("报名成功");
                                    if(trainEnlistBean.getIsMain() != 0) {
                                        CyApplication.getInstance().getUserBean().setIsAmount(1);
                                    }
                                    PayForActivity.this.finish();
                                    hideLoadDialog();
                                }

                                @Override
                                public void onError(int code, String msg) {
                                    showToast(msg);
                                    hideLoadDialog();
                                }
                            }, PayForActivity.this));
                        }
                        else
                        {
                            hideLoadDialog();
                            showToast("报名成功");
                            if(trainEnlistBean.getIsMain() != 0) {
                                CyApplication.getInstance().getUserBean().setIsAmount(1);
                            }
                            PayForActivity.this.finish();
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

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_pay)
    public void onClickPay(View view)
    {
        if(trainEnlistBean.getCost() == 0)
        {
            showLoadDialog("报名中");
            UserApi.getInstance().createTrainOrder(CyApplication.getInstance().getUserBean().getAccountId(), trainEnlistBean.getId(), 1, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
                @Override
                public void onSucceed(String data) {
                    trainEnlistBean.setOrderId(data);
                    if (trainEnlistBean.getCost() == 0) {
                        UserApi.getInstance().payForTrain(data, 1, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener() {
                            @Override
                            public void onSucceed(Object data) {
                                showToast("报名成功");
                                if(trainEnlistBean.getIsMain() != 0) {
                                    CyApplication.getInstance().getUserBean().setIsAmount(1);
                                }
                                hideLoadDialog();
                                PayForActivity.this.finish();
                            }

                            @Override
                            public void onError(int code, String msg) {
                                showToast(msg);
                                hideLoadDialog();
                            }
                        }, PayForActivity.this));
                    } else {
                        payDialog();
                        hideLoadDialog();
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    hideLoadDialog();
                    showToast(msg);
                }
            }, this));
        }
        else {
            if (trainEnlistBean.getIsEntry() == 0 || "未报名".equals(trainEnlistBean.getPayStatus()))//未报名
            {
                showLoadDialog("报名中");
                UserApi.getInstance().createTrainOrder(CyApplication.getInstance().getUserBean().getAccountId(), trainEnlistBean.getId(), 1, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        hideLoadDialog();
                        trainEnlistBean.setOrderId(data);
                        if (trainEnlistBean.getCost() == 0) {
                            showToast("报名成功");
                            if(trainEnlistBean.getIsMain() != 0) {
                                CyApplication.getInstance().getUserBean().setIsAmount(1);
                            }
                        } else {
                            payDialog();
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        hideLoadDialog();
                        showToast(msg);
                    }
                }, this));
            } else {
                payDialog();
            }
        }
    }

    public void payDialog()
    {
        Toast.makeText(this, "暂不能支付", Toast.LENGTH_SHORT).show();
/* add by jjyuan
        PayDialog payDialog = new PayDialog(this);
        Window window = payDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        payDialog.show();
        payDialog.setPaylistener(new PayDialog.PayListener() {
            @Override
            public void onPay(int paytype) {
                if(paytype == 1)//支付宝
                {
                    pType = paytype;
                    PayUtils.alipay(PayForActivity.this, trainEnlistBean.getClassName(), trainEnlistBean.getRemark() + "-课程报名", CommonUtil.forMatPrice(trainEnlistBean.getCost()), trainEnlistBean.getOrderId());
//                    UserApi.getInstance().createTrainOrder(CyApplication.getInstance().getUserBean().getAccountId(), trainEnlistBean.getId(), 1, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
//                        @Override
//                        public void onSucceed(String data) {
//                            hideLoadDialog();
//                            trainEnlistBean.setOrderId(data);
//                            MyLog.d("cost:" + CommonUtil.forMatPrice(trainEnlistBean.getCost()));
//                            PayUtils.alipay(PayForActivity.this, trainEnlistBean.getClassName(), trainEnlistBean.getRemark(), CommonUtil.forMatPrice(trainEnlistBean.getCost()), trainEnlistBean.getOrderId());
//                        }
//
//                        @Override
//                        public void onError(int code, String msg) {
//                            hideLoadDialog();
//                            showToast(msg);
//                        }
//                    }, PayForActivity.this));
                }
                else if(paytype == 2)//微信
                {
                    pType = paytype;
                    payOrder(trainEnlistBean.getOrderId());
//                    UserApi.getInstance().createTrainOrder(CyApplication.getInstance().getUserBean().getAccountId(), trainEnlistBean.getId(), 2, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
//                        @Override
//                        public void onSucceed(String data) {
//                            hideLoadDialog();
//                            trainEnlistBean.setOrderId(data);
//                            payOrder(trainEnlistBean.getOrderId());
////                            MyLog.d("cost:" + CommonUtil.forMatPrice(trainEnlistBean.getCost()));
////                            PayUtils.alipay(PayForActivity.this, trainEnlistBean.getClassName(), trainEnlistBean.getRemark(), CommonUtil.forMatPrice(trainEnlistBean.getCost()), trainEnlistBean.getOrderId());
//                        }
//
//                        @Override
//                        public void onError(int code, String msg) {
//                            hideLoadDialog();
//                            showToast(msg);
//                        }
//                    }, PayForActivity.this));
                }
            }
        });
        */
    }


    public static void startActivity(Context context, TrainEnlistBean trainEnlistBean)
    {
        Intent intent = new Intent(context, PayForActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("trainenlistbean", trainEnlistBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public void initDatas(TrainEnlistBean trainEnlistBean)
    {
        tvTime.setText(trainEnlistBean.getStartDate() + " 到 " + trainEnlistBean.getEndDate());
        tvNmae.setText("姓名：" + CyApplication.getInstance().getUserBean().getStudentName());
        tvCardNum.setText("身份证：" + CyApplication.getInstance().getUserBean().getIdCard());
        tvPhoneNum.setText("电话号码：" + CyApplication.getInstance().getUserBean().getPhone());
        if(trainEnlistBean.getCost() == 0)
        {
            tvCost.setText("费用：免费");
        }
        else {
            tvCost.setText("费用：" + trainEnlistBean.getCost());
        }
        tvDetail.setText(trainEnlistBean.getRemark());
        if(trainEnlistBean.getIsEntry() == 0 || "未报名".equals(trainEnlistBean.getPayStatus()))//未报名
        {
            tvPay.setVisibility(View.VISIBLE);
            tvPay.setText("报名");
        }
        else
        {
            if("支付完成".equals(trainEnlistBean.getPayStatus()))
            {
                tvPay.setVisibility(View.INVISIBLE);
            }
            else
            {
                tvPay.setVisibility(View.VISIBLE);
                tvPay.setText("付款");
            }
        }
    }

//    @OnClick(R.id.ly_wx_pay)
//    public void onClickWxPay(View view)
//    {
//        UserApi.getInstance().payForTrain(trainEnlistBean.getOrderId(), 2, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
//            @Override
//            public void onSucceed(WxOrderBean data) {
//
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//
//            }
//        }, this));
//    }

    public void payOrder(String orderId)
    {
        UserApi.getInstance().payForTrain(orderId, 2, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
            @Override
            public void onSucceed(WxOrderBean data) {
                if(data != null)
                {
                    MyLog.d(data);
                    PayUtils.wxPay(PayForActivity.this, data);
                }
                else
                {
                    showToast("创建订单失败");
                }
                hideLoadDialog();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
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

}

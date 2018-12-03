package com.cyztc.app.views.home.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.EventBusMsgBean;
import com.cyztc.app.bean.OrderBean;
import com.cyztc.app.bean.OrderDetailBean;
import com.cyztc.app.bean.OrderDetailGoodsBean;
import com.cyztc.app.bean.OrderDetailGoodsBeanP;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.dialog.PayDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.pay.PayUtils;
import com.cyztc.app.widget.SquareImageView;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/30.
 */

public class OrderDetailActivity extends BaseActivity{

    @BindView(R.id.siv_img)
    SquareImageView sivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rl_actions)
    RelativeLayout rlactions;
    @BindView(R.id.tv_totalprice)
    TextView tvTotalPrice;
    @BindView(R.id.tv_pay_del)
    TextView tvPay;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private OrderBean orderBean;
    private int pType = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        orderBean = (OrderBean) getIntent().getExtras().get("orderbean");
        setContentView(R.layout.activity_order_detail_layout);
        setBackView();
        setTitle("订单详情");
//        setTextMenuView("删除");
        if(TextUtils.isEmpty(orderBean.getPicture()))
        {
            sivImg.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        }
        else {
            ImageLoad.getInstance().displayImage(OrderDetailActivity.this, sivImg, HttpMethod.IMG_URL + orderBean.getPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        }
        tvName.setText(orderBean.getRemark());
        tvAmount.setText("¥" + orderBean.getCost());
//        tvCount.setText(orderBean.getc);
        if(orderBean.getStatus() == 1)
        {
            rlactions.setVisibility(View.VISIBLE);
            tvTotalPrice.setText("合计：" + orderBean.getCost());
        }

        if(orderBean.getStatus() == 1)
        {
            tvPay.setText("支付");
        }
        else if(orderBean.getStatus() == 2)
        {
            tvPay.setText("已取消");
        }
        else if(orderBean.getStatus() == 3)
        {
            tvPay.setText("已支付");
        }
        else if(orderBean.getStatus() == 4)
        {
            tvPay.setText("正在送货");
        }
        else if(orderBean.getStatus() == 8)
        {
            tvPay.setText("交易完成");
        }
        else
        {
            tvPay.setText("支付");
        }

        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(new CyApplication.OnPayListener() {
                @Override
                public void onPayResult(int code, String msg) {
                    if(code == 0)
                    {
                        if(pType == 1)
                        {
                            UserApi.getInstance().payForGoods(orderBean.getId(), CyApplication.getInstance().getUserBean().getStudentId(), 1, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
                                @Override
                                public void onSucceed(WxOrderBean data) {
                                    showToast("支付成功");
                                    EventBusMsgBean eventBusMsgBean = new EventBusMsgBean(1, true);
                                    EventBus.getDefault().post(eventBusMsgBean);
                                }

                                @Override
                                public void onError(int code, String msg) {
                                    showToast(msg);
                                }
                            }, OrderDetailActivity.this));
                        }
                        else {
                            showToast(msg);
                            EventBusMsgBean eventBusMsgBean = new EventBusMsgBean(1, true);
                            EventBus.getDefault().post(eventBusMsgBean);
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

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        showToast("删除");
    }

    @OnClick(R.id.tv_pay_del)
    public void onClickPay(View view)
    {
        if(orderBean.getStatus() == 1) {
            showPayDialog();
        }
    }

    public static void startActivity(Context context, OrderBean orderBean)
    {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderbean", orderBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void showPayDialog()
    {
        Toast.makeText(this, "暂不能支付", Toast.LENGTH_SHORT).show();
        /* add by jjyuan
        final PayDialog payDialog = new PayDialog(this);
        Window window = payDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        payDialog.show();
        payDialog.setShowOffline();
        payDialog.setPaylistener(new PayDialog.PayListener() {
            @Override
            public void onPay(int paytype) {
                if(paytype == 1)//支付宝
                {
                    pType = 1;
                    PayUtils.alipay(OrderDetailActivity.this, "翠月商品", orderBean.getRemark(), orderBean.getCost() + "", orderBean.getId());
                }
                else if(paytype == 2)//微信
                {
                    pType = 2;
                    payOrder(orderBean.getId(), 2);
                }
                else if(paytype == 3)
                {
                    UserApi.getInstance().payForGoods(orderBean.getId(), CyApplication.getInstance().getUserBean().getStudentId(), 3, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
                        @Override
                        public void onSucceed(WxOrderBean data) {
                            showToast("支付成功");
                            EventBusMsgBean eventBusMsgBean = new EventBusMsgBean(1, true);
                            EventBus.getDefault().post(eventBusMsgBean);
                        }

                        @Override
                        public void onError(int code, String msg) {
                            showToast(msg);
                        }
                    }, OrderDetailActivity.this));
                }
            }
        });
        */
    }

    public void payOrder(String orderId, final int type)
    {
        UserApi.getInstance().payForGoods(CyApplication.getInstance().getUserBean().getStudentId(), orderId, type, 1, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
            @Override
            public void onSucceed(WxOrderBean data) {
                if(data != null && type == 2)
                {
                    MyLog.d(data);
                    PayUtils.wxPay(OrderDetailActivity.this, data);
                }
                else
                {

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsg(EventBusMsgBean messBean) {
        if(messBean.getType() == 1) {
            this.finish();
        }
    }
}

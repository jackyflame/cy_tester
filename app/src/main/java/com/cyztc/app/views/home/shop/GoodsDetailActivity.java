package com.cyztc.app.views.home.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BannerBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.EventBusMsgBean;
import com.cyztc.app.bean.GoodsCartBean;
import com.cyztc.app.bean.GoodsCartBeanP;
import com.cyztc.app.bean.GoodsDetailBean;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.dialog.PayDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.cyztc.app.utils.pay.PayUtils;
import com.cyztc.app.views.home.PayForActivity;
import com.cyztc.app.views.home.life.ApplyRoomActivity;
import com.cyztc.app.widget.ywl5320.CountView;
import com.cyztc.app.widget.ywl5320.adviewpager.AdViewpagerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/11.
 */

public class GoodsDetailActivity extends BaseActivity{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.countview)
    CountView countView;
    @BindView(R.id.tv_count)
    TextView tvCount;

    private String goodsname = "";
    private String goodsid = "";
    private GoodsDetailBean goodsDetailBean;

    private List<BannerBean> bannerBeens;
    private AdViewpagerUtil adViewpagerUtil;

    private int buyCount = 1;
    private List<GoodsCartBean> datas;
    private int pType = -1;
    private String goodsOrderId = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        goodsname = getIntent().getStringExtra("goodsname");
        goodsid = getIntent().getStringExtra("goodsid");
        setContentView(R.layout.activity_goods_detail_layout);
        setBackView();
        setTitle(goodsname);
        setTextMenuView("购物车");
        showDataLoadMsg("数据加载中");
        getGoodsDetail(CyApplication.getInstance().getUserBean().getStudentId(), goodsid);
        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(new CyApplication.OnPayListener() {
                @Override
                public void onPayResult(int code, String msg) {
                    if(code == 0)
                    {
                        if(pType == 1)//支付宝
                        {
                            UserApi.getInstance().payForGoods(goodsOrderId, CyApplication.getInstance().getUserBean().getStudentId(), 1, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
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
                            }, GoodsDetailActivity.this));
                        }
                        else
                        {
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
//        GoodsCartActivity.startActivity(this);
        GoodsCartOrderActivity.startActivity(this);
    }

    @OnClick(R.id.iv_addcart)
    public void onClickAddCar(View veiw)
    {
        if(goodsDetailBean.getCount() > 0) {
            showLoadDialog("添加到购物车");
            //addCart(CyApplication.getInstance().getUserBean().getStudentId(), goodsDetailBean.getId(), buyCount);
//        OrderListActivity.startActivity(this);
            GoodsCartBean goodsCartBean = new GoodsCartBean();
            goodsCartBean.setId(goodsDetailBean.getId());
            goodsCartBean.setSelected(false);
            goodsCartBean.setTitle(goodsDetailBean.getTitle());
            goodsCartBean.setCount(buyCount);
            goodsCartBean.setPrice(goodsDetailBean.getPrice());
            goodsCartBean.setRemark(goodsDetailBean.getRemark());
            goodsCartBean.setThumbnail(goodsDetailBean.getThumbnail());
            goodsCartBean.setChangeCount(0);


            String goodscarts = SharedpreferencesUtil.readString(this, "goods", "goodscart", "");
            Gson gson = new Gson();
            if (TextUtils.isEmpty(goodscarts)) {
                datas = new ArrayList<>();
            } else {
                datas = gson.fromJson(goodscarts, new TypeToken<List<GoodsCartBean>>() {
                }.getType());
            }
            addGoods(goodsCartBean, datas);
            MyLog.d(datas);
            SharedpreferencesUtil.write(this, "goods", "goodscart", gson.toJson(datas));
            hideLoadDialog();
            showToast("添加成功");
        }
        else
        {
            showToast("此商品已经卖完了");
        }
    }

    @OnClick(R.id.iv_buy)
    public void onClickBuy(View view)
    {
        if(goodsDetailBean != null) {
            if(goodsDetailBean.getCount() > 0) {
                showLoadDialog("生成订单中");
                HashMap<String, Integer> goods = new HashMap<>();
                goods.put(goodsDetailBean.getId(), buyCount);
                submitService(goods, goodsDetailBean.getPrice() * buyCount, buyCount, goodsDetailBean.getPicture(), goodsDetailBean.getRemark());
            }
            else {
                showToast("此商品已经卖完了");
            }
        }
        else {
            showToast("获取商品信息失败");
        }
    }


    public static void startActivity(Context context, String goodsname, String goodsid)
    {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goodsname", goodsname);
        intent.putExtra("goodsid", goodsid);
        context.startActivity(intent);
    }

    public void getGoodsDetail(String studentid, String goodsid)
    {
        HomeApi.getInstance().getGoodsDetail(studentid, goodsid, new HttpSubscriber<GoodsDetailBean>(new SubscriberOnListener<GoodsDetailBean>() {
            @Override
            public void onSucceed(GoodsDetailBean data) {

                if(data != null)
                {
                    goodsDetailBean = data;
                    initBanner(data.getPicture());
                    tvDes.setText(data.getRemark());
                    tvPrice.setText("¥" + CommonUtil.forMatPrice(data.getPrice()));
                    countView.initDatas(R.drawable.count_drawable_left, R.drawable.count_drawable_mid, R.drawable.count_drawable_right, 1, data.getCount(), 1);
                    tvCount.setText(data.getProductName() + "件");
                    countView.setOnCountListener(new CountView.OnCountListener() {
                        @Override
                        public void onCount(int count, int changeCount) {
                            buyCount = count;
                        }
                    });
                    setTitle(data.getTitle());
                }
                hideDataLoadMsg();
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));
    }

    public List<BannerBean> getBanners(String s)
    {
        List<BannerBean> bannerBeens = new ArrayList<>();
        if(TextUtils.isEmpty(s))
            return bannerBeens;
        String[] arrs = s.split(",");
        int length = arrs.length;
        for(int i = 0; i < length; i++)
        {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setPicture(arrs[i].replace("\\", "/"));
            bannerBeens.add(bannerBean);
            MyLog.d("imgurl:" + bannerBean.getPicture());
        }
        return bannerBeens;
    }

    public void initBanner(String s)
    {
        bannerBeens = getBanners(s);
        adViewpagerUtil = new AdViewpagerUtil(this, viewPager, null, 6, 6, bannerBeens);
        adViewpagerUtil.initVps();
    }

    public void addCart(String studentId, String goodsId, int count)
    {
        HomeApi.getInstance().addCart(studentId, goodsId, count, new HttpSubscriber<Integer>(new SubscriberOnListener<Integer>() {
            @Override
            public void onSucceed(Integer data) {
                hideLoadDialog();
                showToast("添加成功");
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, this));
    }

    public void submitService(HashMap<String, Integer> goods, float cost, int count, String picture, String remark)
    {
        HomeApi.getInstance().createOrder(CyApplication.getInstance().getUserBean().getStudentId(), 1, goods, "", cost, count, 0, picture, remark, "", "", new HttpSubscriber<String>(new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                hideLoadDialog();
                goodsOrderId = data;
                showPayDialog(goodsOrderId);



            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, this));
    }

    public void addGoods(GoodsCartBean goodsCartBean, List<GoodsCartBean> datas)
    {
        boolean isHave = false;
        for(GoodsCartBean c : datas)
        {
            if(c.getId().equals(goodsCartBean.getId()))
            {
                isHave = true;
                c.setCount(c.getCount() + goodsCartBean.getCount());
            }
        }
        if(!isHave)
        {
            datas.add(goodsCartBean);
        }
    }

    public void showPayDialog(final String orderId)
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
                    PayUtils.alipay(GoodsDetailActivity.this, goodsDetailBean.getTitle(), goodsDetailBean.getRemark(), (goodsDetailBean.getPrice() * buyCount) + "", orderId);
                }
                else if(paytype == 2)//微信
                {
                    pType = 2;
                    payOrder(orderId, 2);
                }
                else if(paytype == 3)
                {
                    UserApi.getInstance().payForGoods(goodsOrderId, CyApplication.getInstance().getUserBean().getStudentId(), 3, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
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
                    }, GoodsDetailActivity.this));
                }
            }
        });
        */
    }

    public void payOrder(String orderId, final int type)
    {
        UserApi.getInstance().payForGoods(orderId, CyApplication.getInstance().getUserBean().getStudentId(),  type, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
            @Override
            public void onSucceed(WxOrderBean data) {
                if(data != null && type == 2)
                {
                    MyLog.d(data);
                    PayUtils.wxPay(GoodsDetailActivity.this, data);
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

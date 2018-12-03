package com.cyztc.app.views.home.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.EventBusMsgBean;
import com.cyztc.app.bean.GoodsCartBean;
import com.cyztc.app.bean.GoodsCartBeanP;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.dialog.PayDialog;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.cyztc.app.utils.pay.PayUtils;
import com.cyztc.app.views.home.PayForActivity;
import com.cyztc.app.views.home.TrainMainInfoActivity;
import com.cyztc.app.views.home.adapter.GoodsCartAdapter;
import com.cyztc.app.views.home.life.ApplyRoomActivity;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2017/1/10.
 */

public class CartFragment extends BaseFragment{

    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.tv_pay_del)
    TextView tvPayDel;
    @BindView(R.id.ly_selected_all)
    LinearLayout lySelectedAll;
    @BindView(R.id.iv_selected)
    ImageView ivSelected;
    @BindView(R.id.tv_totalprice)
    TextView tvTotalPrice;
    @BindView(R.id.ly_wx_pay)
    LinearLayout lyWxPay;
    @BindView(R.id.ly_ali_pay)
    LinearLayout lyAlipay;
    @BindView(R.id.iv_wx)
    ImageView ivWxPay;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;

    private List<GoodsCartBean> datas;
    private GoodsCartAdapter goodsCartAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    private boolean isEdit = false;
    private int submitCount = 0;//提交数据的个数
    private int payWay = -1; //0:微信 1:支付宝

    private String orderId;
    private int pType = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_goods_cart_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        springView.setType(SpringView.Type.FOLLOW);
//        springView.setHeader(new DefaultHeader(getActivity()));
//        springView.setFooter(new DefaultFooter(getActivity()));

        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(new CyApplication.OnPayListener() {
                @Override
                public void onPayResult(int code, String msg) {
                    if(code == 0)
                    {
                        if(pType == 1)//支付宝
                        {
                            UserApi.getInstance().payForGoods(orderId, CyApplication.getInstance().getUserBean().getStudentId(), 1, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
                                @Override
                                public void onSucceed(WxOrderBean data) {
                                    showToast("支付成功");
                                    EventBusMsgBean eventBusMsgBean = new EventBusMsgBean(1, true);
                                    EventBus.getDefault().post(eventBusMsgBean);
                                    deleteGoodsItems();
                                }

                                @Override
                                public void onError(int code, String msg) {
                                    showToast(msg);
                                }
                            }, getActivity()));
                        }
                        else
                        {
                            showToast(msg);
                            EventBusMsgBean eventBusMsgBean = new EventBusMsgBean(1, true);
                            EventBus.getDefault().post(eventBusMsgBean);
                            deleteGoodsItems();
                        }
                    }
                    else
                    {
                        showToast(msg);
                    }
                }
            });
        }

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(isEdit)
                {
                    if(springView != null) {
                        springView.onFinishFreshAndLoad();
                    }
                    showToast("处于编辑状态，刷新无效");
                    return;
                }

                if(!isLoading) {
                    index = 0;
                    getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(isEdit)
                {
                    if(springView != null) {
                        springView.onFinishFreshAndLoad();
                    }
                    showToast("处于编辑状态，加载无效");
                    return;
                }
                if(!isLoading) {
                    getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }
        });

        setAdapter();
        showDataLoadMsg("数据加载中");
        String goodscarts = SharedpreferencesUtil.readString(getActivity(), "goods", "goodscart", "");
        Gson gson = new Gson();
        if(TextUtils.isEmpty(goodscarts))
        {
            datas = new ArrayList<>();
        }
        else {
            datas.addAll((Collection<? extends GoodsCartBean>) gson.fromJson(goodscarts, new TypeToken<List<GoodsCartBean>>() {
            }.getType()));
        }
        if(datas.size() > 0)
        {
            hideDataLoadMsg();
            selectedAll(false);
            goodsCartAdapter.notifyDataSetChanged();
            ((GoodsCartOrderActivity)getActivity()).setTextMenuView("编辑");
        }
        else
        {
            showDataLoadMsg("购物车还没有物品");
        }

//        getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
    }

    public void onClickTextMenu() {
        if(TextUtils.isEmpty(orderId)) {
            isEdit = !isEdit;
            if (isEdit) {
                ((GoodsCartOrderActivity) getActivity()).setTextMenuView("完成");
                tvPayDel.setText("删除");
                tvTotalPrice.setVisibility(View.GONE);
                goodsCartAdapter.setEdit(true);

            } else {
                goodsCartAdapter.setEdit(false);
                ((GoodsCartOrderActivity) getActivity()).setTextMenuView("编辑");
                tvPayDel.setText("支付");
                tvTotalPrice.setVisibility(View.VISIBLE);
                submitGoodsCountChange();
            }
            goodsCartAdapter.notifyDataSetChanged();
        }
        else
        {
            showToast("订单处于支付状态，不可修改");
        }
    }

    @OnClick(R.id.ly_selected_all)
    public void onClickSelectedAllClick(View veiw)
    {
        cllickSelectedAll();
    }

    @OnClick(R.id.tv_pay_del)
    public void onPayOrDelClick(View view)
    {
        if(!isSelectedOne())
        {
            showToast("请先选择商品");
            return;
        }

        if(isEdit)
        {
            deleteGoodsItems();
        }
        else
        {
            if(TextUtils.isEmpty(orderId)) {
                goodsCartAdapter.setPay(true);
                showLoadDialog("正在生成订单");
                submitService("", "", getSelectedGoods(), "", "", getTotalPrice(), 1);
            }
            else
            {
                showPayDialog(orderId);
            }
//            if(payWay == 0)
//            {
//                showToast("微信支付");
//            }
//            else if(payWay == 1)
//            {
//                showToast("支付宝支付");
//            }
//            else
//            {
//                showToast("请选择支付方式");
//            }
        }
    }

    @OnClick(R.id.ly_wx_pay)
    public void onWxPayClick(View view)
    {
        ivWxPay.setSelected(true);
        ivAlipay.setSelected(false);
        payWay = 0;
    }

    @OnClick(R.id.ly_ali_pay)
    public void onAliPayClick(View view)
    {
        ivWxPay.setSelected(false);
        ivAlipay.setSelected(true);
        payWay = 1;
    }


    public void setAdapter()
    {
        datas = new ArrayList<>();
        goodsCartAdapter = new GoodsCartAdapter(getActivity(), datas);
        listView.setAdapter(goodsCartAdapter);
        goodsCartAdapter.setOnAdapterClick(new GoodsCartAdapter.OnAdapterClick() {
            @Override
            public void onSelected() {
                if(TextUtils.isEmpty(orderId)) {
                    if (isSelectedAll()) {
                        ivSelected.setSelected(true);
                    } else {
                        ivSelected.setSelected(false);
                    }
                    tvTotalPrice.setText("合计：" + CommonUtil.forMatPrice(getTotalPrice()));
                }
            }

            @Override
            public void onCount() {
                if(TextUtils.isEmpty(orderId)) {
                    tvTotalPrice.setText("合计：" + CommonUtil.forMatPrice(getTotalPrice()));
                }
            }
        });
        tvTotalPrice.setText("合计：" + CommonUtil.forMatPrice(getTotalPrice()));
    }

    public void getCartList(String studentId, String name, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getCartList(studentId, name, start, rows, new HttpSubscriber<GoodsCartBeanP>(new SubscriberOnListener<GoodsCartBeanP>() {
            @Override
            public void onSucceed(GoodsCartBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        datas.clear();
                    }
                    if(data.getData().size() < pagesize && index != 0)
                    {
                        showToast("没有更多了");
                    }
                    if(data.getData().size() != 0 && data.getData().size() <= pagesize)
                    {
                        index++;
                    }
                    if(data.getData().size() != 0) {
                        datas.addAll(data.getData());
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                if(datas.size() > 0)
                {
                    ((GoodsCartOrderActivity)getActivity()).setTextMenuView("编辑");
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("购物车还没有物品");
                    ((GoodsCartOrderActivity)getActivity()).hideTextMenuView();

                }
                hideLoadDialog();
                if(isSelectedAll())
                {
                    selectedAll(true);
                    ivSelected.setSelected(true);
                }
                else
                {
                    selectedAll(false);
                    ivSelected.setSelected(false);
                }
                goodsCartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                    ((GoodsCartOrderActivity)getActivity()).hideTextMenuView();
                }
                hideLoadDialog();
                isLoading = false;
            }
        }, getActivity()));
    }

    /**
     * 是否全部选中
     * @return
     */
    public boolean isSelectedAll()
    {
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(!goodsCartBean.isSelected())
                return false;
        }
        return true;
    }

    public void selectedAll(boolean selected)
    {
        for(GoodsCartBean goodsCartBean : datas)
        {
            goodsCartBean.setSelected(selected);
        }
    }

    public float getTotalPrice()
    {
        float totalprice = 0;
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(goodsCartBean.isSelected())
            {
                totalprice += goodsCartBean.getCount() * goodsCartBean.getPrice();
            }
        }
        return totalprice;
    }

    public void cllickSelectedAll()
    {
        if(isSelectedAll())
        {
            selectedAll(false);
            ivSelected.setSelected(false);
        }
        else
        {
            selectedAll(true);
            ivSelected.setSelected(true);
        }
        tvTotalPrice.setText("合计：" + CommonUtil.forMatPrice(getTotalPrice()));
        goodsCartAdapter.notifyDataSetChanged();
    }

    public void changeCount(GoodsCartBean goodsCartBean, String studentId, final int size)
    {
        HomeApi.getInstance().addCart(studentId, goodsCartBean.getId(), goodsCartBean.getChangeCount(), new HttpSubscriber<Integer>(new SubscriberOnListener<Integer>() {
            @Override
            public void onSucceed(Integer data) {
                submitCount++;
                if(submitCount >= size)
                {
                    index = 0;
                    getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }

            @Override
            public void onError(int code, String msg) {
                submitCount++;
                if(submitCount >= size)
                {
                    index = 0;
                    getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }
        }, getActivity()));
    }

    public void submitGoodsCountChange()
    {

        Gson gson = new Gson();
        SharedpreferencesUtil.write(getActivity(), "goods", "goodscart", gson.toJson(datas));
        showToast("修改完成");
//        List<GoodsCartBean> goodsCartBeens = new ArrayList<>();
//        for(GoodsCartBean goodsCartBean : datas)
//        {
//            if(goodsCartBean.getChangeCount() != 0)
//            {
//                goodsCartBeens.add(goodsCartBean);
//            }
//        }

//        int size = goodsCartBeens.size();
//        if(size > 0) {
//            showLoadDialog("修改中");
//            submitCount = 0;
//            for (GoodsCartBean g : goodsCartBeens) {
//                changeCount(g, CyApplication.getInstance().getUserBean().getStudentId(), size);
//            }
//        }

    }

    public void deleteGoodsItems()
    {
        List<GoodsCartBean> goodsCartBeens = new ArrayList<>();
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(!goodsCartBean.isSelected())
            {
                goodsCartBeens.add(goodsCartBean);
            }
        }

        datas.clear();
        datas.addAll(goodsCartBeens);
        goodsCartAdapter.notifyDataSetChanged();
        if(datas.size() == 0)
        {
            showDataLoadMsg("购物车还没有物品");
            ((GoodsCartOrderActivity)getActivity()).hideTextMenuView();
            SharedpreferencesUtil.write(getActivity(), "goods", "goodscart", "");
        }
        else {
            Gson gson = new Gson();
            SharedpreferencesUtil.write(getActivity(), "goods", "goodscart", gson.toJson(datas));
        }

//        int size = goodsCartBeens.size();
//        if(size > 0) {
//            showLoadDialog("删除中");
//            submitCount = 0;
//            for (GoodsCartBean g : goodsCartBeens) {
//                deleteItems(CyApplication.getInstance().getUserBean().getStudentId(), g.getId(), size);
//            }
//        }
    }

    public void deleteItems(String studentId, String goodsid, final int size)
    {
        HomeApi.getInstance().deleteCartItems(studentId, goodsid, new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {
                submitCount++;
                if(submitCount >= size)
                {
                    index = 0;
                    getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }

            @Override
            public void onError(int code, String msg) {
                submitCount++;
                if(submitCount >= size)
                {
                    index = 0;
                    getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }
        }, getActivity()));
    }

    public boolean isSelectedOne()
    {
        boolean isslectedone = false;
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(goodsCartBean.isSelected())
                isslectedone = true;
        }
        return isslectedone;
    }

    public HashMap<String, Integer> getSelectedGoods()
    {
        HashMap<String, Integer> goods = new HashMap<>();
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(goodsCartBean.isSelected())
            {
                goods.put(goodsCartBean.getId(), goodsCartBean.getCount());
            }
        }
        return goods;
    }

    public void submitService(String address, String picture, HashMap<String, Integer> goods, String remark, String subType, float cost, int count)
    {
        HomeApi.getInstance().createOrder(CyApplication.getInstance().getUserBean().getStudentId(), 1, goods, address, cost, count, 0, picture, remark, subType, "", new HttpSubscriber<String>(new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                hideLoadDialog();
                orderId = data;
                showPayDialog(data);
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, getActivity()));
    }

    public void showPayDialog(final String orderId)
    {

        Toast.makeText(getContext(), "暂不能支付", Toast.LENGTH_SHORT).show();
        /* add by jjyuan
        final PayDialog payDialog = new PayDialog(getActivity());
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
                    PayUtils.alipay(getActivity(), "商品支付", "翠月商店商品付款", CommonUtil.forMatPrice(getTotalPrice()), orderId);
                }
                else if(paytype == 2)//微信
                {
                    pType = 2;
                    payOrder(orderId, 2);
                }
                else if(paytype == 3)
                {
                    UserApi.getInstance().payForGoods(orderId, CyApplication.getInstance().getUserBean().getStudentId(), 3, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
                        @Override
                        public void onSucceed(WxOrderBean data) {
                            showToast("支付成功");
                            EventBusMsgBean eventBusMsgBean = new EventBusMsgBean(1, true);
                            EventBus.getDefault().post(eventBusMsgBean);
                            deleteGoodsItems();
                        }

                        @Override
                        public void onError(int code, String msg) {
                            showToast(msg);
                        }
                    }, getActivity()));
                }
            }
        });
        */
    }

    public void payOrder(String orderId, final int type)
    {
        UserApi.getInstance().payForGoods(orderId, CyApplication.getInstance().getUserBean().getStudentId(), type, 3, new HttpSubscriber<WxOrderBean>(new SubscriberOnListener<WxOrderBean>() {
            @Override
            public void onSucceed(WxOrderBean data) {
                if(data != null && type == 2)
                {
                    MyLog.d(data);
                    PayUtils.wxPay(getActivity(), data);
                }
                else
                {
                    showToast("创建支付订单失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
            }
        }, getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(CyApplication.getInstance() != null)
        {
            CyApplication.getInstance().setOnPayListener(null);
        }
    }
}

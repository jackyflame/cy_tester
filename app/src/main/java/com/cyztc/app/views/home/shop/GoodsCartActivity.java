package com.cyztc.app.views.home.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GoodsCartBean;
import com.cyztc.app.bean.GoodsCartBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.log.SWToast;
import com.cyztc.app.views.home.adapter.GoodsCartAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/13.
 */

public class GoodsCartActivity extends BaseActivity{

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
    @BindView(R.id.ly_pay_way)
    LinearLayout lyPayWay;

    private List<GoodsCartBean> datas;
    private GoodsCartAdapter goodsCartAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    private boolean isEdit = false;
    private int submitCount = 0;//提交数据的个数
    private int payWay = -1; //0:微信 1:支付宝

    private String[] titles = new String[]{"购物车", "订单"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_cart_layout);
        setBackView();
        setTitle("购物车");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
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
        getCartList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        isEdit = !isEdit;
        if(isEdit)
        {
            setTextMenuView("完成");
            tvPayDel.setText("删除");
            lyPayWay.setVisibility(View.GONE);
            tvTotalPrice.setVisibility(View.GONE);
            goodsCartAdapter.setEdit(true);

        }
        else
        {
            goodsCartAdapter.setEdit(false);
            setTextMenuView("编辑");
            tvPayDel.setText("支付");
            lyPayWay.setVisibility(View.VISIBLE);
            tvTotalPrice.setVisibility(View.VISIBLE);
            submitGoodsCountChange();
        }
        goodsCartAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ly_selected_all)
    public void onClickSelectedAllClick(View veiw)
    {
        cllickSelectedAll();
    }

    @OnClick(R.id.tv_pay_del)
    public void onPayOrDelClick(View view)
    {
        if(isEdit)
        {
            deleteGoodsItems();
        }
        else
        {
            if(payWay == 0)
            {
                showToast("微信支付");
            }
            else if(payWay == 1)
            {
                showToast("支付宝支付");
            }
            else
            {
                showToast("请选择支付方式");
            }

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
        goodsCartAdapter = new GoodsCartAdapter(this, datas);
        listView.setAdapter(goodsCartAdapter);
        goodsCartAdapter.setOnAdapterClick(new GoodsCartAdapter.OnAdapterClick() {
            @Override
            public void onSelected() {
                if(isSelectedAll())
                {
                    ivSelected.setSelected(true);
                }
                else
                {
                    ivSelected.setSelected(false);
                }
                tvTotalPrice.setText("合计：" + getTotalPrice());
            }

            @Override
            public void onCount() {
                tvTotalPrice.setText("合计：" + getTotalPrice());
            }
        });
        tvTotalPrice.setText("合计：" + getTotalPrice());
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
                    setTextMenuView("编辑");
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("购物车还没有物品");
                    hideTextMenuView();

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
                    hideTextMenuView();
                }
                hideLoadDialog();
                isLoading = false;
            }
        }, this));
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, GoodsCartActivity.class);
        context.startActivity(intent);
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

    public int getTotalPrice()
    {
        int totalprice = 0;
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
        tvTotalPrice.setText("合计：" + getTotalPrice());
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
        }, this));
    }

    public void submitGoodsCountChange()
    {
        List<GoodsCartBean> goodsCartBeens = new ArrayList<>();
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(goodsCartBean.getChangeCount() != 0)
            {
                goodsCartBeens.add(goodsCartBean);
            }
        }

        int size = goodsCartBeens.size();
        if(size > 0) {
            showLoadDialog("修改中");
            submitCount = 0;
            for (GoodsCartBean g : goodsCartBeens) {
                changeCount(g, CyApplication.getInstance().getUserBean().getStudentId(), size);
            }
        }

    }

    public void deleteGoodsItems()
    {
        List<GoodsCartBean> goodsCartBeens = new ArrayList<>();
        for(GoodsCartBean goodsCartBean : datas)
        {
            if(goodsCartBean.isSelected())
            {
                goodsCartBeens.add(goodsCartBean);
            }
        }

        int size = goodsCartBeens.size();
        if(size > 0) {
            showLoadDialog("删除中");
            submitCount = 0;
            for (GoodsCartBean g : goodsCartBeens) {
                deleteItems(CyApplication.getInstance().getUserBean().getStudentId(), g.getId(), size);
            }
        }
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
        }, this));
    }

}

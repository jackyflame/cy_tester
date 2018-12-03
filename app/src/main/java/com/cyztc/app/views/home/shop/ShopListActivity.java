package com.cyztc.app.views.home.shop;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BannerBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GoodsBannerBean;
import com.cyztc.app.bean.GoodsBannerBeanP;
import com.cyztc.app.bean.GoodsBean;
import com.cyztc.app.bean.GoodsBeanP;
import com.cyztc.app.bean.TribeRecommendBeanP;
import com.cyztc.app.bean.TypeBean;
import com.cyztc.app.bean.TypeBeanP;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.home.CuiYueCircleDetailActivity;
import com.cyztc.app.views.home.adapter.ShopListAdapter;
import com.cyztc.app.views.knowledge.adapter.TypeAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.cyztc.app.widget.ywl5320.adviewpager.AdViewpagerGoodsUtil;
import com.cyztc.app.widget.ywl5320.adviewpager.AdViewpagerUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/10.
 */

public class ShopListActivity extends BaseActivity{

    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.typelist)
    ListView typelist;
    @BindView(R.id.v_bg)
    View vBg;

    ViewPager adViewpager;
    LinearLayout lyDot;

    private List<GoodsBannerBean> bannerBeens;
    private AdViewpagerGoodsUtil adViewpagerGoodsUtil;

    private ShopListAdapter shopListAdapter;
    private List<GoodsBean> datas;
    private View headView;
    private LayoutInflater headerInflater;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    private TypeAdapter typeAdapter;
    private List<TypeBean> typeBeens;
    private boolean isShowListView = false;
    private int listviewWidth = 0;
    private String subType = "";
    private String subName = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list_layout);
        headerInflater = LayoutInflater.from(this);
        headView = headerInflater.inflate(R.layout.header_shop_list_layout, null);
        adViewpager = (ViewPager) headView.findViewById(R.id.adviewpager);
        lyDot = (LinearLayout) headView.findViewById(R.id.lydot);
        listView.addHeaderView(headView);
        setBackView();
        setTitle("搜索商品");
        setTextMenuView("分类");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), 1, subType, subName, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), 1, subType, subName, index, pagesize);
                }
            }
        });

        listviewWidth = (int)(CommonUtil.getScreenWidth(this) * 0.6f);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listviewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        typelist.setLayoutParams(lp);
        typelist.setVisibility(View.INVISIBLE);
        hideFloor(typelist);
        setTypeAdapter();

        setAdapter();
        showDataLoadMsg("数据加载中");
        getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), 1, subType, subName, index, pagesize);
        searchTypes(1, 0, 50);
        getBannerList();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(typeBeens.size() == 0)
        {
            showToast("没有类型可选");
            return;
        }
        if(isShowListView)
        {
            hideFloor(typelist);
        }
        else {
            showFloor(typelist);
        }
    }

    @Override
    public void onClickTitle() {
        super.onClickTitle();
        SearchGoodsActivity.startActivity(this);
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();

    }

    @OnClick(R.id.iv_cart)
    public void onClickToCart(View veiw)
    {
//        GoodsCartActivity.startActivity(this);
        GoodsCartOrderActivity.startActivity(this);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        shopListAdapter = new ShopListAdapter(this, datas);
        listView.setAdapter(shopListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsBean goodsBean = (GoodsBean) listView.getItemAtPosition(position);
                if(goodsBean != null) {
                    GoodsDetailActivity.startActivity(ShopListActivity.this, goodsBean.getTitle(), goodsBean.getId());
                }
            }
        });
    }

    public void setTypeAdapter()
    {
        typeBeens = new ArrayList<>();
        TypeBean typeBean = new TypeBean();
        typeBean.setSearchType("全部");
        typeBeens.add(typeBean);
        typeAdapter = new TypeAdapter(this, typeBeens);
        typelist.setAdapter(typeAdapter);
        typelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TypeBean typeBean = (TypeBean) typelist.getItemAtPosition(position);
                if(typeBean != null)
                {
                    if(!isLoading) {
                        hideFloor(typelist);
                        showDataLoadMsg("数据加载中");
//                        subName = typeBean.getSearchType();
                        subType = typeBean.getSearchType();
                        if("全部".equals(subType))
                        {
                            subType = "";
                        }
                        index = 0;
                        getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), 1, subType, subName, index, pagesize);
                    }
                }
            }
        });
    }

    public void showFloor(final View v) {
        isShowListView = true;
        typelist.setVisibility(View.VISIBLE);
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(listviewWidth, 0);
        animator.setTarget(v);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationX((Float) animation.getAnimatedValue());
            }
        });

        vBg.animate().alpha(1).setDuration(200);
        vBg.setVisibility(View.VISIBLE);
    }

    public void hideFloor(final View v)
    {
        isShowListView = false;
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(0, listviewWidth);
        animator.setTarget(v);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        vBg.animate().alpha(0).setDuration(200);
        vBg.setVisibility(View.GONE);

    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, ShopListActivity.class);
        context.startActivity(intent);
    }

    public void getGoodsList(String studentId, int type, String subType, String name, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getGoodsList(studentId, type, subType, name, start, rows, new HttpSubscriber<GoodsBeanP>(new SubscriberOnListener<GoodsBeanP>() {
            @Override
            public void onSucceed(GoodsBeanP data) {
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
                        shopListAdapter.notifyDataSetChanged();
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                hideDataLoadMsg();
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                hideDataLoadMsg();
                isLoading = false;
            }
        }, this));
    }

    public void searchTypes(int type, int star, int rows)
    {
        BookApi.getInstance().searchTypes(type, star, rows, new HttpSubscriber<TypeBeanP>(new SubscriberOnListener<TypeBeanP>() {
            @Override
            public void onSucceed(TypeBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(data.getData().size() > 0) {
                        typeBeens.addAll(data.getData());
                        typeAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        showDataLoadWrongMsg("此类型没有数据");
                    }
                }
                else
                {
                    showDataLoadWrongMsg("访问数据失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));
    }

    public void getBannerList()
    {
        HomeApi.getInstance().getRecommendGoods(CyApplication.getInstance().getUserBean().getStudentId(), 0, 4, new HttpSubscriber<GoodsBannerBeanP>(new SubscriberOnListener<GoodsBannerBeanP>() {
            @Override
            public void onSucceed(GoodsBannerBeanP data) {
                if(data != null && data.getData() != null)
                {
                    MyLog.d("bannar:" + data.toString());
                    bannerBeens = new ArrayList<>();
                    bannerBeens.addAll(data.getData());
                    adViewpagerGoodsUtil = new AdViewpagerGoodsUtil(ShopListActivity.this, adViewpager, lyDot, 6, 6, bannerBeens);
                    adViewpagerGoodsUtil.initVps();
                    adViewpagerGoodsUtil.setOnAdItemClickListener(new AdViewpagerGoodsUtil.OnAdItemClickListener() {
                        @Override
                        public void onItemClick(GoodsBannerBean bannerBean) {
//                            CuiYueCircleDetailActivity.startActivity(ShopListActivity.this, bannerBean.getId());
                            GoodsDetailActivity.startActivity(ShopListActivity.this, "", bannerBean.getGoodsId());
                        }
                    });
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }
}

package com.cyztc.app.views.home.tribe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BannerBeanP;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GoodsBannerBean;
import com.cyztc.app.bean.TribeBean;
import com.cyztc.app.bean.TribeRecommendBean;
import com.cyztc.app.bean.TribeRecommendBeanP;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.views.home.adapter.TribeFindAdapter;
import com.cyztc.app.views.home.shop.GoodsDetailActivity;
import com.cyztc.app.views.home.shop.ShopListActivity;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.cyztc.app.widget.ywl5320.adviewpager.AdViewpagerGoodsUtil;
import com.cyztc.app.widget.ywl5320.adviewpager.AdViewpagerTribeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/20.
 */

public class FindTribeFragment extends BaseFragment{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    ViewPager adViewpager;
    LinearLayout lyDot;
    private AdViewpagerTribeUtil adViewpagerTribeUtil;
    private List<TribeRecommendBean> bannerBeens;

    private View listveiwHeader;
    private TribeFindAdapter tribeFindAdapter;
    private List<TribeRecommendBean> datas;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    private LinearLayout lyOne;
    private LinearLayout lyTwo;
    private LinearLayout lyThree;

    private ImageView ivOne;
    private TextView tvNameOne;

    private ImageView ivTwo;
    private TextView tvNameTwo;

    private ImageView ivThree;
    private TextView tvNameThree;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_find_tribe_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listveiwHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_findtribe_layout, null);

        adViewpager = (ViewPager) listveiwHeader.findViewById(R.id.adviewpager);
        lyDot = (LinearLayout) listveiwHeader.findViewById(R.id.lydot);

        lyOne = (LinearLayout) listveiwHeader.findViewById(R.id.ly_one);
        lyTwo = (LinearLayout) listveiwHeader.findViewById(R.id.ly_two);
        lyThree = (LinearLayout) listveiwHeader.findViewById(R.id.ly_three);

        ivOne = (ImageView) listveiwHeader.findViewById(R.id.iv_one);
        tvNameOne = (TextView) listveiwHeader.findViewById(R.id.tv_name_one);

        ivTwo = (ImageView) listveiwHeader.findViewById(R.id.iv_tow);
        tvNameTwo = (TextView) listveiwHeader.findViewById(R.id.tv_name_two);

        ivThree = (ImageView) listveiwHeader.findViewById(R.id.iv_three);
        tvNameThree = (TextView) listveiwHeader.findViewById(R.id.tv_name_three);

        listView.addHeaderView(listveiwHeader);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
                }
            }
        });
        setAdapter();
        showDataLoadMsg("数据加载中");
        getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), "", index, pagesize);
        getRcommendTribes();
    }

    public void getTribeList(String studentId, String name, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getFindTribeList(studentId, name, start, rows, new HttpSubscriber<TribeRecommendBeanP>(new SubscriberOnListener<TribeRecommendBeanP>() {
            @Override
            public void onSucceed(TribeRecommendBeanP data) {
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
                    if(datas.size() > 0) {
                        initTuijian(datas);
                        tribeFindAdapter.notifyDataSetChanged();
                        hideDataLoadMsg();
                    }
                    else
                    {
                        showDataLoadMsg("没有部落数据");
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;

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
        }, getActivity()));
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        tribeFindAdapter = new TribeFindAdapter(getActivity(), datas);
        listView.setAdapter(tribeFindAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TribeRecommendBean tribeRecommendBean = (TribeRecommendBean) listView.getItemAtPosition(position);
                if(tribeRecommendBean != null) {
                    TribeDetailActivity.startActivity(getActivity(), tribeRecommendBean.getId(), tribeRecommendBean.getName());
                }
            }
        });
    }

    public void getRcommendTribes()
    {
        HomeApi.getInstance().getRecommendTribes(CyApplication.getInstance().getUserBean().getStudentId(), 0, 4, new HttpSubscriber<TribeRecommendBeanP>(new SubscriberOnListener<TribeRecommendBeanP>() {
            @Override
            public void onSucceed(final TribeRecommendBeanP data) {
                if(data.getData() != null)
                {
                    bannerBeens = new ArrayList<>();
                    bannerBeens.addAll(data.getData());
                    adViewpagerTribeUtil = new AdViewpagerTribeUtil(getActivity(), adViewpager, lyDot, 6, 6, bannerBeens);
                    adViewpagerTribeUtil.initVps();
                    adViewpagerTribeUtil.setOnAdItemClickListener(new AdViewpagerTribeUtil.OnAdItemClickListener() {
                        @Override
                        public void onItemClick(TribeRecommendBean bannerBean) {
//                            CuiYueCircleDetailActivity.startActivity(ShopListActivity.this, bannerBean.getId());
                            TribeDetailActivity.startActivity(getActivity(), bannerBean.getId(), bannerBean.getName());
                        }
                    });
                }

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, getActivity()));
    }

    public void initTuijian(final List<TribeRecommendBean> datas)
    {
        if(datas.size() >= 3)
        {
            ImageLoad.getInstance().displayeRoundImage(getActivity(), ivOne, HttpMethod.IMG_URL + datas.get(0).getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
            tvNameOne.setText(datas.get(0).getName());
            lyOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TribeDetailActivity.startActivity(getActivity(), datas.get(0).getId(), datas.get(0).getName());
                }
            });

            ImageLoad.getInstance().displayeRoundImage(getActivity(), ivTwo, HttpMethod.IMG_URL + datas.get(1).getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
            tvNameTwo.setText(datas.get(1).getName());
            lyTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TribeDetailActivity.startActivity(getActivity(), datas.get(1).getId(), datas.get(1).getName());
                }
            });

            ImageLoad.getInstance().displayeRoundImage(getActivity(), ivThree, HttpMethod.IMG_URL + datas.get(2).getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
            tvNameThree.setText(datas.get(2).getName());
            lyThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TribeDetailActivity.startActivity(getActivity(), datas.get(2).getId(), datas.get(2).getName());
                }
            });
        }
    }
}

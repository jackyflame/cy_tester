package com.cyztc.app.views.home.tribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.TribeRecommendBeanP;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.ywl5320.TabNavitationLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 部落
 * Created by ywl on 2016/11/19.
 */

public class TribeActivity extends BaseActivity{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab_bar)
    TabNavitationLayout tabbar;

    private String[] titles = new String[]{"发现", "部落"};
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentLists;

    private TribeFragment tribeFragment;
    private FindTribeFragment findTribeFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribe_layout);
        setBackView();
        initFragments();
        tabbar.setViewPager(this, titles, viewPager, R.drawable.tab_drawable_left, R.drawable.tab_drawable_mid, R.drawable.tab_drawable_right, R.color.color_white, R.color.color_12a7e2, 14, 0, 1f, true);
        viewPager.setCurrentItem(0);
        getRcommendTribes();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public void initFragments()
    {
        tribeFragment = new TribeFragment();
        findTribeFragment = new FindTribeFragment();
        fragmentLists = new ArrayList<>();
        fragmentLists.add(findTribeFragment);
        fragmentLists.add(tribeFragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentLists);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }


    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, TribeActivity.class);
        context.startActivity(intent);
    }

    public void getRcommendTribes()
    {
        HomeApi.getInstance().getRecommendTribes(CyApplication.getInstance().getUserBean().getStudentId(), 0, 4, new HttpSubscriber<TribeRecommendBeanP>(new SubscriberOnListener<TribeRecommendBeanP>() {
            @Override
            public void onSucceed(final TribeRecommendBeanP data) {
                if(data.getData() != null && data.getData().size() == 3)
                {
//                    ImageLoad.getInstance().displayeRoundImage(getActivity(), ivOne, HttpMethod.IMG_URL + data.getData().get(0).getCoverPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
//                    tvNameOne.setText(data.getData().get(0).getName());
//                    lyOne.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TribeDetailActivity.startActivity(getActivity(), data.getData().get(0).getId(), data.getData().get(0).getName());
//                        }
//                    });
//
//                    ImageLoad.getInstance().displayeRoundImage(getActivity(), ivTwo, HttpMethod.IMG_URL + data.getData().get(1).getCoverPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
//                    tvNameTwo.setText(data.getData().get(1).getName());
//                    lyTwo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TribeDetailActivity.startActivity(getActivity(), data.getData().get(1).getId(), data.getData().get(1).getName());
//                        }
//                    });
//
//                    ImageLoad.getInstance().displayeRoundImage(getActivity(), ivThree, HttpMethod.IMG_URL + data.getData().get(2).getCoverPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
//                    tvNameThree.setText(data.getData().get(2).getName());
//                    lyThree.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TribeDetailActivity.startActivity(getActivity(), data.getData().get(2).getId(), data.getData().get(2).getName());
//                        }
//                    });
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }
}

package com.cyztc.app.views.home.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.EventBusMsgBean;
import com.cyztc.app.views.home.tribe.FindTribeFragment;
import com.cyztc.app.views.home.tribe.TribeActivity;
import com.cyztc.app.views.home.tribe.TribeFragment;
import com.cyztc.app.widget.ywl5320.TabNavitationLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2017/1/10.
 */

public class GoodsCartOrderActivity extends BaseActivity{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab_bar)
    TabNavitationLayout tabbar;

    private String[] titles = new String[]{"购物车", "我的订单"};
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentLists;

    private CartFragment cartFragment;
    private OrderFragment orderFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_goods_cartorder_layout);
        setBackView();
        initFragments();
        tabbar.setViewPager(this, titles, viewPager, R.drawable.tab_drawable_left, R.drawable.tab_drawable_mid, R.drawable.tab_drawable_right, R.color.color_white, R.color.color_12a7e2, 14, 0, 1f, true);
        viewPager.setCurrentItem(0);
        tabbar.setOnNaPageChangeListener(new TabNavitationLayout.OnNaPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                {
                    showTextMenuView();
                }
                else {
                    hideTextMenuView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(cartFragment != null)
        {
            cartFragment.onClickTextMenu();
        }
    }

    public void initFragments()
    {
        cartFragment = new CartFragment();
        orderFragment = new OrderFragment();
        fragmentLists = new ArrayList<>();
        fragmentLists.add(cartFragment);
        fragmentLists.add(orderFragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentLists);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, GoodsCartOrderActivity.class);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsg(EventBusMsgBean messBean) {
        if(messBean.getType() == 1) {
            this.finish();
        }
    }
}

package com.cyztc.app.views.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.views.home.tribe.FindTribeFragment;
import com.cyztc.app.views.home.tribe.TribeFragment;
import com.cyztc.app.widget.ywl5320.TabNavitationLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/29.
 */

public class BorrowBookActivity extends BaseActivity{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab_bar)
    TabNavitationLayout tabbar;

    private String[] titles = new String[]{"借阅中", "已归还"};
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentLists;

    private BorrowingBookFragment borrowingBookFragment;
    private GaveBackBookFragment gaveBackBookFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book_layout);
        setBackView();
        setTextMenuView("归还");
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
                    setTextMenuViewVisible(true);
                }
                else
                {
                    setTextMenuViewVisible(false);
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
        if(borrowingBookFragment != null)
        {
            borrowingBookFragment.giveBackBook();
        }
    }

    public void initFragments()
    {
        borrowingBookFragment = new BorrowingBookFragment();
        gaveBackBookFragment = new GaveBackBookFragment();
        fragmentLists = new ArrayList<>();
        fragmentLists.add(borrowingBookFragment);
        fragmentLists.add(gaveBackBookFragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentLists);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, BorrowBookActivity.class);
        context.startActivity(intent);
    }
}

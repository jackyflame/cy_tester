package com.cyztc.app.views.grades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.widget.ywl5320.NavitationLayout;
import com.cyztc.app.widget.ywl5320.TabNavitationLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/11/2.
 */

public class GradesFragment extends BaseFragment {

    @BindView(R.id.tnavLayout)
    NavitationLayout navitationLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

//    private String[] titles = new String[]{"班级公告", "班级论坛", "班级风采", "学习感情"};
//    private List<Fragment> fragmentList;
//    private ViewPagerAdapter viewPagerAdapter;

    private String[] titles = new String[]{"班级信息", "班级管理", "班级活动", "宣传之窗", "学习感悟"};
    private List<Fragment> fragmentList;
    private ViewPagerAdapter viewPagerAdapter;
    private int type = 0;
    private String traidId;


    private ClassesElegantFragment learnSensibility;
    private ClassesElegantFragment learnClasses;
    private ClassesElegantFragment learnProduce;
    private ClassesElegantFragment learnActivity;
    private ClassesElegantFragment learnWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_class_layout);
        this.traidId = CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("班级");
//        setMenuView(R.mipmap.icon_classes_notice);
        initFragments();
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();
        showToast("public");
    }

    public void initFragments() {
//        fragmentList = new ArrayList<>();
//        fragmentList.add(new ClassesElegantFragment());
//        fragmentList.add(new ClassesElegantFragment());
//        fragmentList.add(new ClassesElegantFragment());
//        fragmentList.add(new ClassesElegantFragment());
//
//        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
//        viewPager.setAdapter(viewPagerAdapter);
//        viewPager.setOffscreenPageLimit(4);
//
//        navitationLayout.setViewPager(getActivity(), titles, viewPager, R.color.color_333333, R.color.color_12a7e2, 16, 16, 0, 0, true);
//        navitationLayout.setNavLine(getActivity(), 2, R.color.color_12a7e2, 0);

        fragmentList = new ArrayList<>();
        learnSensibility = new ClassesElegantFragment();
        learnSensibility.setType(1);
        learnSensibility.setTrainId(traidId);

        learnClasses = new ClassesElegantFragment();
        learnClasses.setType(2);
        learnClasses.setTrainId(traidId);

        learnProduce = new ClassesElegantFragment();
        learnProduce.setType(3);
        learnProduce.setTrainId(traidId);

        learnActivity = new ClassesElegantFragment();
        learnActivity.setType(4);
        learnActivity.setTrainId(traidId);

        learnWindow = new ClassesElegantFragment();
        learnWindow.setType(5);
        learnWindow.setTrainId(traidId);


        fragmentList.add(learnClasses);
        fragmentList.add(learnProduce);
        fragmentList.add(learnActivity);
        fragmentList.add(learnWindow);
        fragmentList.add(learnSensibility);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        navitationLayout.setViewPager(getActivity(), titles, viewPager, R.color.color_333333, R.color.color_12a7e2, 14, 14, type, 0, true);
        navitationLayout.setNavLine(getActivity(), 2, R.color.color_12a7e2, 0);
    }
}

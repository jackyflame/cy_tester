package com.cyztc.app.views.grades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Window;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.dialog.ClassPublishDialog;
import com.cyztc.app.views.home.tribe.ClassPublishTopicActivity;
import com.cyztc.app.widget.ywl5320.NavitationLayout;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ServiceTypeDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/12/27.
 */

public class GradesActivity extends BaseActivity{

    @BindView(R.id.tnavLayout)
    NavitationLayout navitationLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

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
        type = getIntent().getIntExtra("type", 0);
        traidId = getIntent().getStringExtra("traidId");
        setContentView(R.layout.fragment_class_layout);
        setBackView();
        setMenuView(R.mipmap.icon_classes_notice);
        setTitle("班级");
        initFragments();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();

        final ClassPublishDialog classPublishDialog = new ClassPublishDialog(this);
        Window window = classPublishDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        classPublishDialog.show();
        classPublishDialog.setOnClassTypeListener(new ClassPublishDialog.OnClassTypeListener() {
            @Override
            public void onTypeResult(int type) {
                classPublishDialog.dismiss();
                ClassPublishTopicActivity.startActivity(GradesActivity.this, type);
            }
        });
    }

    public static void startActivity(Context context, int type, String trainId)
    {
        Intent intent = new Intent(context, GradesActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("traidId", trainId);
        context.startActivity(intent);
    }

    public void initFragments()
    {
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

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        navitationLayout.setViewPager(this, titles, viewPager, R.color.color_333333, R.color.color_12a7e2, 14, 14, type, 0, true);
        navitationLayout.setNavLine(this, 2, R.color.color_12a7e2, 0);
    }
}

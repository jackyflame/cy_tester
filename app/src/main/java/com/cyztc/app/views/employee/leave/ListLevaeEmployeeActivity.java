package com.cyztc.app.views.employee.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.widget.CustomViewPager;
import com.cyztc.app.widget.ywl5320.NavitationLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2017-5-14.
 */

public class ListLevaeEmployeeActivity extends BaseActivity{

    @BindView(R.id.navBar)
    NavitationLayout navBar;
    @BindView(R.id.viewpager)
    CustomViewPager customViewPager;

    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments;
    private String[] titles = new String[]{"我提交的", "待我审批"};
    private LeaveListFragment leaveMyFragment;
    private LeaveListFragment leaveNeedMeFragment;

    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", -1);
        setContentView(R.layout.activity_empliee_levave_layout);
        setTitle(CommonUtil.getTitle(type));
        setBackView();
        setTextMenuView("申请");

        initFragments();
        navBar.setViewPager(this, titles, customViewPager, R.color.color_666666, R.color.color_333333, 12, 12, 0, 0, true);
        navBar.setBgLine(this, 1, R.color.color_white);
        navBar.setNavLine(this, 3, R.color.color_12a7e2, 0);
    }

    private void initFragments()
    {
        leaveMyFragment = new LeaveListFragment();
        leaveNeedMeFragment = new LeaveListFragment();

        leaveMyFragment.setType(type, 0);
        leaveNeedMeFragment.setType(type, 1);
        leaveNeedMeFragment.setIsmine(true);

        fragments = new ArrayList<>();
        fragments.add(leaveMyFragment);
        fragments.add(leaveNeedMeFragment);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        customViewPager.setAdapter(viewPagerAdapter);
        customViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(type == 1) {
            LeaveApplyActivity.startActivity(this);
        }
        else if(type == 2)
        {
            TravelApplyActivity.startActivity(this);
        }
        else if(type == 3)
        {
            SealApplyActivity.startActivity(this);
        }
        else if(type == 4)
        {
            CarApplyActivity.startActivity(this);
        }
    }

    public static void statrActivity(Context context, int type)
    {
        Intent intent = new Intent(context, ListLevaeEmployeeActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(myLeaveListFragment != null)
//        {
//            myLeaveListFragment.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}

/**
 *
 * open  class Consts{
 open class Staff{
 static let ApplyTypes=["","请假申请","差旅申请","印章申请","派车申请"]
 static let ApplyTypeTitles=["","请假","差旅","印章","派车"]
 static let ApplySubTypes=[[],
 ["事假","病假","年假","其它"],
 ["普通出差","其它"],
 ["公章","财务章","其它"],
 ["轿车","商务车","大巴车","其它"]
 ]
 static let ApplyStatusColors=[UIColor.fromString(hex: "#40b8e7"),
 UIColor.fromString(hex: "#40b8e7"),
 UIColor.fromString(hex: "#e8595b"),
 UIColor.fromString(hex: "#4fd1a1")]
 static let ApplyStatus=["待审批","审核中","审核不通过","审核通过"]
 static let ApplyStateLineColors=[UIColor.fromString(hex: "#dbdbdb"),UIColor.green,UIColor.fromString(hex: "#1CF93E"),UIColor.green]
 static let ApplyStateImage=[UIImage(named:"imgWaitApply"),UIImage(named:"imgAgree"),UIImage(named:"imgDisagree")]
 }

 }
 *
 */

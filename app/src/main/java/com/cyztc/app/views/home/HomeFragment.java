package com.cyztc.app.views.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BannerBean;
import com.cyztc.app.bean.BannerBeanP;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.EatScanBean;
import com.cyztc.app.bean.HomeItemBean;
import com.cyztc.app.bean.ScanSigninBean;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.cyztc.app.views.employee.CarDeployActivity;
import com.cyztc.app.views.employee.EatActivity;
import com.cyztc.app.views.employee.leave.ListLevaeEmployeeActivity;
import com.cyztc.app.views.grades.GradesActivity;
import com.cyztc.app.views.grades.TimeTableActivity;
import com.cyztc.app.views.home.adapter.HomeMenuAdapter;
import com.cyztc.app.views.home.life.LiveHelerActivity;
import com.cyztc.app.views.home.shop.ShopListActivity;
import com.cyztc.app.views.home.tribe.TribeActivity;
import com.cyztc.app.widget.CustomGridView;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.cyztc.app.widget.ywl5320.adviewpager.AdViewpagerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karics.library.zxing.android.CaptureActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/2.
 */

public class HomeFragment extends BaseFragment{

    @BindView(R.id.adviewpager)
    ViewPager adViewpager;
    @BindView(R.id.lydot)
    LinearLayout lyDot;
    @BindView(R.id.tv_menu_str)
    TextView tvMenuStr;

    @BindView(R.id.gridview1)
    CustomGridView customGridView1;
    @BindView(R.id.gridview2)
    CustomGridView customGridView2;
    @BindView(R.id.gridview3)
    CustomGridView customGridView3;
    @BindView(R.id.springview)
    SpringView springView;

    private HomeMenuAdapter homeMenuAdapter1;
    private HomeMenuAdapter homeMenuAdapter2;
    private HomeMenuAdapter homeMenuAdapter3;

    private List<HomeItemBean> menus1;
    private List<HomeItemBean> menus2;
    private List<HomeItemBean> menus3;

    private List<HomeItemBean> sourse2;
    private List<HomeItemBean> sourse3;

    private List<BannerBean> bannerBeens;
    private AdViewpagerUtil adViewpagerUtil;
    private Map<String, Integer> hdots;

    private static final int REQUESE_2 = 0x002;
    private static final int REQUESE_3 = 0x003;
    private static final int REQUEST_SCAN_CODE = 0x004;

    private static final int REQUEST_CAMERA_CODE = 0x0005;
    private boolean isUpdate = false;
    private int dotType = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hdots = new HashMap<String, Integer>();
        setTitle("首页");
        getBanners();
        if(CyApplication.getInstance().isStudent())
        {
            setStudentMenu();
            setAdapter3();
            tvMenuStr.setText("学员工具");
            dotType = 2;
        }
        else
        {
            setElpee();
            setAdapter3Em();
            tvMenuStr.setText("员工工具");
            dotType = 3;
        }

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
//        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                getBanners();
            }

            @Override
            public void onLoadmore() {
            }
        });
    }

    private void setStudentMenu()
    {
        menus1 = new ArrayList<>();

        HomeItemBean homeItemBean1 = new HomeItemBean();
        homeItemBean1.setIcon("icon_msg_push");
        homeItemBean1.setName("推送通知");
        menus1.add(homeItemBean1);

        HomeItemBean homeItemBean2 = new HomeItemBean();
        homeItemBean2.setIcon("icon_home_online");
        homeItemBean2.setName("在线报名");
        menus1.add(homeItemBean2);

//        HomeItemBean homeItemBean3 = new HomeItemBean();
//        homeItemBean3.setIcon("icon_tribe");
//        homeItemBean3.setName("兴趣部落");
//        menus1.add(homeItemBean3);
//
//        HomeItemBean homeItemBean4 = new HomeItemBean();
//        homeItemBean4.setIcon("icon_shop");
//        homeItemBean4.setName("翠月网店");
//        menus1.add(homeItemBean4);

        HomeItemBean homeItemBean5 = new HomeItemBean();
        homeItemBean5.setIcon("icon_circle");
        homeItemBean5.setName("翠月动态");
        menus1.add(homeItemBean5);

        HomeItemBean homeItemBean6 = new HomeItemBean();
        homeItemBean6.setIcon("icon_home_server");
        homeItemBean6.setName("园区地图");
        menus1.add(homeItemBean6);

        HomeItemBean homeItemBean8 = new HomeItemBean();
        homeItemBean8.setIcon("icon_home_phone");
        homeItemBean8.setName("服务电话");
        menus1.add(homeItemBean8);

        homeMenuAdapter1 = new HomeMenuAdapter(getActivity(), menus1);
        customGridView1.setAdapter(homeMenuAdapter1);

        customGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeItemBean homeItemBean = (HomeItemBean) customGridView1.getItemAtPosition(i);
                if("更多".equals(homeItemBean.getName()))
                {
                }
                else {
                    if("推送通知".equals(homeItemBean.getName()))
                    {
                        MessageActivity.startActivity(getActivity());
                    }
                    else if("在线报名".equals(homeItemBean.getName()))
                    {
                        ClassEnlistActivity.startActivity(getActivity());
                    }
                    else if("兴趣部落".equals(homeItemBean.getName()))
                    {
                        TribeActivity.startActivity(getActivity());
                    }
                    else if("翠月网店".equals(homeItemBean.getName()))
                    {
                        ShopListActivity.startActivity(getActivity());
                    }
                    else if("翠月动态".equals(homeItemBean.getName()))
                    {
                        CuiYueCircleActivity.startActivity(getActivity(), 1);
                    }
                    else if("服务电话".equals(homeItemBean.getName()))
                    {
                        UsedPhoneActivity.startActivity(getActivity());
                    }
                    else if("园区地图".equals(homeItemBean.getName()))
                    {
                        ServiceInfoActivity.startActivity(getActivity());
                    }
                }
            }
        });
    }

    private void setElpee()
    {

        menus1 = new ArrayList<>();

        HomeItemBean homeItemBean1 = new HomeItemBean();
        homeItemBean1.setIcon("icon_msg_push");
        homeItemBean1.setName("推送通知");
        menus1.add(homeItemBean1);

        HomeItemBean homeItemBean2 = new HomeItemBean();
        homeItemBean2.setIcon("icon_home_online");
        homeItemBean2.setName("在线报名");
        menus1.add(homeItemBean2);

//        HomeItemBean homeItemBean3 = new HomeItemBean();
//        homeItemBean3.setIcon("icon_tribe");
//        homeItemBean3.setName("兴趣部落");
//        menus1.add(homeItemBean3);
//
//        HomeItemBean homeItemBean4 = new HomeItemBean();
//        homeItemBean4.setIcon("icon_shop");
//        homeItemBean4.setName("翠月网店");
//        menus1.add(homeItemBean4);

        HomeItemBean homeItemBean5 = new HomeItemBean();
        homeItemBean5.setIcon("icon_circle");
        homeItemBean5.setName("翠月动态");
        menus1.add(homeItemBean5);

        HomeItemBean homeItemBean6 = new HomeItemBean();
        homeItemBean6.setIcon("icon_home_server");
        homeItemBean6.setName("园区地图");
        menus1.add(homeItemBean6);

        HomeItemBean homeItemBean8 = new HomeItemBean();
        homeItemBean8.setIcon("icon_home_phone");
        homeItemBean8.setName("服务电话");
        menus1.add(homeItemBean8);

        homeMenuAdapter1 = new HomeMenuAdapter(getActivity(), menus1);
        customGridView1.setAdapter(homeMenuAdapter1);

        customGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeItemBean homeItemBean = (HomeItemBean) customGridView1.getItemAtPosition(i);
                if("推送通知".equals(homeItemBean.getName()))
                {
                    MessageActivity.startActivity(getActivity());
                }
                else if("电子门锁".equals(homeItemBean.getName()))
                {
                    OpenDoorActivity.startActivity(getActivity(), 1);
                }
                else if("在线报名".equals(homeItemBean.getName()))
                {
                    ClassEnlistActivity.startActivity(getActivity());
                }
                else if("兴趣部落".equals(homeItemBean.getName()))
                {
                    TribeActivity.startActivity(getActivity());
                }
                else if("翠月网店".equals(homeItemBean.getName()))
                {
                    ShopListActivity.startActivity(getActivity());
                }
                else if("翠月动态".equals(homeItemBean.getName()))
                {
                    CuiYueCircleActivity.startActivity(getActivity(), 1);
                }
                else if("服务电话".equals(homeItemBean.getName()))
                {
                    UsedPhoneActivity.startActivity(getActivity());
                }
                else if("园区地图".equals(homeItemBean.getName()))
                {
                    ServiceInfoActivity.startActivity(getActivity());
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
//        getNotReadMessage();
        getHotDots(CyApplication.getInstance().getUserBean().getAccountId());
    }


    public void setAdapter3Em()
    {
        menus3 = new ArrayList<>();
        String menujson3 = SharedpreferencesUtil.readString(getActivity(), "homemenu", "menu3-4ee");
        if(TextUtils.isEmpty(menujson3))
        {
            menujson3 = readRawFile(R.raw.homemenuemee);
        }
        Gson gson = new Gson();
        sourse3 = gson.fromJson(menujson3, new TypeToken<List<HomeItemBean>>(){}.getType());
        menus3.addAll(getSelectedItems(sourse3));

        HomeItemBean homeItemBean11 = new HomeItemBean();
        homeItemBean11.setIcon("icon_home_more");
        homeItemBean11.setName("更多");
        menus3.add(homeItemBean11);

        homeMenuAdapter3 = new HomeMenuAdapter(getActivity(), menus3);
        customGridView3.setAdapter(homeMenuAdapter3);

        customGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeItemBean homeItemBean = (HomeItemBean) customGridView3.getItemAtPosition(i);
//                if(CyApplication.getInstance().getUserBean().getIsAmount() == 0)
//                {
//                    showAskDialog("提示", "你的主课程还没有报名，还有权限查看此项目", "报名", "取消", new NormalAskComplexDialog.OnDalogListener() {
//                        @Override
//                        public void onYes() {
//                            TrainMainInfoActivity.startActivity(getActivity());
//                        }
//
//                        @Override
//                        public void OnNo() {
//                        }
//                    });
//                    return;
//                }

                if("更多".equals(homeItemBean.getName()))
                {
                    Gson gson = new Gson();
                    Intent intent = new Intent(getActivity(), MoreItemMenuActivity.class);
                    intent.putExtra("menustr", gson.toJson(sourse3));
                    getActivity().startActivityForResult(intent, REQUESE_3);
                }
                else {
                    if("翠月网店".equals(homeItemBean.getName()))
                    {
                        ShopListActivity.startActivity(getActivity());
                    }
                    else if("课程安排".equals(homeItemBean.getName()))
                    {
                        TimeTableActivity.startActivity(getActivity());
                    }
                    else if("生活助手".equals(homeItemBean.getName()))
                    {
                        LiveHelerActivity.startActivity(getActivity());
                    }
                    else if("在线报名".equals(homeItemBean.getName()))
                    {

                    }
                    else if("通讯指南".equals(homeItemBean.getName()))
                    {
                        ContactActivity.startActivity(getActivity(), 1);
                    }
                    else if("培训评估".equals(homeItemBean.getName()))
                    {
                        TrainLevelActivity.startActivity(getActivity());
                    }
                    else if("名字待定".equals(homeItemBean.getName()))
                    {
                        ContactActivity.startActivity(getActivity(), 0);
                    }
                    else if("历史培训".equals(homeItemBean.getName()))
                    {
                        TrainHistoryActivity.startActivity(getActivity());
                    }
                    else if("学习感悟".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 4, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("班级信息".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 0, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("班级管理".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 1, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("班级活动".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 2, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("宣传之窗".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 3, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("园区地图".equals(homeItemBean.getName()))
                    {
                        ServiceInfoActivity.startActivity(getActivity());
                    }
                    else if("电子门锁".equals(homeItemBean.getName()))
                    {
                        OpenDoorActivity.startActivity(getActivity(), 1);
                    }
                    else if("请假申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 1);
                    }
                    else if("印章申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 3);
                    }
                    else if("差旅申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 2);
                    }
                    else if("派车申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 4);
//                        CarDeployActivity.startActivity(getActivity());
                    }
                    else if("员工用餐".equals(homeItemBean.getName()))
                    {
                        EatActivity.startActivit(getActivity());
//                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                        {
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
//                        }
//                        else {
//                            Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                            intent.putExtra("type", 1);
//                            getActivity().startActivityForResult(intent, REQUEST_SCAN_CODE);
//                        }
                    }
                    else if("部门动态".equals(homeItemBean.getName()))
                    {
                        CuiYueCircleActivity.startActivity(getActivity(), 2);
                    }
                    else if("员工考勤".equals(homeItemBean.getName()))
                    {
                        EmSigninActivity.startActivity(getActivity(), 1);
                    }
                }
            }
        });
    }

    public void setAdapter3()
    {
        menus3 = new ArrayList<>();

        String menujson3 = SharedpreferencesUtil.readString(getActivity(), "homemenu", "menu3-4");
        if(TextUtils.isEmpty(menujson3))
        {
            menujson3 = readRawFile(R.raw.homemenu3_4);
        }
        Gson gson = new Gson();
        sourse3 = gson.fromJson(menujson3, new TypeToken<List<HomeItemBean>>(){}.getType());
        menus3.addAll(getSelectedItems(sourse3));

        HomeItemBean homeItemBean11 = new HomeItemBean();
        homeItemBean11.setIcon("icon_home_more");
        homeItemBean11.setName("更多");
        menus3.add(homeItemBean11);

        homeMenuAdapter3 = new HomeMenuAdapter(getActivity(), menus3);
        customGridView3.setAdapter(homeMenuAdapter3);

        customGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeItemBean homeItemBean = (HomeItemBean) customGridView3.getItemAtPosition(i);
                if(CyApplication.getInstance().getUserBean().getIsAmount() == 0)
                {
                    showAskDialog("提示", "你的主课程还没有报名，还有权限查看此项目", "报名", "取消", new NormalAskComplexDialog.OnDalogListener() {
                        @Override
                        public void onYes() {
                            TrainMainInfoActivity.startActivity(getActivity());
                        }

                        @Override
                        public void OnNo() {
                        }
                    });
                    return;
                }

                if("更多".equals(homeItemBean.getName()))
                {
                    Gson gson = new Gson();
                    Intent intent = new Intent(getActivity(), MoreItemMenuActivity.class);
                    intent.putExtra("menustr", gson.toJson(sourse3));
                    getActivity().startActivityForResult(intent, REQUESE_3);
                }
                else {
                    if("翠月网店".equals(homeItemBean.getName()))
                    {
                        ShopListActivity.startActivity(getActivity());
                    }
                    else if("课程安排".equals(homeItemBean.getName()))
                    {
                        TimeTableActivity.startActivity(getActivity());
                    }
                    else if("生活助手".equals(homeItemBean.getName()))
                    {
                        LiveHelerActivity.startActivity(getActivity());
                    }
                    else if("在线报名".equals(homeItemBean.getName()))
                    {

                    }
                    else if("通讯指南".equals(homeItemBean.getName()))
                    {
                        ContactActivity.startActivity(getActivity(), 1);
                    }
                    else if("培训评估".equals(homeItemBean.getName()))
                    {
                        TrainLevelActivity.startActivity(getActivity());
                    }
                    else if("名字待定".equals(homeItemBean.getName()))
                    {
                        ContactActivity.startActivity(getActivity(), 0);
                    }
                    else if("历史培训".equals(homeItemBean.getName()))
                    {
                        TrainHistoryActivity.startActivity(getActivity());
                    }
                    else if("学习感悟".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 4, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("班级信息".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 0, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("班级管理".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 1, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("班级活动".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 2, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("宣传之窗".equals(homeItemBean.getName()))
                    {
                        GradesActivity.startActivity(getActivity(), 3, CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId());
                    }
                    else if("园区地图".equals(homeItemBean.getName()))
                    {
                        ServiceInfoActivity.startActivity(getActivity());
                    }
                    else if("电子考勤".equals(homeItemBean.getName()))
                    {
                        SigninActivity.startActivity(getActivity(), 0);
                    }
                    else if("电子门锁".equals(homeItemBean.getName()))
                    {
                        OpenDoorActivity.startActivity(getActivity(), 0);
                    }
                    else if("请假申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 1);
                    }
                    else if("印章申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 3);
                    }
                    else if("差旅申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 2);
                    }
                    else if("派车申请".equals(homeItemBean.getName()))
                    {
                        ListLevaeEmployeeActivity.statrActivity(getActivity(), 4);
                    }
                    else if("餐厅用餐".equals(homeItemBean.getName()))
                    {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
                        }
                        else {
                            Intent intent = new Intent(getActivity(), CaptureActivity.class);
                            intent.putExtra("type", 1);
                            getActivity().startActivityForResult(intent, REQUEST_SCAN_CODE);
                        }
                    }
                    else if("部门动态".equals(homeItemBean.getName()))
                    {
                        CuiYueCircleActivity.startActivity(getActivity(), 2);
                    }
                    else if("上班考勤".equals(homeItemBean.getName()))
                    {
                        EmSigninActivity.startActivity(getActivity(), 1);
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUESE_2) {
                String str = data.getStringExtra("resultdata");
                Gson gson = new Gson();
                sourse2 = gson.fromJson(str, new TypeToken<List<HomeItemBean>>(){}.getType());
                menus2.clear();
                menus2.addAll(getSelectedItems(sourse2));
                HomeItemBean homeItemBean8 = new HomeItemBean();
                homeItemBean8.setIcon("icon_home_more");
                homeItemBean8.setName("更多");
                menus2.add(homeItemBean8);
                homeMenuAdapter2.notifyDataSetChanged();
            } else if (requestCode == REQUESE_3) {
                String str = data.getStringExtra("resultdata");
                Gson gson = new Gson();
                sourse3 = gson.fromJson(str, new TypeToken<List<HomeItemBean>>(){}.getType());
                menus3.clear();
                menus3.addAll(getSelectedItems(sourse3));
                HomeItemBean homeItemBean11 = new HomeItemBean();
                homeItemBean11.setIcon("icon_home_more");
                homeItemBean11.setName("更多");
                menus3.add(homeItemBean11);
                homeMenuAdapter3.notifyDataSetChanged();
            }
            else if(requestCode == REQUEST_SCAN_CODE)
            {
                String scanResult = data.getStringExtra("codedContent");
                Gson gson = new Gson();
                if(scanResult.startsWith("{") && scanResult.endsWith("}"))
                {
                    EatScanBean eatScanBean = gson.fromJson(scanResult, EatScanBean.class);
                    if(eatScanBean != null && !TextUtils.isEmpty(eatScanBean.getCode()))
                    {
                        showToast("验证中");
                        EmployeeApi.getInstance().dinning(CyApplication.getInstance().getUserBean().getAccountId(), eatScanBean.getCode(), new HttpSubscriber<String>(new SubscriberOnListener<String>() {
                            @Override
                            public void onSucceed(String data) {
                                showToast("就餐验证成功");
                                hideLoadDialog();
                            }

                            @Override
                            public void onError(int code, String msg) {
                                showToast(msg);
                                hideLoadDialog();
                            }
                        }, getActivity()));
                    }
                    else
                    {
                        showToast("请扫描正确的用餐二维码");
                    }
                }
                else
                {
                    showToast("请扫描正确的用餐二维码");
                }
            }
        }
    }

    private List<HomeItemBean> getSelectedItems(List<HomeItemBean> homeItemBeens)
    {
        List<HomeItemBean> list = new ArrayList<>();
        for(HomeItemBean homeItemBean : homeItemBeens)
        {
            if(homeItemBean.isSelected())
            {
                list.add(homeItemBean);
            }
        }
        return list;
    }

    public String readRawFile(int rawId)
    {
        String content;
        Resources resources=this.getResources();
        InputStream is=null;
        try{
            is=resources.openRawResource(rawId);
            byte buffer[]=new byte[is.available()];
            is.read(buffer);
            content=new String(buffer);
            return content;
        }
        catch(IOException e)
        {
        }
        finally
        {
            if(is!=null)
            {
                try{
                    is.close();
                }catch(IOException e)
                {

                }
            }
        }
        return "";
    }

    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        if(sourse2 != null) {
            if(CyApplication.getInstance().isStudent())
            {
                SharedpreferencesUtil.write(getActivity(), "homemenu", "menu2", gson.toJson(sourse2));
            }
            else
            {
                SharedpreferencesUtil.write(getActivity(), "homemenu", "menu2ee", gson.toJson(sourse2));
            }

        }
        if(sourse3 != null) {
            if(CyApplication.getInstance().isStudent()) {
                SharedpreferencesUtil.write(getActivity(), "homemenu", "menu3-4", gson.toJson(sourse3));
            }
            else
            {
                SharedpreferencesUtil.write(getActivity(), "homemenu", "menu3-4ee", gson.toJson(sourse3));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getBanners()
    {
        HomeApi.getInstance().getBanners(CyApplication.getInstance().getUserBean().getStudentId(), 0, 4, new HttpSubscriber<BannerBeanP>(new SubscriberOnListener<BannerBeanP>() {
            @Override
            public void onSucceed(BannerBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(!isUpdate) {
                        isUpdate = true;
                        bannerBeens = new ArrayList<>();
                        bannerBeens.addAll(data.getData());
                        adViewpagerUtil = new AdViewpagerUtil(getActivity(), adViewpager, lyDot, 6, 6, bannerBeens);
                        adViewpagerUtil.initVps();
                        adViewpagerUtil.setOnAdItemClickListener(new AdViewpagerUtil.OnAdItemClickListener() {
                            @Override
                            public void onItemClick(BannerBean bannerBean) {
                                CuiYueCircleDetailActivity.startActivity(getActivity(), bannerBean.getId(), dotType);
                            }
                        });
                    }
                    else
                    {
                        adViewpagerUtil.updateAdImgs(data.getData());
                    }
                }
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onError(int code, String msg) {
                springView.onFinishFreshAndLoad();
            }
        }, getActivity()));
    }

    public void getNotReadMessage()
    {
        HomeApi.getInstance().getMessageCount(CyApplication.getInstance().getUserBean().getAccountId(), new HttpSubscriber<Boolean>(new SubscriberOnListener<Boolean>() {
            @Override
            public void onSucceed(Boolean data) {
                for(HomeItemBean homeItemBean : menus1)
                {
                    if(homeItemBean.getName().equals("推送通知"))
                    {
                        if(data)
                        {
                            homeItemBean.setShowDot(true);
                        }
                        else
                        {
                            homeItemBean.setShowDot(false);
                        }
                    }
                }
                homeMenuAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, getActivity()));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == REQUEST_CAMERA_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("type", 1);
                getActivity().startActivityForResult(intent, REQUEST_SCAN_CODE);
            } else
            {
                showToast("请允许打开摄像头权限");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getHotDots(String accountId)
    {
        UserApi.getInstance().getUserDots(accountId, new HttpSubscriber<Map<String, Integer>>(new SubscriberOnListener<Map<String, Integer>>() {
            @Override
            public void onSucceed(Map<String, Integer> data) {
                hdots.clear();
                hdots.putAll(data);
                getDots(hdots);
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, getActivity()));
    }

    public void getDots(Map<String, Integer> data)
    {
        if(data == null)
            return;
        for(HomeItemBean homeItemBean : menus1)
        {
            homeItemBean.setShowDot(false);
        }
        for(HomeItemBean homeItemBean : menus3)
        {
            homeItemBean.setShowDot(false);
        }
        for(String key : data.keySet())
        {
            MyLog.d("key:" + key);
            for(HomeItemBean homeItemBean : menus1) {
                if ("1".equals(key) && homeItemBean.getName().equals("推送消息")) {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("2".equals(key) && homeItemBean.getName().equals("翠月动态"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("3".equals(key) && homeItemBean.getName().equals("部门动态"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("4".equals(key) && homeItemBean.getName().equals("请假申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("5".equals(key) && homeItemBean.getName().equals("差旅申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("6".equals(key) && homeItemBean.getName().equals("派车申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("7".equals(key) && homeItemBean.getName().equals("印章申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("8".equals(key) && homeItemBean.getName().equals("学习感悟"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("8".equals(key) && homeItemBean.getName().equals("学习感悟"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("9".equals(key) && homeItemBean.getName().equals("班级信息"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("10".equals(key) && homeItemBean.getName().equals("班级管理"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("11".equals(key) && homeItemBean.getName().equals("班级活动"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("12".equals(key) && homeItemBean.getName().equals("宣传之窗"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
            }



            for(HomeItemBean homeItemBean : menus3) {
                if ("1".equals(key) && homeItemBean.getName().equals("推送消息")) {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("2".equals(key) && homeItemBean.getName().equals("翠月动态"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("3".equals(key) && homeItemBean.getName().equals("部门动态"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("4".equals(key) && homeItemBean.getName().equals("请假申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("5".equals(key) && homeItemBean.getName().equals("差旅申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("6".equals(key) && homeItemBean.getName().equals("派车申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("7".equals(key) && homeItemBean.getName().equals("印章申请"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("8".equals(key) && homeItemBean.getName().equals("学习感悟"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("8".equals(key) && homeItemBean.getName().equals("学习感悟"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("9".equals(key) && homeItemBean.getName().equals("班级信息"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("10".equals(key) && homeItemBean.getName().equals("班级管理"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("11".equals(key) && homeItemBean.getName().equals("班级活动"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
                else if ("12".equals(key) && homeItemBean.getName().equals("宣传之窗"))
                {
                    if(data.get(key) > 0) {
                        homeItemBean.setShowDot(true);
                    }
                    else
                    {
                        homeItemBean.setShowDot(false);
                    }
                }
            }



        }
        homeMenuAdapter1.notifyDataSetChanged();
        homeMenuAdapter3.notifyDataSetChanged();
    }



}

package com.cyztc.app.views.home.life;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.OrderBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.widget.ywl5320.CountView;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/30.
 */

public class WashServiceActivity extends BaseActivity{

    @BindView(R.id.countview)
    CountView countView;
    @BindView(R.id.countview2)
    CountView countView2;
    @BindView(R.id.countview3)
    CountView countView3;
    @BindView(R.id.countview4)
    CountView countView4;
    @BindView(R.id.et_remark)
    EditText etRemark;

    private HashMap<String, Integer> goods;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_service_layout);
        setTitle("洗衣服务");
        setTextMenuView("提交");
        setBackView();
        goods = new HashMap<>();
        countView.initDatas(R.drawable.count_drawable_left, R.drawable.count_drawable_mid, R.drawable.count_drawable_right, 0, 100, 0);
        countView2.initDatas(R.drawable.count_drawable_left, R.drawable.count_drawable_mid, R.drawable.count_drawable_right, 0, 100, 0);
        countView3.initDatas(R.drawable.count_drawable_left, R.drawable.count_drawable_mid, R.drawable.count_drawable_right, 0, 100, 0);
        countView4.initDatas(R.drawable.count_drawable_left, R.drawable.count_drawable_mid, R.drawable.count_drawable_right, 0, 100, 0);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        goods.clear();
        if(countView.getCount() > 0)
        {
            goods.put("601DC13A-DDF7-4280-9C67-8BF1795C6749", countView.getCount());
        }
        if(countView2.getCount() > 0)
        {
            goods.put("6FEB3AC8-911A-4BD8-9F06-932915D4F38A", countView2.getCount());
        }
        if(countView3.getCount() > 0)
        {
            goods.put("035E9E92-204E-4B7B-9B3E-C63DF828D2B2", countView3.getCount());
        }
        if(countView4.getCount() > 0)
        {
            goods.put("82264EA6-7068-42A4-8BA0-0440300A7915", countView4.getCount());
        }
        if(goods.size() > 0) {
            String remark = etRemark.getText().toString().trim();
            submitService(CyApplication.getInstance().getUserBean().getBedRoom(), "", goods, remark, "");
        }
        else
        {
            showToast("请选择服务项");
        }
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, WashServiceActivity.class);
        context.startActivity(intent);
    }

    public void submitService(String address, String picture, HashMap<String, Integer> goods, String remark, String subType)
    {
        HomeApi.getInstance().createOrder(CyApplication.getInstance().getUserBean().getStudentId(), 2, goods, address, 0, 0, 0, picture, remark, subType, "", new HttpSubscriber<String>(new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                hideLoadDialog();
                showToast("申请提交成功");
                WashServiceActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, WashServiceActivity.this));
    }

}

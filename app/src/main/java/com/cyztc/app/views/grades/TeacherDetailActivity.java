package com.cyztc.app.views.grades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.TeacherInfoBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/27.
 */

public class TeacherDetailActivity extends BaseActivity{


    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_head)
    ImageView ivHead;


    private String teacherId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info_layout);
        teacherId = getIntent().getStringExtra("teacherId");
        setBackView();
        getTeacherInfo(teacherId);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, String teacherId)
    {
        Intent intent = new Intent(context, TeacherDetailActivity.class);
        intent.putExtra("teacherId", teacherId);
        context.startActivity(intent);
    }

    public void getTeacherInfo(String teacherId)
    {
        HomeApi.getInstance().getTeacherInfo(teacherId, new HttpSubscriber<TeacherInfoBean>(new SubscriberOnListener<TeacherInfoBean>() {
            @Override
            public void onSucceed(TeacherInfoBean data) {
                if(data != null) {
                    ImageLoad.getInstance().displayCircleImage(TeacherDetailActivity.this, ivHead, HttpMethod.IMG_URL + data.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
                    tvName.setText(data.getName());
                    tvRemark.setText(data.getRemark());
                    tvPhone.setText(data.getPhone());
                    tvEmail.setText(data.getEmail());
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }
}

package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;

/**
 * Created by ywl on 2016/12/31.
 */

public class DaibiaoKnowActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daibiao_know_layout);
        setTitle("代表须知");
        setBackView();
        getMeetingNotice("");
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, DaibiaoKnowActivity.class);
        context.startActivity(intent);
    }

    public void getMeetingNotice(String meetingId)
    {
        HomeApi.getInstance().getMeetingNotice(meetingId, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }
}

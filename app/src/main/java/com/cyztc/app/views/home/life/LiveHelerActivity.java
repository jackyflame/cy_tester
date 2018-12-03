package com.cyztc.app.views.home.life;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/18.
 */

public class LiveHelerActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_helper_layout);
        setTitle("生活助手");
        setBackView();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.rl_kefang)
    public void onClickKefang(View view)
    {
        RoomServiceActivity.startActivity(this, 4);
    }

    @OnClick(R.id.rl_meet)
    public void onClickMeet(View view)
    {
        RoomServiceActivity.startActivity(this, 5);
    }

    @OnClick(R.id.rl_yiliao)
    public void onClickYiliao(View view)
    {

    }

    @OnClick(R.id.rl_wash)
    public void onClickWash(View view)
    {

    }

    @OnClick(R.id.rl_shop)
    public void onClickShop(View view)
    {

    }

    @OnClick(R.id.rl_fix)
    public void onClickFix(View view)
    {

    }


    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, LiveHelerActivity.class);
        context.startActivity(intent);
    }
}

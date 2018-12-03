package com.cyztc.app.views.home.life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.views.home.shop.ShopListActivity;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/27.
 */

public class LiveHelperFragment extends BaseFragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_helper_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("生活助手");
    }

    @OnClick(R.id.rl_kefang)
    public void onClickKefang(View view)
    {
        RoomServiceActivity.startActivity(getActivity(), 4);
    }

    @OnClick(R.id.rl_meet)
    public void onClickMeet(View view)
    {
        RoomServiceActivity.startActivity(getActivity(), 5);
    }

    @OnClick(R.id.rl_yiliao)
    public void onClickYiliao(View view)
    {
        RoomServiceActivity.startActivity(getActivity(), 6);
    }

    @OnClick(R.id.rl_wash)
    public void onClickWash(View view)
    {
        RoomServiceActivity.startActivity(getActivity(), 2);
    }

    @OnClick(R.id.rl_shop)
    public void onClickShop(View view)
    {
        ShopListActivity.startActivity(getActivity());
    }

    @OnClick(R.id.rl_fix)
    public void onClickFix(View view)
    {
        RoomServiceActivity.startActivity(getActivity(), 3);
    }
}

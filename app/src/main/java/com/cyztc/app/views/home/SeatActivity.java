package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;

/**
 * Created by ywl on 2016/12/31.
 */

public class SeatActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_layout);
        setTitle("座次安排");
        setBackView();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, SeatActivity.class);
        context.startActivity(intent);
    }
}

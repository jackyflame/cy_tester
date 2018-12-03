package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;

/**
 * Created by ywl on 2016/12/20.
 */

public class StudentDailyTimeActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dailytime_layout);
        setBackView();
        setTitle("学员考勤");
        setTextMenuView("考勤记录");
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, StudentDailyTimeActivity.class);
        context.startActivity(intent);
    }
}

package com.cyztc.app.views.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/31.
 */

public class UpdatePwdActivity extends BaseActivity{

    @BindView(R.id.et_oldpwd)
    EditText etOldPwd;
    @BindView(R.id.et_newpwd)
    EditText etNewPwd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd_layout);
        setTitle("修改密码");
        setBackView();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_submit)
    public void onClickSubmit(View view)
    {
        String oldpwd = etOldPwd.getText().toString().trim();
        String newpwd = etNewPwd.getText().toString().trim();
        if(TextUtils.isEmpty(oldpwd))
        {
            showToast("请输入原始密码");
            return;
        }
        if(oldpwd.length() < 6)
        {
            showToast("原始密码位数少于6位");
            return;
        }
        if(TextUtils.isEmpty(newpwd))
        {
            showToast("请输入新密码");
            return;
        }
        if(newpwd.length() < 8)
        {
            showToast("新密码位数不能少于8位");
            return;
        }
        showLoadDialog("正在修改密码");
        updatePwd(CyApplication.getInstance().getUserBean().getAccountId(), oldpwd, newpwd);
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, UpdatePwdActivity.class);
        context.startActivity(intent);
    }

    public void updatePwd(String accountId, String oldPwd, String newPwd)
    {
        UserApi.getInstance().updatePwd(accountId, oldPwd, newPwd, new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {
                showToast("修改成功");
                hideLoadDialog();
                UpdatePwdActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
            }
        }, this));
    }
}

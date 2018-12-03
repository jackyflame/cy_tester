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
import com.cyztc.app.bean.EventBusBean;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.TrainMainInfoActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/31.
 */

public class ResetPwdActivity extends BaseActivity{

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_cardid)
    EditText etCardId;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.et_name)
    EditText etName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd_layout);
        setTitle("重置密码");
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
//        final String account = etAccount.getText().toString().trim();
//        String idcardid = etCardId.getText().toString().trim();
//        String phonenum = etPhonenum.getText().toString().trim();
//        String name = etName.getText().toString().trim();
////
//        if(TextUtils.isEmpty(account))
//        {
//            showToast("请输入账号");
//            return;
//        }
//        if(TextUtils.isEmpty(idcardid))
//        {
//            showToast("请输入身份证号");
//            return;
//        }
//        if(TextUtils.isEmpty(phonenum))
//        {
//            showToast("请输入电话号码");
//            return;
//        }
//        if(TextUtils.isEmpty(name))
//        {
//            showToast("请输入姓名");
//            return;
//        }
        showAskDialog("提示", "您的密码将被重置", "重置", "取消", new NormalAskComplexDialog.OnDalogListener() {
            @Override
            public void onYes() {
                showLoadDialog("正在重置密码");
                resetPwd(CyApplication.getInstance().getUserBean().getAccount(), "", "", "");
            }

            @Override
            public void OnNo() {
            }
        });
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        context.startActivity(intent);
    }

    public void resetPwd(String account, String cardid, String phonenum, String name)
    {
        UserApi.getInstance().resetPwd(account, cardid, phonenum, name, new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                showToast("重置密码成功");
                hideLoadDialog();
                EventBus.getDefault().post(new EventBusBean(1, true));
                LoginActivity.startActivity(ResetPwdActivity.this);
                ResetPwdActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, this));
    }
}

package com.cyztc.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.TrainingInfoBean;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.constant.Const;
import com.cyztc.app.constant.Constant;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.RSAUtils;
import com.cyztc.app.utils.SafeUtils;
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.cyztc.app.views.user.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by ywl on 2017/1/15.
 */

public class LoadingActivity extends BaseActivity {

  private String account;
  private String pwd;
  private boolean autoLogin;
  private boolean isempe;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading_layout);
    boolean check = SafeUtils.apkVerifyWithCRC(this,getString(R.string.classesdex_crc));
    if(check){
      account = SharedpreferencesUtil.readString(this, Constant.SP_USER, Constant.SP_USER_ACCOUNT);
      pwd = SharedpreferencesUtil.readString(this, Constant.SP_USER, Constant.SP_USER_PWD);
      autoLogin =  SharedpreferencesUtil.readBoolean(this, Constant.SP_USER, Constant.SP_USER_AUTOLOGIN,false);
      isempe = SharedpreferencesUtil.readBoolean(this, Constant.SP_USER, Constant.SP_USER_RMEMP, false);
      handler.postDelayed(runnable, 2000);
    }else{
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("警告")
              .setMessage("客户端可能被篡改，请重新安装最新的版本")
              .setCancelable(false)
              .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
                  finish();
                }
              }).create().show();
    }
  }

  Handler handler = new Handler();
  Runnable runnable = new Runnable() {
    @Override
    public void run() {
      if (autoLogin)//自动登录
      {
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
          //                    boolean isempe = SharedpreferencesUtil.readBoolean(LoadingActivity.this, Constant.SP_USER, Constant.SP_USER_STUDENTLOGIN, false);
          MyLog.d("isloginStudent:" + isempe);
          int loginType = 0;
          if (!isempe) {
            loginType = 0;
          } else {
            loginType = 1;
          }
          loginBuAccount(account, pwd, CommonUtil.getDeviceID(LoadingActivity.this), loginType);
        } else {
          LoginActivity.startActivity(LoadingActivity.this);
          LoadingActivity.this.finish();
        }
      } else {
        LoginActivity.startActivity(LoadingActivity.this);
        LoadingActivity.this.finish();
      }
    }
  };

  public void loginBuAccount(final String account, final String pwd, String deviceID,
      int loginType) {
    UserApi.getInstance()
        .loginAccount(account, pwd, deviceID, loginType,
            new HttpSubscriber<String>(new SubscriberOnListener<String>() {
              @Override
              public void onSucceed(String content) {
                String result = RSAUtils.decryptRSA(content);
                LogUtils.i(result);
                if (TextUtils.isEmpty(result)) {
                  hideLoadDialog();
                  showToast("登录失败");
                  return;
                }
                Gson gson = new Gson();
                UserBean data = null;
                try {
                  data = gson.fromJson(result, new TypeToken<UserBean>() {
                  }.getType());
                } catch (Exception e) {
                  e.printStackTrace();
                  hideLoadDialog();
                  showToast("登录失败");
                  return;
                }
                if (data == null) {
                  hideLoadDialog();
                  showToast("登录失败");
                  return;
                }
                JPushInterface.setAliasAndTags(getApplicationContext(), data.getAccount(), null,
                    new TagAliasCallback() {
                      @Override
                      public void gotResult(int i, String s, Set<String> set) {

                      }
                    });
                SPUtils.getInstance().put(Const.SP_ACCOUNT_ID, data.getAccountId());
                SPUtils.getInstance().put(Const.SP_TOKEN, data.getToken());
                SPUtils.getInstance()
                    .put(Const.SP_TOKEN_ENCRYPT, RSAUtils.encryptRSA(data.getToken()));
                SharedpreferencesUtil.write(LoadingActivity.this, Constant.SP_USER,
                    Constant.SP_USER_LOGIN_DATA, result);
                CyApplication.getInstance().setUserBean(data);
                if (CyApplication.getInstance().isStudent()) {
                  getTranningInfos();
                } else {
                  MainActivity.staticActivity(LoadingActivity.this);
                  LoadingActivity.this.finish();
                }
              }

              @Override
              public void onError(int code, String msg) {
                LoginActivity.startActivity(LoadingActivity.this);
                LoadingActivity.this.finish();
              }
            }, this));
  }

  public void getTranningInfos() {
    ClassApi.getInstance()
        .getMyTrainingInfos(CyApplication.getInstance().getUserBean().getAccountId(),
            new HttpSubscriber<TrainingInfoBean>(new SubscriberOnListener<TrainingInfoBean>() {
              @Override
              public void onSucceed(TrainingInfoBean data) {
                CyApplication.getInstance().setTrainingInfoBean(data);
                MainActivity.staticActivity(LoadingActivity.this);
                LoadingActivity.this.finish();
              }

              @Override
              public void onError(int code, String msg) {
                LoginActivity.startActivity(LoadingActivity.this);
                LoadingActivity.this.finish();
              }
            }, this));
  }

  @Override
  public void onBackPressed() {
    //        super.onBackPressed();
  }

  public void check() {
    String apkPath = getPackageCodePath();
    Long dexCrc = Long.parseLong(getString(R.string.classesdex_crc));
    try {
      ZipFile zipfile = new ZipFile(apkPath);
      ZipEntry dexentry = zipfile.getEntry("classes.dex");
      Log.i("verification", "classes.dexcrc=" + dexentry.getCrc());
      if (dexentry.getCrc() != dexCrc) {
        Log.i("verification", "Dexhas been modified!");
      } else {
        Log.i("verification", "Dex hasn't been modified!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

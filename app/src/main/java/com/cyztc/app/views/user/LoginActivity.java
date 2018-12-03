package com.cyztc.app.views.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.cyztc.app.CyApplication;
import com.cyztc.app.MainActivity;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.EventBusBean;
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
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ywl on 2016/11/7.
 */
public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

  private static final int RC_PREMISSIONS_PERM = 124;
  private static final String[] PREMISSIONS =
      {Manifest.permission.READ_PHONE_STATE};
  @BindView(R.id.et_account)
  EditText etAccount;
  @BindView(R.id.et_pwd)
  EditText etPwd;
  @BindView(R.id.tv_login)
  TextView tvLogin;
  @BindView(R.id.ly_remember_pwd)
  LinearLayout lyRememberPwd;
  @BindView(R.id.iv_remember_pwd)
  ImageView ivRememberPwd;
  @BindView(R.id.iv_student)
  ImageView ivStudent;
  @BindView(R.id.iv_eplee)
  ImageView ivEplee;
  @BindView(R.id.switch_empe)
  SwitchButton btnEmpe;
  @BindView(R.id.switch_rempwd)
  SwitchButton btnRmPwd;
  @BindView(R.id.switch_autologin)
  SwitchButton btnAutoLogin;
  private boolean isRememberPwd = true;
  private int loginType = 0; //0学院 1员工
  private boolean autoLogin;
  private boolean isempe;
  private boolean isrmpwd;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    setContentView(R.layout.activity_login_layout);
    setTitle("登录");
    autoLogin =
        SharedpreferencesUtil.readBoolean(this, Constant.SP_USER, Constant.SP_USER_AUTOLOGIN,
            false);
    isempe =
        SharedpreferencesUtil.readBoolean(this, Constant.SP_USER, Constant.SP_USER_RMEMP, false);
    isrmpwd =
        SharedpreferencesUtil.readBoolean(this, Constant.SP_USER, Constant.SP_USER_RMPWD, false);
    String account =
        SharedpreferencesUtil.readString(this, Constant.SP_USER, Constant.SP_USER_ACCOUNT);
    String pwd = SharedpreferencesUtil.readString(this, Constant.SP_USER, Constant.SP_USER_PWD);
    if (autoLogin)//自动登录
    {
      btnAutoLogin.setChecked(true);
    } else {
      btnAutoLogin.setChecked(false);
    }
    if (!TextUtils.isEmpty(account)) {
      etAccount.setText(account);
    }
    if (isrmpwd) {
      btnRmPwd.setChecked(true);
      if (!TextUtils.isEmpty(pwd)) {
        etPwd.setText(pwd);
      }
    } else {
      btnRmPwd.setChecked(false);
    }
    if (!isempe) {
      setStudent(0);
    } else {
      setStudent(1);
    }
    permissionsTask();
  }

  @Override
  public void onClickBack() {
    super.onClickBack();
    this.finish();
  }

  @OnClick(R.id.ly_remember_pwd)
  public void onClickRememberPwd(View view) {
    isRememberPwd = !isRememberPwd;
    if (isRememberPwd) {
      ivRememberPwd.setImageResource(R.mipmap.icon_auto_login_selected);
    } else {
      ivRememberPwd.setImageResource(R.mipmap.icon_auto_login_unselected);
    }
  }

  @OnClick(R.id.ly_student)
  public void onClickStudent(View view) {
    setStudent(0);
  }

  @OnClick(R.id.ly_eplee)
  public void onClickEplee(View view) {
    setStudent(1);
  }

  @OnClick(R.id.tv_login)
  public void onClickLogin(View view) {
    String account = etAccount.getText().toString().trim();
    String pwd = etPwd.getText().toString().trim();
    if (TextUtils.isEmpty(account)) {
      showToast("请输入用户名");
      etAccount.requestFocus();
    } else if (TextUtils.isEmpty(pwd)) {
      showToast("请输入密码");
      etPwd.requestFocus();
    } else if (pwd.length() < 6) {
      showToast("密码不能少于6位");
      etPwd.requestFocus();
    } else {
      if (hasPermissions()) {
        loginBuAccount(account, pwd, CommonUtil.getDeviceID(this));
      } else {
        permissionsTask();
      }
    }
  }
  //    @OnClick(R.id.tv_resetpwd)
  //    public void onClickResetPwd(View veiw)
  //    {
  //        ResetPwdActivity.startActivity(this);
  //    }

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    context.startActivity(intent);
  }

  public void loginBuAccount(final String account, final String pwd, String deviceID) {
    if (btnEmpe.isChecked()) {
      loginType = 1;
    } else {
      loginType = 0;
    }
    showLoadDialog("登录中");
    UserApi.getInstance()
        .loginAccount(account, pwd, deviceID, loginType,
            new HttpSubscriber<String>(new SubscriberOnListener<String>() {
              @Override
              public void onSucceed(String content) {
                MyLog.d(content);
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
                if (btnAutoLogin.isChecked())//自动登录
                {
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_AUTOLOGIN, true);
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_ACCOUNT, account);
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_PWD, pwd);
                } else {
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_AUTOLOGIN, false);
                }
                if (btnEmpe.isChecked()) {
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_RMEMP, true);
                } else {
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_RMEMP, false);
                }
                if (btnRmPwd.isChecked()) {
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_ACCOUNT, account);
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_PWD, pwd);
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_RMPWD, true);
                } else {
                  SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                      Constant.SP_USER_RMPWD, false);
                }
                //                SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER, Constant.SP_USER_ACCOUNT, account);
                //                SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER, Constant.SP_USER_PWD, pwd);
                //                SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER, Constant.SP_USER_REMEMBERPWD, isRememberPwd);
                //                SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER, Constant.SP_USER_STUDENTLOGIN, isStudentLogin);
                SharedpreferencesUtil.write(LoginActivity.this, Constant.SP_USER,
                    Constant.SP_USER_LOGIN_DATA, result);
                CyApplication.getInstance().setUserBean(data);
                if (CyApplication.getInstance().isStudent()) {
                  getTranningInfos();
                } else {
                  MainActivity.staticActivity(LoginActivity.this);
                  hideLoadDialog();
                  LoginActivity.this.finish();
                }
              }

              @Override
              public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
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
                MainActivity.staticActivity(LoginActivity.this);
                hideLoadDialog();
                LoginActivity.this.finish();
              }

              @Override
              public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
              }
            }, this));
  }

  /**
   * 设置通知栏样式 - 定义通知栏Layout
   */
  private void setStyleCustom() {
    CustomPushNotificationBuilder builder =
        new CustomPushNotificationBuilder(this, R.layout.customer_notitfication_layout, R.id.icon,
            R.id.title, R.id.text);
    builder.layoutIconDrawable = R.drawable.icon_unselected;
    builder.developerArg0 = "翠月直通车";
    JPushInterface.setPushNotificationBuilder(2, builder);
    //        Toast.makeText(PushSetActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void eventMsg(EventBusBean messBean) {
    if (messBean.getType() == 1) {
      this.finish();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private void setStudent(int type) {
    if (type == 0) {
      //            ivStudent.setSelected(true);
      //            ivEplee.setSelected(false);
      loginType = 0;
      isempe = false;
      btnEmpe.setChecked(false);
    } else {
      //            ivStudent.setSelected(false);
      //            ivEplee.setSelected(true);
      loginType = 1;
      isempe = true;
      btnEmpe.setChecked(true);
    }
  }

  @Override
  public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
  }

  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
  }

  @AfterPermissionGranted(RC_PREMISSIONS_PERM)
  public void permissionsTask() {
    if (!hasPermissions()) {
      // Ask for both permissions
      EasyPermissions.requestPermissions(
          this,
          "App需要您开启权限",
          RC_PREMISSIONS_PERM,
          PREMISSIONS);
    }
  }

  private boolean hasPermissions() {
    return EasyPermissions.hasPermissions(this, PREMISSIONS);
  }
}

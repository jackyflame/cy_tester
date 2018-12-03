package com.cyztc.app.views.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import butterknife.OnClick;
import com.blankj.utilcode.util.ActivityUtils;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.EventBusBean;
import com.cyztc.app.bean.PgyUpdateBean;
import com.cyztc.app.constant.Constant;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.google.gson.Gson;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ywl on 2017/1/10.
 */
public class SettingActivity extends BaseActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    setContentView(R.layout.activity_setting_layout);
    setBackView();
    setTitle("设置");
  }

  @Override
  public void onClickBack() {
    super.onClickBack();
    this.finish();
  }

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, SettingActivity.class);
    context.startActivity(intent);
  }

  @OnClick(R.id.rl_resetpwd)
  public void onClickResetPwd(View view) {
    ResetPwdActivity.startActivity(this);
  }

  @OnClick(R.id.rl_updatepwd)
  public void onClickUpdatePwd(View view) {
    UpdatePwdActivity.startActivity(this);
  }

  @OnClick(R.id.rl_head)
  public void onClickUpdateHead(View view) {
    UpdateUserInfoActivity.startActivity(this);
  }

  @OnClick(R.id.tv_loginout)
  public void onClickLoginOut(View view) {
    UserApi.getInstance().logout(new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
      @Override
      public void onSucceed(Object data) {
      }

      @Override
      public void onError(int code, String msg) {
      }
    }, getApplicationContext()));
    MyLog.d("isloginStudent:" + SharedpreferencesUtil
        .readBoolean(SettingActivity.this, Constant.SP_USER, Constant.SP_USER_RMEMP, false));
//        SharedpreferencesUtil.write(SettingActivity.this, Constant.SP_USER, Constant.SP_USER_ACCOUNT, "");
//        SharedpreferencesUtil.write(SettingActivity.this, Constant.SP_USER, Constant.SP_USER_PWD, "");
//        SharedpreferencesUtil.write(SettingActivity.this, Constant.SP_USER, Constant.SP_USER_REMEMBERPWD, false);
    CommonUtil.loginOut(getApplicationContext());
    ActivityUtils.startActivity(LoginActivity.class);
    finish();
    MyLog.d("isloginStudent:" + SharedpreferencesUtil
        .readBoolean(SettingActivity.this, Constant.SP_USER, Constant.SP_USER_RMEMP, false));
  }

  @OnClick(R.id.rl_update)
  public void onClickUpdate(View view) {
    showLoadDialog("检查更新中");
    checkUpdate();
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

  private void checkUpdate() {
    PgyUpdateManager.register(SettingActivity.this,
        getString(R.string.provider_file),
        new UpdateManagerListener() {
          @Override
          public void onUpdateAvailable(final String result) {
            hideLoadDialog();
            MyLog.d(result);
            JSONObject jsonData;
            try {
              jsonData = new JSONObject(result);
              if ("0".equals(jsonData.getString("code"))) {
                JSONObject jsonObject = jsonData.getJSONObject("data");
                String data = jsonObject.toString();
                MyLog.d(data);
                Gson gson = new Gson();
                final PgyUpdateBean pgyUpdateBean = gson.fromJson(data, PgyUpdateBean.class);
                MyLog.d(pgyUpdateBean);
                if (TextUtils.isEmpty(pgyUpdateBean.getReleaseNote())) {
                  pgyUpdateBean.setReleaseNote("1、优化新能\n2、修改若干问题");
                }
                showAskDialog(true, "有新版本", pgyUpdateBean.getReleaseNote(), "更新", "取消",
                    new NormalAskComplexDialog.OnDalogListener() {
                      @Override
                      public void onYes() {
                        startDownloadTask(SettingActivity.this, pgyUpdateBean.getDownloadURL());
                      }

                      @Override
                      public void OnNo() {
                      }
                    });
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override
          public void onNoUpdateAvailable() {
            hideLoadDialog();
            Toast.makeText(getApplicationContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
          }
        });
  }
}

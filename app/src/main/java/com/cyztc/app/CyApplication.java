package com.cyztc.app;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.cyztc.app.bean.TrainingInfoBean;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.constant.Const;
import com.cyztc.app.constant.Constant;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.RSAUtils;
import com.cyztc.app.utils.SharedpreferencesUtil;
import com.cyztc.app.views.user.LoginActivity;
import com.facebook.stetho.Stetho;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import org.xutils.x;

/**
 * Created by ywl on 2016/11/2.
 */
public class CyApplication extends Application {

  private static CyApplication instance;
  private UserBean userBean;
  private TrainingInfoBean trainingInfoBean;
  private OnPayListener onPayListener;
  protected String userAgent;
  private String baseFileUrl = "";

  @Override
  public void onCreate() {
    super.onCreate();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
      StrictMode.setVmPolicy(builder.build());
    }
    instance = this;
    Utils.init(this);
    AppUtils.registerAppStatusChangedListener(CyApplication.class.getName(),
        new Utils.OnAppStatusChangedListener() {
          @Override
          public void onForeground() {
            long starTime = SPUtils.getInstance().getLong(Const.SP_ENTER_APP_BACKGROUND_START_TIME);
            if (System.currentTimeMillis() - starTime >= Const.MINUTE_30) {
              CommonUtil.loginOut(getApplicationContext());
              ActivityUtils.startActivity(LoginActivity.class);
            }
          }

          @Override
          public void onBackground() {
            SPUtils.getInstance()
                .put(Const.SP_ENTER_APP_BACKGROUND_START_TIME, System.currentTimeMillis());
          }
        });
    x.Ext.init(this);
    x.Ext.setDebug(!BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    JPushInterface.setDebugMode(true);
    JPushInterface.init(this);
    SDKInitializer.initialize(this);
    userAgent = Util.getUserAgent(this, "ExoPlayerPlayer");
    if (BuildConfig.IS_DEBUG) {
      Stetho.initializeWithDefaults(this);
    }
  }

  public static CyApplication getInstance() {
    if (instance == null) {
      instance = new CyApplication();
    }
    return instance;
  }

  public UserBean getUserBean() {
    if (userBean != null) {
      return userBean;
    } else {
      String userdata = SharedpreferencesUtil.readString(this, Constant.SP_USER, Constant.SP_USER_LOGIN_DATA);
      Gson gson = new Gson();
      userBean = gson.fromJson(RSAUtils.decryptRSA(userdata), UserBean.class);
      return userBean;
    }
  }

  public void setUserBean(UserBean userBean) {
    SharedpreferencesUtil.write(this, Constant.SP_USER, Constant.SP_USER_LOGIN_DATA, "");
    this.userBean = userBean;
  }

  public static void setInstance(CyApplication instance) {
    CyApplication.instance = instance;
  }

  public TrainingInfoBean getTrainingInfoBean() {
    return trainingInfoBean == null ? new TrainingInfoBean() : trainingInfoBean;
  }

  public void setTrainingInfoBean(TrainingInfoBean trainingInfoBean) {
    this.trainingInfoBean = trainingInfoBean;
  }

  public boolean isLogin() {
    if (this.userBean != null) {
      return true;
    }
    return false;
  }

  public OnPayListener getOnPayListener() {
    return onPayListener;
  }

  public void setOnPayListener(OnPayListener onPayListener) {
    this.onPayListener = onPayListener;
  }

  public interface OnPayListener {

    void onPayResult(int code, String msg);
  }

  public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultDataSourceFactory(this, bandwidthMeter,
        buildHttpDataSourceFactory(bandwidthMeter));
  }

  public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getBaseFileUrl() {
    return baseFileUrl;
  }

  public void setBaseFileUrl(String baseFileUrl) {
    this.baseFileUrl = baseFileUrl;
  }

  public boolean isStudent() {
    if (userBean != null) {
//      if (TextUtils.isEmpty(userBean.getTrainingInfoId()) && TextUtils.isEmpty(
//          userBean.getTrainingInfoName())) {
//        return false;
//      }
      return true;
    }
    return false;
  }

  public boolean isStudentForLogin() {
    if (userBean != null) {
      if (TextUtils.isEmpty(userBean.getTrainingInfoId()) && TextUtils.isEmpty(
              userBean.getTrainingInfoName())) {
        return false;
      }
      return true;
    }
    return false;
  }
}

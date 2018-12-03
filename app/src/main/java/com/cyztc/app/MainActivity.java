package com.cyztc.app;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseFileUrlBean;
import com.cyztc.app.bean.EventBusBean;
import com.cyztc.app.bean.PgyUpdateBean;
import com.cyztc.app.bus.RxBus;
import com.cyztc.app.bus.RxBusResult;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.grades.GradesFragment;
import com.cyztc.app.views.home.HomeFragment;
import com.cyztc.app.views.home.TrainMainInfoActivity;
import com.cyztc.app.views.home.life.LiveHelperFragment;
import com.cyztc.app.views.knowledge.KnowFragment;
import com.cyztc.app.views.user.MeFragment;
import com.cyztc.app.widget.ywl5320.TabLayoutView;
import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    @BindView(R.id.tabview)
    TabLayoutView tabLayoutView;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private long firstTime = 0;

    private List<Fragment> fragments;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] tabimgs = {R.drawable.home_tab_selector, R.drawable.class_tab_selector, R.drawable.know_tab_selector, R.drawable.me_tab_selector};
    private HomeFragment homeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        tabLayoutView.setDataSource(tabimgs, 4, 0);
        tabLayoutView.setImageStyle(CommonUtil.dip2px(this, 42), CommonUtil.dip2px(this, 42));
        tabLayoutView.initDatas();
        tabLayoutView.setOnItemOnclickListener(new TabLayoutView.OnItemOnclickListener() {
            @Override
            public void onItemClick(int index) {
                viewPager.setCurrentItem(index, false);
            }
        });
        initFragments();

//        showToast(CommonUtil.getIpAddress(this));

        RxBus.getInstance().toObserverableOnMainThread("loginout", new RxBusResult() {
            @Override
            public void onRxBusResult(Object o) {
                boolean isLoginout = (boolean) o;
                if(isLoginout)
                {
                    MainActivity.this.finish();
                }
            }
        });
        getBaseFileUrl();
//        checkUpdate();
    }

    public void initFragments()
    {
        homeFragment = new HomeFragment();
        fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(new LiveHelperFragment());
        fragments.add(new GradesFragment());
        fragments.add(new MeFragment());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayoutView.setSelectStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeFragment.onActivityResult(requestCode, resultCode, data);
    }

    public static void staticActivity(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
                showToast("再按一次退出程序");
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                MainActivity.this.finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public void getBaseFileUrl()
    {

        RequestParams requestParams = new RequestParams(HttpMethod.BASE_URL + "fileBaseDirectory.do");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                BaseFileUrlBean basefileurl = gson.fromJson(result, BaseFileUrlBean.class);
                if(basefileurl.isSuccess())
                {
                    //downloadFile(basefileurl.getData() + videoDetailBean.getFile(), getFilePath(videoDetailBean.getFile()));
                    CyApplication.getInstance().setBaseFileUrl(basefileurl.getData());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsg(EventBusBean messBean) {
        if(messBean.getType() == 1) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //版本检查更新注释
//        PgyUpdateManager.unregister();
    }

    private void checkUpdate()
    {
        PgyUpdateManager.register(MainActivity.this,
                getString(R.string.provider_file),
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        MyLog.d(result);
                        JSONObject jsonData;
                        try {
                            jsonData = new JSONObject(result);
                            if ("0".equals(jsonData.getString("code")))
                            {
                                JSONObject jsonObject = jsonData.getJSONObject("data");
                                String data = jsonObject.toString();
                                MyLog.d(data);
                                Gson gson = new Gson();
                                final PgyUpdateBean pgyUpdateBean = gson.fromJson(data, PgyUpdateBean.class);
                                MyLog.d(pgyUpdateBean);
                                if(TextUtils.isEmpty(pgyUpdateBean.getReleaseNote()))
                                {
                                    pgyUpdateBean.setReleaseNote("1、优化新能\n2、修改若干问题");
                                }
                                showAskDialog(true, "有新版本", pgyUpdateBean.getReleaseNote(), "更新", "取消", new NormalAskComplexDialog.OnDalogListener() {
                                    @Override
                                    public void onYes() {
                                        startDownloadTask(MainActivity.this,pgyUpdateBean.getDownloadURL());
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
//                        Toast.makeText(getApplicationContext(),"没有更新",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

package com.cyztc.app.views.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CorseBean;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.bean.EmSignDetailBean;
import com.cyztc.app.bean.ScanSigninBean;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.TimeUtil;
import com.cyztc.app.views.grades.adapter.SigninAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.google.gson.Gson;
import com.karics.library.zxing.android.CaptureActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/28.
 */

public class EmSigninActivity extends BaseActivity{

    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.iv_start)
    ImageView ivStart;
    @BindView(R.id.iv_end)
    ImageView ivEnd;
    @BindView(R.id.tv_start_status)
    TextView tvStartStatus;
    @BindView(R.id.tv_end_status)
    TextView tvEndStatus;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;

    private TextView tvWeek;
    private TextView tvDate;

    private String nowDate;

    private boolean isLoading = false;

    private static final int REQUEST_CAMERA_CODE = 0x0002;
    private static final int REQUEST_SCAN_CODE = 0x0003;
    private static final int REQUEST_HISTORY_CODE = 0x0004;
    private int type;

    private boolean signAm = false;
//    private boolean signPm = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", -1);
        setContentView(R.layout.activity_emsingnin_layout);
        tvWeek = (TextView) findViewById(R.id.tv_week);
        tvDate = (TextView) findViewById(R.id.tv_date);
        nowDate = TimeUtil.getDate2();
        setBackView();
        if(type == 0) {
            setTitle("学员考勤");
        }
        else if(type == 1)
        {
            setTitle("员工考勤");
        }
        setTextMenuView("考勤记录");

        tvWeek.setText(TimeUtil.getWeekDay());
        tvDate.setText(TimeUtil.getDate());


        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
//        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    isLoading = true;
                    getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
                }
            }
        });
        showDataLoadWrongMsg("数据加载中");
        getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
    }

    @OnClick(R.id.rl_item1)
    public void onClickSinin(View view)
    {
        if(!signAm)
        {
            showLoadDialog("签到中");
            ClassApi.getInstance().getEmSign(CyApplication.getInstance().getUserBean().getAccountId(), CommonUtil.getConnectWifiSsid(this), 1, TimeUtil.getNowDate(), CommonUtil.getIpAddress(this), new HttpSubscriber<EmSignDetailBean>(new SubscriberOnListener() {
                @Override
                public void onSucceed(Object data) {
                    hideLoadDialog();
                    showToast("签到成功");
                    if(!isLoading) {
                        isLoading = true;
                        getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    hideLoadDialog();
                    showToast(msg);
                }
            }, this));
        }
        else
        {
            showToast("您已签到了");
        }

    }

    @OnClick(R.id.rl_item2)
    public void onClickSinoff(View view)
    {
//        if(!signPm)
        {
            showLoadDialog("签到中");
            ClassApi.getInstance().getEmSign(CyApplication.getInstance().getUserBean().getAccountId(), CommonUtil.getConnectWifiSsid(this), 2, TimeUtil.getNowDate(), CommonUtil.getIpAddress(this), new HttpSubscriber<EmSignDetailBean>(new SubscriberOnListener() {
                @Override
                public void onSucceed(Object data) {
                    hideLoadDialog();
                    showToast("签到成功");
                    if(!isLoading) {
                        isLoading = true;
                        getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    hideLoadDialog();
                    showToast(msg);
                }
            }, this));
        }
    }


    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        Intent intent = new Intent(this, EmSigninHistoryActivity.class);
        startActivityForResult(intent,REQUEST_HISTORY_CODE);
    }

    @OnClick(R.id.iv_scan_signin)
    public void onClickScanSignin(View view)
    {
        if (ContextCompat.checkSelfPermission(EmSigninActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(EmSigninActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }
        else
        {
            Intent intent = new Intent(EmSigninActivity.this, CaptureActivity.class);
            intent.putExtra("type", 0);
            EmSigninActivity.this.startActivityForResult(intent, REQUEST_SCAN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_SCAN_CODE)
            {
                String scanResult = data.getStringExtra("codedContent");
                Gson gson = new Gson();
                if(scanResult.startsWith("{") && scanResult.endsWith("}"))
                {
                    ScanSigninBean scanSigninBean = gson.fromJson(scanResult, ScanSigninBean.class);
                    if(scanSigninBean != null && !TextUtils.isEmpty(scanSigninBean.getTrainingId())) {
                        showLoadDialog("签到中");
                        UserApi.getInstance().sign(CyApplication.getInstance().getUserBean().getAccountId(), scanSigninBean.getTrainingId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
                            @Override
                            public void onSucceed(Object data) {
                                showToast("签到成功");
                                if (!isLoading) {
                                    getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
                                }
                                hideLoadDialog();
                            }

                            @Override
                            public void onError(int code, String msg) {
                                showToast(msg);
                                hideLoadDialog();
                            }
                        }, EmSigninActivity.this));
                    }
                    else
                    {
                        showToast("请扫描正确的签到二维码");
                    }
                }
                else
                {
                    showToast("请扫描正确的签到二维码");
                }
            }
            else if(requestCode == REQUEST_HISTORY_CODE)
            {
                nowDate = data.getStringExtra("date");
                String week = data.getStringExtra("week");
                tvWeek.setText(week);
                String d[] = nowDate.split("-");
                if(d.length == 3) {
                    tvDate.setText(d[0] + "年" + d[1] + "月" + d[2] + "日");
                }
                isLoading = true;
                getCorses(CyApplication.getInstance().getUserBean().getAccountId(), nowDate);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == REQUEST_CAMERA_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(EmSigninActivity.this, CaptureActivity.class);
                intent.putExtra("type", 0);
                EmSigninActivity.this.startActivityForResult(intent, REQUEST_SCAN_CODE);
            } else
            {
                showToast("请允许打开摄像头权限");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 0:学员
     * 1:员工
     * @param context
     * @param type
     */
    public static void startActivity(Context context, int type)
    {
        Intent intent = new Intent(context, EmSigninActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public void getCorses(String accountId, String date)
    {
        showDataLoadWrongMsg("数据加载中");
        isLoading = true;
        ClassApi.getInstance().getEmCourses(accountId, date, new HttpSubscriber<EmSignDetailBean>(new SubscriberOnListener<EmSignDetailBean>() {
            @Override
            public void onSucceed(EmSignDetailBean data) {
                if(data != null)
                {
                    initData(data);
                    hideDataLoadMsg();
                }
                else
                {
                    //showDataLoadWrongMsg("没有签到数据");
                    hideDataLoadMsg();
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                showDataLoadWrongMsg(msg);
                isLoading = false;
            }
        }, this));
    }

    private void initData(EmSignDetailBean data)
    {
        if(!TextUtils.isEmpty(data.getInTime()))
        {
            ivStart.setImageResource(R.mipmap.icon_em_start);
            tvStartStatus.setText("已签到(" + data.getInTime() + ")");
            iv1.setVisibility(View.VISIBLE);
            signAm = true;
        }
        else
        {
            ivStart.setImageResource(R.mipmap.icon_em_start_no);
            tvStartStatus.setText("点击签到");
            iv1.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(data.getOffTime()))
        {
            ivEnd.setImageResource(R.mipmap.icon_em_end);
            tvEndStatus.setText("已签到(" + data.getOffTime() + ")");
            iv2.setVisibility(View.VISIBLE);
//            signPm = true;
        }
        else
        {
            ivEnd.setImageResource(R.mipmap.icon_em_end_no);
            tvEndStatus.setText("点击签到");
            iv2.setVisibility(View.GONE);
        }
    }

}

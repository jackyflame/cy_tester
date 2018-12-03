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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CorseBean;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.bean.ScanSigninBean;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
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

public class SigninActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private TextView tvWeek;
    private TextView tvDate;

    private LayoutInflater headerInflater;
    private View headView;

    private List<CorseBean> datas;
    private SigninAdapter signinAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    private static final int REQUEST_CAMERA_CODE = 0x0002;
    private static final int REQUEST_SCAN_CODE = 0x0003;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", -1);
        headerInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_singnin_layout);
        headView = headerInflater.inflate(R.layout.item_signin_header_layout, null);
        tvWeek = (TextView) headView.findViewById(R.id.tv_week);
        tvDate = (TextView) headView.findViewById(R.id.tv_date);
        AutoUtils.auto(headView);
        listView.addHeaderView(headView);

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
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getCorses(CyApplication.getInstance().getUserBean().getAccountId());
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getCorses(CyApplication.getInstance().getUserBean().getAccountId());
                }
            }
        });
        showDataLoadMsg("数据加载中");
        setAdapter();
        getCorses(CyApplication.getInstance().getUserBean().getAccountId());
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        SigninHistoryActivity.startActivity(this);
    }

    @OnClick(R.id.iv_scan_signin)
    public void onClickScanSignin(View view)
    {
        if (ContextCompat.checkSelfPermission(SigninActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SigninActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }
        else
        {
            Intent intent = new Intent(SigninActivity.this, CaptureActivity.class);
            intent.putExtra("type", 0);
            SigninActivity.this.startActivityForResult(intent, REQUEST_SCAN_CODE);
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
                                    index = 0;
                                    getCorses(CyApplication.getInstance().getUserBean().getAccountId());
                                }
                                hideLoadDialog();
                            }

                            @Override
                            public void onError(int code, String msg) {
                                showToast(msg);
                                hideLoadDialog();
                            }
                        }, SigninActivity.this));
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == REQUEST_CAMERA_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(SigninActivity.this, CaptureActivity.class);
                intent.putExtra("type", 0);
                SigninActivity.this.startActivityForResult(intent, REQUEST_SCAN_CODE);
            } else
            {
                showToast("请允许打开摄像头权限");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        signinAdapter = new SigninAdapter(this, datas);
        listView.setAdapter(signinAdapter);
    }

    /**
     * 0:学员
     * 1:员工
     * @param context
     * @param type
     */
    public static void startActivity(Context context, int type)
    {
        Intent intent = new Intent(context, SigninActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public void getCorses(String accountId)
    {
        showDataLoadMsg("数据加载中");
        isLoading = true;
        ClassApi.getInstance().getCourses(accountId, TimeUtil.getDate2(), index, pagesize, new HttpSubscriber<CorseBeanP>(new SubscriberOnListener<CorseBeanP>() {
            @Override
            public void onSucceed(CorseBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        datas.clear();
                    }
                    if(data.getData().size() < pagesize && index != 0)
                    {
                        showToast("没有更多了");
                    }
                    if(data.getData().size() != 0 && data.getData().size() <= pagesize)
                    {
                        index++;
                    }
                    if(data.getData().size() != 0) {
                        datas.addAll(data.getData());
                        signinAdapter.notifyDataSetChanged();
                        hideDataLoadMsg();
                    }
                    else
                    {
                        showDataLoadMsg("没有签到数据");
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                hideDataLoadMsg();
                isLoading = false;
            }
        }, this));
    }

}

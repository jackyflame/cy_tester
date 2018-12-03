package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.CorseBean;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.dialog.ChoiceDateDialog;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.TimeUtil;
import com.cyztc.app.views.grades.adapter.SigninAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/28.
 */

public class SigninHistoryActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tv_month)
    TextView tvMonth;

    private List<CorseBean> datas;
    private SigninAdapter signinAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;
    private String date = "";

    private String nyear;
    private String nmonth;
    private String nday;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_history_layout);
        setBackView();
        setTitle("考勤记录");

        date = TimeUtil.getDateStr();

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
        tvMonth.setText(TimeUtil.getDate2());
        showDataLoadMsg("数据加载中");
        setAdapter();
        getCorses(CyApplication.getInstance().getUserBean().getAccountId());
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_month)
    public void onClickMonth(View view)
    {
        ChoiceDateDialog choiceDateDialog = new ChoiceDateDialog(this);
        String[] d = date.split("-");
        nyear = d[0];
        nmonth = d[1];
        nday = d[2];
        choiceDateDialog.setDate(Integer.parseInt(nyear), Integer.parseInt(nmonth), Integer.parseInt(nday));
        choiceDateDialog.show();
        choiceDateDialog.setBirthdayListener(new ChoiceDateDialog.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                nyear = year;
                nmonth = month;
                nday = day;
                date = year + "-" + month + "-" + day;
                if(!isLoading) {
                    index = 0;
                    getCorses(CyApplication.getInstance().getUserBean().getAccountId());
                }
                tvMonth.setText(date);
            }
        });
    }

    @OnClick(R.id.iv_pre)
    public void onClickPre(View view)
    {
        String stamp = CommonUtil.date2TimeStamp(date, "yyyy-MM-dd");
        long time = Long.parseLong(stamp) - 60 * 60 * 24l;
        date = CommonUtil.timeStamp2Date(time + "", "yyyy-MM-dd");
        showDataLoadMsg("数据加载中");
        if(!isLoading) {
            index = 0;
            getCorses(CyApplication.getInstance().getUserBean().getAccountId());
        }
        String[] d = date.split("-");
        nyear = d[0];
        nmonth = d[1];
        nday = d[2];
        tvMonth.setText(date);

    }

    @OnClick(R.id.iv_next)
    public void onClickNext(View view)
    {
        String stamp = CommonUtil.date2TimeStamp(date, "yyyy-MM-dd");
        long time = Long.parseLong(stamp) + 60 * 60 * 24l;
        long now = System.currentTimeMillis() / 1000;
        if(time > now)
        {
            showToast("已经到最新一天了");
            return;
        }

        date = CommonUtil.timeStamp2Date(time + "", "yyyy-MM-dd");
        showDataLoadMsg("数据加载中");
        if(!isLoading) {
            index = 0;
            getCorses(CyApplication.getInstance().getUserBean().getAccountId());
        }
        String[] d = date.split("-");
        nyear = d[0];
        nmonth = d[1];
        nday = d[2];
        tvMonth.setText(date);

    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, SigninHistoryActivity.class);
        context.startActivity(intent);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        signinAdapter = new SigninAdapter(this, datas);
        listView.setAdapter(signinAdapter);
    }

    public void getCorses(String accountId)
    {
        isLoading = true;
        ClassApi.getInstance().getCourses(accountId, date, index, pagesize, new HttpSubscriber<CorseBeanP>(new SubscriberOnListener<CorseBeanP>() {
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
                    }
                }
                if(datas.size() == 0)
                {
                    showDataLoadMsg("没有历史签到记录");
                }
                else
                {
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
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                hideDataLoadMsg();
                isLoading = false;
            }
        }, this));
    }
}

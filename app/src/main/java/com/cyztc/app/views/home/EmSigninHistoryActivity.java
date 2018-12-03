package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.CorseBean;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.bean.EmSignBean;
import com.cyztc.app.bean.EmSignDetailBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.dialog.ChoiceDateDialog;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.TimeUtil;
import com.cyztc.app.views.grades.adapter.EmSigninAdapter;
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

public class EmSigninHistoryActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tv_month)
    TextView tvMonth;

    private List<EmSignBean> datas;
    private EmSigninAdapter signinAdapter;

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
        setContentView(R.layout.activity_emsignin_history_layout);
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
                    isLoading = true;
                    getCorses(CyApplication.getInstance().getUserBean().getAccountId());
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    isLoading = true;
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
        Intent intent = new Intent(context, EmSigninHistoryActivity.class);
        context.startActivity(intent);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        signinAdapter = new EmSigninAdapter(this, datas);
        listView.setAdapter(signinAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmSignBean emSignBean = (EmSignBean) listView.getItemAtPosition(position);
                if(emSignBean != null)
                {
                    Intent intent = new Intent();
                    intent.putExtra("date", emSignBean.getWorkDate());
                    intent.putExtra("week", emSignBean.getWeek());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void getCorses(String accountId)
    {
        isLoading = true;
        ClassApi.getInstance().getEmSignList(accountId, index, pagesize, new HttpSubscriber<PageBean<EmSignBean>>(new SubscriberOnListener<PageBean<EmSignBean>>() {
            @Override
            public void onSucceed(PageBean<EmSignBean> data) {
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
                isLoading = false;
            }
        }, this));
    }
}

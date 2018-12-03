package com.cyztc.app.views.grades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CorseBean;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.bean.TrainingInfoBean;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.grades.adapter.TimeTableAdapter;
import com.cyztc.app.views.home.ContactDetailActivity;
import com.cyztc.app.views.home.SigninActivity;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by ywl on 2016/12/18.
 */

public class TimeTableActivity extends BaseActivity{

    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.listview)
    ListView listView;

    private List<CorseBean> datas;
    private TimeTableAdapter timeTableAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_layout);
        setTitle("课程表");
        setBackView();

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

    public void setAdapter()
    {
        datas = new ArrayList<>();
        timeTableAdapter = new TimeTableAdapter(this, datas);
        listView.setAdapter(timeTableAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CorseBean corseBean = (CorseBean) listView.getItemAtPosition(position);
                if(corseBean != null)
                {
//                    TeacherDetailActivity.startActivity(TimeTableActivity.this, corseBean.getTeacherId());
                    SigninActivity.startActivity(TimeTableActivity.this, 0);
                }
            }
        });
    }


    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, TimeTableActivity.class);
        context.startActivity(intent);
    }

    public void getCorses(String accountId)
    {
        isLoading = true;
        ClassApi.getInstance().getCourses(accountId, "", index, pagesize, new HttpSubscriber<CorseBeanP>(new SubscriberOnListener<CorseBeanP>() {
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
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
                if(datas.size() > 0) {
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("暂时没有数据");
                }
                isLoading = false;
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadMsg(msg);
                }
                else {
                    hideDataLoadMsg();
                }
                isLoading = false;
            }
        }, this));
    }
}

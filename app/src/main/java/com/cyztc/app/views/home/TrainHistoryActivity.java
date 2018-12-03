package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TrainHistoryBean;
import com.cyztc.app.bean.TrainHistoryBeanP;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.grades.GradesActivity;
import com.cyztc.app.views.home.adapter.TrainHistoryAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/12/26.
 */

public class TrainHistoryActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    private List<TrainHistoryBean> datas;
    private TrainHistoryAdapter trainHistoryAdapter;

    private View headView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_history_layout);
        setBackView();
        setTitle("历史培训");

        headView = LayoutInflater.from(this).inflate(R.layout.head_trainhistory_layout, null);
//        listView.addHeaderView(headView);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getTrainHistory(CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getTrainHistory(CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize);
                }
            }
        });
        showDataLoadMsg("数据加载中");
        setAdapter();
        getTrainHistory(CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        trainHistoryAdapter = new TrainHistoryAdapter(this, datas);
        listView.setAdapter(trainHistoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrainHistoryBean trainHistoryBean = (TrainHistoryBean) listView.getItemAtPosition(position);
                if(trainHistoryBean != null)
                {
                    GradesActivity.startActivity(TrainHistoryActivity.this, 0, trainHistoryBean.getId());
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, TrainHistoryActivity.class);
        context.startActivity(intent);
    }

    public void getTrainHistory(String studentId, int start, int rows)
    {
        isLoading = true;
        UserApi.getInstance().trainHistory(studentId, start, rows, new HttpSubscriber<TrainHistoryBeanP>(new SubscriberOnListener<TrainHistoryBeanP>() {
            @Override
            public void onSucceed(TrainHistoryBeanP data) {
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
                        trainHistoryAdapter.notifyDataSetChanged();
                    }
                }
                hideDataLoadMsg();
                if(datas.size() == 0)
                {
                    showDataLoadMsg("没有相关数据");
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

package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.AssessListBean;
import com.cyztc.app.bean.TrainEnlistBean;
import com.cyztc.app.bean.TrainEnlistBeanP;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.adapter.TrainAssessAdapter;
import com.cyztc.app.views.home.adapter.TrainEnlistAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2017/1/8.
 */

public class ClassEnlistActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<TrainEnlistBean> datas;
    private TrainEnlistAdapter trainEnlistAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_enlist_layout);
        setTitle("课程报名");
        setBackView();

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getTrainEnlist(CyApplication.getInstance().getUserBean().getStudentId(), 0, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getTrainEnlist(CyApplication.getInstance().getUserBean().getStudentId(), 0, index, pagesize);
                }
            }
        });
        setAdapter();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDataLoadMsg("数据加载中");
        index = 0;
        getTrainEnlist(CyApplication.getInstance().getUserBean().getStudentId(), 0, index, pagesize);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        trainEnlistAdapter = new TrainEnlistAdapter(this, datas);
        listView.setAdapter(trainEnlistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrainEnlistBean trainEnlistBean = (TrainEnlistBean) listView.getItemAtPosition(position);
                if(trainEnlistBean != null)
                {
                    PayForActivity.startActivity(ClassEnlistActivity.this, trainEnlistBean);
                }
            }
        });
    }
    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        this.finish();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, ClassEnlistActivity.class);
        context.startActivity(intent);
    }

    public void getTrainEnlist(String studentId, int type, int start, int rows)
    {
        isLoading = true;
        ClassApi.getInstance().getOptionTrainList(studentId, type, start, rows, new HttpSubscriber<TrainEnlistBeanP>(new SubscriberOnListener<TrainEnlistBeanP>() {
            @Override
            public void onSucceed(TrainEnlistBeanP data) {
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
                        trainEnlistAdapter.notifyDataSetChanged();
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
                hideDataLoadMsg();
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

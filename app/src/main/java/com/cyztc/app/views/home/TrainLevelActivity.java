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
import com.cyztc.app.bean.AssessListBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.adapter.TrainAssessAdapter;
import com.cyztc.app.views.home.popup.TrainLevelTypePopMenu;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/12/20.
 */

public class TrainLevelActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<AssessListBean> datas;
    private TrainAssessAdapter trainAssessAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;
    private int type = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_level_layout);
        setTitle("培训评估");
        setBackView();
        setTextMenuView("删选");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getAssessList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getAssessList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
                }
            }
        });
        showDataLoadMsg("数据加载中");
        setAdapter();
        getAssessList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        TrainLevelTypePopMenu trainLevelTypePopMenu = new TrainLevelTypePopMenu(this);
        trainLevelTypePopMenu.showLocation(R.id.tv_right);
        trainLevelTypePopMenu.setOnItemClickListener(new TrainLevelTypePopMenu.OnItemClickListener() {
            @Override
            public void onClick(int type) {
                if(!isLoading) {
                    showDataLoadMsg("数据加载中");
                    index = 0;
                    getAssessList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
                }
            }
        });
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        trainAssessAdapter = new TrainAssessAdapter(this, datas);
        listView.setAdapter(trainAssessAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssessListBean assessListBean = (AssessListBean) listView.getItemAtPosition(position);
                if(assessListBean != null)
                {
//                    TrainLevelDetailActivity.startActivity(TrainLevelActivity.this, assessListBean.getType());
                    InvestgateListsActivity.startActivity(TrainLevelActivity.this, assessListBean.getType());
                }
            }
        });
    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, TrainLevelActivity.class);
        context.startActivity(intent);
    }

    public void getAssessList(String studentId, int type, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getAssessLists(studentId, type, start, rows, new HttpSubscriber<AssessListBeanP>(new SubscriberOnListener<AssessListBeanP>() {
            @Override
            public void onSucceed(AssessListBeanP data) {
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
                        trainAssessAdapter.notifyDataSetChanged();
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

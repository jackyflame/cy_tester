package com.cyztc.app.views.home;

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
import com.cyztc.app.bean.AssessListBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.InvestgateBean;
import com.cyztc.app.bean.InvestgateBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.adapter.InvestgateAdapter;
import com.cyztc.app.views.home.adapter.TrainAssessAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2017/2/18.
 */

public class InvestgateListsActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<InvestgateBean> datas;
    private InvestgateAdapter investgateAdapter;

    private int type = 1;
    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investgate_lists_layout);
        type = getIntent().getIntExtra("type", 1);

        setBackView();
        setTitle("评估选项");
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getInvategateLists(type);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getInvategateLists(type);
                }
            }
        });

        setAdapter();
//        getInvategateLists(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isLoading) {
            showDataLoadMsg("数据加载中");
            index = 0;
            getInvategateLists(type);
        }
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        investgateAdapter = new InvestgateAdapter(this, datas);
        listView.setAdapter(investgateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvestgateBean investgateBean = (InvestgateBean) listView.getItemAtPosition(position);
                if(investgateBean != null)
                {
                    if(investgateBean.getIsSubmitedAnswer() == 0)//没有提交答案
                    {
                        TrainLevelDetailActivity.startActivity(InvestgateListsActivity.this, type, investgateBean.getId());
                    }
                    else
                    {
                        MyLog.d("answer:" + investgateBean.getAnswer().toString());
                        TrainLevelAnswerActivity.startActivity(InvestgateListsActivity.this, type, investgateBean);
                    }
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, int type)
    {
        Intent intent = new Intent(context, InvestgateListsActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public void getInvategateLists(int type)
    {
        HomeApi.getInstance().getInvestgateLists(type, CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize, new HttpSubscriber<InvestgateBeanP>(new SubscriberOnListener<InvestgateBeanP>() {
            @Override
            public void onSucceed(InvestgateBeanP data) {
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
                        investgateAdapter.notifyDataSetChanged();
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
                isLoading = false;
                hideDataLoadMsg();
            }
        }, this));
    }
}

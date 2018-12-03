package com.cyztc.app.views.home.tribe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TribeBean;
import com.cyztc.app.bean.TribeBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.adapter.TribeListAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/20.
 */

public class TribeFragment extends BaseFragment{

    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<TribeBean> tribeBeens;
    private TribeListAdapter tribeListAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tribe_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize);
                }
            }
        });

        setAdapter();
        showDataLoadMsg("数据加载中");
        getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), 0, 10);
    }

    @Override
    public void onResume() {
        super.onResume();
        index = 0;
        getTribeList(CyApplication.getInstance().getUserBean().getStudentId(), index, pagesize);
    }

    public void setAdapter()
    {
        tribeBeens = new ArrayList<>();
        tribeListAdapter = new TribeListAdapter(getActivity(), tribeBeens);
        gridView.setAdapter(tribeListAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TribeBean tribeBean = (TribeBean) gridView.getItemAtPosition(position);
                if(tribeBean.getType() != 0) {
                    CreateTribeActivity.startActivity(getActivity());
                }
                else
                {
                    TribeDetailActivity.startActivity(getActivity(), tribeBean.getId(), tribeBean.getName());
                }
            }
        });
    }

    public void getTribeList(String studentId, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getTribeList(studentId, start, rows, new HttpSubscriber<TribeBeanP>(new SubscriberOnListener<TribeBeanP>() {
            @Override
            public void onSucceed(TribeBeanP data) {

                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        tribeBeens.clear();
                    }
                    if(data.getData().size() < pagesize && index != 0)
                    {
                        showToast("没有更多了");
                    }
                    if(data.getData().size() != 0 && data.getData().size() <= pagesize)
                    {
                        index++;
                    }
                    tribeBeens.addAll(data.getData());
                    TribeBean tribeBean = new TribeBean();
                    tribeBean.setType(1);
                    tribeBeens.add(tribeBean);
                    tribeListAdapter.notifyDataSetChanged();
                }
                if(tribeBeens.size() > 0) {
                    springView.onFinishFreshAndLoad();
                    isLoading = false;
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("没有部落数据");
                }
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(tribeBeens.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                hideDataLoadMsg();
                isLoading = false;
            }
        }, getActivity()));
    }
}

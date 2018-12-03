package com.cyztc.app.views.employee.leave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.LeaveBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.employee.leave.adapter.LeaveAdapater;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2017-5-14.
 */

public class LeaveListFragment extends BaseFragment{

    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.listview)
    ListView listView;

    private LeaveAdapater leaveAdapater;
    private List<LeaveBean> leaveDatas;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    private int type = -1;
    private int applyType = -1;
    private boolean ismine = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_empliee_myleave_layout);
    }

    public void setType(int applyType, int type) {
        this.type = type;
        this.applyType = applyType;
    }

    public void setIsmine(boolean ismine) {
        this.ismine = ismine;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isLoading) {
            index = 0;
            isLoading = true;
            getApplyList(CyApplication.getInstance().getUserBean().getAccountId(), applyType, type, -1, index, pagesize);
        }
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
                    isLoading = true;
                    getApplyList(CyApplication.getInstance().getUserBean().getAccountId(), applyType, type, -1, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    isLoading = true;
                    getApplyList(CyApplication.getInstance().getUserBean().getAccountId(), applyType, type, -1, index, pagesize);
                }
            }
        });
        setAdapter();
        showDataLoadMsg("数据加载中");
        isLoading = true;
        getApplyList(CyApplication.getInstance().getUserBean().getAccountId(), applyType, type, -1, index, pagesize);
    }

    private void setAdapter()
    {
        leaveDatas = new ArrayList<>();
        leaveAdapater = new LeaveAdapater(getActivity(), leaveDatas);
        listView.setAdapter(leaveAdapater);
        leaveAdapater.setType(applyType);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeaveBean leaveBean = (LeaveBean) listView.getItemAtPosition(position);
                if(leaveBean != null) {
                    LeaveDetailActivity.startActivity(getActivity(), leaveBean.getId(), applyType);
                }
            }
        });
    }
    private void getApplyList(String accountId, int type, int isAudit, int status, int start, int rows)
    {
        EmployeeApi.getInstance().getApplyList(accountId, type, isAudit, status, start, rows, new HttpSubscriber<PageBean<LeaveBean>>(new SubscriberOnListener<PageBean<LeaveBean>>() {
            @Override
            public void onSucceed(PageBean<LeaveBean> data) {
                MyLog.d(data);
                if(ismine)
                {
                    leaveAdapater.setIsmine(true);
                }
                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        leaveDatas.clear();
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
                        leaveDatas.addAll(data.getData());
                        leaveAdapater.notifyDataSetChanged();
                        hideDataLoadMsg();
                    }
                    else
                    {
                        showDataLoadMsg("还没有申请记录");
                    }
                }
                else
                {
                    showDataLoadMsg("还没有申请记录");
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(leaveDatas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                else {
                    hideDataLoadMsg();
                }
                isLoading = false;
            }
        }, getActivity()));
    }


}

package com.cyztc.app.views.home.shop;

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
import com.cyztc.app.bean.OrderBean;
import com.cyztc.app.bean.OrderBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.TrainLevelActivity;
import com.cyztc.app.views.home.TrainLevelDetailActivity;
import com.cyztc.app.views.home.adapter.OrderListAdapter;
import com.cyztc.app.views.home.adapter.TrainAssessAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/12/30.
 */

public class OrderListActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<OrderBean> datas;
    private OrderListAdapter orderListAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_layout);
        setTitle("我的订单");
        setBackView();

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getMyOrders(CyApplication.getInstance().getUserBean().getStudentId(), 1, 1, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getMyOrders(CyApplication.getInstance().getUserBean().getStudentId(), 1, 1, index, pagesize);
                }
            }
        });
        showDataLoadMsg("数据加载中");
        setAdapter();
        getMyOrders(CyApplication.getInstance().getUserBean().getStudentId(), 1, 1, index, pagesize);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(this, datas);
        listView.setAdapter(orderListAdapter);
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, OrderListActivity.class);
        context.startActivity(intent);
    }

    public void getMyOrders(String studentId, int type, int status, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getOrderList(studentId, type, status, start, rows, new HttpSubscriber<OrderBeanP>(new SubscriberOnListener<OrderBeanP>() {
            @Override
            public void onSucceed(OrderBeanP data) {
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
                        orderListAdapter.notifyDataSetChanged();
                    }
                }
                hideDataLoadMsg();
                if(datas.size() == 0)
                {
                    showDataLoadMsg("您还没有订单");
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

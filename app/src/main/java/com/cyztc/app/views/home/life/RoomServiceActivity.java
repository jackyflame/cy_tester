package com.cyztc.app.views.home.life;

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
import com.cyztc.app.bean.GoodsBean;
import com.cyztc.app.bean.GoodsBeanP;
import com.cyztc.app.bean.OrderBean;
import com.cyztc.app.bean.OrderBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.adapter.RoomServiceAdapter;
import com.cyztc.app.views.home.adapter.ShopListAdapter;
import com.cyztc.app.views.home.shop.GoodsDetailActivity;
import com.cyztc.app.views.home.shop.ShopListActivity;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/18.
 */

public class RoomServiceActivity extends BaseActivity{

    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.listview)
    ListView listView;

    private RoomServiceAdapter roomServiceAdapter;
    private List<OrderBean> datas;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;
    private int type = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", -1);
        setContentView(R.layout.activity_kefang_service_layout);
        if(type == 2)
        {
            setTitle("洗衣服务");
        }
        else if(type == 3)
        {
            setTitle("报修服务");
        }
        else if(type == 4)
        {
            setTitle("客房服务");
        }
        else if(type == 5)
        {
            setTitle("会场服务");
        }
        else if(type == 6)
        {
            setTitle("医疗服务");
        }
        setTextMenuView("申请");
        setBackView();

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), type, 0, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), type, 0, index, pagesize);
                }
            }
        });
//        showDataLoadMsg("数据加载中");
        setAdapter();
//        getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), type, 0, index, pagesize);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isLoading) {
            index = 0;
            showDataLoadMsg("数据加载中");
            getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), type, 0, index, pagesize);
        }
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(type == 2)
        {
            WashServiceActivity.startActivity(this);
        }
        else {
            ApplyRoomActivity.startActivity(this, type);
        }
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        roomServiceAdapter = new RoomServiceAdapter(this, datas);
        listView.setAdapter(roomServiceAdapter);
    }

    public static void startActivity(Context context, int type)
    {
        Intent intent = new Intent(context, RoomServiceActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public void getGoodsList(String studentId, int type, int status, int start, int rows)
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
                        roomServiceAdapter.notifyDataSetChanged();
                    }
                }
                if(datas.size() > 0)
                {
                    hideDataLoadMsg();
                    roomServiceAdapter.notifyDataSetChanged();
                }
                else
                {
                    showDataLoadWrongMsg("没有服务信息");
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
                else {
                    hideDataLoadMsg();
                }
                isLoading = false;
            }
        }, this));
    }
}

package com.cyztc.app.views.employee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.bean.EatHistoryBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.adapter.EatListAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2017-6-29.
 */

public class EatActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tv_eat)
    TextView tvEat;

    private List<EatHistoryBean> eats;
    EatListAdapter eatListAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empee_eat_layout);
        setTitle("用餐");
        setBackView();
        setAdapter();

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getEatList(CyApplication.getInstance().getUserBean().getAccountId(), index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getEatList(CyApplication.getInstance().getUserBean().getAccountId(), index, pagesize);
                }
            }
        });

        tvEat.setText("用餐（剩余" + CyApplication.getInstance().getUserBean().getDinnerCount() + "次）");
        showDataLoadMsg("数据加载中");
        getEatList(CyApplication.getInstance().getUserBean().getAccountId(), index, pagesize);
    }

    private void setAdapter()
    {
        eats = new ArrayList<>();
        eatListAdapter = new EatListAdapter(this, eats);
        listView.setAdapter(eatListAdapter);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_eat)
    public void onClickEat(View view)
    {
        int dinnercount = CyApplication.getInstance().getUserBean().getDinnerCount();
        if(dinnercount > 0) {
            showLoadDialog("加载中");
            EmployeeApi.getInstance().dinning(CyApplication.getInstance().getUserBean().getAccountId(), "", new HttpSubscriber<String>(new SubscriberOnListener() {
                @Override
                public void onSucceed(Object data) {
                    hideLoadDialog();
                    showToast("就餐成功");
                    CyApplication.getInstance().getUserBean().setDinnerCount(CyApplication.getInstance().getUserBean().getDinnerCount() - 1);
                    tvEat.setText("用餐（剩余" + CyApplication.getInstance().getUserBean().getDinnerCount() + "次）");
                    if (!isLoading) {
                        index = 0;
                        getEatList(CyApplication.getInstance().getUserBean().getAccountId(), index, pagesize);
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    showToast(msg);
                    hideLoadDialog();
                }
            }, this));
        }
        else
        {
            showToast("就餐次数已经用完");
        }
    }

    private void getEatList(String accountId, int start, int rows)
    {
        isLoading = true;
        EmployeeApi.getInstance().eatList(accountId, start, rows, new HttpSubscriber<PageBean<EatHistoryBean>>(new SubscriberOnListener<PageBean<EatHistoryBean>>() {
            @Override
            public void onSucceed(PageBean<EatHistoryBean> data) {
                eats.addAll(data.getData());
                eatListAdapter.notifyDataSetChanged();

                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        eats.clear();
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
                        eats.addAll(data.getData());

                    }
                    if(eats.size() > 0)
                    {
                        eatListAdapter.notifyDataSetChanged();
                        hideDataLoadMsg();
                    }
                    else
                    {
                        showDataLoadMsg("没有收藏记录");
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;

            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(eats.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                else {
                    hideDataLoadMsg();
                }
                isLoading = false;
            }
        }, this));
    }

    public static void startActivit(Context context)
    {
        Intent intent = new Intent(context, EatActivity.class);
        context.startActivity(intent);
    }

}

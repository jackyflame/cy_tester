package com.cyztc.app.views.home.tribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.TribeMemberBean;
import com.cyztc.app.bean.TribeMemberBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.adapter.TribeMemberAdapter;
import com.cyztc.app.views.home.adapter.TribeMemberAdapter2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2017-8-29.
 */

public class TribeMemberActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    private List<TribeMemberBean> datas;
    private TribeMemberAdapter2 tribeMemberAdapter;

    private String tribeid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tribeid = getIntent().getStringExtra("tribeid");
        setContentView(R.layout.activity_club_members_layout);
        setTitle("部落成员");
        setBackView();
        showDataLoadMsg("加载中");
        setAdapter();
        getTribeMenember(CyApplication.getInstance().getUserBean().getStudentId(), tribeid);
    }

    private void setAdapter()
    {
        datas = new ArrayList<>();
        tribeMemberAdapter = new TribeMemberAdapter2(this, datas);
        listView.setAdapter(tribeMemberAdapter);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    private void getTribeMenember(String studentId, String tribeId)
    {
        HomeApi.getInstance().getTribeMemember(studentId, tribeId, new HttpSubscriber<TribeMemberBeanP>(new SubscriberOnListener<TribeMemberBeanP>() {
            @Override
            public void onSucceed(TribeMemberBeanP data) {
                if(data != null)
                {
                    if(data.getData() != null)
                    {
                        datas.addAll(data.getData());
                        hideDataLoadMsg();
                        tribeMemberAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        showDataLoadMsg("加载数据失败");
                    }
                }
                else
                {
                    showDataLoadMsg("加载数据失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadMsg(msg);
            }
        }, this));
    }

    public static void startActivity(Context context, String tribeId)
    {
        Intent intent = new Intent(context, TribeMemberActivity.class);
        intent.putExtra("tribeid", tribeId);
        context.startActivity(intent);
    }
}

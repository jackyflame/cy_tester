package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.MessageBean;
import com.cyztc.app.bean.MessageBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.adapter.MessageAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/12/27.
 */

public class MessageActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<MessageBean> datas;
    private MessageAdapter messageAdapter;

    private int pagesize = 50;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_layout);
        setBackView();
        setTitle("推送消息");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getMessage(CyApplication.getInstance().getUserBean().getAccountId(), index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getMessage(CyApplication.getInstance().getUserBean().getAccountId(), index, pagesize);
                }
            }
        });
        setAdapter();
        showDataLoadMsg("数据加载中");
        getMessage(CyApplication.getInstance().getUserBean().getAccountId(), 0, 10);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, datas);
        listView.setAdapter(messageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageBean messageBean = (MessageBean) listView.getItemAtPosition(position);
                if(messageBean != null)
                {
                    readMessage(messageBean.getId());
                    MessageDetailActivity.startActivity(MessageActivity.this, messageBean);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(messageAdapter != null)
        {
            messageAdapter.notifyDataSetChanged();
        }
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    public void getMessage(String studentId, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getMessage(studentId, start, rows, new HttpSubscriber<MessageBeanP>(new SubscriberOnListener<MessageBeanP>() {
            @Override
            public void onSucceed(MessageBeanP data) {
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
                        messageAdapter.notifyDataSetChanged();
                    }
                }
                hideDataLoadMsg();

                messageAdapter.notifyDataSetChanged();
                if(datas.size() == 0)
                {
                    showDataLoadMsg("没有推送消息");
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

    public void readMessage(final String messageId)
    {
        HomeApi.getInstance().readMessage(CyApplication.getInstance().getUserBean().getAccountId(), messageId, new HttpSubscriber<Integer>(new SubscriberOnListener<Integer>() {
            @Override
            public void onSucceed(Integer data) {
                for(MessageBean message : datas)
                {
                    if(message.getId().equals(messageId))
                    {
                        message.setStatus(1);
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

}

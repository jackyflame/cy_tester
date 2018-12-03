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
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CuiYueCircleBean;
import com.cyztc.app.bean.CuiYueCircleBeanP;
import com.cyztc.app.bean.MessageBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.employee.leave.LeaveDetailActivity;
import com.cyztc.app.views.home.adapter.CuiYueCircleAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2016/12/27.
 */

public class CuiYueCircleActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<CuiYueCircleBean> datas;
    private CuiYueCircleAdapter cuiYueCircleAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;
    private int type;
    private int dotType = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 1);
        setContentView(R.layout.activity_cuiyue_circle_layout);
        setBackView();
//        if(CyApplication.getInstance().isStudent())
//        {
//            setTitle("翠月动态");
//        }
//        else {
//            setTitle("部门动态");
//        }
//        setTextMenuView("发布");
        if(type == 1)
        {
            setTitle("翠月动态");
            dotType = 2;
        }
        else
        {
            setTitle("部门动态");
            dotType = 3;
        }

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    isLoading = true;
                    index = 0;
                    getDataList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    isLoading = true;
                    getDataList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
                }
            }
        });
        showDataLoadMsg("动态加载中");
        setAdapter();
        getDataList(CyApplication.getInstance().getUserBean().getStudentId(), type, index, pagesize);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

//    @Override
//    public void onClickTextMenu() {
//        super.onClickTextMenu();
//        showToast("发布动态");
//    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        cuiYueCircleAdapter = new CuiYueCircleAdapter(this, datas);
        cuiYueCircleAdapter.setDotType(dotType);
        listView.setAdapter(cuiYueCircleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CuiYueCircleBean cuiYueCircleBean = (CuiYueCircleBean) listView.getItemAtPosition(position);
                if(cuiYueCircleBean != null)
                {
                    CuiYueCircleDetailActivity.startActivity(CuiYueCircleActivity.this, cuiYueCircleBean.getId(), dotType);
                }
            }
        });
    }

    /**
     *
     * @param context
     * @param type 1:崔月动态 2：部门动态
     */
    public static void startActivity(Context context, int type)
    {
        Intent intent = new Intent(context, CuiYueCircleActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public void getDataList(String studentId, int type, int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getCuiYueDynamicList(studentId, type, start, rows, new HttpSubscriber<CuiYueCircleBeanP>(new SubscriberOnListener<CuiYueCircleBeanP>() {
            @Override
            public void onSucceed(CuiYueCircleBeanP data) {
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
                        cuiYueCircleAdapter.notifyDataSetChanged();
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

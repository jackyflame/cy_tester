package com.cyztc.app.views.grades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ClassMineBean;
import com.cyztc.app.bean.ClassMineBeanP;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.grades.adapter.ClassesElegantAdapter;
import com.cyztc.app.views.home.CuiYueCircleDetailActivity;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/3.
 */

public class ClassesElegantFragment extends BaseFragment{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;
    ImageView ivPhotos;

    private LayoutInflater headerInflater;
    private View headView;

    private List<ClassMineBean> datas;
    private ClassesElegantAdapter classesElegantAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    private int type = 1;
    private String trainId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_classes_elegant_layout);
        headerInflater = LayoutInflater.from(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headView = headerInflater.inflate(R.layout.header_classes_layout, null);
        ivPhotos = (ImageView) headView.findViewById(R.id.ivphotos);
        Glide.with(getActivity()).load(R.drawable.icon_album_bg).dontAnimate().into(ivPhotos);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassAlbumActivity.startActivity(getActivity());
            }
        });
        listView.addHeaderView(headView);

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getData(CyApplication.getInstance().getUserBean().getAccountId(), trainId, index, pagesize, type);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    isLoading = true;
                    getData(CyApplication.getInstance().getUserBean().getAccountId(), trainId, index, pagesize, type);
                }
            }
        });

        setAdapter();
        if(CyApplication.getInstance().getUserBean() != null) {
            isLoading = true;
            getData(CyApplication.getInstance().getUserBean().getAccountId(), trainId, index, pagesize, type);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isLoading) {
            index = 0;
            getData(CyApplication.getInstance().getUserBean().getAccountId(), trainId, index, pagesize, type);
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        classesElegantAdapter = new ClassesElegantAdapter(getActivity(), datas);
        listView.setAdapter(classesElegantAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassMineBean classMineBean = (ClassMineBean) listView.getItemAtPosition(position);
                if(classMineBean != null)
                {
                    ClassCircleDetailActivity.startActivity(getActivity(), classMineBean);
                    if(type == 1)//班级信息
                    {
                        handelMessage(classMineBean.getId(), 9);
                    }
                    else if(type == 2)//班级管理
                    {
                        handelMessage(classMineBean.getId(), 10);
                    }
                    else if(type == 3)//班级活动
                    {
                        handelMessage(classMineBean.getId(), 11);
                    }
                    else if(type == 4)//宣传之窗
                    {
                        handelMessage(classMineBean.getId(), 12);
                    }
                    else if(type == 5)//学习感悟
                    {
                        handelMessage(classMineBean.getId(), 8);
                    }
                }
            }
        });

    }

    public void getData(String accountId, String trainId, int start, int tows, int type)
    {
        ClassApi.getInstance().getClassMien(accountId, trainId, start, tows, type, new HttpSubscriber<ClassMineBeanP>(new SubscriberOnListener<ClassMineBeanP>() {
            @Override
            public void onSucceed(ClassMineBeanP data) {

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
                        classesElegantAdapter.notifyDataSetChanged();
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                hideDataLoadMsg();
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
        }, getActivity()));
    }

    private void handelMessage(String targetId, int type)
    {
        UserApi.getInstance().delMessagePeding(CyApplication.getInstance().getUserBean().getAccountId(), targetId, type, "", new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, getActivity()));
    }

}

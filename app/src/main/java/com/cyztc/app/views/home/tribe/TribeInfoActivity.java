package com.cyztc.app.views.home.tribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.TribeDetailBean;
import com.cyztc.app.bean.TribeMemberBean;
import com.cyztc.app.bean.TribeMemberBeanP;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.TrainMainInfoActivity;
import com.cyztc.app.views.home.adapter.TribeMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeInfoActivity extends BaseActivity{

    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_createtime)
    TextView tvCreateTime;
    @BindView(R.id.tv_disrc)
    TextView tvDisrc;
    @BindView(R.id.tv_memember)
    TextView tvMemember;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.rl_exit)
    RelativeLayout rlExit;

    private String tribeid;
    private String creator;
    private TribeMemberAdapter tribeMemberAdapter;
    private List<TribeMemberBean> datas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tribeid = getIntent().getStringExtra("tribeid");
        creator = getIntent().getStringExtra("creator");
        setContentView(R.layout.activity_tribe_info_layout);
        setBackView();
        setTitle("部落资料");
        setAdapter();
        getTribeDetail(tribeid);
        getTribeMenember(CyApplication.getInstance().getUserBean().getStudentId(), tribeid);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        tribeMemberAdapter = new TribeMemberAdapter(this, datas);
        gridview.setAdapter(tribeMemberAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TribeMemberActivity.startActivity(TribeInfoActivity.this, tribeid);
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_exittribe)
    public void onClickExitTribe(View view)
    {
        showAskDialog("提示", "是否退出此部落？", "退出", "取消", new NormalAskComplexDialog.OnDalogListener() {
            @Override
            public void onYes() {
                HomeApi.getInstance().deleteTribe(tribeid, CyApplication.getInstance().getUserBean().getStudentId(), new HttpSubscriber<Object>(new SubscriberOnListener() {
                    @Override
                    public void onSucceed(Object data) {
                        showToast("已退出部落");
                        hideLoadDialog();
                        TribeInfoActivity.this.finish();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        showToast(msg);
                    }
                }, TribeInfoActivity.this));
            }

            @Override
            public void OnNo() {
            }
        });
    }

    public void getTribeDetail(String tribeId)
    {
        HomeApi.getInstance().getTribeDetail(CyApplication.getInstance().getUserBean().getStudentId(), tribeId, new HttpSubscriber<TribeDetailBean>(new SubscriberOnListener<TribeDetailBean>() {
            @Override
            public void onSucceed(TribeDetailBean data) {
                MyLog.d(data);
                if(data != null)
                {
                    initViews(data);
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    private void getTribeMenember(String studentId, String tribeId)
    {
        HomeApi.getInstance().getTribeMemember(studentId, tribeId, new HttpSubscriber<TribeMemberBeanP>(new SubscriberOnListener<TribeMemberBeanP>() {
            @Override
            public void onSucceed(TribeMemberBeanP data) {
                if(data != null)
                {
                    tvMemember.setText(data.getTotalCount() + "");
                    if(data.getData() != null)
                    {
                        datas.addAll(data.getData());
                    }
                }
                tribeMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    private void initViews(TribeDetailBean data) {

        ImageLoad.getInstance().displayImage(this, ivImg, HttpMethod.IMG_URL + data.getCoverPicture(), 0, 0);
        tvName.setText(data.getName());
        tvCreateTime.setText("创建于" + data.getCreateTime());
        tvDisrc.setText(data.getRemark());
        if(!creator.equals(CyApplication.getInstance().getUserBean().getStudentId()))
        {
            rlExit.setVisibility(View.VISIBLE);
        }
    }


    public static void startActivity(Context context, String tribeid, String creator)
    {
        Intent intent = new Intent(context, TribeInfoActivity.class);
        intent.putExtra("tribeid", tribeid);
        intent.putExtra("creator", creator);
        context.startActivity(intent);
    }
}

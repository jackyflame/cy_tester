package com.cyztc.app.views.employee.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.ApplyAuditorBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.LeaveDetailBean;
import com.cyztc.app.httpservice.beans.SchduleCarBodyBean;
import com.cyztc.app.httpservice.serviceapi.EmployeeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.employee.CarDeployActivity;
import com.cyztc.app.views.employee.leave.adapter.LeaveDetailAdapter;
import com.cyztc.app.widget.CustomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.http.Body;

/**
 * Created by ywl on 2017-5-20.
 */

public class LeaveDetailActivity extends BaseActivity{

    @BindView(R.id.listview)
    CustomListView listView;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ly_action)
    LinearLayout lyAction;
    @BindView(R.id.ly_cars)
    LinearLayout lyCars;
    @BindView(R.id.tv_agree)
    TextView tvAgree;

    private boolean isDiaodu = false;

    private LeaveDetailAdapter leaveDetailAdapter;
    private List<ApplyAuditorBean> datas;

    private String applyId;
    private int type;
    private LayoutInflater layoutInflater;
    private SchduleCarBodyBean schduleCarBodyBean = new SchduleCarBodyBean();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyId = getIntent().getStringExtra("applyId");
        type = getIntent().getIntExtra("type", -1);
        setContentView(R.layout.activity_liave_detail_layout);
        layoutInflater = LayoutInflater.from(this);
        if(type == 1) {
            setTitle("请假详情");
            handelMessage(applyId, 4);
        }
        else if(type == 2)
        {
            setTitle("差旅详情");
            handelMessage(applyId, 5);
        }
        else if(type == 3)
        {
            setTitle("印章详情");
            handelMessage(applyId, 7);
        }
        else if(type == 4)
        {
            setTitle("派车详情");
            handelMessage(applyId, 6);
        }
        setBackView();
        setAdapter();
        showDataLoadMsg("数据加载中");
        getDetail(applyId);

    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }
    @OnClick(R.id.tv_disagree)
    public void onClickDisAgree(View view)
    {
        applyAgree(CyApplication.getInstance().getUserBean().getAccountId(), applyId, 3, schduleCarBodyBean.getDriver(), schduleCarBodyBean.getVehicle(), schduleCarBodyBean.getVehicleNo(), schduleCarBodyBean.getMobile());
    }

    @OnClick(R.id.tv_agree)
    public void onClickAgree(View view)
    {
        if(tvAgree.getText().toString().trim().equals("调度"))
        {
            Intent intent = new Intent(this, CarDeployActivity.class);
            startActivityForResult(intent, 0x1002);
        }
        else
        {
            applyAgree(CyApplication.getInstance().getUserBean().getAccountId(), applyId, 5, schduleCarBodyBean.getDriver(), schduleCarBodyBean.getVehicle(), schduleCarBodyBean.getVehicleNo(), schduleCarBodyBean.getMobile());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == 0x1002)
            {
                schduleCarBodyBean = (SchduleCarBodyBean) data.getExtras().getSerializable("cardeploy");
                if(schduleCarBodyBean != null)
                {
                    schduleCarBodyBean.setApplyInfoId(applyId);
                    applyAgree(CyApplication.getInstance().getUserBean().getAccountId(), applyId, 5, schduleCarBodyBean.getDriver(), schduleCarBodyBean.getVehicle(), schduleCarBodyBean.getVehicleNo(), schduleCarBodyBean.getMobile());
                }

            }
        }
    }

    private void setAdapter()
    {
        datas = new ArrayList<>();
        leaveDetailAdapter = new LeaveDetailAdapter(this, datas);
        listView.setAdapter(leaveDetailAdapter);
    }

    private void initCarsPoints(List<String> points)
    {
        for(int i = 0; i < points.size(); i++)
        {
            View view = layoutInflater.inflate(R.layout.item_midel_point_car_show_layout, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_start);
            TextView tvPoint = (TextView) view.findViewById(R.id.et_point);
            if(i == 0)
            {
                tvName.setText("起点");
            }
            else{
                tvName.setText("途经点");
            }
            tvPoint.setText(points.get(i));

            if(i == points.size() - 1)
            {
                tvName.setText("终点");
                tvPoint.setText(points.get(i));
            }
            lyCars.addView(view);

        }
    }


    public static void startActivity(Context context, String applyId, int type)
    {
        Intent intent = new Intent(context, LeaveDetailActivity.class);
        intent.putExtra("applyId", applyId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private void getDetail(String appliId)
    {
        EmployeeApi.getInstance().getDetail(CyApplication.getInstance().getUserBean().getAccountId(), appliId, new HttpSubscriber<LeaveDetailBean>(new SubscriberOnListener<LeaveDetailBean>() {
            @Override
            public void onSucceed(LeaveDetailBean data) {
                if(data != null) {
                    hideDataLoadMsg();
                    initData(data);
                }
                else
                {
                    showDataLoadMsg("获取数据失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadMsg(msg);
            }
        }, this));
    }

    private void initData(LeaveDetailBean leaveDetailBean)
    {
        tvCreateTime.setText(leaveDetailBean.getCreateTime());
        if(leaveDetailBean.getStatus() == 0 || leaveDetailBean.getStatus() == 1)
        {
            tvStatus.setText("待审核");
            tvStatus.setTextColor(this.getResources().getColor(R.color.color_12a7e2));
        }
        else if(leaveDetailBean.getStatus() == 3)
        {
            tvStatus.setText("不同意");
            tvStatus.setTextColor(this.getResources().getColor(R.color.color_e10000));
        }
        else if(leaveDetailBean.getStatus() == 5)
        {
            tvStatus.setText("已同意");
            tvStatus.setTextColor(this.getResources().getColor(R.color.color_0cd38a));
        }

        tvStartTime.setText(leaveDetailBean.getStartTime());
        tvEndTime.setText(leaveDetailBean.getEndTime());
        if(type == 1) {
            tvType.setText("请假类型");
            tvValue.setText(CommonUtil.getApplyLeaveStr(leaveDetailBean.getSubType()));
            tvContent.setText("请假事由：" + leaveDetailBean.getContent());
        }
        else if(type == 2)
        {
            tvType.setText("差旅类型");
            tvValue.setText(CommonUtil.getTrivTitle(leaveDetailBean.getSubType()));
            tvContent.setText("差旅事由：" + leaveDetailBean.getContent());
        }
        else if(type == 3)
        {
            tvType.setText("印章类型");
            tvValue.setText(CommonUtil.getSeal(leaveDetailBean.getSubType()));
            tvContent.setText("印章事由：" + leaveDetailBean.getContent());
        }
        else if(type == 4)
        {
            tvType.setText("派车类型");
            tvValue.setText(CommonUtil.getCarTitle(leaveDetailBean.getSubType()));
            tvContent.setText("派车事由：" + leaveDetailBean.getContent());
        }
        if(leaveDetailBean.getAuditors() != null)
        {
            datas.clear();
            datas.addAll(leaveDetailBean.getAuditors());
            leaveDetailAdapter.notifyDataSetChanged();

        }
        if(leaveDetailBean.isMyDuty())
        {
            lyAction.setVisibility(View.VISIBLE);
            if(leaveDetailBean.getAuditors() != null)
            {
                if(type == 4) {
                    if (isDiaodu = isLastOne(leaveDetailBean.getAuditors()) && leaveDetailBean.getUserType() != 1) {
                        tvAgree.setText("调度");
                    } else {
                        tvAgree.setText("同意");
                    }
                }
                else
                {
                    tvAgree.setText("同意");
                }
            }


        }
        else
        {
            lyAction.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(leaveDetailBean.getStartAddress()))
        {
            lyCars.setVisibility(View.VISIBLE);
            String [] points = leaveDetailBean.getStartAddress().split("::::");
            List<String> ps = new ArrayList<>();
            for(int i = 0; i < points.length; i++)
            {
                ps.add(points[i]);
            }
            ps.add(leaveDetailBean.getEndAddress());
            initCarsPoints(ps);
        }
        else
        {
            lyCars.setVisibility(View.GONE);
        }

    }

    private void applyAgree(String accountId, String applyInfoId, int Status, String driver, String vehicle, String vehicleNo, String mobile)
    {
        showToast("处理中");
        EmployeeApi.getInstance().applyStatus(accountId, applyInfoId, Status, driver, vehicle, vehicleNo, mobile, new HttpSubscriber<List<DepartMentBean>>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                showToast("处理成功");
                getDetail(applyId);
                lyAction.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
            }
        }, this));
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
        }, LeaveDetailActivity.this));
    }

    private boolean isLastOne(List<ApplyAuditorBean> applyAuditorBeens)
    {
        if(applyAuditorBeens.size() == 1)
        {
            return true;
        }
        if(applyAuditorBeens.size() > 1)
        {
            if(applyAuditorBeens.get(applyAuditorBeens.size() - 2).getStatus() == 5 && applyAuditorBeens.get(applyAuditorBeens.size() - 1).getStatus() != 5)
            {
                return true;
            }
            return false;
        }
        return false;
    }

}

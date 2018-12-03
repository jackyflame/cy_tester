package com.cyztc.app.views.employee.leave.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.LeaveBean;
import com.cyztc.app.utils.CommonUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

/**
 * Created by ywl on 2017-5-15.
 */

public class LeaveAdapater extends BaseDataAdapter{

    private int type;
    private boolean ismine = false;

    public void setIsmine(boolean ismine) {
        this.ismine = ismine;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LeaveAdapater(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LeaveBean leaveBean = (LeaveBean) mDatas.get(position);
        ViewHolder viewHolder;

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_leave_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTime.setVisibility(View.GONE);
        viewHolder.tvTime.setText(leaveBean.getOperateTime());
        //0：申请，1：审核中，3：审核不通过，5：审核通过
        if (leaveBean.getStatus() == 0 || leaveBean.getStatus() == 1) {
            viewHolder.tvStatus.setText("待审核");
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_12a7e2));
        } else if (leaveBean.getStatus() == 3) {
            viewHolder.tvStatus.setText("不同意");
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_e10000));
        } else if (leaveBean.getStatus() == 5) {
            viewHolder.tvStatus.setText("已同意");
            viewHolder.tvTime.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_0cd38a));
        }
        if(ismine) {
            viewHolder.tvApplyman.setText("申请人：" + leaveBean.getApplyMan());
            viewHolder.tvApplyman.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.tvApplyman.setVisibility(View.GONE);
        }

        if(type == 1) {//请假
            viewHolder.tvName.setText(CommonUtil.getApplyLeaveStr(leaveBean.getSubtype()));
            viewHolder.tvContent.setText(leaveBean.getContent());
            viewHolder.tvContentTitle.setText("请假内容：");
        }
        else if(type == 2)
        {
            viewHolder.tvName.setText(CommonUtil.getTrivTitle(leaveBean.getSubtype()));
            viewHolder.tvContent.setText(leaveBean.getContent());
            viewHolder.tvContentTitle.setText("申请备注：");
        }
        else if(type == 3)
        {
            viewHolder.tvName.setText(CommonUtil.getSeal(leaveBean.getSubtype()));
            viewHolder.tvContent.setText(leaveBean.getContent());
            viewHolder.tvContentTitle.setText("申请备注：");
        }
        else if(type == 4)
        {
            viewHolder.tvName.setText(CommonUtil.getCarTitle(leaveBean.getSubtype()));
            viewHolder.tvContent.setText(leaveBean.getContent());
            viewHolder.tvContentTitle.setText("申请备注：");
        }

        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_content_title)
        TextView tvContentTitle;
        @BindView(R.id.tv_applyman)
        TextView tvApplyman;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }

    }

}

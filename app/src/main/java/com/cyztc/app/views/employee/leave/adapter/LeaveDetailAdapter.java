package com.cyztc.app.views.employee.leave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.ApplyAuditorBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.LeaveDetailBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-5-20.
 */

public class LeaveDetailAdapter extends BaseDataAdapter{

    private boolean isAgree;

    public LeaveDetailAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        ApplyAuditorBean applyAuditorBean = (ApplyAuditorBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_lieave_agree_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.rivHead, HttpMethod.IMG_URL + applyAuditorBean.getAuditorPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        viewHolder.tvName.setText(applyAuditorBean.getAuditorName());
        viewHolder.tvPosition.setText(applyAuditorBean.getPosition());
        viewHolder.tvTime.setText(applyAuditorBean.getOperateTime());
        if(applyAuditorBean.getStatus() == 0 || applyAuditorBean.getStatus() == 1)
        {
            viewHolder.tvStatus.setText("待审核");
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_12a7e2));
            viewHolder.ivTop.setVisibility(View.VISIBLE);
            viewHolder.ivMid.setImageResource(R.mipmap.icon_need_agree);
            viewHolder.ivButtom.setImageResource(R.color.color_e8e8e8);
            viewHolder.ivTop.setImageResource(R.color.color_e8e8e8);
            if(position >= 1)
            {
                if(((ApplyAuditorBean)mDatas.get(0)).getStatus() == 5)
                {
                    viewHolder.ivTop.setImageResource(R.color.color_0cd38a);
                }
            }
        }
        else if(applyAuditorBean.getStatus() == 3)
        {
            if(isAgree)
            {

            }
            else {
                viewHolder.tvStatus.setText("不同意");
            }
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_e10000));
            viewHolder.ivTop.setVisibility(View.VISIBLE);
            viewHolder.ivMid.setImageResource(R.mipmap.icon_noagree);
            viewHolder.ivButtom.setImageResource(R.color.color_e8e8e8);
            viewHolder.ivTop.setImageResource(R.color.color_e8e8e8);
            if(position >= 1)
            {
                if(((ApplyAuditorBean)mDatas.get(0)).getStatus() == 5)
                {
                    viewHolder.ivTop.setImageResource(R.color.color_0cd38a);
                }
            }
        }
        else if(applyAuditorBean.getStatus() == 5)
        {
            isAgree = true;
            viewHolder.tvStatus.setText("已同意");
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_0cd38a));
            viewHolder.ivTop.setVisibility(View.VISIBLE);
            viewHolder.ivMid.setImageResource(R.mipmap.icon_agree);
            viewHolder.ivTop.setImageResource(R.color.color_0cd38a);
            if(position != mDatas.size() - 1)
            {
                if(((ApplyAuditorBean)mDatas.get(mDatas.size() - 1)).getStatus() == 5)
                {
                    viewHolder.ivButtom.setImageResource(R.color.color_0cd38a);
                }
            }
        }
        if(position < mDatas.size() - 1)
        {
            viewHolder.ivButtom.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.ivButtom.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.riv_head)
        RoundImageView rivHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_top)
        ImageView ivTop;
        @BindView(R.id.iv_mid)
        ImageView ivMid;
        @BindView(R.id.iv_buttom)
        ImageView ivButtom;


        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

}

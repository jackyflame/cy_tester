package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.OrderBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.ywl5320.RichTextLayoutH;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/18.
 */

public class RoomServiceAdapter extends BaseDataAdapter{

    public RoomServiceAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        OrderBean orderBean = (OrderBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_room_service_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(orderBean.getPicture())) {
            viewHolder.ivPicture.setVisibility(View.VISIBLE);
            ImageLoad.getInstance().displayeRoundImage(context, viewHolder.ivPicture, HttpMethod.IMG_URL + orderBean.getPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        }
        else {
            viewHolder.ivPicture.setVisibility(View.GONE);
        }
        if(orderBean.getType() == 6)
        {
            viewHolder.lyAddress.setVisibility(View.INVISIBLE);
        }

        if(orderBean.getStatus() == 1)
        {
            viewHolder.tvStatus.setText("申请中");
        }
        else
        {
            viewHolder.tvStatus.setText("已完成");
        }

        viewHolder.tvAddress.setText(orderBean.getAddress());
        viewHolder.tvRemark.setText(orderBean.getRemark());
        viewHolder.tvTime.setText(orderBean.getCreateTime());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.ly_address)
        LinearLayout lyAddress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.OrderBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.views.home.shop.OrderDetailActivity;
import com.cyztc.app.widget.SquareImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/30.
 */

public class OrderListAdapter extends BaseDataAdapter{


    public OrderListAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final OrderBean orderBean = (OrderBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_order_list_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvOrderId.setText(orderBean.getId());
        viewHolder.tvTime.setText(orderBean.getCreateTime());
        viewHolder.tvMark.setText(orderBean.getRemark());
        viewHolder.tvAmount.setText(orderBean.getCost() + "");

        if(orderBean.getStatus() == 1)
        {
            viewHolder.tvStatus.setText("待支付");
        }
        else if(orderBean.getStatus() == 2)
        {
            viewHolder.tvStatus.setText("取消");
        }
        else if(orderBean.getStatus() == 3)
        {
            viewHolder.tvStatus.setText("已支付");
        }
        else if(orderBean.getStatus() == 4)
        {
            viewHolder.tvStatus.setText("正在送货");
        }
        else if(orderBean.getStatus() == 8)
        {
            viewHolder.tvStatus.setText("交易完成");
        }
        else
        {
            viewHolder.tvStatus.setText("待支付");
        }

        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.tv_orderid)
        TextView tvOrderId;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_mark)
        TextView tvMark;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

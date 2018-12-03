package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TrainEnlistBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017/1/8.
 */

public class TrainEnlistAdapter extends BaseDataAdapter{

    public TrainEnlistAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        TrainEnlistBean trainEnlistBean = (TrainEnlistBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_enlist_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(trainEnlistBean.getClassName());
        viewHolder.tvTime.setText(trainEnlistBean.getStartDate() + "~" + trainEnlistBean.getEndDate());
        viewHolder.tvStatus.setText(trainEnlistBean.getPayStatus());
        if(trainEnlistBean.getCost() == 0)
        {
            viewHolder.tvPrice.setText("免费");
        }
        else
        {
            viewHolder.tvPrice.setText("¥" + trainEnlistBean.getCost());
        }
        viewHolder.tvTeacher.setText(trainEnlistBean.getTeacherName());
        ImageLoad.getInstance().displayImage(context, viewHolder.ivImg, HttpMethod.IMG_URL + trainEnlistBean.getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);


        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_teacher)
        TextView tvTeacher;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

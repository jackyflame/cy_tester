package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TrainHistoryBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017/1/10.
 */

public class TrainHistoryAdapter extends BaseDataAdapter{

    public TrainHistoryAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrainHistoryBean trainHistoryBean = (TrainHistoryBean) mDatas.get(position);
        ViewHolder viewHolder = null;

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_train_history_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.ivImg, HttpMethod.IMG_URL + trainHistoryBean.getTrainPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        viewHolder.tvName.setText(trainHistoryBean.getTrainName());
        viewHolder.tvTeacher.setText("培训教师：" + trainHistoryBean.getTeacherName());
        viewHolder.tvTime.setText("培训时间：" + trainHistoryBean.getStartDate());
        viewHolder.tvAmount.setText("培训费用：" + trainHistoryBean.getCost() + "");


        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_teacher)
        TextView tvTeacher;
        @BindView(R.id.tv_amount)
        TextView tvAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

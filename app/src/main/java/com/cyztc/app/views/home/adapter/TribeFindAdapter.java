package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TribeRecommendBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.SquareImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/27.
 */

public class TribeFindAdapter extends BaseDataAdapter{

    public TribeFindAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        TribeRecommendBean tribeRecommendBean = (TribeRecommendBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_findtribe_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.ivImg, HttpMethod.IMG_URL + tribeRecommendBean.getThumbnail(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        viewHolder.tvName.setText(tribeRecommendBean.getName());
        viewHolder.tvRemark.setText(tribeRecommendBean.getRemark());
        viewHolder.tvRmember.setText("成员：" + tribeRecommendBean.getMemberNum());
        viewHolder.tvTopics.setText("话题：" + tribeRecommendBean.getTopicNum());
        viewHolder.tvType.setText(tribeRecommendBean.getStatus() + "");

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_img)
        SquareImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_remember)
        TextView tvRmember;
        @BindView(R.id.tv_topics)
        TextView tvTopics;
        @BindView(R.id.tv_type)
        TextView tvType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

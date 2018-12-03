package com.cyztc.app.views.knowledge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.VideoBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/9.
 */

public class VideoDetailAdapter extends BaseDataAdapter{

    private int type;

    public VideoDetailAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        VideoBean videoBean = (VideoBean) mDatas.get(position);

        if(convertView == null) {
            convertView = mlayoutInflate.inflate(R.layout.item_video_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(videoBean.getTitle());
        viewHolder.tvRemark.setText(videoBean.getRemark());
        viewHolder.tvPlayCount.setText(videoBean.getReadCount() + "播放");
        if(type == 2)
        {
            viewHolder.ivImgTop.setImageResource(R.mipmap.icon_video_item_img);
        }
        else if(type == 3)
        {
            viewHolder.ivImgTop.setImageResource(R.mipmap.icon_audio_item_img);
        }

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.iv_img_top)
        ImageView ivImgTop;
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_playcount)
        TextView tvPlayCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.cyztc.app.views.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TribeTopicBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.views.home.tribe.TribeCommentActivity;
import com.cyztc.app.views.home.tribe.TribeDetailActivity;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.cyztc.app.widget.ywl5320.RichTextLayoutH;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/21.
 */

public class TribeDetailAdapter extends BaseDataAdapter{

    public TribeDetailAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final TribeTopicBean tribeTopicBean = (TribeTopicBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_tribe_detail_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayCircleImage(context, viewHolder.ivhead, HttpMethod.IMG_URL + tribeTopicBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        viewHolder.tvName.setText(tribeTopicBean.getStudentName());
        viewHolder.tvTime.setText(tribeTopicBean.getCreateTime());
        viewHolder.tvTitle.setText(tribeTopicBean.getTitle());
        viewHolder.richTextLayouth.setDatas(tribeTopicBean.getContent().replaceAll("\\n", ""), R.color.color_333333, 3, (Activity) context);
        viewHolder.richTextLayouth.setOnImgClickListener(new RichTextLayoutH.OnImgClickListenerH() {
            @Override
            public void onImtClick(String url) {
//                ImgShowDialog imgShowDialog = new ImgShowDialog(context);
//                imgShowDialog.setImgpath(HttpMethod.IMG_URL + url);
//                imgShowDialog.show();
                Gson gson = new Gson();
                String topstr = gson.toJson(tribeTopicBean);
                TribeCommentActivity.startActivity(context, topstr);
            }
        });
        viewHolder.tvSee.setText(tribeTopicBean.getReadCount() + "");
        viewHolder.tvReplay.setText(tribeTopicBean.getReplyCount() + "");
        viewHolder.tvZan.setText(tribeTopicBean.getLikeCount() + "");
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_head)
        ImageView ivhead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_see)
        TextView tvSee;
        @BindView(R.id.tv_replay)
        TextView tvReplay;
        @BindView(R.id.tv_zan)
        TextView tvZan;
        @BindView(R.id.rtl_text)
        RichTextLayoutH richTextLayouth;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

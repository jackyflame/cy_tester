package com.cyztc.app.views.home.adapter;

import android.app.Activity;
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
import com.cyztc.app.bean.CuiYueCircleBean;
import com.cyztc.app.bean.TribeTopicBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.home.CuiYueCircleActivity;
import com.cyztc.app.views.home.CuiYueCircleDetailActivity;
import com.cyztc.app.widget.ywl5320.RichTextLayoutH;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/27.
 */

public class CuiYueCircleAdapter extends BaseDataAdapter{

    public int dotType;

    public void setDotType(int dotType) {
        this.dotType = dotType;
    }

    public CuiYueCircleAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final CuiYueCircleBean cuiYueCircleBean = (CuiYueCircleBean) mDatas.get(position);

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

        ImageLoad.getInstance().displayCircleImage(context, viewHolder.ivhead, HttpMethod.IMG_URL + cuiYueCircleBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        viewHolder.tvName.setText(cuiYueCircleBean.getStudentName());
        viewHolder.tvTime.setText(cuiYueCircleBean.getCreateTime());
        viewHolder.tvTitle.setText(cuiYueCircleBean.getTitle());
        viewHolder.richTextLayouth.setDatas(cuiYueCircleBean.getContent().replaceAll("\\n", ""), R.color.color_333333, 3, (Activity) context);
        viewHolder.richTextLayouth.setOnImgClickListener(new RichTextLayoutH.OnImgClickListenerH() {
            @Override
            public void onImtClick(String url) {
//                ImgShowDialog imgShowDialog = new ImgShowDialog(context);
//                imgShowDialog.setImgpath(HttpMethod.IMG_URL + url);
//                imgShowDialog.show();
                CuiYueCircleDetailActivity.startActivity(context, cuiYueCircleBean.getId(), dotType);
            }
        });
        viewHolder.tvSee.setText(cuiYueCircleBean.getReadCount() + "");
        viewHolder.lyAction.setVisibility(View.GONE);
        String des = CommonUtil.getStringFromContent(cuiYueCircleBean.getContent());
        if(TextUtils.isEmpty(des))
        {
            viewHolder.tvRemark.setText(CommonUtil.getStrFromRichContent(cuiYueCircleBean.getContent().replaceAll("\\n", "")));
        }
        else {
            viewHolder.tvRemark.setText(des);
        }
//        viewHolder.tvReplay.setText(cuiYueCircleBean.getReplyCount() + "");
//        viewHolder.tvZan.setText(cuiYueCircleBean.getLikeCount() + "");
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
        @BindView(R.id.ly_action)
        LinearLayout lyAction;
        @BindView(R.id.rtl_text)
        RichTextLayoutH richTextLayouth;
        @BindView(R.id.tv_remark)
        TextView tvRemark;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

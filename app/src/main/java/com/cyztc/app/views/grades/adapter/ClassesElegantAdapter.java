package com.cyztc.app.views.grades.adapter;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ClassMineBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.grades.ClassCircleDetailActivity;
import com.cyztc.app.widget.ywl5320.RichTextLayoutH;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/3.
 */

public class ClassesElegantAdapter extends BaseDataAdapter{

    public ClassesElegantAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final ClassMineBean classMineBean = (ClassMineBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_classes_info_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvContent.setAuto(false);
        viewHolder.tvTitle.setText(classMineBean.getTitle());
        viewHolder.tvContent.setDatas(classMineBean.getContent().replaceAll("\\n", ""), R.color.color_333333, 3, (Activity) context);
        String des = CommonUtil.getStringFromContent(classMineBean.getContent());
        if(TextUtils.isEmpty(des))
        {
            viewHolder.tvDes.setVisibility(View.GONE);
        }
        else {
            viewHolder.tvDes.setVisibility(View.VISIBLE);
            viewHolder.tvDes.setText(des);
        }
        viewHolder.tvContent.setOnImgClickListener(new RichTextLayoutH.OnImgClickListenerH() {
            @Override
            public void onImtClick(String url) {
                ClassCircleDetailActivity.startActivity(context, classMineBean);
            }
        });
        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        RichTextLayoutH tvContent;
        @BindView(R.id.tv_des)
        TextView tvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

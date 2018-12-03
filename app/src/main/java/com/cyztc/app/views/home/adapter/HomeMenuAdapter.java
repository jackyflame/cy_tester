package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.HomeItemBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by ywl on 2016/11/15.
 */

public class HomeMenuAdapter extends BaseDataAdapter {

    public HomeMenuAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeItemBean homeItemBean = (HomeItemBean) mDatas.get(position);
        convertView = mlayoutInflate.inflate(R.layout.item_home_menu_layout, parent, false);
        AutoUtils.auto(convertView);

        int id_up = context.getResources().getIdentifier(homeItemBean.getIcon(), "mipmap", context.getPackageName());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_menu);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        ImageView ivDot = (ImageView) convertView.findViewById(R.id.iv_dot);
        imageView.setImageResource(id_up);
        tvName.setText(homeItemBean.getName());
        if(homeItemBean.isShowDot())
        {
            ivDot.setVisibility(View.VISIBLE);
        }
        else
        {
            ivDot.setVisibility(View.GONE);
        }

        return convertView;
    }
}

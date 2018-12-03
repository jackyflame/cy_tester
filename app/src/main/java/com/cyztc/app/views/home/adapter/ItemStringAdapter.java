package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.StringBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by ywl on 2016/12/20.
 */

public class ItemStringAdapter extends BaseDataAdapter{

    public ItemStringAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StringBean stringBean = (StringBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_only_image_layout, parent, false);
            AutoUtils.auto(convertView);
        }
        ImageView ivimg = (ImageView) convertView.findViewById(R.id.iv_img);
        ImageLoad.getInstance().displayImage(context, ivimg, HttpMethod.IMG_URL + stringBean.getStr().replace("\\", "/"), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        return convertView;
    }
}

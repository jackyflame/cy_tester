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
import com.cyztc.app.bean.TribeMemberBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeMemberAdapter extends BaseDataAdapter{

    public TribeMemberAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        TribeMemberBean tribeMemberBean = (TribeMemberBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_tribe_member_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayCircleImage(context, viewHolder.ivhead, HttpMethod.IMG_URL + tribeMemberBean.getPhoto(),R.mipmap.icon_load_img, R.mipmap.icon_load_img);

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_head)
        ImageView ivhead;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

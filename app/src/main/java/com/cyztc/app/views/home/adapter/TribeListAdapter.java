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
import com.cyztc.app.bean.TribeBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.SquareImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/20.
 */

public class TribeListAdapter extends BaseDataAdapter{

    public TribeListAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        TribeBean tribeBean = (TribeBean) mDatas.get(position);
        if(convertView == null) {
            convertView = mlayoutInflate.inflate(R.layout.item_tribe_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(tribeBean.getType() == 0)
        {
            viewHolder.ivCreateTribe.setVisibility(View.GONE);
            viewHolder.ivImg.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText(tribeBean.getName());
            ImageLoad.getInstance().displayImage(context, viewHolder.ivImg, HttpMethod.IMG_URL + tribeBean.getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        }
        else
        {
            viewHolder.ivCreateTribe.setVisibility(View.VISIBLE);
            viewHolder.ivImg.setVisibility(View.INVISIBLE);
            viewHolder.tvName.setText("");
        }

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.iv_img)
        SquareImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_create_tribe)
        ImageView ivCreateTribe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

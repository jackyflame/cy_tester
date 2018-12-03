package com.cyztc.app.views.grades.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.AlbumBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.SquareImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/30.
 */

public class ClassAlbumAdapter extends BaseDataAdapter{

    public ClassAlbumAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        AlbumBean albumBean = (AlbumBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_album_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoad.getInstance().displayImage(context, viewHolder.sivImg, HttpMethod.IMG_URL + albumBean.getPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.siv_img)
        SquareImageView sivImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

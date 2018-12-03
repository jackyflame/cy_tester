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
import com.cyztc.app.bean.GridViewImgBean;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.widget.SquareImageView;
import com.cyztc.app.widget.ywl5320.CountView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/22.
 */

public class GridVeiwImgAdapter extends BaseDataAdapter{

    public GridVeiwImgAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewImgBean gridViewImgBean = (GridViewImgBean) mDatas.get(position);

        ViewHolder viewholder = null;

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_gridview_img_adapter_layout, parent, false);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);
        }
        else
        {
            viewholder = (ViewHolder) convertView.getTag();
        }

        if(gridViewImgBean.getType() == 0)//空白
        {
            viewholder.squareImageView.setImageResource(R.mipmap.icon_tribe_avatar);
        }
        else
        {
            ImageLoad.getInstance().displayImage(context, viewholder.squareImageView, gridViewImgBean.getStr(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        }

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_img)
        SquareImageView squareImageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

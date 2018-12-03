package com.cyztc.app.views.knowledge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/30.
 */

public class BackBookAdapter extends BaseDataAdapter{

    public BackBookAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        BookBean bookBean = (BookBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_back_book_adapter, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.iv_img, HttpMethod.IMG_URL + bookBean.getPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        viewHolder.tvTitle.setText(bookBean.getTitle());
        viewHolder.tvContent.setText("简介：" + bookBean.getRemark());

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.iv_img)
        ImageView iv_img;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

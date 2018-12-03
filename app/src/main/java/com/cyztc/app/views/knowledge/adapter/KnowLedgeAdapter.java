package com.cyztc.app.views.knowledge.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.bean.BookListBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/11/3.
 */

public class KnowLedgeAdapter extends BaseDataAdapter{

    public KnowLedgeAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        BookBean bookBean = (BookBean) mDatas.get(position);

        if(convertView == null) {
            convertView = mlayoutInflate.inflate(R.layout.item_know_info_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(bookBean.getTitle());
        viewHolder.tvRemark.setText(bookBean.getRemark());

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.ly_collec)
        LinearLayout lyCollec;
        @BindView(R.id.ly_download)
        LinearLayout lyDownload;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

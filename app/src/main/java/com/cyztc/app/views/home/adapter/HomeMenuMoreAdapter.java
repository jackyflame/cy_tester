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
import com.cyztc.app.bean.HomeItemBean;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/4.
 */

public class HomeMenuMoreAdapter extends BaseDataAdapter{

    private OnSelectedListener onSelectedListener;

    public HomeMenuMoreAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final HomeItemBean homeItemBean = (HomeItemBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_home_more_item_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int id_up = context.getResources().getIdentifier(homeItemBean.getIcon(), "mipmap", context.getPackageName());
        viewHolder.ivIcon.setImageResource(id_up);
        viewHolder.tvName.setText(homeItemBean.getName());
        if(homeItemBean.isSelected())
        {
            viewHolder.ivSelected.setSelected(true);
        }
        else
        {
            viewHolder.ivSelected.setSelected(false);
        }

        viewHolder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeItemBean.setSelected(!homeItemBean.isSelected());
                notifyDataSetChanged();
                if(onSelectedListener != null)
                {
                    onSelectedListener.onSelected();
                }
            }
        });

        return convertView;
    }
    static class ViewHolder {

        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSelectedListener
    {
        void onSelected();
    }

}

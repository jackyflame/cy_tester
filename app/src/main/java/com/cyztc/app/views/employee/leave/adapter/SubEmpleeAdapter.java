package com.cyztc.app.views.employee.leave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.DepartMentSubBean;
import com.cyztc.app.widget.CustomListView;
import com.cyztc.app.widget.RoundImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-5-26.
 */

public class SubEmpleeAdapter extends BaseDataAdapter{

    public SubEmpleeAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        DepartMentSubBean departMentSubBean = (DepartMentSubBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_choice_department_personal_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(departMentSubBean.getName());
        viewHolder.tvPosition.setText(departMentSubBean.getPosition());
        viewHolder.tvPhone.setText(departMentSubBean.getPhoto());
        if(departMentSubBean.isSelected())
        {
            viewHolder.ivSelected.setSelected(true);
        }
        else
        {
            viewHolder.ivSelected.setSelected(false);
        }



        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.riv_head)
        RoundImageView rivHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.tv_phone)
        TextView tvPhone;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}

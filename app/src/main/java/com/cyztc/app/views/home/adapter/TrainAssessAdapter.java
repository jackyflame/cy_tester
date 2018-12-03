package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.AssessListBean;
import com.cyztc.app.bean.BaseBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/23.
 */

public class TrainAssessAdapter extends BaseDataAdapter{

    public TrainAssessAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        AssessListBean assessListBean = (AssessListBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_train_assess_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(assessListBean.getTitle());
        viewHolder.tvRemark.setText(assessListBean.getRemark());
        viewHolder.tvTime.setText(assessListBean.getCreateTime());

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

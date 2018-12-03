package com.cyztc.app.views.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.EatHistoryBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-6-29.
 */

public class EatListAdapter extends BaseDataAdapter{

    public EatListAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EatHistoryBean eatHistoryBean = (EatHistoryBean) mDatas.get(position);
        ViewHolder viewHolder;

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_eat_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvEat.setText(eatHistoryBean.getDinnerTime());

        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.tv_eat)
        TextView tvEat;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}

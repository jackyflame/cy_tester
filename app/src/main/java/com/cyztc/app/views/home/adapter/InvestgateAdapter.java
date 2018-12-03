package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.AssessListBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.InvestgateBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017/2/18.
 */

public class InvestgateAdapter extends BaseDataAdapter{

    public InvestgateAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        InvestgateBean investgateBean = (InvestgateBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_investgate_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(investgateBean.getIsSubmitedAnswer() == 0)
        {
            viewHolder.tvAlready.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.tvAlready.setVisibility(View.VISIBLE);
        }

        viewHolder.tvName.setText(investgateBean.getName());

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_already)
        TextView tvAlready;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.cyztc.app.views.grades.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CorseBean;
import com.cyztc.app.bean.EmSignBean;
import com.cyztc.app.bean.EmSignDetailBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/18.
 */

public class EmSigninAdapter extends BaseDataAdapter{

    public EmSigninAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        EmSignBean emSignDetailBean = (EmSignBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_emsignin_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDate.setText(emSignDetailBean.getWorkDate());
        viewHolder.tvWeek.setText(emSignDetailBean.getWeek());

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_week)
        TextView tvWeek;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

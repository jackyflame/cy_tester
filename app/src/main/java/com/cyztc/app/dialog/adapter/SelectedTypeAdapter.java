package com.cyztc.app.dialog.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.OnlyStringBean;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-5-20.
 */

public class SelectedTypeAdapter extends BaseDataAdapter{

    public SelectedTypeAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OnlyStringBean onlyStringBean = (OnlyStringBean) mDatas.get(position);
        ViewHolder viewHolder ;

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_selected_type_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(onlyStringBean.getItem());

        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.tv_name)
        TextView tvName;
        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}

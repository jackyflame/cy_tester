package com.cyztc.app.views.employee.leave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.widget.CustomListView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-5-26.
 */

public class SubDepartAdapter extends BaseDataAdapter{

    public SubDepartAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        DepartMentBean departMentBean = (DepartMentBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_choice_department_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(departMentBean.getSubDept() != null && departMentBean.getEmployee() != null) {
            viewHolder.tvName.setText(departMentBean.getDeptName() + "（" + (departMentBean.getSubDept().size() + departMentBean.getEmployee().size()) + "）");
        }
        else if(departMentBean.getSubDept() != null && departMentBean.getEmployee() == null)
        {
            viewHolder.tvName.setText(departMentBean.getDeptName() + "（" + departMentBean.getSubDept().size() + "）");
        }
        else if(departMentBean.getSubDept() == null && departMentBean.getEmployee() != null)
        {
            viewHolder.tvName.setText(departMentBean.getDeptName() + "（" + departMentBean.getEmployee().size() + "）");
        }else
        {
            viewHolder.tvName.setText(departMentBean.getDeptName() + "（0）");
        }



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

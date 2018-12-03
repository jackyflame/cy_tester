package com.cyztc.app.views.employee.leave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.DepartMentSubBean;
import com.cyztc.app.widget.CustomListView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-5-26.
 */

public class DepartMentAdapter extends BaseDataAdapter{

    private SubDepartAdapter subDepartAdapter;
    private SubEmpleeAdapter subEmpleeAdapter;

//    private List<DepartMentBean> dBeans;
//    private List<DepartMentSubBean> dsBeans;

    private OnDepartItemLisener onDepartItemLisener;

    public void setOnDepartItemLisener(OnDepartItemLisener onDepartItemLisener) {
        this.onDepartItemLisener = onDepartItemLisener;
    }

    public DepartMentAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final DepartMentBean departMentBean = (DepartMentBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_choice_list_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(departMentBean.getSubDept() != null) {
            subDepartAdapter = new SubDepartAdapter(context, departMentBean.getSubDept());
            viewHolder.lvDepart.setAdapter(subDepartAdapter);
            subDepartAdapter.notifyDataSetChanged();
        }
        if(departMentBean.getEmployee() != null)
        {
            subEmpleeAdapter = new SubEmpleeAdapter(context, departMentBean.getEmployee());
            viewHolder.lvEmplee.setAdapter(subEmpleeAdapter);
            subEmpleeAdapter.notifyDataSetChanged();
        }

        viewHolder.lvDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartMentBean departMen = (DepartMentBean) viewHolder.lvDepart.getItemAtPosition(position);
                if(departMen != null && onDepartItemLisener != null)
                {
                    onDepartItemLisener.onDepartClick(departMen);
                }
            }
        });

        viewHolder.lvEmplee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartMentSubBean departMentSubBean = (DepartMentSubBean) viewHolder.lvEmplee.getItemAtPosition(position);
                if(departMentSubBean != null && onDepartItemLisener != null)
                {
                    selectedEmplee(viewHolder.lvEmplee, departMentBean.getEmployee(), departMentSubBean);
                    onDepartItemLisener.onEmpleeClick(departMentSubBean);
                }
            }
        });




        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.lv_depart)
        CustomListView lvDepart;
        @BindView(R.id.lv_emplee)
        CustomListView lvEmplee;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnDepartItemLisener
    {
        void onDepartClick(DepartMentBean departMentBean);

        void onEmpleeClick(DepartMentSubBean departMentSubBean);
    }

    private void selectedEmplee(CustomListView customListView, List<DepartMentSubBean> emplees, DepartMentSubBean departMentSubBean)
    {
        for(DepartMentSubBean departMent : emplees)
        {
            if(departMent.getName().equals(departMentSubBean.getName()))
            {
                departMentSubBean.setSelected(true);
            }
            else
            {
                departMent.setSelected(false);
            }
        }
        if(subEmpleeAdapter != null) {
            subEmpleeAdapter = new SubEmpleeAdapter(context, emplees);
            customListView.setAdapter(subEmpleeAdapter);
            subEmpleeAdapter.notifyDataSetChanged();
        }
    }

}

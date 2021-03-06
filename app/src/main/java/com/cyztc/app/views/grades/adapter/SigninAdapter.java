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
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/18.
 */

public class SigninAdapter extends BaseDataAdapter{

    public SigninAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        CorseBean corseBean = (CorseBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_signin_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTime.setText(corseBean.getCourseDate() + "    " + corseBean.getStartTime() + "-" + corseBean.getEndTime());
        viewHolder.tvContent.setText("内容：" + corseBean.getCourseName());
        viewHolder.tvAddress.setText("地址：" + corseBean.getAddress());
        if(TextUtils.isEmpty(corseBean.getTeacher()))
        {
            viewHolder.tvTeacher.setText("教师：");
        }
        else
        {
            viewHolder.tvTeacher.setText("教师：" + corseBean.getTeacher());
        }

        if(corseBean.getIsSign() == 1)
        {
            viewHolder.tvSigntxt.setText("已签到");
            viewHolder.tvSigntxt.setTextColor(context.getResources().getColor(R.color.color_0cd38a));
            viewHolder.imSignin.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.tvSigntxt.setText("未签到");
            viewHolder.tvSigntxt.setTextColor(context.getResources().getColor(R.color.color_e10000));
            viewHolder.imSignin.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_teacher)
        TextView tvTeacher;
        @BindView(R.id.iv_signin)
        ImageView imSignin;
        @BindView(R.id.tv_signtxt)
        TextView tvSigntxt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

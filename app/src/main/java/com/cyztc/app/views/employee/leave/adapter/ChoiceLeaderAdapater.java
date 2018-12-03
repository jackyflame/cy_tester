package com.cyztc.app.views.employee.leave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DepartMentSubBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017-5-28.
 */

public class ChoiceLeaderAdapater extends BaseDataAdapter{

    private OnDelListener onDelListener;

    public ChoiceLeaderAdapater(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    public OnDelListener getOnDelListener() {
        return onDelListener;
    }

    public void setOnDelListener(OnDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DepartMentSubBean departMentSubBean = (DepartMentSubBean) mDatas.get(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_lieave_agree_choice_adapter_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(departMentSubBean.getName());
        viewHolder.tvPosition.setText(departMentSubBean.getPosition());
        ImageLoad.getInstance().displayImage(context, viewHolder.rivHead, HttpMethod.IMG_URL + departMentSubBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDelListener != null)
                {
                    onDelListener.onDel(departMentSubBean);
                }
            }
        });

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
        @BindView(R.id.iv_del)
        ImageView ivDel;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnDelListener
    {
        void onDel(DepartMentSubBean departMentSubBean);
    }
}

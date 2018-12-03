package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by window10 on 2016/3/30.
 */
public class PhoneContactAdapter extends BaseDataAdapter {

    private OnSelectedListener onSelectedListener;

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    public PhoneContactAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        return ((ContactBean) mDatas.get(position)).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        ViewHolderIndex viewHolderIndex = null;
        final ContactBean contactBean = (ContactBean) mDatas.get(position);
        int type = contactBean.getType();
        if(convertView == null)
        {
            if(type == 0) {
                viewHolder = new ViewHolder();
                convertView = mlayoutInflate.inflate(R.layout.item_phone_contact_adapter, parent, false);
                viewHolder.tvname = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.ivhead = (ImageView) convertView.findViewById(R.id.iv_head);
                viewHolder.tvPhoneNum = (TextView) convertView.findViewById(R.id.tv_phonenum);
                viewHolder.tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
                AutoUtils.auto(convertView);
                convertView.setTag(viewHolder);
            }
            else if(type == 1)
            {
                viewHolderIndex = new ViewHolderIndex();
                convertView = mlayoutInflate.inflate(R.layout.item_letter_index, parent, false);
                viewHolderIndex.tvindex = (TextView) convertView.findViewById(R.id.tv_index);
                convertView.setTag(viewHolderIndex);
            }
        }
        else
        {
            if(type == 0) {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            else if(type == 1)
            {
                viewHolderIndex = (ViewHolderIndex) convertView.getTag();
            }
        }
        if(type == 0) {
            ImageLoad.getInstance().displayCircleImage(context, viewHolder.ivhead, HttpMethod.IMG_URL + contactBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
            viewHolder.tvname.setText(contactBean.getStudentName());
            viewHolder.tvPhoneNum.setText(contactBean.getPhoneNum());
            viewHolder.tvPosition.setText(contactBean.getPosition());
        }
        else if(type == 1)
        {
            viewHolderIndex.tvindex.setText(contactBean.getStudentName());
        }

        return convertView;
    }

    public class ViewHolder
    {
        public TextView tvname;
        public ImageView ivhead;
        public TextView tvPhoneNum;
        public TextView tvPosition;
    }

    public class ViewHolderIndex
    {
        public TextView tvindex;
    }

    public interface OnSelectedListener
    {
        void onSelected(ContactBean contactBean);
    }
}

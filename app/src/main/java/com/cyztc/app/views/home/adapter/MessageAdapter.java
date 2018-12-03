package com.cyztc.app.views.home.adapter;

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
import com.cyztc.app.bean.MessageBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/27.
 */

public class MessageAdapter extends BaseDataAdapter{

    public MessageAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        MessageBean messageBean = (MessageBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_message_adapter_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTime.setText(messageBean.getSendtime());
        viewHolder.tvTitle.setText(messageBean.getSender());
        viewHolder.tvTime.setText(messageBean.getSendtime());
        viewHolder.tvSubtitle.setText(messageBean.getTitle());
        viewHolder.tvContent.setText(sortData(messageBean.getContent()));
        if(messageBean.getStatus() == 0)
        {
            viewHolder.ivDot.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.ivDot.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.iv_dot)
        ImageView ivDot;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public String sortData(String content)
    {
        List<String> soruce = new ArrayList<>();
        String[] datas = content.split("\\[img\\]");
        for(int i = 0; i < datas.length; i++)
        {
            if(datas[i].contains("[/img]"))
            {
                String[] data = datas[i].split("\\[/img\\]");
                for(int j = 0; j < data.length; j++)
                {
                    soruce.add(data[j]);
                }
            }
            else {
                String []t = datas[i].split("\\n");
                int l = t.length;
                for(int j = 0; j < l; j++)
                {
                    if(!TextUtils.isEmpty(t[j]))
                    {
                        soruce.add(t[j]);
                    }
                }

            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(String s : soruce)
        {
            if(!s.endsWith(".jpg") && !s.endsWith(".png"))
            {
                stringBuffer.append(s);
            }
        }
        return stringBuffer.toString();
    }
}

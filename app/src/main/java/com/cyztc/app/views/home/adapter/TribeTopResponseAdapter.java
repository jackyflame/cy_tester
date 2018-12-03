package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.TopResponBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.utils.EmojiParser;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/20.
 */

public class TribeTopResponseAdapter extends BaseDataAdapter{

    private boolean isAutor = false;
    private OnDeleteTopicListener onDeleteTopicListener;

    public TribeTopResponseAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
        EmojiParser.init(context);
    }

    public void setAutor(boolean autor) {
        isAutor = autor;
    }

    public void setOnDeleteTopicListener(OnDeleteTopicListener onDeleteTopicListener) {
        this.onDeleteTopicListener = onDeleteTopicListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final TopResponBean topResponBean = (TopResponBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_tribe_comment_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(isAutor)
        {
            viewHolder.tvDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.tvDelete.setVisibility(View.GONE);
        }

        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteTopicListener != null)
                {
                    onDeleteTopicListener.onDelete(topResponBean);
                }
            }
        });
        ImageLoad.getInstance().displayCircleImage(context, viewHolder.ivhead, HttpMethod.IMG_URL + topResponBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        viewHolder.tvName.setText(topResponBean.getStudentName());
        viewHolder.tvContent.setText(EmojiParser.getInstance().strToSmiley(topResponBean.getContent(), 50));
        viewHolder.tvTime.setText(topResponBean.getCreateTime());

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_head)
        ImageView ivhead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_delete)
        TextView tvDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface  OnDeleteTopicListener
    {
        void onDelete(TopResponBean topResponBean);
    }
}

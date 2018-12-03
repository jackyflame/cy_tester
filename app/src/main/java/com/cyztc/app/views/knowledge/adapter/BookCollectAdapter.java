package com.cyztc.app.views.knowledge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ywl on 2017/1/17.
 */

public class BookCollectAdapter extends BaseDataAdapter{

    private OnCollecListener onCollecListener;

    public void setOnCollecListener(OnCollecListener onCollecListener) {
        this.onCollecListener = onCollecListener;
    }

    public BookCollectAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final BookBean bookBean = (BookBean) mDatas.get(position);
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_back_book_adapter, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.iv_img, HttpMethod.IMG_URL + bookBean.getPicture(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        viewHolder.tvTitle.setText(bookBean.getTitle());
        viewHolder.tvContent.setText("简介：" + bookBean.getRemark());
        if(bookBean.getIsFavorite() == 0)
        {
            viewHolder.ivCollec.setSelected(false);
        }
        else {
            viewHolder.ivCollec.setSelected(true);
        }
        viewHolder.ivCollec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCollecListener != null)
                {
                    onCollecListener.onCollec(bookBean);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.iv_img)
        ImageView iv_img;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_collec)
        ImageView ivCollec;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCollecListener
    {
        void onCollec(BookBean bookBean);
    }

}

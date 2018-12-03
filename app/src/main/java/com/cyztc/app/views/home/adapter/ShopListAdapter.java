package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GoodsBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/10.
 */

public class ShopListAdapter extends BaseDataAdapter{

    public ShopListAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        GoodsBean goodsBean = (GoodsBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_shop_list_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.ivImg, HttpMethod.IMG_URL + goodsBean.getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        viewHolder.tvName.setText(goodsBean.getTitle());
        viewHolder.tvStyle.setText(goodsBean.getRemark());
        viewHolder.tvPrice.setText("¥" + CommonUtil.forMatPrice(goodsBean.getPrice()));
        viewHolder.tvSoldCount.setText("已售:" + goodsBean.getSoldCount());
        viewHolder.tvKucun.setText("库存:" + goodsBean.getCount());
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_style)
        TextView tvStyle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_soldcount)
        TextView tvSoldCount;
        @BindView(R.id.tv_kucun)
        TextView tvKucun;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

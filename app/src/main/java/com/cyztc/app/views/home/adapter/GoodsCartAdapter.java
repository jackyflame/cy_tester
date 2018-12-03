package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GoodsCartBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.SWToast;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.widget.ywl5320.CountView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/13.
 */

public class GoodsCartAdapter extends BaseDataAdapter{

    private boolean isEdit = false;
    private OnAdapterClick onAdapterClick;
    private boolean isPay = false;


    public GoodsCartAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    public void setOnAdapterClick(OnAdapterClick onAdapterClick) {
        this.onAdapterClick = onAdapterClick;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final GoodsCartBean goodsCartBean = (GoodsCartBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_goods_addcar_layout, parent, false);
            AutoUtils.auto(convertView);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoad.getInstance().displayImage(context, viewHolder.ivImg, HttpMethod.IMG_URL + goodsCartBean.getThumbnail(), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
        viewHolder.tvName.setText(goodsCartBean.getTitle());
        viewHolder.tvPrice.setText("¥" + CommonUtil.forMatPrice(goodsCartBean.getPrice()));
        viewHolder.tvRemark.setText(goodsCartBean.getRemark());
        viewHolder.tvCount.setText("x" + goodsCartBean.getCount());
        if(isEdit) {
            viewHolder.countView.setVisibility(View.VISIBLE);
            viewHolder.countView.initDatas(R.drawable.count_drawable_left, R.drawable.count_drawable_mid, R.drawable.count_drawable_right, goodsCartBean.getCount(), 100, 1);
        }
        else
        {
            viewHolder.countView.setVisibility(View.GONE);
        }

        if(!isPay) {
            if (goodsCartBean.isSelected()) {
                viewHolder.ivSelected.setSelected(true);
            } else {
                viewHolder.ivSelected.setSelected(false);
            }

            viewHolder.countView.setOnCountListener(new CountView.OnCountListener() {
                @Override
                public void onCount(int count, int changeCount) {
                    goodsCartBean.setCount(count);
                    goodsCartBean.setChangeCount(changeCount);
                    notifyDataSetChanged();
                    if (onAdapterClick != null) {
                        onAdapterClick.onCount();
                    }
                    //changeCount(goodsCartBean, CyApplication.getInstance().getUserBean().getStudentId());
                }
            });

            viewHolder.ivSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsCartBean.setSelected(!goodsCartBean.isSelected());
                    if (onAdapterClick != null) {
                        onAdapterClick.onSelected();
                    }
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.countview)
        CountView countView;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_count)
        TextView tvCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnAdapterClick
    {
        void onSelected();

        void onCount();
    }

    public void changeCount(GoodsCartBean goodsCartBean, String studentId)
    {
        HomeApi.getInstance().addCart(studentId, goodsCartBean.getId(), goodsCartBean.getChangeCount(), new HttpSubscriber<Integer>(new SubscriberOnListener<Integer>() {
            @Override
            public void onSucceed(Integer data) {

            }

            @Override
            public void onError(int code, String msg) {
                SWToast.getToast().toast("msg");
            }
        }, context));
    }
}

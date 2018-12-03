package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.DoorStatueBean;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.SWToast;
import com.cyztc.app.widget.ywl5320.ColorProgressBarView;
import com.cyztc.app.widget.ywl5320.CountView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ywl on 2016/12/22.
 */

public class DoorStatueAdapter extends BaseDataAdapter{

    private OnOpenListener onOpenListener;

    public DoorStatueAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    public void setOnOpenListener(OnOpenListener onOpenListener) {
        this.onOpenListener = onOpenListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final DoorStatueBean doorStatueBean = (DoorStatueBean) mDatas.get(position);

        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_door_statue_adapter__layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            AutoUtils.auto(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(doorStatueBean.getName());
        if(doorStatueBean.isOpen())
        {
            viewHolder.colorProgressBarView.setCurrentDegree(360, 360, false);
            viewHolder.ivLock.setImageResource(R.mipmap.icon_door_open);
        }
        else
        {
            viewHolder.colorProgressBarView.setCurrentDegree(360, 360, false);
            viewHolder.ivLock.setImageResource(R.mipmap.icon_door_close);
        }

        if(position == 0) {
            if(CyApplication.getInstance().getUserBean() != null) {
                viewHolder.tvTime.setVisibility(View.VISIBLE);
                viewHolder.tvAddress.setVisibility(View.VISIBLE);
                viewHolder.tvTime.setText("有效时间：" + CyApplication.getInstance().getUserBean().getLockTime());
                viewHolder.tvAddress.setText("寝室地址：" + CyApplication.getInstance().getUserBean().getBedRoom());
            }
        }
        else
        {
            viewHolder.tvTime.setVisibility(View.GONE);
            viewHolder.tvAddress.setVisibility(View.GONE);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.rlOpenDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doorStatueBean.setOpen(!doorStatueBean.isOpen());
                UserApi.getInstance().openLock(CyApplication.getInstance().getUserBean().getStudentId(),doorStatueBean.getCode(), doorStatueBean.getType(), new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
                    @Override
                    public void onSucceed(BaseBean data) {
                        if(onOpenListener != null)
                        {
                            onOpenListener.onOpen(true);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        if(onOpenListener != null)
                        {
                            onOpenListener.onOpen(false);
                        }
                    }
                }, context));

                if(doorStatueBean.isOpen())
                {
                    finalViewHolder.colorProgressBarView.setCurrentDegree(360, 150, true);

                }
                else
                {
                    finalViewHolder.colorProgressBarView.setCurrentDegree(360, 150, true);
                }

    }
});


        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.rl_open_door)
        RelativeLayout rlOpenDoor;
        @BindView(R.id.colorProgressbar)
        ColorProgressBarView colorProgressBarView;
        @BindView(R.id.iv_lock)
        ImageView ivLock;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_address)
        TextView tvAddress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnOpenListener
    {
        void onOpen(boolean success);
    }
}

package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CuiYueCircleDetailBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.tribe.TribeCommentActivity;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;


/**
 * Created by ywl on 2016/12/27.
 */

public class CuiYueCircleDetailActivity extends BaseActivity{

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_see)
    TextView tvSee;
    @BindView(R.id.richtextview)
    RichTextLayout richTextLayout;
    @BindView(R.id.tv_ctitle)
    TextView tvcTitle;

    private String cyid;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuiyue_circle_detail_layout);
        cyid = getIntent().getStringExtra("cyid");
        type = getIntent().getIntExtra("type", -1);
        setBackView();
        setTitle("动态详情");
        showDataLoadMsg("详情加载中");
        getDetail(cyid);
        handelMessage(cyid, type);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, String cyid, int type)
    {
        Intent intent = new Intent(context, CuiYueCircleDetailActivity.class);
        intent.putExtra("cyid", cyid);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public void getDetail(String id)
    {
        HomeApi.getInstance().getCuiYueDynamicDetail(id, new HttpSubscriber<CuiYueCircleDetailBean>(new SubscriberOnListener<CuiYueCircleDetailBean>() {
            @Override
            public void onSucceed(CuiYueCircleDetailBean data) {
                if(data != null)
                {
                    ImageLoad.getInstance().displayCircleImage(CuiYueCircleDetailActivity.this, ivHead, HttpMethod.IMG_URL + data.getCreatorPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
                    tvName.setText(data.getCreator());
                    tvTime.setText(data.getCreateTime());
                    tvSee.setText(data.getReadCount() + "");
                    tvcTitle.setText(data.getTitle());
                    richTextLayout.setAuto(true);
                    richTextLayout.setDatas(data.getContent(), R.color.color_333333, CuiYueCircleDetailActivity.this);
                    richTextLayout.setOnImgClickListener(new RichTextLayout.OnImgClickListener() {
                        @Override
                        public void onImtClick(String url) {
                            ImgShowDialog imgShowDialog = new ImgShowDialog(CuiYueCircleDetailActivity.this);
                            imgShowDialog.setImgpath(HttpMethod.IMG_URL + url);
                            imgShowDialog.show();
                        }
                    });
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("获取详情失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadMsg(msg);
            }
        }, this));
    }

    private void handelMessage(String targetId, int type)
    {
        UserApi.getInstance().delMessagePeding(CyApplication.getInstance().getUserBean().getAccountId(), targetId, type, "", new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        }, CuiYueCircleDetailActivity.this));
    }
}

package com.cyztc.app.views.grades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.ClassMineBean;
import com.cyztc.app.bean.CuiYueCircleDetailBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;


/**
 * Created by ywl on 2016/12/27.
 */

public class ClassCircleDetailActivity extends BaseActivity{

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

    private ClassMineBean classMineBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classMineBean = (ClassMineBean) getIntent().getExtras().get("classMineBean");
        setContentView(R.layout.activity_class_circle_detail_layout);
        setBackView();
        setTitle(classMineBean.getTitle());
        ImageLoad.getInstance().displayCircleImage(ClassCircleDetailActivity.this, ivHead, HttpMethod.IMG_URL + classMineBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        tvName.setText(classMineBean.getCreatorName());
        tvTime.setText(classMineBean.getCreateTime());
//        tvSee.setText(classMineBean.getReadCount() + "");
        richTextLayout.setAuto(true);
        richTextLayout.setDatas(classMineBean.getContent(), R.color.color_333333, ClassCircleDetailActivity.this);
        richTextLayout.setOnImgClickListener(new RichTextLayout.OnImgClickListener() {
            @Override
            public void onImtClick(String url) {
                ImgShowDialog imgShowDialog = new ImgShowDialog(ClassCircleDetailActivity.this);
                imgShowDialog.setImgpath(HttpMethod.IMG_URL + url);
                imgShowDialog.show();
            }
        });
//        getDetail(classMineBean.getId());
        hideDataLoadMsg();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, ClassMineBean classMineBean)
    {
        Intent intent = new Intent(context, ClassCircleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("classMineBean", classMineBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void getDetail(String id)
    {
        HomeApi.getInstance().getCuiYueDynamicDetail(id, new HttpSubscriber<CuiYueCircleDetailBean>(new SubscriberOnListener<CuiYueCircleDetailBean>() {
            @Override
            public void onSucceed(CuiYueCircleDetailBean data) {
                if(data != null)
                {
                    ImageLoad.getInstance().displayCircleImage(ClassCircleDetailActivity.this, ivHead, HttpMethod.IMG_URL + data.getCreatorPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
                    tvName.setText(data.getCreator());
                    tvTime.setText(data.getCreateTime());
                    tvSee.setText(data.getReadCount() + "");
                    richTextLayout.setDatas(data.getContent(), R.color.color_333333, ClassCircleDetailActivity.this);
                    richTextLayout.setOnImgClickListener(new RichTextLayout.OnImgClickListener() {
                        @Override
                        public void onImtClick(String url) {
                            ImgShowDialog imgShowDialog = new ImgShowDialog(ClassCircleDetailActivity.this);
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
}

package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.MessageBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.cyztc.app.widget.ywl5320.RichTextLayoutW;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/27.
 */

public class MessageDetailActivity extends BaseActivity{

    @BindView(R.id.tv_mtitle)
    TextView tvmTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rtext)
    RichTextLayoutW rtext;
    @BindView(R.id.tv_subtitle)
    TextView tvSubmit;

    private MessageBean messageBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail_layout);
        messageBean = (MessageBean) getIntent().getExtras().get("messagebean");
        setBackView();
        setTitle("消息详情");

        tvSubmit.setText(messageBean.getSender());
        tvmTitle.setText(messageBean.getTitle());
        tvTime.setText("时间：" + messageBean.getSendtime());
        rtext.setDatas(messageBean.getContent(), R.color.color_333333, this);

        rtext.setOnImgClickListener(new RichTextLayoutW.OnImgClickListener() {
            @Override
            public void onImtClick(String url) {
                ImgShowDialog imgShowDialog = new ImgShowDialog(MessageDetailActivity.this);
                imgShowDialog.setImgpath(HttpMethod.IMG_URL + url);
                imgShowDialog.show();
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public static void startActivity(Context context, MessageBean messageBean)
    {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("messagebean", messageBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}

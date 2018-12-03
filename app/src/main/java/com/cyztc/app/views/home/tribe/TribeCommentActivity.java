package com.cyztc.app.views.home.tribe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.EmojiBean;
import com.cyztc.app.bean.StringBean;
import com.cyztc.app.bean.TopResponBean;
import com.cyztc.app.bean.TopResponBeanP;
import com.cyztc.app.bean.TribeBean;
import com.cyztc.app.bean.TribeTopicBean;
import com.cyztc.app.dialog.EmojiDialog;
import com.cyztc.app.dialog.NormalAskComplexDialog;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.EmojiParser;
import com.cyztc.app.views.home.adapter.ItemStringAdapter;
import com.cyztc.app.views.home.adapter.TribeTopResponseAdapter;
import com.cyztc.app.widget.CustomListView;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.cyztc.app.widget.ywl5320.RichTextLayout;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/20.
 */

public class TribeCommentActivity extends BaseActivity{

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
    @BindView(R.id.customlistview)
    CustomListView customListView;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.et_input)
    EditText editText;
    @BindView(R.id.iv_zan)
    ImageView ivZan;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.iv_span)
    ImageView ivSpan;


    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    private String topStr = "";
    private TribeTopicBean topicBean;
    private List<TopResponBean> datas;
    private TribeTopResponseAdapter tribeTopResponseAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribe_comment_layout);
        setTitle("详情");
        setBackView();
        topStr = getIntent().getStringExtra("topstr");
        Gson gson = new Gson();
        topicBean = gson.fromJson(topStr, TribeTopicBean.class);

        ImageLoad.getInstance().displayCircleImage(this, ivHead, HttpMethod.IMG_URL + topicBean.getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
        tvName.setText(topicBean.getStudentName());
        tvTime.setText(topicBean.getCreateTime());
        tvSee.setText(topicBean.getReadCount() + "");
        ivZan.setVisibility(View.VISIBLE);
        tvSend.setVisibility(View.INVISIBLE);

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getTopResponList(CyApplication.getInstance().getUserBean().getStudentId(), topicBean.getTopicId(), index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getTopResponList(CyApplication.getInstance().getUserBean().getStudentId(), topicBean.getTopicId(), index, pagesize);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0)
                {
                    ivZan.setVisibility(View.INVISIBLE);
                    tvSend.setVisibility(View.VISIBLE);
                }
                else
                {
                    ivZan.setVisibility(View.VISIBLE);
                    tvSend.setVisibility(View.INVISIBLE);
                }
            }
        });

        showDataLoadMsg("数据加载中");
        richTextLayout.setAuto(true);
        richTextLayout.setDatas(topicBean.getContent(), R.color.color_333333, this);
        richTextLayout.setOnImgClickListener(new RichTextLayout.OnImgClickListener() {
            @Override
            public void onImtClick(String url) {
                ImgShowDialog imgShowDialog = new ImgShowDialog(TribeCommentActivity.this);
                imgShowDialog.setImgpath(HttpMethod.IMG_URL + url);
                imgShowDialog.show();
            }
        });
        setAdapter();
        getTopResponList(CyApplication.getInstance().getUserBean().getStudentId(), topicBean.getTopicId(), index, pagesize);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.tv_send)
    public void onClickSend(View view)
    {
        String content = editText.getText().toString().trim();
        if(!TextUtils.isEmpty(content))
        {
            showLoadDialog("评论中");
            HomeApi.getInstance().topicReplies(content, CyApplication.getInstance().getUserBean().getStudentId(), topicBean.getTopicId(), new HttpSubscriber<TopResponBeanP>(new SubscriberOnListener() {
                @Override
                public void onSucceed(Object data) {
                    hideLoadDialog();
                    editText.setText("");
                    index = 0;
                    getTopResponList(CyApplication.getInstance().getUserBean().getStudentId(), topicBean.getTopicId(), index, pagesize);
                }

                @Override
                public void onError(int code, String msg) {
                    hideLoadDialog();
                    showToast(msg);
                }
            }, this));
        }
    }


    public void setAdapter()
    {
        datas = new ArrayList<>();
        tribeTopResponseAdapter = new TribeTopResponseAdapter(this, datas);
        customListView.setAdapter(tribeTopResponseAdapter);
        if(topicBean.getStudentId().equals(CyApplication.getInstance().getUserBean().getStudentId()))
        {
            tribeTopResponseAdapter.setAutor(true);
        }
        tribeTopResponseAdapter.setOnDeleteTopicListener(new TribeTopResponseAdapter.OnDeleteTopicListener() {
            @Override
            public void onDelete(final TopResponBean topResponBean) {
                showAskDialog("提示", "是否删除此条回复", "删除", "取消", new NormalAskComplexDialog.OnDalogListener() {
                    @Override
                    public void onYes() {
                        deleteTopicReplay(topResponBean);
                    }

                    @Override
                    public void OnNo() {
                    }
                });
            }
        });
    }

    public static void startActivity(Context context, String topStr)
    {
        Intent intent = new Intent(context, TribeCommentActivity.class);
        intent.putExtra("topstr", topStr);
        context.startActivity(intent);
    }

//    public void sortData(String content)
//    {
//        soruce = new ArrayList<>();
//        String[] datas = content.split("\\[img\\]");
//        for(int i = 0; i < datas.length; i++)
//        {
//            if(datas[i].contains("[/img]"))
//            {
//                String[] data = datas[i].split("\\[/img\\]");
//                for(int j = 0; j < data.length; j++)
//                {
//                    StringBean stringBean = new StringBean();
//                    stringBean.setStr(data[j]);
//                    soruce.add(stringBean);
//                }
//            }
////            else {
////                soruce.add(datas[i]);
////            }
//        }
//        itemStringAdapter = new ItemStringAdapter(this, soruce);
//        gridView.setAdapter(itemStringAdapter);
//    }

    public void getTopResponList(String studentId, String topId, final int start, int rows)
    {
        isLoading = true;
        HomeApi.getInstance().getTpoResponList(studentId, topId, start, rows, new HttpSubscriber<TopResponBeanP>(new SubscriberOnListener<TopResponBeanP>() {
            @Override
            public void onSucceed(TopResponBeanP data) {

                if(data != null && data.getData() != null)
                {
                    tvCount.setText(data.getTotalCount() + "");
                    if(index == 0)
                    {
                        datas.clear();
                    }
                    if(data.getData().size() < pagesize && index != 0)
                    {
                        showToast("没有更多了");
                    }
                    if(data.getData().size() != 0 && data.getData().size() <= pagesize)
                    {
                        index++;
                    }
                    if(data.getData().size() != 0) {
                        datas.addAll(data.getData());
                        tribeTopResponseAdapter.notifyDataSetChanged();
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                hideDataLoadMsg();
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                hideDataLoadMsg();
                isLoading = false;
            }
        }, this));
    }

    @OnClick(R.id.iv_emoji)
    public void onClickEmoji(View view)
    {
        EmojiDialog emojiDialog = new EmojiDialog(this);
        Window window = emojiDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        emojiDialog.show();
        emojiDialog.setOnEmojiListener(new EmojiDialog.OnEmojiListener() {
            @Override
            public void onEmoji(EmojiBean emojiBean) {
                String content = editText.getText().toString().trim();
                content += emojiBean.getName();
                EmojiParser.getInstance().strToSmiley(content, editText, 32);
            }
        });

        emojiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivSpan.getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                ivSpan.requestLayout();
            }
        });

        WindowManager wm = getWindowManager();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivSpan.getLayoutParams();
        lp.setMargins(0, 0, 0, CommonUtil.dip2px(this, 240));
        ivSpan.requestLayout();
    }

    @OnClick(R.id.iv_zan)
    public void onClickZan(View view)
    {
        likeTopic(CyApplication.getInstance().getUserBean().getStudentId(), topicBean.getTopicId(), 1);
    }

    public void likeTopic(String studentId, String topicId, final int islike)
    {
        HomeApi.getInstance().likeTopic(studentId, topicId, islike, new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                if(islike == 1)
                {
                    showToast("已点赞");
                }
                else if(islike == 0)
                {
                    showToast("取消点赞");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
            }
        }, this));
    }

    public void deleteTopicReplay(final TopResponBean topResponBean)
    {
        HomeApi.getInstance().deleteTopicReplay(topResponBean.getId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                showToast("删除成功");
                datas.remove(topResponBean);
                int count = Integer.parseInt(tvCount.getText().toString().trim());
                tvCount.setText((count - 1) + "");
                tribeTopResponseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
            }
        }, this));
    }
}

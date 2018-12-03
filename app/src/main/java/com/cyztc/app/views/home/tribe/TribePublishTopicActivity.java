package com.cyztc.app.views.home.tribe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CreateTribeBean;
import com.cyztc.app.bean.EmojiBean;
import com.cyztc.app.bean.TribeDetailBean;
import com.cyztc.app.bean.UploadPhotoBean;
import com.cyztc.app.dialog.EmojiDialog;
import com.cyztc.app.fileupload.FileUploadParams;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.utils.EmojiParser;
import com.cyztc.app.widget.ywl5320.pickphoto.beans.ImgBean;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.PickPhotoDialog;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cyztc.app.R.id.imageView;

/**
 * Created by ywl on 2016/12/26.
 */

public class TribePublishTopicActivity extends BaseActivity{


    @BindView(R.id.et_content)
    EditText etContent;
//    @BindView(R.id.listview)
//    ListView listView;
    @BindView(R.id.et_title)
    EditText etTitle;

    private String trible;
    private PickPhotoDialog pickPhotoDialog;

    private List<String> updateImsgs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trible_publish_tipic_layout);
        trible = getIntent().getStringExtra("trible");
        setTitle("发表话题");
        setBackView();
        setTextMenuView("发表");
//        EmojiParser.init(this);
        updateImsgs = new ArrayList<>();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if(TextUtils.isEmpty(title))
        {
            showToast("请输入标题");
            return;
        }
        if(title.length() < 4)
        {
            showToast("标题不能少于4个字");
            return;
        }
        if(TextUtils.isEmpty(content))
        {
            showToast("请输入内容");
            return;
        }
        if(content.length() < 10)
        {
            showToast("内容不能少于10个字");
            return;
        }
        HomeApi.getInstance().publishTopic(content, "", CyApplication.getInstance().getUserBean().getStudentId(), title, trible, new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                showToast("发表成功");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
            }
        }, this));
    }

    @OnClick(R.id.iv_emoji)
    public void onClickEmoji(View view)
    {
//        EmojiDialog emojiDialog = new EmojiDialog(this);
//        Window window = emojiDialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.DialogEnter);
//        emojiDialog.show();
//        emojiDialog.setOnEmojiListener(new EmojiDialog.OnEmojiListener() {
//            @Override
//            public void onEmoji(EmojiBean emojiBean) {
//                String content = etContent.getText().toString().trim();
//                content += emojiBean.getName();
//                EmojiParser.getInstance().strToSmiley(content, etContent, 40);
//            }
//        });
    }

    @OnClick(R.id.iv_img)
    public void onClickImg(View view)
    {
        if(pickPhotoDialog == null) {
            pickPhotoDialog = new PickPhotoDialog(this, this);
        }
        Window window = pickPhotoDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        pickPhotoDialog.setCutImg(false, 1);
        pickPhotoDialog.setOnPhotoResultListener(new PickPhotoDialog.OnPhotoResultListener() {
            @Override
            public void onCameraResult(String path) {
                if(!TextUtils.isEmpty(path))
                {
                    UploadPhoto(path);
                }
                else
                {
                    showToast("照片无效");
                }
            }

            @Override
            public void onCutPhotoResult(Bitmap bitmap) {

            }

            @Override
            public void onPhotoResult(List<ImgBean> selectedImgs) {
                if(selectedImgs != null && selectedImgs.size() > 0)
                {
                    UploadPhoto(selectedImgs.get(0).getPath());
                }
                else
                {
                    showToast("图片无效");
                }
            }
        });
        pickPhotoDialog.show();
    }

    public void UploadPhoto(String filepath)
    {
        List<File> files = new ArrayList<>();
        files.add(new File(filepath));
        FileUploadParams fileUploadParams = new FileUploadParams();
        //fileUploadParams.file = files;
        fileUploadParams.setFile(filepath);
        fileUploadParams.addBodyParameter("accountId", CyApplication.getInstance().getUserBean().getAccountId());
        fileUploadParams.addBodyParameter("type", "2");
        fileUploadParams.setMultipart(true);

        showLoadDialog("图片上传中");
        x.http().post(fileUploadParams, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {

                } else {
                    showLoadDialog("上传进度:" + ((current * 100 / total)) + "%");

                }
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPhotoBean uploadPhotoBean = gson.fromJson(result, UploadPhotoBean.class);
                if (uploadPhotoBean.isSuccess() && uploadPhotoBean.getData() != null) {
                    int index = etContent.getSelectionStart();
                    Editable editable = etContent.getText();
                    String imgurl = "[img]" + uploadPhotoBean.getData().getVirtualPath() + "[/img]";
                    updateImsgs.add(Pattern.quote(imgurl));
                    editable.insert(index, imgurl);
                    final SpannableStringBuilder builder = new SpannableStringBuilder(etContent.getText().toString().trim());
                    int size = updateImsgs.size();
                    for(int i = 0; i < size; i++)
                    {
                        Pattern mPattern = Pattern.compile(updateImsgs.get(i));
                        final Matcher matcher = mPattern.matcher(etContent.getText().toString().trim());
                        while (matcher.find())
                        {
                            String img = matcher.group();
                            img = img.replaceAll("\\[img]", "").replaceAll("\\[/img]", "");
                            final int start = matcher.start();
                            final int end = matcher.end();
                            Glide.with(TribePublishTopicActivity.this).load(HttpMethod.IMG_URL + img).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    BitmapDrawable bd = new BitmapDrawable(resource);
                                    int width = etContent.getWidth() - 60;
                                    int h = width * resource.getHeight() / resource.getWidth();
                                    bd.setBounds(20, 20, width, h);
                                    ImageSpan imageSpan = new ImageSpan(bd, ImageSpan.ALIGN_BASELINE);
                                    builder.setSpan(imageSpan, start,
                                            end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    etContent.setText(builder);
                                    etContent.setSelection(etContent.getText().toString().trim().length());
                                }
                            });


                        }
                    }
                }
                hideLoadDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.d(ex.getMessage());
                hideLoadDialog();
                showToast("图片上传失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(pickPhotoDialog != null)
        {
            pickPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(pickPhotoDialog != null)
        {
            pickPhotoDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

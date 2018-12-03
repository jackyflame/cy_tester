package com.cyztc.app.views.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GridViewImgBean;
import com.cyztc.app.bean.UploadPhotoBean;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.fileupload.FileUploadParams;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.UserApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.adapter.GridVeiwImgAdapter;
import com.cyztc.app.views.home.life.ApplyRoomActivity;
import com.cyztc.app.widget.RoundImageView;
import com.cyztc.app.widget.ywl5320.pickphoto.beans.ImgBean;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.PickPhotoDialog;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ywl on 2017/1/14.
 */

public class UpdateUserInfoActivity extends BaseActivity{

    @BindView(R.id.ly_system_parent)
    LinearLayout lysystemparent;
    @BindView(R.id.riv_head)
    RoundImageView rivHead;
    @BindView(R.id.et_nickname)
    TextView etNickName;

    private Bitmap hdbitmap;
    String ALBUM_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + "cache_files" + File.separator;
    private PickPhotoDialog pickPhotoDialog;
    String imgName = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name_and_head_layout);
        setBackView();
        setTitle("个人信息");
        setTextMenuView("保存");
        lysystemparent.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        ImageLoad.getInstance().displayCircleImage(this, rivHead, HttpMethod.IMG_URL + CyApplication.getInstance().getUserBean().getPhoto(), R.mipmap.icon_default_head, R.mipmap.icon_default_head);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();

        String nickname = etNickName.getText().toString().trim();
        if(!TextUtils.isEmpty(nickname))
        {
            if(hdbitmap != null)
            {
                UploadPhoto(ALBUM_PATH + imgName, nickname);
            }
            else
            {
                showLoadDialog("更新信息中");
                updateUserInfo(nickname, CyApplication.getInstance().getUserBean().getPhoto());
            }
        }
        else
        {
            showToast("请输入昵称");
        }
    }

    @OnClick(R.id.riv_head)
    public void onClickHead(View view)
    {
        imgName = System.currentTimeMillis() + ".jpg";
        pickPhotoDialog = new PickPhotoDialog(UpdateUserInfoActivity.this, UpdateUserInfoActivity.this);
        Window window = pickPhotoDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        pickPhotoDialog.setCutImg(true, 1);
        pickPhotoDialog.setOnPhotoResultListener(new PickPhotoDialog.OnPhotoResultListener() {
            @Override
            public void onCameraResult(String path) {

            }

            @Override
            public void onCutPhotoResult(Bitmap bitmap) {
                hdbitmap = bitmap;
                try {
                    saveFile(hdbitmap, imgName);
                    ImageLoad.getInstance().displayImage(UpdateUserInfoActivity.this, rivHead, ALBUM_PATH + imgName, 0, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPhotoResult(List<ImgBean> selectedImgs) {

            }
        });

        pickPhotoDialog.show();
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, UpdateUserInfoActivity.class);
        context.startActivity(intent);
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

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public void UploadPhoto(String filepath, final String nikname)
    {
        if(TextUtils.isEmpty(filepath)) {
            showToast("上传图片地址为空");
            return;
        }
        List<File> files = new ArrayList<>();
        files.add(new File(filepath));
        FileUploadParams fileUploadParams = new FileUploadParams();
        //fileUploadParams.file = files;
        fileUploadParams.setFile(filepath);
        fileUploadParams.addBodyParameter("accountId", CyApplication.getInstance().getUserBean().getAccountId());
        fileUploadParams.addBodyParameter("type", "2");
        fileUploadParams.setMultipart(true);

        showLoadDialog("头像上传中");
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
                MyLog.d(result);
                Gson gson = new Gson();
                UploadPhotoBean uploadPhotoBean = gson.fromJson(result, UploadPhotoBean.class);

                 if (uploadPhotoBean.isSuccess()) {
                    showLoadDialog("正在更新信息");
                    MyLog.d(uploadPhotoBean.getData().getVirtualPath());
//                    submitService(address, uploadPhotoBean.getData().getVirtualPath(), goods, remark, subType);
                    updateUserInfo(nikname, uploadPhotoBean.getData().getVirtualPath());

                } else {
                    hideLoadDialog();
                    showToast("头像上传失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.d(ex.getMessage());
                hideLoadDialog();
                showToast("头像上传失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void updateUserInfo(String nikname, final String piture)
    {
        UserApi.getInstance().updateUserInfo(CyApplication.getInstance().getUserBean().getAccountId(), nikname, piture, new HttpSubscriber<Object>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                showToast("更新成功");
                hideLoadDialog();
                CyApplication.getInstance().getUserBean().setPhoto(piture);
                UpdateUserInfoActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, UpdateUserInfoActivity.this));
    }

}

package com.cyztc.app.views.grades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.AlbumBean;
import com.cyztc.app.bean.AlbumBeanP;
import com.cyztc.app.bean.AssessListBean;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.GridViewImgBean;
import com.cyztc.app.bean.UploadPhotoBean;
import com.cyztc.app.fileupload.FileUploadParams;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.ClassApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.grades.adapter.ClassAlbumAdapter;
import com.cyztc.app.views.home.TrainLevelActivity;
import com.cyztc.app.views.home.TrainLevelDetailActivity;
import com.cyztc.app.views.home.adapter.GridVeiwImgAdapter;
import com.cyztc.app.views.home.adapter.TrainAssessAdapter;
import com.cyztc.app.views.home.life.ApplyRoomActivity;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;
import com.cyztc.app.widget.ywl5320.pickphoto.beans.ImgBean;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ImgShowDialog;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.PickPhotoDialog;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/12/30.
 */

public class ClassAlbumActivity extends BaseActivity{

    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<AlbumBean> datas;
    private ClassAlbumAdapter classAlbumAdapter;

    private int pagesize = 20;
    private int index = 0;
    private boolean isLoading = false;
    private PickPhotoDialog pickPhotoDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_album_layout);
        setBackView();
        setTitle("班级相册");
        setTextMenuView("上传");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    isLoading = true;
                    getAlbum(CyApplication.getInstance().getUserBean().getStudentId(), CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), "", index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    isLoading = true;
                    getAlbum(CyApplication.getInstance().getUserBean().getStudentId(), CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), "", index, pagesize);
                }
            }
        });
        showDataLoadMsg("数据加载中");
        setAdapter();
        isLoading = true;
        getAlbum(CyApplication.getInstance().getUserBean().getStudentId(), CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), "", index, pagesize);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(pickPhotoDialog == null) {
            pickPhotoDialog = new PickPhotoDialog(ClassAlbumActivity.this, ClassAlbumActivity.this);
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

    public void setAdapter()
    {
        datas = new ArrayList<>();
        classAlbumAdapter = new ClassAlbumAdapter(this, datas);
        gridView.setAdapter(classAlbumAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumBean albumBean = (AlbumBean) gridView.getItemAtPosition(position);
                if(albumBean != null)
                {
                    ImgShowDialog imgShowDialog = new ImgShowDialog(ClassAlbumActivity.this);
                    imgShowDialog.setImgpath(HttpMethod.IMG_URL + albumBean.getPicture());
                    imgShowDialog.show();
                }
            }
        });
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, ClassAlbumActivity.class);
        context.startActivity(intent);
    }

    public void getAlbum(String studentId, String trainId, String title, int start, int rows)
    {
        ClassApi.getInstance().getAlbum(studentId, trainId, title, start, rows, new HttpSubscriber<AlbumBeanP>(new SubscriberOnListener<AlbumBeanP>() {
            @Override
            public void onSucceed(AlbumBeanP data) {
                if(data != null && data.getData() != null)
                {
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
                        classAlbumAdapter.notifyDataSetChanged();
                    }
                }

//                for(int i = 0; i < 20; i++)
//                {
//                    AlbumBean albumBean = new AlbumBean();
//                    datas.add(albumBean);
//                }
                classAlbumAdapter.notifyDataSetChanged();

                hideDataLoadMsg();
                if(datas.size() == 0)
                {
                    showDataLoadMsg("没有相关数据");
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
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

    public void UploadPhoto(String filepath)
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
        fileUploadParams.addBodyParameter("type", "9");
        fileUploadParams.setMultipart(true);

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
                    showLoadDialog("数据提交中");
                    MyLog.d(uploadPhotoBean.getData().getVirtualPath());
                    //submitService(address, uploadPhotoBean.getData().getVirtualPath(), goods, remark, subType);
                    ClassApi.getInstance().uploadAlbum(CyApplication.getInstance().getUserBean().getStudentId(),
                            uploadPhotoBean.getData().getVirtualPath(),
                            CyApplication.getInstance().getUserBean().getStudentName() + "上传班级相册图片",
                            "班级相册图片",
                            CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
                                @Override
                                public void onSucceed(Object data) {
                                    showToast("上传成功");
                                    hideLoadDialog();
                                    index = 0;
                                    getAlbum(CyApplication.getInstance().getUserBean().getStudentId(), CyApplication.getInstance().getTrainingInfoBean().getTrainingInfoId(), "", index, pagesize);
                                }

                                @Override
                                public void onError(int code, String msg) {
                                    showToast(msg);
                                    hideLoadDialog();
                                }
                            }, ClassAlbumActivity.this));

                } else {
                    hideLoadDialog();
                    showToast("班级图片上传失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.d(ex.toString());
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
}

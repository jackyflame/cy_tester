package com.cyztc.app.views.home.tribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.adapter.ViewPagerAdapter;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.CreateTribeBean;
import com.cyztc.app.bean.TribeDetailBean;
import com.cyztc.app.bean.UploadPhotoBean;
import com.cyztc.app.fileupload.FileUploadParams;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.widget.CustomViewPager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by ywl on 2016/11/20.
 */

public class CreateTribeActivity extends BaseActivity{

    @BindView(R.id.viewpager)
    CustomViewPager customViewPager;

    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentLists;

    private int currentPage = 0;

    private CreateTribeFragment1 createTribeFragment1;
    private CreateTribeFragment2 createTribeFragment2;
    private CreateTribeFragment3 createTribeFragment3;
    private CreateTribeFragment4 createTribeFragment4;

    private String tribeName;
    private String tribeHeadImg;
    private String tribeBgPath;
    private String tribeDestr;
    private int uploadCount = 0;
    private String headimg = "";
    private String bgimg = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tribe_layout);
        setBackView();
        setTextMenuView("下一步");
        setTitle("创建部落");
        initFragments();
    }

    public void initFragments()
    {
        fragmentLists = new ArrayList<>();
        createTribeFragment1 = new CreateTribeFragment1();
        createTribeFragment2 = new CreateTribeFragment2();
        createTribeFragment3 = new CreateTribeFragment3();
        createTribeFragment4 = new CreateTribeFragment4();

        fragmentLists.add(createTribeFragment1);
        fragmentLists.add(createTribeFragment4);
        fragmentLists.add(createTribeFragment2);
        fragmentLists.add(createTribeFragment3);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentLists);
        customViewPager.setAdapter(viewPagerAdapter);
        customViewPager.setOffscreenPageLimit(4);
        customViewPager.setScrool(true);
        customViewPager.setCurrentItem(0);
        customViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        if(currentPage > 0)
        {
            currentPage --;
            if(currentPage < 2)
            {
                setTextMenuView("下一步");
            }
            customViewPager.setCurrentItem(currentPage);
        }
        else
        {
            this.finish();
        }
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(currentPage < 3) {
            if(currentPage == 0)
            {
                tribeName = createTribeFragment1.getTribeName();
                if(TextUtils.isEmpty(tribeName))
                {
                    showToast("请输入部落名称");
                    return;
                }
            }
            if(currentPage == 1)
            {
                tribeHeadImg = createTribeFragment4.getImgPath();
                if(TextUtils.isEmpty(tribeHeadImg))
                {
                    showToast("请选择部落头像");
                    return;
                }
            }
            if(currentPage == 2)
            {
                tribeBgPath = createTribeFragment2.getImgPath();
                if(TextUtils.isEmpty(tribeBgPath))
                {
                    showToast("请选择部落封面");
                    return;
                }
            }

            currentPage++;
            if(currentPage == 3)
            {
                setTextMenuView("完成");
            }
            customViewPager.setCurrentItem(currentPage);
        }
        else
        {
            if(currentPage == 3)
            {
                tribeDestr = createTribeFragment3.getTribeDisrc();
                if(TextUtils.isEmpty(tribeDestr))
                {
                    showToast("请输入部落简介");
                    return;
                }
            }
            showLoadDialog("部落图片上传中");
            uploadCount = 0;
            UploadPhoto(tribeHeadImg);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(createTribeFragment2 != null)
        {
            createTribeFragment2.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if(createTribeFragment4 != null)
        {
            createTribeFragment4.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(createTribeFragment2 != null){
            createTribeFragment2.onActivityResult(requestCode, resultCode, data);
        }
        if(createTribeFragment4 != null){
            createTribeFragment4.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, CreateTribeActivity.class);
        context.startActivity(intent);
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
                    uploadCount++;
                    if(uploadCount == 1)
                    {
                        headimg = uploadPhotoBean.getData().getVirtualPath();
                        showDataLoadMsg("上传部落背景");
                        UploadPhoto(tribeBgPath);
                    }
                    else if(uploadCount == 2) {
                        bgimg = uploadPhotoBean.getData().getVirtualPath();
                        showLoadDialog("创建部落中");
                        MyLog.d(uploadPhotoBean.getData().getVirtualPath());
                        CreateTribeBean createTribeBean = new CreateTribeBean();
                        createTribeBean.setCoverPicture(bgimg);
                        createTribeBean.setCreator(CyApplication.getInstance().getUserBean().getStudentId());
                        createTribeBean.setName(tribeName);
                        createTribeBean.setRemark(tribeDestr);
                        createTribeBean.setThumbnail(headimg);

                        HomeApi.getInstance().createTribe(createTribeBean, new HttpSubscriber<TribeDetailBean>(new SubscriberOnListener() {
                            @Override
                            public void onSucceed(Object data) {
                                showToast("创建部落成功");
                                hideLoadDialog();
                                CreateTribeActivity.this.finish();
                            }

                            @Override
                            public void onError(int code, String msg) {
                                showToast(msg);
                                hideLoadDialog();
                            }
                        }, CreateTribeActivity.this));
                    }

                } else {
                    hideLoadDialog();
                    showToast("创建部落失败");
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
}

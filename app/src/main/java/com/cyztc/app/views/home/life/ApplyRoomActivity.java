package com.cyztc.app.views.home.life;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CreateTribeBean;
import com.cyztc.app.bean.GridViewImgBean;
import com.cyztc.app.bean.StringBean;
import com.cyztc.app.bean.TribeDetailBean;
import com.cyztc.app.bean.UploadPhotoBean;
import com.cyztc.app.fileupload.FileUploadParams;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.adapter.GridVeiwImgAdapter;
import com.cyztc.app.views.home.tribe.CreateTribeActivity;
import com.cyztc.app.widget.CustomGridView;
import com.cyztc.app.widget.ywl5320.pickphoto.beans.ImgBean;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.PickPhotoDialog;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ServiceTypeDialog;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ServiceTypeFixDialog;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ServiceTypeMeetDialog;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.ServiceTypeYiliaoDialog;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/19.
 */

public class ApplyRoomActivity extends BaseActivity{

    @BindView(R.id.rl_service_type)
    RelativeLayout rlServiceType;
    @BindView(R.id.gridview)
    CustomGridView gridView;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_subtype)
    TextView tvSubtype;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;

    private List<GridViewImgBean> imgs;
    private GridVeiwImgAdapter gridVeiwImgAdapter;

    private String subType = "";
    private int serviceType;
    private HashMap<String, Integer> goods;
    private PickPhotoDialog pickPhotoDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceType = getIntent().getIntExtra("serviceType", serviceType);
        setContentView(R.layout.activity_apply_room_layout);
        setTitle("申请服务");
        setBackView();
        setTextMenuView("提交");
        if(serviceType == 6)
        {
            rlAddress.setVisibility(View.GONE);
        }
        if(serviceType == 4)
        {
            etAddress.setEnabled(false);
            etAddress.setText(CyApplication.getInstance().getUserBean().getBedRoom());
        }
        goods = new HashMap<>();
        setAdapters();
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();

        String address = etAddress.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();

        if(TextUtils.isEmpty(subType))
        {
            showToast("请选择服务类型");
            return;
        }
        if(serviceType != 6) {
            if (TextUtils.isEmpty(address)) {
                showToast("请填写地址");
                return;
            }
        }
        if(TextUtils.isEmpty(remark))
        {
            showToast("请填写备注");
            return;
        }

        if(serviceType == 3)
        {
            goods.put("801c7a9b-9b25-11e6-812b-9801a7b09290", 3);
        }
        else if(serviceType == 4)
        {
            goods.put("918fc275-9b26-11e6-812b-9801a7b09290", 4);
        }
        else if(serviceType == 5)
        {
            goods.put("a58036af-9b27-11e6-812b-9801a7b09290", 5);
        }
        else if(serviceType == 6)
        {
            goods.put("bd1f3528-9b27-11e6-812b-9801a7b09290", 6);
        }

        if(imgs.size() > 1)
        {
            showLoadDialog("图片上传中");
            UploadPhoto(imgs.get(0).getStr(), "卫生打扫", address, remark);
        }
        else
        {
            showLoadDialog("申请提交中");
            submitService(address, "", goods, remark, "卫生打扫");
        }
    }

    public void setAdapters()
    {
        imgs = new ArrayList<>();
        GridViewImgBean gridViewImgBean = new GridViewImgBean();
        gridViewImgBean.setType(0);
        imgs.add(gridViewImgBean);
        gridVeiwImgAdapter = new GridVeiwImgAdapter(this, imgs);
        gridView.setAdapter(gridVeiwImgAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewImgBean gridViewImgBean1 = (GridViewImgBean) gridView.getItemAtPosition(position);
                if(gridViewImgBean1 != null)
                {
                    if(gridViewImgBean1.getType() == 0)
                    {
                        pickPhotoDialog = new PickPhotoDialog(ApplyRoomActivity.this, ApplyRoomActivity.this);
                        Window window = pickPhotoDialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);
                        window.setWindowAnimations(R.style.DialogEnter);
                        pickPhotoDialog.setCutImg(false, 1);
                        pickPhotoDialog.setOnPhotoResultListener(new PickPhotoDialog.OnPhotoResultListener() {
                            @Override
                            public void onCameraResult(String path) {
                                imgs.clear();
                                GridViewImgBean gridViewImgBean2 = new GridViewImgBean();
                                gridViewImgBean2.setType(1);
                                gridViewImgBean2.setStr(path);
                                imgs.add(gridViewImgBean2);
                                GridViewImgBean gridViewImgBean = new GridViewImgBean();
                                gridViewImgBean.setType(0);
                                imgs.add(gridViewImgBean);
                                gridVeiwImgAdapter = new GridVeiwImgAdapter(ApplyRoomActivity.this, imgs);
                                gridView.setAdapter(gridVeiwImgAdapter);
                            }

                            @Override
                            public void onCutPhotoResult(Bitmap bitmap) {

                            }

                            @Override
                            public void onPhotoResult(List<ImgBean> selectedImgs) {
                                MyLog.d("size:" + selectedImgs.size());
                                if(selectedImgs.size() == 0)
                                    return;
                                imgs.clear();
                                for(ImgBean imgBean : selectedImgs)
                                {
                                    GridViewImgBean gridViewImgBean2 = new GridViewImgBean();
                                    gridViewImgBean2.setType(1);
                                    gridViewImgBean2.setStr(imgBean.getPath());
                                    imgs.add(gridViewImgBean2);
                                }
                                GridViewImgBean gridViewImgBean = new GridViewImgBean();
                                gridViewImgBean.setType(0);
                                imgs.add(gridViewImgBean);
                                gridVeiwImgAdapter = new GridVeiwImgAdapter(ApplyRoomActivity.this, imgs);
                                gridView.setAdapter(gridVeiwImgAdapter);
                            }
                        });

                        pickPhotoDialog.show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.rl_service_type)
    public void onClickServiceType(View veiw)
    {
        if(serviceType == 4) {
            ServiceTypeDialog serviceTypeDialog = new ServiceTypeDialog(this);
            Window window = serviceTypeDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.DialogEnter);
            serviceTypeDialog.show();
            serviceTypeDialog.setOnServiceTypeListener(new ServiceTypeDialog.OnServiceTypeListener() {
                @Override
                public void onTypeResult(int type) {
                    if (type == 1) {
                        subType = "更换物品";
                        tvSubtype.setText(subType);
                    } else if (type == 2) {
                        subType = "卫生打扫";
                        tvSubtype.setText(subType);
                    }
                    else if(type == 3)
                    {
                        subType = "其他";
                        tvSubtype.setText(subType);
                    }
                }
            });
        }
        else if(serviceType == 5)
        {
            ServiceTypeMeetDialog serviceTypeMeetDialog = new ServiceTypeMeetDialog(this);
            Window window = serviceTypeMeetDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.DialogEnter);
            serviceTypeMeetDialog.show();
            serviceTypeMeetDialog.setOnServiceTypeListener(new ServiceTypeMeetDialog.OnServiceTypeListener() {
                @Override
                public void onTypeResult(int type) {
                    if (type == 1) {
                        subType = "门口指引";
                        tvSubtype.setText(subType);
                    } else if (type == 2) {
                        subType = "添茶倒水";
                        tvSubtype.setText(subType);
                    }else if (type == 3) {
                        subType = "临时需要";
                        tvSubtype.setText(subType);
                    }else if (type == 4) {
                        subType = "其他";
                        tvSubtype.setText(subType);
                    }
                }
            });
        }
        else if(serviceType == 6)
        {
            ServiceTypeYiliaoDialog serviceTypeYiliaoDialog = new ServiceTypeYiliaoDialog(this);
            Window window = serviceTypeYiliaoDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.DialogEnter);
            serviceTypeYiliaoDialog.show();
            serviceTypeYiliaoDialog.setOnServiceTypeListener(new ServiceTypeYiliaoDialog.OnServiceTypeListener() {
                @Override
                public void onTypeResult(int type) {
                    if (type == 1) {
                        subType = "看病咨询";
                        tvSubtype.setText(subType);
                    } else if (type == 2) {
                        subType = "买药";
                        tvSubtype.setText(subType);
                    }else if (type == 3) {
                        subType = "医生急诊";
                        tvSubtype.setText(subType);
                    }else if (type == 4) {
                        subType = "其他";
                        tvSubtype.setText(subType);
                    }
                }
            });
        }
        else if(serviceType == 3)
        {
            ServiceTypeFixDialog serviceTypeFixDialog = new ServiceTypeFixDialog(this);
            Window window = serviceTypeFixDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.DialogEnter);
            serviceTypeFixDialog.show();
            serviceTypeFixDialog.setOnServiceTypeListener(new ServiceTypeFixDialog.OnServiceTypeListener() {
                @Override
                public void onTypeResult(int type) {
                    if (type == 1) {
                        subType = "家具报修";
                        tvSubtype.setText(subType);
                    } else if (type == 2) {
                        subType = "水电报修";
                        tvSubtype.setText(subType);
                    }
                    else if (type == 3) {
                        subType = "其他";
                        tvSubtype.setText(subType);
                    }
                }
            });
        }
    }

    public static void startActivity(Context context, int serviceType)
    {
        Intent intent = new Intent(context, ApplyRoomActivity.class);
        intent.putExtra("serviceType", serviceType);
        context.startActivity(intent);
    }

    public void UploadPhoto(String filepath, final String subType, final String address, final String remark)
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
                    showLoadDialog("申请提交中");
                    MyLog.d(uploadPhotoBean.getData().getVirtualPath());
                    submitService(address, uploadPhotoBean.getData().getVirtualPath(), goods, remark, subType);

                } else {
                    hideLoadDialog();
                    showToast("提交申请失败");
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

    public void submitService(String address, String picture, HashMap<String, Integer> goods, String remark, String subType)
    {
        HomeApi.getInstance().createOrder(CyApplication.getInstance().getUserBean().getStudentId(), serviceType, goods, address, 0, 0, 0, picture, remark, subType, "", new HttpSubscriber<String>(new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                hideLoadDialog();
                showToast("申请提交成功");
                ApplyRoomActivity.this.finish();
            }

            @Override
            public void onError(int code, String msg) {
                hideLoadDialog();
                showToast(msg);
            }
        }, ApplyRoomActivity.this));
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

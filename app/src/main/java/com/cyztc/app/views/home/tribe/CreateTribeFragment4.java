package com.cyztc.app.views.home.tribe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import butterknife.BindView;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.widget.ywl5320.pickphoto.beans.ImgBean;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.PickPhotoDialog;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by ywl on 2016/11/20.
 */

public class CreateTribeFragment4 extends BaseFragment{

    PickPhotoDialog pickPhotoDialog;

    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.iv_bg)
    ImageView ivBg;

    private String imgPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_tribe_step4_layout);
    }

    @OnClick(R.id.iv_img)
    public void OnClickChoiceImg(View veiw)
    {
        pickPhotoDialog = new PickPhotoDialog(getActivity(), getActivity());
        Window window = pickPhotoDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        pickPhotoDialog.setCutImg(false, 1);
        pickPhotoDialog.setOnPhotoResultListener(new PickPhotoDialog.OnPhotoResultListener() {
            @Override
            public void onCameraResult(String path) {
                imgPath = path;
                ImageLoad.getInstance().displayImage(getActivity(), ivBg, path, 0, 0);
            }

            @Override
            public void onCutPhotoResult(Bitmap bitmap) {

            }

            @Override
            public void onPhotoResult(List<ImgBean> selectedImgs) {
                if(selectedImgs != null && selectedImgs.size() > 0)
                {
                    imgPath = selectedImgs.get(0).getPath();
                    ImageLoad.getInstance().displayImage(getActivity(), ivBg, selectedImgs.get(0).getPath(), 0, 0);
                }
            }
        });

        pickPhotoDialog.show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(pickPhotoDialog != null)
        {
            pickPhotoDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        {
            if(pickPhotoDialog != null)
            {
                pickPhotoDialog.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public String getImgPath() {
        return imgPath;
    }

}

package com.cyztc.app.widget.ywl5320.pickphoto.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyztc.app.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ywl on 2016/11/23.
 */

public class ImgShowDialog extends BaseDialog{

    private PhotoView photoView;
    private String imgpath;
    private PhotoViewAttacher mAttacher;

    public ImgShowDialog(Context context) {
        super(context);
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo_show_layout);
        photoView = (PhotoView) findViewById(R.id.photoview);
//        imgpath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487398600173&di=af1cc1ef541f0d7e5bfa4f60b2e9ae01&imgtype=0&src=http%3A%2F%2Fwww.discuz.images.zq.sd.cn%2FDiscuz%2Fforum%2F201307%2F03%2F122908loenfzjchnmcmxyv.gif";
        if(imgpath.endsWith(".gif"))
        {
            Glide.with(context).load(imgpath).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);
        }
        else {
            Glide.with(context).load(imgpath).into(photoView);
        }
        mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
    }
}

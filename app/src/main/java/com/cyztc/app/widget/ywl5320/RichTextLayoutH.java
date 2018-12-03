package com.cyztc.app.widget.ywl5320;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cyztc.app.R;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.imageload.ImageLoad;
import com.cyztc.app.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywl on 2016/11/29.
 */

public class RichTextLayoutH extends LinearLayout{

    private Context context;
    private List<String> soruce;
    private OnImgClickListenerH onImgClickListener;
    private int screenWidth = 0;
    private int padding = 0;
    private int txtcolor = 0;
    private int imgcount = 0;

    private boolean isAuto = false;

    public RichTextLayoutH(Context context) {
        this(context, null);
    }

    public RichTextLayoutH(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextLayoutH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setOnImgClickListener(OnImgClickListenerH onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    public void setDatas(String content, int txtcolor, int imgcount, Activity activity) {
        removeAllViews();
        screenWidth = getScreenWidth(activity);
        padding = dip2px(context, 12);
        this.txtcolor = txtcolor;
        this.imgcount = imgcount;
        sortData(content);
        initDatas();
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public void initDatas()
    {
        int count = 0;
        if(soruce == null)
            return;
        int size = soruce.size();
        for(int i = 0; i < size; i++)
        {
            if(count < imgcount) {
                if (soruce.get(i).toLowerCase().endsWith(".jpg") || soruce.get(i).toLowerCase().endsWith(".png") || soruce.get(i).toLowerCase().endsWith(".gif")) {
                    final int index = i;
                    LayoutParams lp = new LayoutParams(0, screenWidth * 1 / 3 - padding);
                    lp.weight = 1;
                    lp.setMargins(0, padding, padding, 0);
                    final ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if(!isAuto) {
                        ImageLoad.getInstance().displayImage(context, imageView, HttpMethod.IMG_URL + soruce.get(i).replace("\\", "/"), R.mipmap.icon_load_img, R.mipmap.icon_load_img);
                    }
                    else {
                        final String url = HttpMethod.IMG_URL + soruce.get(i).replace("\\", "/");
                        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                int h = CommonUtil.getScreenWidth((Activity) context) * resource.getHeight() / resource.getWidth();
                                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
                                imageView.setLayoutParams(lp);
                                lp.setMargins(0, padding, 0, 0);
                                imageView.setImageBitmap(resource);
                                imageView.invalidate();
                                if(url.toLowerCase().endsWith(".gif")) {
                                    Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                                }
                            }
                        });
                    }
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onImgClickListener != null) {
                                onImgClickListener.onImtClick(soruce.get(index));
                            }
                        }
                    });
                    count++;
                    addView(imageView, lp);
                }
            }
        }
        if(count < 3 && count != 0)
        {
            int n = 3 - count;
            for(int j = 0; j < n; j++)
            {
                LayoutParams lp = new LayoutParams(0, screenWidth * 1 / 3 - padding);
                lp.weight = 1;
                lp.setMargins(0, padding, padding, 0);
                TextView textView = new TextView(context);
//                textView.setLayoutParams(lp);
                addView(textView, lp);
            }
        }
    }

    public void sortData(String content)
    {
        soruce = new ArrayList<>();
        String[] datas = content.split("\\[img\\]");
        for(int i = 0; i < datas.length; i++)
        {
            if(datas[i].contains("[/img]"))
            {
                String[] data = datas[i].split("\\[/img\\]");
                for(int j = 0; j < data.length; j++)
                {
                    soruce.add(data[j]);
                }
            }
            else if(datas[i].toLowerCase().endsWith(".jpg") || datas[i].toLowerCase().endsWith(".png")){
                soruce.add(datas[i]);
            }
        }
    }

    public interface OnImgClickListenerH
    {
        void onImtClick(String url);
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int getScreenWidth(Activity context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        return  mScreenWidth;
    }
}

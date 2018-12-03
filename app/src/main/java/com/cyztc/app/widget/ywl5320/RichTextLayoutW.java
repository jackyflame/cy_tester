package com.cyztc.app.widget.ywl5320;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywl on 2016/11/29.
 */

public class RichTextLayoutW extends LinearLayout{

    private Context context;
    private List<String> soruce;
    private OnImgClickListener onImgClickListener;
    private int screenWidth = 0;
    private int padding = 0;
    private int txtcolor = 0;
    private LayoutInflater layoutInflater;

    public RichTextLayoutW(Context context) {
        this(context, null);
    }

    public RichTextLayoutW(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextLayoutW(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnImgClickListener(OnImgClickListener onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    public void setDatas(String content, int txtcolor, Activity activity) {
        removeAllViews();
        screenWidth = getScreenWidth(activity);
        padding = dip2px(context, 12);
        this.txtcolor = txtcolor;
        sortData(content);
        initDatas();
    }

    public void initDatas()
    {
        if(soruce == null)
            return;
        int size = soruce.size();
        for(int i = 0; i < size; i++)
        {
            if(soruce.get(i).toLowerCase().endsWith(".jpg") || soruce.get(i).toLowerCase().endsWith(".png") || soruce.get(i).toLowerCase().endsWith(".gif"))
            {
                final int index = i;

                final ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onImgClickListener != null)
                        {
                            onImgClickListener.onImtClick(soruce.get(index));
                        }
                    }
                });

                final String url = HttpMethod.IMG_URL + soruce.get(i).replace("\\", "/");
//                final String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487398600173&di=af1cc1ef541f0d7e5bfa4f60b2e9ae01&imgtype=0&src=http%3A%2F%2Fwww.discuz.images.zq.sd.cn%2FDiscuz%2Fforum%2F201307%2F03%2F122908loenfzjchnmcmxyv.gif";
                Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int h =  CommonUtil.getScreenWidth((Activity) context)   * resource.getHeight() / resource.getWidth();
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
                addView(imageView);
            }
            else
            {
                String []stxt = soruce.get(i).split("\\n");
                int length = stxt.length;
                for(int j = 0; j < length; j++)
                {
                    stxt[j] = stxt[j].replaceAll(" ", "").replaceAll("\\n", "").replace("\r", "");
                    if(!TextUtils.isEmpty(stxt[j]))
                    {
                        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, padding, 0, 0);
                        TextView textView = (TextView) layoutInflater.inflate(R.layout.item_rich_textview, null);
                        AutoUtils.auto(textView);
                        textView.setLayoutParams(lp);
                        textView.setText("        " + stxt[j]);
                        textView.setTextSize(18);
//                        textView.setTextColor(context.getResources().getColor(txtcolor));
                        addView(textView);
                    }
                }
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
            else {
                String []t = datas[i].split("\\n");
                int l = t.length;
                for(int j = 0; j < l; j++)
                {
                    if(!TextUtils.isEmpty(t[j]))
                    {
                        soruce.add(t[j]);
                    }
                }

            }
        }
    }

    public interface OnImgClickListener
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

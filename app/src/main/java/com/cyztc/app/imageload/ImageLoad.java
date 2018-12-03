package com.cyztc.app.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 图片加载
 */
public class ImageLoad {
    private static ImageLoad instance = null;

    private ImageLoad() {

    }

    /**
     * 单例延迟加载（需要的时候才去加载），线程不安全
     * synchronized解决了线程不安全的问题，效率有点低，每次调用实例时都会加载
     * 双重检验锁，只在第一次初始化的时候加上同步锁，并发量不多，安全性不高的情况下或许能很完美运行单例模式
     *
     * @return
     */
    public static ImageLoad getInstance() {
        if (instance == null) {
            instance = new ImageLoad();
        }
        return instance;
    }

    /**
     * 加载显示圆型图网络图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayCircleImage(Context context, ImageView imageView, String url, int placeHolderRes, int errorRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
                .transform(new CircleTransform(context))
                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(x, y)//调整图片大小
                .into(imageView);
    }
    /**
     * 加载显示圆角图网络图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayeRoundImage(Context context, ImageView imageView, String url, int placeHolderRes, int errorRes,int round) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
                .transform(new RoundedCornersTransformation(context,25,0))
//                .crossFade()//动画
//                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(w,h)//调整图片大小
                .into(imageView);
    }

    /**
     * 加载显示圆角图网络图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayeRoundImage(Context context, ImageView imageView, String url, int placeHolderRes, int errorRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
                .transform(new GlideRoundTransform(context,10))
//                .crossFade()//动画
//                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(w,h)//调整图片大小
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param imageView
     * @param url
     * @param placeHolderRes
     * @param errorRes
     */
    public void displayeCircleImage(final Context context, final ImageView imageView, String url, int placeHolderRes, int errorRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载显示圆角图网络图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayeWHRoundImage(Context context, ImageView imageView, String url, int placeHolderRes, int errorRes,int w,int h) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
                .transform(new GlideRoundTransform(context))
//                .crossFade()//动画
//                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
                .override(w,h)//调整图片大小
                .into(imageView);
    }


    /**
     * 加载网络图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayImage(Context context, ImageView imageView, String url, int placeHolderRes, int errorRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
//                .crossFade()//动画
                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(x, y)//调整图片大小
                .into(imageView);
    }

    /**
     * 加载drawable图片
     * @param context 上下文
     * @param imageView imageView
     * @param drawable drawable路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayDrawableImage(Context context, ImageView imageView, int drawable, int placeHolderRes, int errorRes) {
        Glide.with(context)
                .load(drawable)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
                .transform(new GlideRoundTransform(context,20))
//                .crossFade()//动画
//                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(AppDdipOrpx.px2dip(context,180),AppDdipOrpx.px2dip(context,180))//调整图片大小
                .into(imageView);
    }

    /**
     * 加载File图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     */
    public void displayImage(Context context, ImageView imageView, File url, int placeHolderRes, int errorRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
//                .crossFade()//动画
                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(x, y)//调整图片大小
                .into(imageView);
    }

    /**
     *
     * 加载File图片
     * @param context 上下文
     * @param imageView imageView
     * @param url 路径
     * @param placeHolderRes 加载默认图片
     * @param errorRes 加载错误图片
     * @param w 图片宽
     * @param h 图片高
     */
    public void displayImage(Context context, ImageView imageView, File url, int placeHolderRes, int errorRes,int w,int h) {
        Glide.with(context)
                .load(url)
//                .asBitmap()//截取gif第一帧
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存全尺寸
                .placeholder(placeHolderRes)//加载占位图
                .error(errorRes)//错误占位图
//                .crossFade()//动画
//                .dontAnimate()//取消动画
//                .centerCrop()//缩放图像fitCenter
//                .override(x, y)//调整图片大小
                .into(imageView);
    }
    /**
     * 清除图片缓存
     */
    public void clearCache(final Context context) {
        Glide.get(context).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

}

package com.cyztc.app.widget.ywl5320;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywl on 2016/11/2.
 */

public class TabLayoutView extends LinearLayout{

    private Context context;
    private int[] imgs;
    private int imgwidth;
    private int imgheight;

    private List<ImageView> imageViews;
    private int currentIndex = 0;
    private int tabsize = 0;

    private OnItemOnclickListener onItemOnclickListener;

    public TabLayoutView(Context context) {
        this(context, null);
    }

    public TabLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setOnItemOnclickListener(OnItemOnclickListener onItemOnclickListener) {
        this.onItemOnclickListener = onItemOnclickListener;
    }

    public void setDataSource(int[] imgs, int tabsize, int currentIndex)
    {
        this.imgs = imgs;
        this.tabsize = tabsize;
        this.currentIndex = currentIndex;
    }

    public void setImageStyle(int imgwidth, int imgheight)
    {
        this.imgwidth = imgwidth;
        this.imgheight = imgheight;
    }


    public void initDatas()
    {
        imageViews = new ArrayList<>();

        setOrientation(HORIZONTAL);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER;

        LayoutParams imglp = new LayoutParams(imgwidth, imgheight);
        imglp.gravity = Gravity.CENTER;


        for(int i = 0; i < tabsize; i++)
        {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(imglp);
            imageView.setImageResource(imgs[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);


            LinearLayout cly = new LinearLayout(context);
            cly.setGravity(Gravity.CENTER);
            cly.setOrientation(VERTICAL);
            cly.setLayoutParams(imglp);
            cly.addView(imageView);

            final int index = i;
            cly.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, titles[index], Toast.LENGTH_SHORT).show();
                    setSelectStyle(index);
                    if(onItemOnclickListener != null)
                    {
                        onItemOnclickListener.onItemClick(index);
                    }
                }
            });

            addView(cly, lp);

            imageViews.add(imageView);
        }
        setSelectStyle(currentIndex);
    }

    public void setSelectStyle(int index)
    {
        for(int i = 0; i < tabsize; i++)
        {
            if(i == index)
            {
                imageViews.get(i).setSelected(true);
            }
            else
            {
                imageViews.get(i).setSelected(false);
            }
        }
    }

    public interface OnItemOnclickListener
    {
        void onItemClick(int index);
    }
}

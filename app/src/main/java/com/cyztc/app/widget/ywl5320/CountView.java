package com.cyztc.app.widget.ywl5320;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyztc.app.R;

/**
 * Created by ywl on 2016/12/11.
 */

public class CountView extends LinearLayout{

    private Context context;
    private int count = 1;
    private int maxCount = 0;
    private int minCount = 0;
    private TextView txtCount;
    private OnCountListener onCountListener;
    private int changeCount = 0;

    public CountView(Context context) {
        this(context, null);
    }

    public CountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(HORIZONTAL);
    }

    public void setOnCountListener(OnCountListener onCountListener) {
        this.onCountListener = onCountListener;
    }

    public void initDatas(int dleft, int dmid, int dright, int count, int maxcount, int mincount)
    {
        removeAllViews();
        this.count = count;
        this.maxCount = maxcount;
        this.minCount = mincount;
        initViews(dleft, dmid, dright);
    }

    private void initViews(final int dleft, int dmid, final int dright)
    {
        changeCount = 0;
        for(int i = 0; i < 3; i++)
        {
            LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            lp.gravity = Gravity.CENTER;
            final TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            if(i == 0)
            {
                lp.setMargins(0, 0, 0, 0);
                textView.setBackgroundResource(dleft);
                textView.setText("-");
                textView.setTextSize(18);
                textView.setTextColor(context.getResources().getColor(R.color.color_666666));
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count > minCount) {
                            count--;
                            changeCount--;
//                            if(count == 1)
//                            {
//                                textView.setBackgroundColor(context.getResources().getColor(R.color.color_ededed));
//                            }
//                            else
//                            {
//                                textView.setBackgroundResource(dleft);
//                            }
                            if (txtCount != null) {
                                txtCount.setText(count + "");
                            }

                            if(onCountListener != null)
                            {
                                onCountListener.onCount(count, changeCount);
                            }
                        }
                    }
                });
            }
            else if(i == 1)
            {
                lp.setMargins(-dip2px(context, 1), 0, 0, 0);
                textView.setBackgroundResource(dmid);
                textView.setText(count + "");
                textView.setTextSize(16);
                textView.setTextColor(context.getResources().getColor(R.color.color_666666));
                txtCount = textView;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            else if(i == 2)
            {
                lp.setMargins(-dip2px(context, 1), 0, 0, 0);
                textView.setBackgroundResource(dright);
                textView.setText("+");
                textView.setTextSize(18);
                textView.setTextColor(context.getResources().getColor(R.color.color_666666));
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count < maxCount) {
                            count++;
                            changeCount++;
//                            if(count == maxCount)
//                            {
//                                textView.setBackgroundColor(context.getResources().getColor(R.color.color_ededed));
//                            }
//                            else
//                            {
//                                textView.setBackgroundResource(dright);
//                            }
                            if (txtCount != null) {
                                txtCount.setText(count + "");
                            }
                            if(onCountListener != null)
                            {
                                onCountListener.onCount(count, changeCount);
                            }
                        }
                    }
                });
            }
            addView(textView, lp);
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public interface OnCountListener
    {
        void onCount(int count, int changeCount);
    }

    public int getCount() {
        return count;
    }
}

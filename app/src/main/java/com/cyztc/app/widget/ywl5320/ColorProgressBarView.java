package com.cyztc.app.widget.ywl5320;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.cyztc.app.R;
import com.cyztc.app.utils.CommonUtil;

/**
 * Created by ywl on 2016/12/21.
 */

public class ColorProgressBarView extends View{

    private Paint paint;//画笔
    private RectF oval; // RectF对象（圆环）
    private static int strokeWidth = 16;//画笔大小
    private int height;//控件高
    private int width;//控件宽
    private int circleWidth;//圆环宽
    private float centerX;
    private float centerY;
    private float lastDegree = 200;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;
    private int[] colors = new int[]{ 0xff0fd423, 0xff00d989, 0xff00d989, 0xff00d989, 0xff0fd423};

    private float currentDegree = 200;
    private ValueAnimator progressAnimator;
    private boolean isOpen = false;

    private Bitmap bitmap;



    public ColorProgressBarView(Context context) {
        this(context, null);
    }

    public ColorProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        strokeWidth = CommonUtil.dip2px(context, 5);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        oval = new RectF();
        paint.setStrokeWidth(strokeWidth); // 线宽
        paint.setStyle(Paint.Style.STROKE);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        rotateMatrix = new Matrix();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_door_lock_bg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        //计算最小宽度
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);

        if(width >= height)
        {
            circleWidth = height;
        }
        else
        {
            circleWidth = width;
        }

        setMeasuredDimension(circleWidth,circleWidth);
        oval.left = strokeWidth / 2 + 1; // 左边
        oval.top = strokeWidth / 2 + 1; // 上边
        oval.right = circleWidth - strokeWidth / 2 ; // 右边
        oval.bottom = circleWidth - strokeWidth / 2 ; // 下边
        centerX = (oval.left + oval.right) / 2;
        centerY = (oval.bottom + oval.top) / 2;
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);

        rotateMatrix.setRotate(-90, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        paint.setShader(sweepGradient);

        Rect rsrc = new Rect();
        rsrc.top = 0;
        rsrc.left = 0;
        rsrc.right = bitmap.getWidth();
        rsrc.bottom = bitmap.getHeight();

        Rect rdsc = new Rect();
        rdsc.top = 0;
        rdsc.left = 0;
        rdsc.right = getMeasuredWidth();
        rdsc.bottom = getMeasuredHeight();
        canvas.drawBitmap(bitmap, rsrc, rdsc, paint);
        canvas.drawArc(oval, -120, currentDegree, false, paint); // 绘制圆弧 1.35f是每个色块宽度
    }

    public void setCurrentDegree(float currentDegree, float lastd, boolean issmooth)
    {
        lastDegree = lastd;
        if(issmooth) {
            setAnimation(lastDegree, currentDegree, 5000);
        }
        else
        {
            this.currentDegree = currentDegree;
            invalidate();
        }
    }

    /**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(final float last, float current, int length) {
        invalidate();
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentDegree);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentDegree= (float) animation.getAnimatedValue();
                lastDegree = currentDegree;
                invalidate();
                if(lastDegree < 360)
                {
                    isOpen = false;
                }
                else
                {
                    isOpen = true;
                }
            }
        });
        progressAnimator.start();
    }

    public boolean isOpen() {
        return isOpen;
    }
}

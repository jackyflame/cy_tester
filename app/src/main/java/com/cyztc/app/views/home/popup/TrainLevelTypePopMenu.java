package com.cyztc.app.views.home.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cyztc.app.R;

/**
 * Created by ywl on 2016/6/17.
 */
public class TrainLevelTypePopMenu extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private View popView;

    private View v_item1;
    private View v_item2;
    private View v_item3;
    private View v_item4;
    private View v_item5;

    private OnItemClickListener onItemClickListener;


    public TrainLevelTypePopMenu(Activity activity) {
        super(activity);
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.popmenu_shopdetail_layout, null);// 加载菜单布局文件
        this.setContentView(popView);// 把布局文件添加到popupwindow中
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);// 获取焦点
        this.setTouchable(true); // 设置PopupWindow可触摸
        this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);

        // 获取选项卡
        v_item1 = popView.findViewById(R.id.ly_item1);
        v_item2 = popView.findViewById(R.id.ly_item2);
        v_item3 = popView.findViewById(R.id.ly_item3);
        v_item4 = popView.findViewById(R.id.ly_item4);
        v_item5 = popView.findViewById(R.id.ly_item5);
        // 添加监听
        v_item1.setOnClickListener(this);
        v_item2.setOnClickListener(this);
        v_item3.setOnClickListener(this);
        v_item4.setOnClickListener(this);
        v_item5.setOnClickListener(this);

    }

    /**
     * 设置显示的位置
     *
     * @param resourId
     *            这里的x,y值自己调整可以
     */
    public void showLocation(int resourId) {
        showAsDropDown(activity.findViewById(resourId), dip2px(activity, 0),
                dip2px(activity, 0));
    }

    @Override
    public void onClick(View v) {
        int type = -1;
        if (v == v_item1) {
            type = 1;
        } else if (v == v_item2) {
            type = 2;
        }
        else if(v == v_item3)
        {
            type = 3;
        }
        else if(v == v_item4)
        {
            type = 4;
        }
        else if(v == v_item5)
        {
            type = -1;
        }
        if (onItemClickListener != null) {
            onItemClickListener.onClick(type);
        }
        dismiss();
    }

    // dip转换为px
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 点击监听接口
    public interface OnItemClickListener {
        public void onClick(int type);
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}

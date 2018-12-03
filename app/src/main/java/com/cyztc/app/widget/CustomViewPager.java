package com.cyztc.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	
	private boolean isScroll = true;

	public CustomViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setScrool(boolean isScroll)
	{
		this.isScroll = isScroll;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(!isScroll)
		{
			return false;
		}
		else
		{
			return super.onTouchEvent(arg0);
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(!isScroll)
		{
			return false;
		}
		else {
			return super.onInterceptTouchEvent(arg0);
		}
	}
}

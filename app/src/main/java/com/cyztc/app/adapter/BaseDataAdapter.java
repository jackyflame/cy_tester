
package com.cyztc.app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyztc.app.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 适配器基类，只需实现getView方法即可
 * 
 * @author ywl
 *
 */
public class BaseDataAdapter extends BaseAdapter {
	protected Context context;
	protected LayoutInflater mlayoutInflate;
	protected List<? extends BaseBean> mDatas = new ArrayList<BaseBean>();

	public BaseDataAdapter(Context context, List<? extends BaseBean> mDatas) {
		this.context = context;
		this.mDatas = mDatas;
		if(this.context!=null)
		mlayoutInflate = LayoutInflater.from(this.context);
	}
	public BaseDataAdapter(Context context, List<? extends BaseBean> mDatas,int isb) {
		this.context = context;
		this.mDatas = mDatas;
		mlayoutInflate = LayoutInflater.from(this.context);
	}


	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}

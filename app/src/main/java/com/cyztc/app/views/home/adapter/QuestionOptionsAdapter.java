package com.cyztc.app.views.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.bean.BaseBean;

import java.util.List;

/**
 * Created by ywl on 2016/12/26.
 */

public class QuestionOptionsAdapter extends BaseDataAdapter{

    public QuestionOptionsAdapter(Context context, List<? extends BaseBean> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = mlayoutInflate.inflate(R.layout.item_exam_options_layout, parent, false);
        }
        return convertView;
    }
}

package com.cyztc.app.views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.HomeItemBean;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.views.home.adapter.HomeMenuMoreAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2016/12/4.
 */

public class MoreItemMenuActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.iv_selected)
    ImageView ivSelected;

    private String menustr;
    private List<HomeItemBean> homeItemBeens;
    private HomeMenuMoreAdapter homeMenuMoreAdapter;
    private boolean isSelectedAll = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menustr = getIntent().getStringExtra("menustr");
        MyLog.d("menustr:" + menustr);
        setContentView(R.layout.activity_more_item_layout);
        setBackView();
        setTitle("更多应用");
        setAdapter();
    }

    public void setAdapter()
    {
        Gson gson = new Gson();
        homeItemBeens = gson.fromJson(menustr, new TypeToken<List<HomeItemBean>>(){}.getType());
        homeMenuMoreAdapter = new HomeMenuMoreAdapter(this, homeItemBeens);
        listView.setAdapter(homeMenuMoreAdapter);
        isSelectedAll = isSelectedAll();
        if(isSelectedAll)
        {
            ivSelected.setSelected(true);
        }else
        {
            ivSelected.setSelected(false);
        }
        homeMenuMoreAdapter.setOnSelectedListener(new HomeMenuMoreAdapter.OnSelectedListener() {
            @Override
            public void onSelected() {
                if(isSelectedAll())
                {
                    ivSelected.setSelected(true);
                    isSelectedAll = true;
                }
                else
                {
                    isSelectedAll = false;
                    ivSelected.setSelected(false);
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        setDataRsult();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setDataRsult();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setDataRsult()
    {
        if(homeItemBeens != null) {
            Intent intent = new Intent();
            Gson gson = new Gson();
            intent.putExtra("resultdata", gson.toJson(homeItemBeens));
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @OnClick(R.id.iv_selected)
    public void onClickSelectedAll(View view)
    {
        isSelectedAll = !isSelectedAll;
        if(isSelectedAll)
        {
            selectedAll(true);
            ivSelected.setSelected(true);
        }
        else
        {
            selectedAll(false);
            ivSelected.setSelected(false);
        }
        homeMenuMoreAdapter.notifyDataSetChanged();
    }

    public boolean isSelectedAll()
    {
        for(HomeItemBean homeItemBean : homeItemBeens)
        {
            if(!homeItemBean.isSelected())
                return false;
        }
        return true;
    }

    public void selectedAll(boolean selected)
    {
        for(HomeItemBean homeItemBean : homeItemBeens)
        {
            homeItemBean.setSelected(selected);
        }
    }
}

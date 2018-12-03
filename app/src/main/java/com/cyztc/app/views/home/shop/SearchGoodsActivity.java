package com.cyztc.app.views.home.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.GoodsBean;
import com.cyztc.app.bean.GoodsBeanP;
import com.cyztc.app.httpservice.serviceapi.HomeApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.home.adapter.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ywl on 2017/3/13.
 */

public class SearchGoodsActivity extends BaseActivity{

    @BindView(R.id.et_search)
    EditText metsearch;
    @BindView(R.id.listview)
    ListView listView;

    private ShopListAdapter shopListAdapter;
    private List<GoodsBean> datas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods_layout);
        setBackView();
        setTextMenuView("搜索");
        setAdapter();
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        shopListAdapter = new ShopListAdapter(this, datas);
        listView.setAdapter(shopListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsBean goodsBean = (GoodsBean) listView.getItemAtPosition(position);
                if(goodsBean != null) {
                    GoodsDetailActivity.startActivity(SearchGoodsActivity.this, goodsBean.getTitle(), goodsBean.getId());
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        String keyWords = metsearch.getText().toString().trim();
        if(TextUtils.isEmpty(keyWords))
        {
            showToast("请先输入商品名称");
            return;
        }
        showDataLoadMsg("搜索中...");
        hideKeyBord(metsearch);
        datas.clear();
        HomeApi.getInstance().getGoodsList(CyApplication.getInstance().getUserBean().getStudentId(), 1, "", keyWords, 0, 50, new HttpSubscriber<GoodsBeanP>(new SubscriberOnListener<GoodsBeanP>() {
            @Override
            public void onSucceed(GoodsBeanP data) {
                if(data != null && data.getData() != null)
                {
                    datas.addAll(data.getData());
                }
                if(datas.size() > 0) {
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("没有相关商品信息");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, SearchGoodsActivity.class);
        context.startActivity(intent);
    }

    /**
     * 隐藏键盘
     * @param view
     */
    public void hideKeyBord(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

}

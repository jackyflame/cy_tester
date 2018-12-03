package com.cyztc.app.views.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.knowledge.adapter.BookCollectAdapter;
import com.cyztc.app.views.knowledge.adapter.BookListAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2017/1/17.
 */

public class BookCollecActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private List<BookBean> datas;
    private BookCollectAdapter bookListAdapter;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_collect_layout);
        setBackView();
        setTitle("图书收藏");

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getFavorBook(index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getFavorBook(index, pagesize);
                }
            }
        });
        setAdapter();
        getFavorBook(index, pagesize);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        bookListAdapter = new BookCollectAdapter(this, datas);
        listView.setAdapter(bookListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = (BookBean) listView.getItemAtPosition(position);
                if(bookBean != null)
                {
                    BookDetailActivity.startActivity(BookCollecActivity.this, bookBean.getBookId(), bookBean.getTitle(), 1);
                }
            }
        });

        bookListAdapter.setOnCollecListener(new BookCollectAdapter.OnCollecListener() {
            @Override
            public void onCollec(BookBean bookBean) {
                if(bookBean.getIsFavorite() == 0)
                {
                    favoriBook(bookBean);
                }
                else
                {
                    unfavoriBook(bookBean);
                }
            }
        });
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    public void getFavorBook(int start, final int pagesize)
    {
        isLoading = true;
        BookApi.getInstance().getFavorBook(CyApplication.getInstance().getUserBean().getStudentId(), start, pagesize, new HttpSubscriber<BookListBean>(new SubscriberOnListener<BookListBean>() {
            @Override
            public void onSucceed(BookListBean data) {
                if(data != null && data.getData() != null)
                {
                    if(index == 0)
                    {
                        datas.clear();
                    }
                    if(data.getData().size() < pagesize && index != 0)
                    {
                        showToast("没有更多了");
                    }
                    if(data.getData().size() != 0 && data.getData().size() <= pagesize)
                    {
                        index++;
                    }
                    if(data.getData().size() != 0) {
                        datas.addAll(data.getData());

                    }
                    if(datas.size() > 0)
                    {
                        for(BookBean bookBean : datas)
                        {
                            bookBean.setIsFavorite(1);
                        }
                        bookListAdapter.notifyDataSetChanged();
                        hideDataLoadMsg();
                    }
                    else
                    {
                        showDataLoadMsg("没有收藏记录");
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                if(springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if(datas.size() == 0) {
                    showDataLoadWrongMsg(msg);
                }
                else {
                    hideDataLoadMsg();
                }
                isLoading = false;
            }
        }, this));
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, BookCollecActivity.class);
        context.startActivity(intent);
    }

    public void favoriBook(final BookBean bookBean)
    {
        BookApi.getInstance().favoriteBook(CyApplication.getInstance().getUserBean().getStudentId(), bookBean.getBookId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                bookBean.setIsFavorite(1);
                bookListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }

    public void unfavoriBook(final BookBean bookBean)
    {
        BookApi.getInstance().unfavoriteBook(CyApplication.getInstance().getUserBean().getStudentId(), bookBean.getBookId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
            @Override
            public void onSucceed(Object data) {
                bookBean.setIsFavorite(0);
                bookListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, this));
    }
}

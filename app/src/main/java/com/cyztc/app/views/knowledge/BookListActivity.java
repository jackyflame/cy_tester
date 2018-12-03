package com.cyztc.app.views.knowledge;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.bean.TypeBean;
import com.cyztc.app.bean.TypeBeanP;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.CommonUtil;
import com.cyztc.app.views.knowledge.adapter.BookListAdapter;
import com.cyztc.app.views.knowledge.adapter.TypeAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2017/1/17.
 */

public class BookListActivity extends BaseActivity{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tv_right2)
    TextView tvRight2;

    @BindView(R.id.typelist)
    ListView typelist;
    @BindView(R.id.v_bg)
    View vBg;

    private List<BookBean> datas;
    private BookListAdapter bookListAdapter;
    private int listviewWidth = 0;

    private TypeAdapter typeAdapter;
    private List<TypeBean> typeBeens;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;
    private boolean isShowListView = false;

    private String subType = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_layout);
        setBackView();
        setTitle("图书列表");
        setTextMenuView("分类");
        tvRight2.setText("借阅");
        tvRight2.setVisibility(View.VISIBLE);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getBookList(index, pagesize, subType);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getBookList(index, pagesize, subType);
                }
            }
        });

        tvRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BorrowBookActivity.startActivity(BookListActivity.this);
            }
        });
        listviewWidth = (int)(CommonUtil.getScreenWidth(this) * 0.6f);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listviewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        typelist.setLayoutParams(lp);
        typelist.setVisibility(View.INVISIBLE);
        hideFloor(typelist);
        setTypeAdapter();
        setAdapter();
        showDataLoadMsg("数据加载中");
        getBookList(index, pagesize, subType);
        searchTypes(2, 0, 50);
    }

    public void setTypeAdapter()
    {
        typeBeens = new ArrayList<>();
        TypeBean typeBean = new TypeBean();
        typeBean.setSearchType("全部");
        typeBeens.add(typeBean);
        typeAdapter = new TypeAdapter(this, typeBeens);
        typelist.setAdapter(typeAdapter);
        typelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TypeBean typeBean = (TypeBean) typelist.getItemAtPosition(position);
                if(typeBean != null)
                {
                    if(!isLoading) {
                        hideFloor(typelist);
                        showDataLoadMsg("数据加载中");
                        subType = typeBean.getSearchType();
                        if("全部".equals(subType))
                        {
                            subType = "";
                        }
                        index = 0;
                        getBookList(index, pagesize, subType);
                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_collect)
    public void onClickToCollect(View view)
    {
        BookCollecActivity.startActivity(this);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        bookListAdapter = new BookListAdapter(this, datas);
        listView.setAdapter(bookListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = (BookBean) listView.getItemAtPosition(position);
                if(bookBean != null)
                {
                    BookDetailActivity.startActivity(BookListActivity.this, bookBean.getId(), bookBean.getTitle(), bookBean.getIsFavorite());
                }
            }
        });


        bookListAdapter.setOnCollecListener(new BookListAdapter.OnCollecListener() {
            @Override
            public void onCollec(BookBean bookBean) {
                if(bookBean.getIsFavorite() == 0)
                {
//                    showToast("收藏");
                    favoriBook(bookBean);
                }
                else
                {
//                    showToast("取消收藏");
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

    @Override
    public void onClickTextMenu() {
        super.onClickTextMenu();
        if(typeBeens.size() == 0)
        {
            showToast("没有类型可选");
            return;
        }
        if(isShowListView)
        {
            hideFloor(typelist);
        }
        else {
            showFloor(typelist);
        }
    }

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }

    public void getBookList(int start, int rows, String type)
    {
        isLoading = true;
        BookApi.getInstance().getBookList(CyApplication.getInstance().getUserBean().getStudentId(), type, "", start, rows, new HttpSubscriber<BookListBean>(new SubscriberOnListener<BookListBean>() {
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
                        bookListAdapter.notifyDataSetChanged();
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                hideDataLoadMsg();
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

    public void favoriBook(final BookBean bookBean)
    {
        BookApi.getInstance().favoriteBook(CyApplication.getInstance().getUserBean().getStudentId(), bookBean.getId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
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
        BookApi.getInstance().unfavoriteBook(CyApplication.getInstance().getUserBean().getStudentId(), bookBean.getId(), new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
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

    public void showFloor(final View v) {
        isShowListView = true;
        typelist.setVisibility(View.VISIBLE);
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(listviewWidth, 0);
        animator.setTarget(v);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationX((Float) animation.getAnimatedValue());
            }
        });

        vBg.animate().alpha(1).setDuration(200);
        vBg.setVisibility(View.VISIBLE);
    }

    public void hideFloor(final View v)
    {
        isShowListView = false;
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(0, listviewWidth);
        animator.setTarget(v);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        vBg.animate().alpha(0).setDuration(200);
        vBg.setVisibility(View.GONE);

    }

    public void searchTypes(int type, int star, int rows)
    {
        BookApi.getInstance().searchTypes(type, star, rows, new HttpSubscriber<TypeBeanP>(new SubscriberOnListener<TypeBeanP>() {
            @Override
            public void onSucceed(TypeBeanP data) {
                if(data != null && data.getData() != null)
                {
                    if(data.getData().size() > 0) {
                        typeBeens.addAll(data.getData());
                        typeAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        showDataLoadWrongMsg("此类型没有数据");
                    }
                }
                else
                {
                    showDataLoadWrongMsg("访问数据失败");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));
    }
}

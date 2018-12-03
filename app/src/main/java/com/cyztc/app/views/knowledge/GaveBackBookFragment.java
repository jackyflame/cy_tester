package com.cyztc.app.views.knowledge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.adapter.BaseDataAdapter;
import com.cyztc.app.base.BaseFragment;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.views.knowledge.adapter.BackBookAdapter;
import com.cyztc.app.views.knowledge.adapter.BorrowBookAdapter;
import com.cyztc.app.widget.refresh.DefaultFooter;
import com.cyztc.app.widget.refresh.DefaultHeader;
import com.cyztc.app.widget.refresh.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/29.
 */

public class GaveBackBookFragment extends BaseFragment{

    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.springview)
    SpringView springView;

    private BackBookAdapter bookAdapter;
    private List<BookBean> datas;

    private int pagesize = 10;
    private int index = 0;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_giveback_book_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    index = 0;
                    getMyBookList(CyApplication.getInstance().getUserBean().getStudentId(), 4, "", index, pagesize);
                }
            }

            @Override
            public void onLoadmore() {
                if(!isLoading) {
                    getMyBookList(CyApplication.getInstance().getUserBean().getStudentId(), 4, "", index, pagesize);
                }
            }
        });

        setAdapter();
        showDataLoadMsg("数据加载中");
        getMyBookList(CyApplication.getInstance().getUserBean().getStudentId(), 4, "", index, pagesize);
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        bookAdapter = new BackBookAdapter(getActivity(), datas);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = (BookBean) listView.getItemAtPosition(position);
                if(bookBean != null) {
                    BookDetailActivity.startActivity(getActivity(), bookBean.getId(), bookBean.getTitle(), bookBean.getIsFavorite());
                }
            }
        });

    }

    public void getMyBookList(String studentId, int status, String name, int start, int rows)
    {
        isLoading = true;
        BookApi.getInstance().getMyBookList(studentId, status, name, start, rows, new HttpSubscriber<BookListBean>(new SubscriberOnListener<BookListBean>() {
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
                        bookAdapter.notifyDataSetChanged();
                    }
                }
                springView.onFinishFreshAndLoad();
                isLoading = false;
                if(datas.size() > 0) {
                    hideDataLoadMsg();
                }
                else
                {
                    showDataLoadMsg("没有已归还图书");
                }
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
        }, getActivity()));
    }
}

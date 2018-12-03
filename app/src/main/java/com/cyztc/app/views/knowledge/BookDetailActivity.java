package com.cyztc.app.views.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cyztc.app.CyApplication;
import com.cyztc.app.R;
import com.cyztc.app.base.BaseActivity;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookDetailBean;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.serviceapi.BookApi;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.httpservice.subscribers.SubscriberOnListener;
import com.cyztc.app.imageload.ImageLoad;

import butterknife.OnClick;

/**
 * Created by ywl on 2017/1/17.
 */

public class BookDetailActivity extends BaseActivity{

    @BindView(R.id.iv_ebook)
    ImageView ivEbook;
    @BindView(R.id.tv_etitle)
    TextView tveTitle;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_borrow)
    TextView tvBorrow;
    @BindView(R.id.iv_collec)
    ImageView ivCollec;


    private String bookid;
    private String title;
    private int isfavor;
    private BookDetailBean bookDetailBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail_layout);
        bookid = getIntent().getStringExtra("bookid");
        title = getIntent().getStringExtra("title");
        isfavor = getIntent().getIntExtra("isfavor", -1);
        setBackView();
        setTitle(title);
        showDataLoadMsg("数据加载中");
        getBookDetail(CyApplication.getInstance().getUserBean().getStudentId(), bookid);
    }

    @Override
    public void onClickBack() {
        super.onClickBack();
        this.finish();
    }

    @OnClick(R.id.ly_collec)
    public void onClickCollec(View view)
    {
        if(isfavor == 1) {
            showLoadDialog("取消收藏");
            unfavoriteBook(bookid);
        }
        else
        {
            showLoadDialog("收藏");
            favoriBook(bookid);
        }
    }

    @OnClick(R.id.tv_borrow)
    public void onClickBorrow(View view)
    {
        if(bookDetailBean != null)
        {
            if(bookDetailBean.getStatus() ==0 || bookDetailBean.getStatus() == 4)
            {
                showLoadDialog("加载中");
                BookApi.getInstance().lendBook(CyApplication.getInstance().getUserBean().getStudentId(), bookid, new HttpSubscriber<BaseBean>(new SubscriberOnListener() {
                    @Override
                    public void onSucceed(Object data) {
                        hideLoadDialog();
                        tvBorrow.setText("借阅待审核");
                        bookDetailBean.setStatus(1);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        showToast(msg);
                        hideLoadDialog();
                    }
                }, BookDetailActivity.this));
            }
        }
    }

    public static void startActivity(Context context, String bookid, String title, int isfavor)
    {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("title", title);
        intent.putExtra("isfavor", isfavor);
        context.startActivity(intent);
    }

    public void getBookDetail(String studentId, String resourceId)
    {
        BookApi.getInstance().getBookDetail(studentId, resourceId, new HttpSubscriber<BookDetailBean>(new SubscriberOnListener<BookDetailBean>() {
            @Override
            public void onSucceed(BookDetailBean data) {
                bookDetailBean = data;
                isfavor = data.getIsFavorite();
                hideDataLoadMsg();
                ImageLoad.getInstance().displayImage(BookDetailActivity.this, ivEbook, HttpMethod.IMG_URL + data.getPicture(), 0, 0);
                tveTitle.setText(data.getTitle());
                tvRemark.setText(data.getRemark());
                tvStatus.setText("库存：" + data.getCount() + " 字数：" + data.getWordCount() + "字");
                if(data.getStatus() == 0 || data.getStatus() == 4)//status意义表示 0：未借，1：借阅待审核，2: 借阅中，3：归还待审核，4：已归还，9：其他。
                {
                    tvBorrow.setText("借阅");
                }
                else if(data.getStatus() == 1)
                {
                    tvBorrow.setText("借阅待审核");
                }
                else if(data.getStatus() == 2)
                {
                    tvBorrow.setText("借阅中");
                }
                else if(data.getStatus() == 3)
                {
                    tvBorrow.setText("归还待审核");
                }

                if(isfavor == 1)
                {
                    ivCollec.setSelected(true);
                }
                else
                {
                    ivCollec.setSelected(false);
                }
            }

            @Override
            public void onError(int code, String msg) {
                showDataLoadWrongMsg(msg);
            }
        }, this));

//        BookApi.getInstance().getVideoDetail(studentId, resourceId, new HttpSubscriber<VideoDetailBean>(new SubscriberOnListener<VideoDetailBean>() {
//            @Override
//            public void onSucceed(VideoDetailBean data) {
//                ImageLoad.getInstance().displayImage(EbookDetailActivity.this, ivEbook, HttpMethod.IMG_URL + data.getPicture(), 0, 0);
//                tveTitle.setText(data.getTitle());
//                tvRemark.setText(data.getRemark());
//                tvStatus.setText("库存：? 字数：" + data.getWordCount() + "字");
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//
//            }
//        }, this));
    }


    public void favoriBook(String ebookid)
    {
        BookApi.getInstance().favoriteBook(CyApplication.getInstance().getUserBean().getStudentId(), ebookid, new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {
                isfavor = 1;
                ivCollec.setSelected(true);
                hideLoadDialog();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
            }
        }, this));
    }

    public void unfavoriteBook(String ebookid)
    {
        BookApi.getInstance().unfavoriteBook(CyApplication.getInstance().getUserBean().getStudentId(), ebookid, new HttpSubscriber<BaseBean>(new SubscriberOnListener<BaseBean>() {
            @Override
            public void onSucceed(BaseBean data) {
                isfavor = 0;
                ivCollec.setSelected(false);
                hideLoadDialog();
            }

            @Override
            public void onError(int code, String msg) {
                showToast(msg);
                hideLoadDialog();
            }
        }, this));
    }

}

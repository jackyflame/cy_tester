package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookBean;
import com.cyztc.app.bean.BookDetailBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.bean.TypeBeanP;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.beans.FavoriBookBodyBean;
import com.cyztc.app.httpservice.beans.LendBookBodyBean;
import com.cyztc.app.httpservice.service.BaseApi;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;

import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Administrator on 2016/8/12.
 */
public class BookApi extends BaseApi{

    public static BookApi bookApi;
    public BookService bookService;
    private static String httpMethod;
    public BookApi()
    {
        bookService = HttpMethod.getInstance().createApi(BookService.class);
    }
    public static BookApi getInstance()
    {
        if(bookApi == null)
        {
            bookApi = new BookApi();
        }
        return bookApi;
    }

    public void getBookList(String studentId, String bookType, String name, int start, int rows, HttpSubscriber<BookListBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getBookList(studentId, bookType, name, start, rows)
                    .map(new HttpResultFunc<BookListBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getVideoList(String studentId, int type, String subtype, String title, int start, int rows, HttpSubscriber<VideoListBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getVideoList(studentId, type, subtype, title, start, rows)
                    .map(new HttpResultFunc<VideoListBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getVideoDetail(String studentId, String elecResourceId, HttpSubscriber<VideoDetailBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getVideoDetail(studentId, elecResourceId)
                    .map(new HttpResultFunc<VideoDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getMyBookList(String studentId, int status, String name, int start, int rows, HttpSubscriber<BookListBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getMyBookList(studentId, status, name, start, rows)
                    .map(new HttpResultFunc<BookListBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void searchTypes(int type, int start, int rows, HttpSubscriber<TypeBeanP> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.searchTypes(type, start,rows)
                    .map(new HttpResultFunc<TypeBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void favoriteBook(String studentId, String bookid, HttpSubscriber<BaseBean> subscriber) {
        if(bookService != null) {
//            FavoriBookBodyBean favoriBookBodyBean = new FavoriBookBodyBean();
//            favoriBookBodyBean.setStudentId(studentId);
//            favoriBookBodyBean.setBookId(bookid);
            Observable observable = bookService.favoriteBook(studentId, bookid)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void unfavoriteBook(String studentId, String bookid, HttpSubscriber<BaseBean> subscriber) {
        if(bookService != null) {
//            FavoriBookBodyBean favoriBookBodyBean = new FavoriBookBodyBean();
//            favoriBookBodyBean.setStudentId(studentId);
//            favoriBookBodyBean.setBookId(bookid);
            Observable observable = bookService.unfavoriteBook(studentId, bookid)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getBaseFileUrl(HttpSubscriber<String> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getBaseFileUrl()
                    .map(new HttpResultFunc<String>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getBookDetail(String studentId, String bookId, HttpSubscriber<BookDetailBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getBookDetail(studentId, bookId)
                    .map(new HttpResultFunc<BookDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void returnBook(String studentId, String bookIds, HttpSubscriber<BaseBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.returnBook(studentId, bookIds)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void lendBook(String studentId, String bookIds, HttpSubscriber<BaseBean> subscriber) {
        if(bookService != null) {
//            LendBookBodyBean lendBookBodyBean = new LendBookBodyBean();
//            lendBookBodyBean.setStudentId(studentId);
//            lendBookBodyBean.setBookId(bookIds);
            Observable observable = bookService.lendBook(studentId, bookIds)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getFavorBook(String studentId, int start, int rows, HttpSubscriber<BookListBean> subscriber) {
        if(bookService != null) {
            Observable observable = bookService.getFavorBook(studentId, start, rows)
                    .map(new HttpResultFunc<BookListBean>());
            toSubscribe(observable, subscriber);
        }
    }


}

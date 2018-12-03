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
import com.cyztc.app.httpservice.httpresult.HttpResult;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface BookService {

    /**
     * 查询图书列表
     * @return
     */
    @GET("bookList.do")
    Observable<HttpResult<BookListBean>> getBookList(@Query("studentId") String studentId, @Query("bookType") String bookType, @Query("name") String name, @Query("start") int start, @Query("rows") int rows);

    /**
     * 服务类型 1：电子书，2：视频，3：音频
     */
    @GET("elecResourceList.do")
    Observable<HttpResult<VideoListBean>> getVideoList(@Query("studentId") String studentId, @Query("type") int type, @Query("subType") String subtype, @Query("title") String title, @Query("start") int start, @Query("rows") int rows);

    /**
     * 音视频详情
     * @param studentId
     * @param elecResourceId
     * @return
     */
    @GET("elecResourceDetail.do")
    Observable<HttpResult<VideoDetailBean>> getVideoDetail(@Query("studentId") String studentId, @Query("elecResourceId") String elecResourceId);


    /**
     * 获取我的借书列表
     * @param studentId
     * @param status
     * @param name
     * @param start
     * @param rows
     * @return
     */
    @GET("myBookList.do")
    Observable<HttpResult<BookListBean>> getMyBookList(@Query("studentId") String studentId, @Query("status") int status, @Query("name") String name, @Query("start") int start, @Query("rows") int rows);

    /**
     * 搜索列表类型查询
     * @param type 1:商品,2:书籍,3:电子书，4:音频，5：视频
     * @param start
     * @param rows
     * @return
     */
    @GET("searchType.do")
    Observable<HttpResult<TypeBeanP>> searchTypes(@Query("type") int type, @Query("start") int start, @Query("rows") int rows);


    /**
     * 收藏图书
     * @return
     */
    @POST("favoriteBook.do")
    Observable<HttpResult<BaseBean>> favoriteBook(@Query("studentId") String studentId, @Query("bookId") String bookId);


    /**
     * 取消收藏
     * @param studentId
     * @param bookId
     * @return
     */
    @DELETE("favoriteBook.do")
    Observable<HttpResult<BaseBean>> unfavoriteBook(@Query("studentId") String studentId, @Query("bookId") String bookId);

    /**
     * 获取基础文件路径
     * @return
     */
    @GET("fileBaseDirectory.do")
    Observable<HttpResult<String>> getBaseFileUrl();

    /**
     * 获取图书详情
     * @param studentId
     * @param bookId
     * @return
     */
    @GET("bookDetail.do")
    Observable<HttpResult<BookDetailBean>> getBookDetail(@Query("studentId") String studentId, @Query("bookId") String bookId);

    /**
     * 归还图书
     * @param studentId
     * @param bookIds
     * @return
     */
    @POST("returnBook.do")
    Observable<HttpResult<BaseBean>> returnBook(@Query("studentId") String studentId, @Query("bookIds") String bookIds);

    /**
     * 图书借阅
     * @return
     */
    @POST("lendBook.do")
    Observable<HttpResult<BaseBean>> lendBook(@Query("studentId") String studentId, @Query("bookId") String bookId);

//    /**
//     * 图书借阅
//     * @return
//     */
//    @GET("lendBook.do")
//    Observable<HttpResult<BaseBean>> lendBook(@Query("studentId") String studentId, @Query("bookId") String bookId);

    /**
     * 图书收藏列表
     * @param studentId
     * @param start
     * @param rows
     * @return
     */
    @GET("favoriteBook.do")
    Observable<HttpResult<BookListBean>> getFavorBook(@Query("studentId") String studentId, @Query("start") int start, @Query("rows") int rows);

}

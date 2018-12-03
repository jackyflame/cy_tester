package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.bean.AlbumBeanP;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ClassMineBeanP;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.bean.EmSignBean;
import com.cyztc.app.bean.EmSignDetailBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.bean.TrainEnlistBeanP;
import com.cyztc.app.bean.TrainingInfoBean;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.httpservice.beans.UploadAlbumBodyBean;
import com.cyztc.app.httpservice.httpresult.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface ClassService {

    /**
     * 2查询班级风采
     * @return
     */
    @GET("mien.do")
    Observable<HttpResult<ClassMineBeanP>> getClassMien(@Query("accountId") String accountId, @Query("trainId") String trainId, @Query("start") int start, @Query("rows") int rows, @Query("type") int type);

    /**
     * 获取我的培训信息
     * @param accountId
     * @return
     */
    @GET("trainingInfos.do")
    Observable<HttpResult<TrainingInfoBean>> getMyTrainingInfos(@Query("accountId") String accountId);

    /**
     * 获取课程列表
     * @param accountId
     * @param start
     * @param rows
     * @return
     */
    @GET("courses.do")
    Observable<HttpResult<CorseBeanP>> getCourses(@Query("accountId") String accountId, @Query("courseDate") String courseDate, @Query("start") int start, @Query("rows") int rows);

    /**
     * 员工签到详情
     * @param accountId
     * @param signDate
     * @return
     */
    @GET("employee/sign.do")
    Observable<HttpResult<EmSignDetailBean>> getEmCourses(@Query("accountId") String accountId, @Query("signDate") String signDate);

    /**
     * 员工签到
     * @param accountId
     * @param signDate
     * @return
     */
    @POST("employee/sign.do")
    Observable<HttpResult<EmSignDetailBean>> getEmSign(@Query("accountId") String accountId, @Query("wifiName") String wifiName, @Query("type") int type, @Query("signDate") String signDate, @Query("ip") String ip);

    /**
     * 员工签到详情
     * @param accountId
     * @return
     */
    @GET("employee/signList.do")
    Observable<HttpResult<PageBean<EmSignBean>>> getEmSignList(@Query("accountId") String accountId, @Query("start") int start, @Query("rows") int rows);

    /**
     * 获取相册列表
     * @param studentId
     * @param trainId
     * @param title
     * @param start
     * @param rows
     * @return
     */
    @GET("album.do")
    Observable<HttpResult<AlbumBeanP>> getAlbum(@Query("studentId") String studentId, @Query("trainId") String trainId, @Query("title") String title, @Query("start") int start, @Query("rows") int rows);


    /**
     * 上传班级相册图片
     * @param uploadAlbumBodyBean
     * @return
     */
    @POST("album.do")
    Observable<HttpResult<BaseBean>> uploadAlbum(@Body UploadAlbumBodyBean uploadAlbumBodyBean);


    /**
     * 获取缴费列表
     * @param studentId
     * @param type
     * @param start
     * @param rows
     * @return
     */
    @GET("optionalTraining.do")
    Observable<HttpResult<TrainEnlistBeanP>> getOptionTrainList(@Query("studentId") String studentId, @Query("type") int type, @Query("start") int start, @Query("rows") int rows);



}

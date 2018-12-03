package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookDetailBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.EatHistoryBean;
import com.cyztc.app.bean.EmployeeApplyBean;
import com.cyztc.app.bean.LeaveBean;
import com.cyztc.app.bean.LeaveDetailBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.bean.TypeBeanP;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.beans.SchduleCarBodyBean;
import com.cyztc.app.httpservice.httpresult.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface EmployeeService {

    /**
     * 员工申请
     * @return
     */
    @POST("employee/apply.do")
    Observable<HttpResult<String>> apply(@Body EmployeeApplyBean employeeApplyBean);


    /**
     * 获取申请列表
     * @param accountId
     * @param type
     * @param isAudit
     * @param status
     * @param start
     * @param rows
     * @return
     */
    @GET("employee/applyList.do")
    Observable<HttpResult<PageBean<LeaveBean>>> getApplyList(@Query("accountId") String accountId, @Query("type") int type, @Query("isAudit") int isAudit, @Query("status") int status, @Query("start") int start, @Query("rows") int rows);


    /**
     * 获取申请详情
     * @param accountId
     * @param applyInfoId
     * @return
     */
    @GET("employee/apply.do")
    Observable<HttpResult<LeaveDetailBean>> getDetail(@Query("accountId") String accountId, @Query("applyInfoId") String applyInfoId);

    /**
     * 获取员工审批列表
     * @param accountId
     * @return
     */
    @GET("employee/auditors.do")
    Observable<HttpResult<List<DepartMentBean>>> getAuditors(@Query("accountId") String accountId);


    /**
     * 审批申请
     * @param accountId
     * @param applyInfoId
     * @param Status
     * @return
     */
    @PUT("employee/apply.do")
    Observable<HttpResult<List<DepartMentBean>>> applyStatus(@Query("accountId") String accountId, @Query("applyInfoId") String applyInfoId, @Query("status") int Status, @Body SchduleCarBodyBean schduleCarBodyBean);

    /**
     * 就餐扫码
     * @param accountId
     * @param code
     * @return
     */
    @POST("employee/dining.do")
    Observable<HttpResult<String>> dinning(@Query("accountId") String accountId, @Query("code") String code);

    /**
     * 就餐列表
     * @param accountId
     * @param start
     * @param rows
     * @return
     */
    @GET("employee/dining.do")
    Observable<HttpResult<PageBean<EatHistoryBean>>> eatList(@Query("accountId") String accountId, @Query("start") int start, @Query("rows") int rows);

    /**
     * 车辆调度
     * @param accountId
     * @param schduleCarBodyBean
     * @return
     */
    @GET("employee/{accountId}/schedule.do")
    Observable<HttpResult<PageBean<EatHistoryBean>>> schdueCar(@Path("accountId") String accountId, @Body SchduleCarBodyBean schduleCarBodyBean);

}

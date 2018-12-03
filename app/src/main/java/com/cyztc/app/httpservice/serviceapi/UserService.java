package com.cyztc.app.httpservice.serviceapi;

import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CheckPayBean;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.bean.TrainHistoryBeanP;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.httpservice.beans.ClassPublishTopBodyBean;
import com.cyztc.app.httpservice.beans.CreateTrainOrderBodyBean;
import com.cyztc.app.httpservice.beans.PendingMessageBodyBean;
import com.cyztc.app.httpservice.beans.UpdateUserInfoBodyBean;
import com.cyztc.app.httpservice.httpresult.HttpResult;
import java.util.List;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface UserService {

  /**
   * 用户登录
   */
  @GET("login.do")
  Observable<HttpResult<String>> loginAccount(@Query("deviceNo") String data);

  /**
   * 获取缴费信息
   */
  @GET("checkPay.do")
  Observable<HttpResult<CheckPayBean>> checkPay(@Query("studentId") String studentId);

  /**
   * 我的通讯录
   */
  @GET("addressBook.do")
  Observable<HttpResult<List<ContactBean>>> getAddressBook(@Query("accountId") String accountId,
      @Query("isMain") int isMain);

  /**
   * 修改密码
   *
   * @returnF
   */
  @POST("rePassword.do")
  Observable<HttpResult<BaseBean>> updatePwd(@Query("accountId") String accountId,
      @Query("oldPass") String password, @Query("newPass") String newPass);

  /**
   * 重置密码
   */
  @POST("resetPassword.do")
  Observable<HttpResult<BaseBean>> resetPwd(@Query("account") String account);

  /**
   * 课程支付
   */
  @POST("{orderId}/trainPay.do")
  Observable<HttpResult<WxOrderBean>> payForTrain(@Path("orderId") String orderId,
      @Query("payType") int payType);

  /**
   * 课程报名
   */
  @POST("trainOrder.do")
  Observable<HttpResult<String>> createTrainOrder(
      @Body CreateTrainOrderBodyBean createTrainOrderBodyBean);

  /**
   * 签到
   */
  @POST("sign.do")
  Observable<HttpResult<BaseBean>> sign(@Query("accountId") String accountId,
      @Query("courseArrangeId") String courseArrangeId);

  /**
   * 培训历史
   */
  @GET("trainStory.do")
  Observable<HttpResult<TrainHistoryBeanP>> trainHistory(@Query("studentId") String studentId,
      @Query("start") int start, @Query("rows") int rows);

  /**
   * 商品支付
   */
  @POST("{orderId}/order.do")
  Observable<HttpResult<WxOrderBean>> payForGoods(@Path("orderId") String orderId,
      @Query("studentId") String studentId, @Query("payType") int payType,
      @Query("status") int status);

  /**
   * 获取常用电话号码
   */
  @GET("meeting/phoneInfo.do")
  Observable<HttpResult<String[]>> getNormalPhonNums(@Query("meetingId") String meetingId);

  /**
   * 开寝室门
   */
  @POST("openLock.do")
  Observable<HttpResult<BaseBean>> openLock(@Query("studentId") String studentId,
      @Query("code") String code, @Query("type") int type);

  /**
   * 发布班级风采
   */
  @POST("mien.do")
  Observable<HttpResult<BaseBean>> publishClassTopic(
      @Body ClassPublishTopBodyBean classPublishTopBodyBean);

  /**
   * 更新头像昵称
   */
  @POST("user.do")
  Observable<HttpResult<Object>> updateUserInfo(
      @Body UpdateUserInfoBodyBean updateUserInfoBodyBean);

  /**
   * 获取小红点
   */
  @GET("{accountId}/pending.do")
  Observable<HttpResult<Map<String, Integer>>> getUserDots(@Path("accountId") String accountId);

  /**
   * 获取小红点
   */
  @POST("{accountId}/pending.do")
  Observable<HttpResult<BaseBean>> delMessagePeding(@Path("accountId") String accountId,
      @Body PendingMessageBodyBean pendingMessageBodyBean);

  /**
   * 退出登录
   *
   * @return
   */
  @GET("logout.do")
  Observable<HttpResult<BaseBean>> logout();
}

package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.bean.AssessListBeanP;
import com.cyztc.app.bean.BannerBeanP;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.bean.CreateTribeBean;
import com.cyztc.app.bean.CuiYueCircleBeanP;
import com.cyztc.app.bean.CuiYueCircleDetailBean;
import com.cyztc.app.bean.ExamDetailBean;
import com.cyztc.app.bean.GoodsBannerBeanP;
import com.cyztc.app.bean.GoodsBeanP;
import com.cyztc.app.bean.GoodsCartBeanP;
import com.cyztc.app.bean.GoodsDetailBean;
import com.cyztc.app.bean.InvestgateBeanP;
import com.cyztc.app.bean.MessageBeanP;
import com.cyztc.app.bean.OrderBeanP;
import com.cyztc.app.bean.OrderDetailBean;
import com.cyztc.app.bean.TeacherInfoBean;
import com.cyztc.app.bean.TopResponBeanP;
import com.cyztc.app.bean.TribeBeanP;
import com.cyztc.app.bean.TribeDetailBean;
import com.cyztc.app.bean.TribeMemberBeanP;
import com.cyztc.app.bean.TribeRecommendBeanP;
import com.cyztc.app.bean.TribeTopicBeanP;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.beans.AddCartBodyBean;
import com.cyztc.app.httpservice.beans.CreateOrderBodyBean;
import com.cyztc.app.httpservice.beans.CreateTribeBodyBean;
import com.cyztc.app.httpservice.beans.ExamAnswerBodyBean;
import com.cyztc.app.httpservice.beans.PublishTopicBodyBean;
import com.cyztc.app.httpservice.beans.TopicResponBodyBean;
import com.cyztc.app.httpservice.httpresult.HttpResult;

import java.util.Objects;

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
public interface HomeService {

    /**
     * 我的部落列表
     * @return
     */
    @GET("{studentId}/tribleList.do")
    Observable<HttpResult<TribeBeanP>> getTribeList(@Path("studentId") String studentId, @Query("start") int start, @Query("rows") int rows);

    /**
     * 部落详情
     * @param studentId
     * @param tribleId
     * @return
     */
    @GET("tribleDetail.do")
    Observable<HttpResult<TribeDetailBean>> getTribeDetail(@Query("studentId") String studentId, @Query("tribleId") String tribleId);


    /**
     * 获取部落会话列表
     * @param studentId
     * @param tribleId
     * @param start
     * @param rows
     * @return
     */
    @GET("tribleTopic.do")
    Observable<HttpResult<TribeTopicBeanP>> getTribeTopic(@Query("studentId") String studentId, @Query("tribleId") String tribleId, @Query("start") int start, @Query("rows") int rows);

    /**
     * 创建部落
     * @param createTribeBean
     * @return
     */
    @POST("createTrible.do")
    Observable<HttpResult<TribeDetailBean>> createTribe(@Body CreateTribeBean createTribeBean);


    /**
     * 推荐部落列表
     * @param studentId
     * @param name
     * @param start
     * @param rows
     * @return
     */
    @GET("tribleList.do")
    Observable<HttpResult<TribeRecommendBeanP>> getFindTribeList(@Query("studentId") String studentId, @Query("name") String name, @Query("start") int start, @Query("rows") int rows);

    /**
     * 获取部落成员列表
     * @param studentId
     * @param tribleId
     * @return
     */
    @GET("tribleMembers.do")
    Observable<HttpResult<TribeMemberBeanP>> getTribeMemember(@Query("studentId") String studentId, @Query("tribleId") String tribleId);

    /**
     * 提供商品/洗衣服务列表
     * @param studentId
     * @param type 服务类型 1：商品，2：洗衣，3：报修，4:客房，5：会场，6:医疗
     * @param subtype
     * @param name
     * @param start
     * @param rows
     * @return
     */
    @GET("goodsList.do")
    Observable<HttpResult<GoodsBeanP>> getGoodsList(@Query("studentId") String studentId, @Query("type") int type, @Query("subType") String subtype, @Query("name") String name, @Query("start") int start, @Query("rows") int rows);


    /**
     * 商品详情
     * @param studentId
     * @param goodsId
     * @return
     */
    @GET("goodsDetail.do")
    Observable<HttpResult<GoodsDetailBean>> getGoodsDetail(@Query("studentId") String studentId, @Query("goodsId") String goodsId);

    /**
     * 加入购物车
     * @param studentId
     * @param addCartBodyBean
     * @return
     */
    @POST("{studentId}/cart.do")
    Observable<HttpResult<Integer>> addCart(@Path("studentId") String studentId, @Body AddCartBodyBean addCartBodyBean);

    /**
     * 获取购物车列表
     * @param studentId
     * @param name
     * @param start
     * @param rows
     * @return
     */
    @GET("{studentId}/cart.do")
    Observable<HttpResult<GoodsCartBeanP>> getCartList(@Path("studentId") String studentId, @Query("name") String name, @Query("start") int start, @Query("rows") int rows);

    /**
     * 删除购物车
     * @param studentId
     * @param goodsId
     * @return
     */
    @DELETE("{studentId}/cart.do")
    Observable<HttpResult<BaseBean>> deleteCartItems(@Path("studentId") String studentId, @Query("goodsId") String goodsId);

    /**
     * 获取订单列表
     * @param studentId
     * @param type
     * @param start
     * @param rows
     * @return
     */
    @GET("{studentId}/order.do")
    Observable<HttpResult<OrderBeanP>> getOrderList(@Path("studentId") String studentId, @Query("type") int type, @Query("start") int start, @Query("rows") int rows);

    /**
     * 获取话题回复列表
     * @param studentId
     * @param topicId
     * @param start
     * @param rows
     * @return
     */
    @GET("topicReplies.do")
    Observable<HttpResult<TopResponBeanP>> getTpoResponList(@Query("studentId") String studentId, @Query("topicId") String topicId, @Query("start") int start, @Query("rows") int rows);


    /**
     * 回复话题
     * @param topicResponBodyBean
     * @return
     */
    @POST("topicReplies.do")
    Observable<HttpResult<TopResponBeanP>> topicReplies(@Body TopicResponBodyBean topicResponBodyBean);

    /**
     * 获取评估列表
     * @param studentId
     * @param type
     * @param start
     * @param rows
     * @return
     */
    @GET("investgate/exam.do")
    Observable<HttpResult<AssessListBeanP>> getAssessLists(@Query("studentId") String studentId, @Query("type") int type, @Query("start") int start, @Query("rows") int rows);

    /**
     * 加入部落
     * @param studentId
     * @param tribleId
     * @return
     */
    @POST("joinTrible.do")
    Observable<HttpResult<BaseBean>> joinTrible(@Query("studentId") String studentId, @Query("tribleId") String tribleId);

    /**
     * 发布部落话题
     * @param publishTopicBodyBean
     * @return
     */
    @POST("tribleTopic.do")
    Observable<HttpResult<BaseBean>> publishTopic(@Body PublishTopicBodyBean publishTopicBodyBean);

    /**
     * 获取评估问题列表
     * @param type
     * @return
     */
    @GET("investgate/examDetail.do")
    Observable<HttpResult<ExamDetailBean>> getExamDetail(@Query("type") int type);

    /**
     * 提交评估
     * @param examAnswerBodyBean
     * @return
     */
    @POST("investgate/answer.do")
    Observable<HttpResult<BaseBean>> subExamAnswer(@Body ExamAnswerBodyBean examAnswerBodyBean);

    /**
     * 获取推送消息列表
     * @param accountId
     * @param start
     * @param rows
     * @return
     */
    @GET("message.do")
    Observable<HttpResult<MessageBeanP>> getMessage(@Query("accountId") String accountId, @Query("start") int start, @Query("rows") int rows);

    /**
     * 阅读消息
     * @param messageId
     * @return
     */
    @POST("{accountId}/message.do")
    Observable<HttpResult<Integer>> readMessage(@Path("accountId") String accountId, @Query("messageId") String messageId);

    /**
     * 获取是否有未读消息
     * @param accountId
     * @return
     */
    @GET("{accountId}/message.do")
    Observable<HttpResult<Boolean>> getMessageCount(@Path("accountId") String accountId);

    /**
     * 获取翠月动态列表
     * @param studentId
     * @param start
     * @param rows
     * @return
     */
    @GET("trainingDynamic/dynamicInfo.do")
    Observable<HttpResult<CuiYueCircleBeanP>> getCuiYueDynamicList(@Query("studentId") String studentId,  @Query("type") int type, @Query("start") int start, @Query("rows") int rows);

    /**
     * 获取翠月动态详情
     * @param id
     * @return
     */
    @GET("trainingDynamic/detail.do")
    Observable<HttpResult<CuiYueCircleDetailBean>> getCuiYueDynamicDetail(@Query("id") String id);

    /**
     * 获取教师详情
     * @param userId
     * @return
     */
    @GET("teacher.do")
    Observable<HttpResult<TeacherInfoBean>> getTeacherInfo(@Query("userId") String userId);

    /**
     * isLike 1：点赞，0：取消
     * @param studentId
     * @param topicId
     * @param isLike
     * @return
     */
    @POST("likeTopic.do")
    Observable<HttpResult<BaseBean>> likeTopic(@Query("studentId") String studentId, @Query("topicId") String topicId, @Query("isLike") int isLike);

    /**
     * 部落帖子删除回复
     * @param replyId
     * @return
     */
    @DELETE("topicReplies.do")
    Observable<HttpResult<BaseBean>> deleteTopicReplay(@Query("replyId") String replyId);

    /**
     * 商品服务下单
     * @param studentId
     * @param createOrderBodyBean
     * @return
     */
    @POST("{studentId}/order.do")
    Observable<HttpResult<String>> createOrder(@Path("studentId") String studentId, @Body CreateOrderBodyBean createOrderBodyBean);

    /**
     * 获取订单详情
     * @param orderId
     * @param studentId
     * @return
     */
    @GET("orderDetail.do")
    Observable<HttpResult<OrderDetailBean>> getOrderDetail(@Query("orderId") String orderId, @Query("studentId") String studentId);

//    /**
//     * 获取我的订单列表
//     * @param studentId
//     * @param type
//     * @param status
//     * @param start
//     * @param rows
//     * @return
//     */
//    @GET("{studentId}/order.do")
//    Observable<HttpResult<OrderDetailBean>> getOrderList(@Path("studentId") String studentId, @Query("type") int type, @Query("status") int status, @Query("start") int start, @Query("rows") int rows);

    /**
     * 获取代表须知
     * @param meetingId
     * @return
     */
    @GET("meeting/meetingNotice.do")
    Observable<HttpResult<String>> getMeetingNotice(@Query("meetingId") String meetingId);

    /**
     * 获取常用电话
     * @param meetingId
     * @return
     */
    @GET("meeting/phoneInfo.do")
    Observable<HttpResult<String>> getMeetingPhone(@Query("meetingId") String meetingId);

    /**
     * 翠月推荐banner
     * @param studentId
     * @param start
     * @return
     */
    @GET("trainingDynamic/recommend.do")
    Observable<HttpResult<BannerBeanP>> getBanners(@Query("studentId") String studentId, @Query("start") int start);

    /**
     * 获取部落推荐列表
     * @param studentId
     * @param start
     * @param rows
     * @return
     */
    @GET("recommendTribes.do")
    Observable<HttpResult<TribeRecommendBeanP>> getRecommendTribes(@Query("studentId") String studentId, @Query("start") int start);

    /**
     * 获取商品推荐列表
     * @param studentId
     * @param start
     * @param rows
     * @return
     */
    @GET("recommendGoods.do")
    Observable<HttpResult<GoodsBannerBeanP>> getRecommendGoods(@Query("studentId") String studentId, @Query("start") int start, @Query("rows") int rows);

    /**
     * 删除部落
     * @param tribeId
     * @param studentId
     * @return
     */
    @DELETE("tribe.do")
    Observable<HttpResult<Object>> deleteTribe(@Query("tribeId") String tribeId, @Query("studentId") String studentId);

    /**
     * 获取评估对象
     * @param type
     * @param studentId
     * @param start
     * @param rows
     * @return
     */
    @GET("investgate/targets.do")
    Observable<HttpResult<InvestgateBeanP>> getInvestgateLists(@Query("type") int type, @Query("studentId") String studentId, @Query("start") int start, @Query("rows") int rows);
}

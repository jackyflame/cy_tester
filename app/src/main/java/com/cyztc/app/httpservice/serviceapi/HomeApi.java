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
import com.cyztc.app.httpservice.service.BaseApi;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Administrator on 2016/8/12.
 */
public class HomeApi extends BaseApi{

    public static HomeApi homeApi;
    public HomeService homeService;
    private static String httpMethod;
    public HomeApi()
    {
        homeService = HttpMethod.getInstance().createApi(HomeService.class);
    }
    public static HomeApi getInstance()
    {
        if(homeApi == null)
        {
            homeApi = new HomeApi();
        }
        return homeApi;
    }

    public void getTribeList(String studentId, int start, int rows, HttpSubscriber<TribeBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getTribeList(studentId, start, rows)
                    .map(new HttpResultFunc<TribeBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getTribeDetail(String studentId, String tribleId, HttpSubscriber<TribeDetailBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getTribeDetail(studentId, tribleId)
                    .map(new HttpResultFunc<TribeDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getTribeTopic(String studentId, String tribleId, int start, int rows, HttpSubscriber<TribeTopicBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getTribeTopic(studentId, tribleId, start, rows)
                    .map(new HttpResultFunc<TribeTopicBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void createTribe(CreateTribeBean tribe, HttpSubscriber<TribeDetailBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.createTribe(tribe)
                    .map(new HttpResultFunc<TribeDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getFindTribeList(String studentId, String name, int start, int rows, HttpSubscriber<TribeRecommendBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getFindTribeList(studentId, name, start, rows)
                    .map(new HttpResultFunc<TribeRecommendBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getTribeMemember(String studentId, String tribeId, HttpSubscriber<TribeMemberBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getTribeMemember(studentId, tribeId)
                    .map(new HttpResultFunc<TribeMemberBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getGoodsList(String studentId, int type, String subtype, String name, int start,int rows, HttpSubscriber<GoodsBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getGoodsList(studentId, type, subtype, name, start, rows)
                    .map(new HttpResultFunc<GoodsBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getGoodsDetail(String studentId, String goodsId, HttpSubscriber<GoodsDetailBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getGoodsDetail(studentId, goodsId)
                    .map(new HttpResultFunc<GoodsDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void addCart(String studentId, String goodsId, int count, HttpSubscriber<Integer> subscriber) {
        if(homeService != null) {
            AddCartBodyBean addCartBodyBean = new AddCartBodyBean();
            addCartBodyBean.setGoodsId(goodsId);
            addCartBodyBean.setCount(count);
            Observable observable = homeService.addCart(studentId, addCartBodyBean)
                    .map(new HttpResultFunc<Integer>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getCartList(String studentId, String name, int start, int rows, HttpSubscriber<GoodsCartBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getCartList(studentId, name, start, rows)
                    .map(new HttpResultFunc<GoodsCartBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void deleteCartItems(String studentId, String goodsid, HttpSubscriber<BaseBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.deleteCartItems(studentId, goodsid)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getOrderList(String studentId, int type, int status, int start, int rows, HttpSubscriber<OrderBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getOrderList(studentId, type, start, rows)
                    .map(new HttpResultFunc<OrderBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getTpoResponList(String studentId, String topicId, int start, int rows, HttpSubscriber<TopResponBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getTpoResponList(studentId, topicId, start, rows)
                    .map(new HttpResultFunc<TopResponBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void topicReplies(String content, String studentid, String topidid, HttpSubscriber<TopResponBeanP> subscriber) {
        if(homeService != null) {
            TopicResponBodyBean topicResponBodyBean = new TopicResponBodyBean();
            topicResponBodyBean.setContent(content);
            topicResponBodyBean.setStudentId(studentid);
            topicResponBodyBean.setTopicId(topidid);
            Observable observable = homeService.topicReplies(topicResponBodyBean)
                    .map(new HttpResultFunc<TopResponBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getAssessLists(String studentId, int type, int start, int rows, HttpSubscriber<AssessListBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getAssessLists(studentId, type, start, rows)
                    .map(new HttpResultFunc<AssessListBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void joinTrible(String studentId, String tribleId, HttpSubscriber<BaseBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.joinTrible(studentId, tribleId)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void publishTopic(String content, String picture, String studentId, String title, String tribleId, HttpSubscriber<BaseBean> subscriber) {
        if(homeService != null) {
            PublishTopicBodyBean publishTopicBodyBean = new PublishTopicBodyBean();
            publishTopicBodyBean.setContent(content);
            publishTopicBodyBean.setPicture(picture);
            publishTopicBodyBean.setTitle(title);
            publishTopicBodyBean.setStudentId(studentId);
            publishTopicBodyBean.setTribleId(tribleId);
            Observable observable = homeService.publishTopic(publishTopicBodyBean)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getExamDetail(int type, HttpSubscriber<ExamDetailBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getExamDetail(type)
                    .map(new HttpResultFunc<ExamDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void subExamAnswer(String[] answer, String examId, String studentId, String targetId, HttpSubscriber<BaseBean> subscriber) {
        if(homeService != null) {
            ExamAnswerBodyBean examAnswerBodyBean = new ExamAnswerBodyBean();
            examAnswerBodyBean.setAnswer(answer);
            examAnswerBodyBean.setExamId(examId);
            examAnswerBodyBean.setStudentId(studentId);
            examAnswerBodyBean.setTargetId(targetId);
            Observable observable = homeService.subExamAnswer(examAnswerBodyBean)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getMessage(String studentId, int start, int rows, HttpSubscriber<MessageBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getMessage(studentId, start, rows)
                    .map(new HttpResultFunc<MessageBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void readMessage(String accountId, String messageId, HttpSubscriber<Integer> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.readMessage(accountId, messageId)
                    .map(new HttpResultFunc<Integer>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getMessageCount(String accountId, HttpSubscriber<Boolean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getMessageCount(accountId)
                    .map(new HttpResultFunc<Boolean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getCuiYueDynamicList(String studentId, int type, int start, int rows, HttpSubscriber<CuiYueCircleBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getCuiYueDynamicList(studentId, type, start, rows)
                    .map(new HttpResultFunc<CuiYueCircleBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getCuiYueDynamicDetail(String id, HttpSubscriber<CuiYueCircleDetailBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getCuiYueDynamicDetail(id)
                    .map(new HttpResultFunc<CuiYueCircleDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getTeacherInfo(String teacherId, HttpSubscriber<TeacherInfoBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getTeacherInfo(teacherId)
                    .map(new HttpResultFunc<TeacherInfoBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void likeTopic(String studentId, String topicId, int isLike, HttpSubscriber<BaseBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.likeTopic(studentId, topicId, isLike)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void deleteTopicReplay(String replyId, HttpSubscriber<BaseBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.deleteTopicReplay(replyId)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void createOrder(String studentId, int type, HashMap<String, Integer> goods, String address, float cost, int count, int payType, String picture, String remark, String subType, String takeTime, HttpSubscriber<String> subscriber) {
        if(homeService != null) {
            CreateOrderBodyBean createOrderBodyBean = new CreateOrderBodyBean();
            createOrderBodyBean.setType(type);
            createOrderBodyBean.setGoods(goods);
            createOrderBodyBean.setAddress(address);
            createOrderBodyBean.setCost(cost);
            createOrderBodyBean.setCount(count);
            createOrderBodyBean.setPayType(payType);
            createOrderBodyBean.setPicture(picture);
            createOrderBodyBean.setRemark(remark);
            createOrderBodyBean.setSubType(subType);
            createOrderBodyBean.setTakeTime(takeTime);

            Observable observable = homeService.createOrder(studentId, createOrderBodyBean)
                    .map(new HttpResultFunc<String>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getOrderDetail(String orderId, String studentId, HttpSubscriber<OrderDetailBean> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getOrderDetail(orderId, studentId)
                    .map(new HttpResultFunc<OrderDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getMeetingNotice(String meetingId, HttpSubscriber<String> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getMeetingNotice(meetingId)
                    .map(new HttpResultFunc<String>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getMeetingPhone(String meetingId, HttpSubscriber<String> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getMeetingPhone(meetingId)
                    .map(new HttpResultFunc<String>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getBanners(String studentId, int start, int rows, HttpSubscriber<BannerBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getBanners(studentId, start)
                    .map(new HttpResultFunc<BannerBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getRecommendTribes(String studentId, int start, int rows, HttpSubscriber<TribeRecommendBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getRecommendTribes(studentId, start)
                    .map(new HttpResultFunc<TribeRecommendBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getRecommendGoods(String studentId, int start, int rows, HttpSubscriber<GoodsBannerBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getRecommendGoods(studentId, start, rows)
                    .map(new HttpResultFunc<GoodsBannerBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void deleteTribe(String tribeId, String studentId, HttpSubscriber<Object> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.deleteTribe(tribeId, studentId)
                    .map(new HttpResultFunc<Object>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getInvestgateLists(int type, String studentId, int start, int rows, HttpSubscriber<InvestgateBeanP> subscriber) {
        if(homeService != null) {
            Observable observable = homeService.getInvestgateLists(type, studentId, start, rows)
                    .map(new HttpResultFunc<InvestgateBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

}

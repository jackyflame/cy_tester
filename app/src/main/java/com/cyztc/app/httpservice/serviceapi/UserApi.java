package com.cyztc.app.httpservice.serviceapi;

import com.blankj.utilcode.util.LogUtils;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.CheckPayBean;
import com.cyztc.app.bean.ContactBean;
import com.cyztc.app.bean.TrainHistoryBeanP;
import com.cyztc.app.bean.WxOrderBean;
import com.cyztc.app.httpservice.beans.ClassPublishTopBodyBean;
import com.cyztc.app.httpservice.beans.CreateTrainOrderBodyBean;
import com.cyztc.app.httpservice.beans.PendingMessageBodyBean;
import com.cyztc.app.httpservice.beans.UpdateUserInfoBodyBean;
import com.cyztc.app.httpservice.service.BaseApi;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;
import com.cyztc.app.utils.RSAUtils;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/12.
 */
public class UserApi extends BaseApi {

  public static UserApi userApi;
  public UserService userService;
  private static String httpMethod;

  public UserApi() {
    userService = HttpMethod.getInstance().createApi(UserService.class);
  }

  public static UserApi getInstance() {
    if (userApi == null) {
      userApi = new UserApi();
    }
    return userApi;
  }

  public void loginAccount(String account, String password, String deviceNo, int isEmployee,
      HttpSubscriber<String> subscriber) {
    if (userService != null) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("account", account);
      jsonObject.addProperty("password", password);
      jsonObject.addProperty("deviceIdentity", deviceNo);
      String content = jsonObject.toString();
      LogUtils.i(content);
      String data = RSAUtils.encryptRSA(content);
      LogUtils.i(data);
      Observable observable =
          userService.loginAccount(data)
              .map(new HttpResultFunc<String>());
      toSubscribe(observable, subscriber);
    }
  }

  public void checkPay(String studentId, HttpSubscriber<CheckPayBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.checkPay(studentId)
          .map(new HttpResultFunc<CheckPayBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void getAddressBook(String accountId, int isMain,
      HttpSubscriber<List<ContactBean>> subscriber) {
    if (userService != null) {
      Observable observable = userService.getAddressBook(accountId, isMain)
          .map(new HttpResultFunc<List<ContactBean>>());
      toSubscribe(observable, subscriber);
    }
  }

  public void updatePwd(String accountId, String password, String newPass,
      HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("accountId", accountId);
      jsonObject.addProperty("oldPass", password);
      jsonObject.addProperty("newPass", newPass);
      String data = RSAUtils.encryptRSA(jsonObject.toString());
      Observable observable =
          userService.updatePwd(data)
              .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void resetPwd(String account, String idCard, String phoneNum, String studentName,
      HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.resetPwd(RSAUtils.encryptRSA(account))
          .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void payForTrain(String orderId, int payType, HttpSubscriber<WxOrderBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.payForTrain(orderId, payType)
          .map(new HttpResultFunc<WxOrderBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void createTrainOrder(String accountId, String trainInfoId, int status,
      HttpSubscriber<String> subscriber) {
    if (userService != null) {
      CreateTrainOrderBodyBean createTrainOrderBodyBean = new CreateTrainOrderBodyBean();
      createTrainOrderBodyBean.setAccountId(accountId);
      createTrainOrderBodyBean.setTrainInfoId(trainInfoId);
      createTrainOrderBodyBean.setStatus(status);
      Observable observable = userService.createTrainOrder(createTrainOrderBodyBean)
          .map(new HttpResultFunc<String>());
      toSubscribe(observable, subscriber);
    }
  }

  public void sign(String accountId, String courseArrangeId, HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.sign(accountId, courseArrangeId)
          .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void trainHistory(String studentId, int start, int rows,
      HttpSubscriber<TrainHistoryBeanP> subscriber) {
    if (userService != null) {
      Observable observable = userService.trainHistory(studentId, start, rows)
          .map(new HttpResultFunc<TrainHistoryBeanP>());
      toSubscribe(observable, subscriber);
    }
  }

  public void payForGoods(String studentId, String orderId, int payType, int status,
      HttpSubscriber<WxOrderBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.payForGoods(studentId, orderId, payType, status)
          .map(new HttpResultFunc<WxOrderBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void getNormalPhonNums(String meetingId, HttpSubscriber<String[]> subscriber) {
    if (userService != null) {
      Observable observable = userService.getNormalPhonNums(meetingId)
          .map(new HttpResultFunc<String[]>());
      toSubscribe(observable, subscriber);
    }
  }

  public void openLock(String meetingId, String code, int type,
      HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.openLock(meetingId, code, type)
          .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void publishClassTopic(String content, String creator, String picture, int readCount,
      String remark, String thumbnail, String title, String trainingInfoId, int type,
      HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      ClassPublishTopBodyBean classPublishTopBodyBean = new ClassPublishTopBodyBean();
      classPublishTopBodyBean.setContent(content);
      classPublishTopBodyBean.setCreator(creator);
      classPublishTopBodyBean.setPicture(picture);
      classPublishTopBodyBean.setRemark(remark);
      classPublishTopBodyBean.setReadCount(readCount);
      classPublishTopBodyBean.setThumbnail(thumbnail);
      classPublishTopBodyBean.setTitle(title);
      classPublishTopBodyBean.setTrainingInfoId(trainingInfoId);
      classPublishTopBodyBean.setType(type);
      Observable observable = userService.publishClassTopic(classPublishTopBodyBean)
          .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void updateUserInfo(String accountId, String nickName, String picture,
      HttpSubscriber<Object> subscriber) {
    if (userService != null) {
      UpdateUserInfoBodyBean updateUserInfoBodyBean = new UpdateUserInfoBodyBean();
      updateUserInfoBodyBean.setAccountId(accountId);
      updateUserInfoBodyBean.setNickName(nickName);
      updateUserInfoBodyBean.setPicture(picture);
      Observable observable = userService.updateUserInfo(updateUserInfoBodyBean)
          .map(new HttpResultFunc<Object>());
      toSubscribe(observable, subscriber);
    }
  }

  public void getUserDots(String accountId, HttpSubscriber<Map<String, Integer>> subscriber) {
    if (userService != null) {
      Observable observable = userService.getUserDots(accountId)
          .map(new HttpResultFunc<Map<String, Integer>>());
      toSubscribe(observable, subscriber);
    }
  }

  public void delMessagePeding(String accountId, String targetId, int type, String remark,
      HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      PendingMessageBodyBean pendingMessageBodyBean = new PendingMessageBodyBean();
      pendingMessageBodyBean.setTargetId(targetId);
      pendingMessageBodyBean.setType(type);
      pendingMessageBodyBean.setRemark(remark);
      Observable observable = userService.delMessagePeding(accountId, pendingMessageBodyBean)
          .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }

  public void logout(HttpSubscriber<BaseBean> subscriber) {
    if (userService != null) {
      Observable observable = userService.logout()
          .map(new HttpResultFunc<BaseBean>());
      toSubscribe(observable, subscriber);
    }
  }
}

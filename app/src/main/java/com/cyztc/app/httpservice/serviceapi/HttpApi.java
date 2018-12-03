package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.httpservice.httpresult.HttpResult;
import com.cyztc.app.httpservice.service.BaseApi;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;

import rx.Observable;


/**
 * Created by Administrator on 2016/8/12.
 */
public class HttpApi extends BaseApi{

    public static HttpApi activityApi;
    public HttpService httpService;
    public HttpApi()
    {
        httpService = HttpMethod.getInstance().createApi(HttpService.class);
    }
    public static HttpApi getInstance()
    {
        if(activityApi == null)
        {
            activityApi = new HttpApi();
        }
        return activityApi;
    }

    public void loginAccount(HttpSubscriber<Boolean> subscriber) {
        Observable observable = httpService.loginAccount()
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    //    /**
//     * 获取活动列表
//     */
//    public void loginAccount(String sellerKey, HttpSubscriber<Boolean> subscriber){
//        LoginBean loginBean = new LoginBean();
//        loginBean.setSellerKey(sellerKey);
//        Observable observable = httpService.loginAccount(loginBean)
//                .map(new HttpResultFunc());
//        toSubscribe(observable, subscriber);
//    }


}

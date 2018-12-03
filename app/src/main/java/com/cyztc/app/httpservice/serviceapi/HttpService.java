package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.httpservice.httpresult.HttpResult;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface HttpService {

//    /**
//     * 商户登录
//     * @return
//     */
//    @POST("seller/checkseller")
//    Observable<HttpResult<Boolean>> loginAccount(@Body LoginBean loginBean);

    /**
     * 商户登录
     * @return
     */
    @POST("seller/checkseller")
    Observable<HttpResult<Boolean>> loginAccount();

}

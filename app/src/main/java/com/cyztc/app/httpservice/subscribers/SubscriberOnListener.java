package com.cyztc.app.httpservice.subscribers;

/**
 * Created by ywl on 2016/5/19.
 */
public interface SubscriberOnListener<T> {

    void onSucceed(T data);

    void onError(int code, String msg);

}

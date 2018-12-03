package com.cyztc.app.httpservice.subscribers;

import android.content.Context;


import com.cyztc.app.httpservice.service.ExciptionApi;
import com.cyztc.app.log.MyLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by ywl on 2016/5/19.
 */
public class HttpSubscriber<T> extends Subscriber<T>{

    private SubscriberOnListener subscriberOnListener;
    private Context context;

//    public HttpSubscriber(SubscriberOnListener subscriberOnListener)
//    {
//        this.subscriberOnListener = subscriberOnListener;
//    }

    public HttpSubscriber(SubscriberOnListener subscriberOnListener, Context context)
    {
        this.subscriberOnListener = subscriberOnListener;
        this.context = context;
    }




    public void onUnsubscribe() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
            MyLog.d("unsubscribe");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.d("start...");
    }

    @Override
    public void onCompleted() {
        if(subscriberOnListener != null && context != null)
        {
//            subscriberOnListener.onCompleted(-1);
        }
        else
        {
            onUnsubscribe();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(subscriberOnListener != null && context != null) {
            try {
                if (e instanceof SocketTimeoutException) {
                    subscriberOnListener.onError(-1001, "网络超时，请检查您的网络状态");
                } else if (e instanceof ConnectException) {
                    subscriberOnListener.onError(-1002, "网络中断，请检查您的网络状态");
                } else if (e instanceof ExciptionApi) {
                    subscriberOnListener.onError(((ExciptionApi) e).getApiExceptionCode(), ((ExciptionApi) e).getApiExceptionMsg());
                } else {
//                    MyLog.e("ERROR", e.toString());
//                    if (AppUtil.isNetworkAvailable(BdApplication.CONTEXT)) {
//                        subscriberOnListener.onError(-1003, "网络连接失败");
//                    } else {
//                        subscriberOnListener.onError(-1004, "网络连接失败");
//                    }
                    MyLog.d(e.toString());
                    subscriberOnListener.onError(-1003, "访问数据出错啦");
                }
//            subscriberOnListener.onCompleted(1);
            }
            catch(IllegalStateException e1)
            {
                MyLog.e("ERROR2", e1.toString());
            }
        }
        else
        {
            onUnsubscribe();
        }
    }

    @Override
    public void onNext(T t) {
        if(subscriberOnListener != null && context != null)
        {
            subscriberOnListener.onSucceed(t);
//            subscriberOnListener.onCompleted(1);
        }
        else
        {
            onUnsubscribe();
        }
    }
}

package com.cyztc.app.httpservice.service;

/**
 * Created by ywl on 2016/5/19.
 */
public class ExciptionApi extends RuntimeException {

    private static int resultCode;
    private static String msg;

    public ExciptionApi(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public int getApiExceptionCode()
    {
        return resultCode;
    }

    public String getApiExceptionMsg()
    {
        return msg;
    }

}

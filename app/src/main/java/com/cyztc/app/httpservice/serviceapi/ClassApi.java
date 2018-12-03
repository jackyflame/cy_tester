package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.bean.AlbumBeanP;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.ClassMineBeanP;
import com.cyztc.app.bean.CorseBeanP;
import com.cyztc.app.bean.EmSignBean;
import com.cyztc.app.bean.EmSignDetailBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.bean.TrainEnlistBeanP;
import com.cyztc.app.bean.TrainingInfoBean;
import com.cyztc.app.bean.UserBean;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.httpservice.beans.UploadAlbumBodyBean;
import com.cyztc.app.httpservice.service.BaseApi;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Administrator on 2016/8/12.
 */
public class ClassApi extends BaseApi{

    public static ClassApi userApi;
    public ClassService classService;
    private static String httpMethod;
    public ClassApi()
    {
        classService = HttpMethod.getInstance().createApi(ClassService.class);
    }
    public static ClassApi getInstance()
    {
        if(userApi == null)
        {
            userApi = new ClassApi();
        }
        return userApi;
    }

    public void getClassMien(String accountId, String trainId,int start, int rows, int type, HttpSubscriber<ClassMineBeanP> subscriber) {
        if(classService != null) {
            Observable observable = classService.getClassMien(accountId, trainId, start, rows, type)
                    .map(new HttpResultFunc<ClassMineBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getMyTrainingInfos(String accountId, HttpSubscriber<TrainingInfoBean> subscriber) {
        if(classService != null) {
            Observable observable = classService.getMyTrainingInfos(accountId)
                    .map(new HttpResultFunc<TrainingInfoBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getCourses(String accountId, String courseDate, int start, int rows, HttpSubscriber<CorseBeanP> subscriber) {
        if(classService != null) {
            Observable observable = classService.getCourses(accountId, courseDate, start, rows)
                    .map(new HttpResultFunc<CorseBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getEmCourses(String accountId, String signDate, HttpSubscriber<EmSignDetailBean> subscriber) {
        if(classService != null) {
            Observable observable = classService.getEmCourses(accountId, signDate)
                    .map(new HttpResultFunc<EmSignDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getEmSignList(String accountId, int start, int rows, HttpSubscriber<PageBean<EmSignBean>> subscriber) {
        if(classService != null) {
            Observable observable = classService.getEmSignList(accountId, start, rows)
                    .map(new HttpResultFunc<PageBean<EmSignBean>>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getAlbum(String studentId, String trainId, String title, int start, int rows, HttpSubscriber<AlbumBeanP> subscriber) {
        if(classService != null) {
            Observable observable = classService.getAlbum(studentId, trainId, title, start, rows)
                    .map(new HttpResultFunc<AlbumBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    public void uploadAlbum(String creator, String picture, String remark, String title, String trainingInfoId, HttpSubscriber<BaseBean> subscriber) {
        if(classService != null) {
            UploadAlbumBodyBean uploadAlbumBodyBean = new UploadAlbumBodyBean();
            uploadAlbumBodyBean.setCreator(creator);
            uploadAlbumBodyBean.setPicture(picture);
            uploadAlbumBodyBean.setRemark(remark);
            uploadAlbumBodyBean.setTrainingInfoId(trainingInfoId);
            Observable observable = classService.uploadAlbum(uploadAlbumBodyBean)
                    .map(new HttpResultFunc<BaseBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getOptionTrainList(String studentId, int type, int start, int rows, HttpSubscriber<TrainEnlistBeanP> subscriber) {
        if(classService != null) {
            Observable observable = classService.getOptionTrainList(studentId, type, start, rows)
                    .map(new HttpResultFunc<TrainEnlistBeanP>());
            toSubscribe(observable, subscriber);
        }
    }

    //getEmSign
    public void getEmSign(String accountId, String wifiName, int type, String signDate, String ip, HttpSubscriber<EmSignDetailBean> subscriber) {
        if(classService != null) {
            Observable observable = classService.getEmSign(accountId, wifiName, type, signDate, ip)
                    .map(new HttpResultFunc<EmSignDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

}

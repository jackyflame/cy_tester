package com.cyztc.app.httpservice.serviceapi;


import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.BookDetailBean;
import com.cyztc.app.bean.BookListBean;
import com.cyztc.app.bean.ClassMineBeanP;
import com.cyztc.app.bean.DepartMentBean;
import com.cyztc.app.bean.EatHistoryBean;
import com.cyztc.app.bean.EmployeeApplyBean;
import com.cyztc.app.bean.LeaveBean;
import com.cyztc.app.bean.LeaveDetailBean;
import com.cyztc.app.bean.PageBean;
import com.cyztc.app.bean.TypeBeanP;
import com.cyztc.app.bean.VideoDetailBean;
import com.cyztc.app.bean.VideoListBean;
import com.cyztc.app.httpservice.beans.SchduleCarBodyBean;
import com.cyztc.app.httpservice.service.BaseApi;
import com.cyztc.app.httpservice.service.HttpMethod;
import com.cyztc.app.httpservice.subscribers.HttpSubscriber;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Administrator on 2016/8/12.
 */
public class EmployeeApi extends BaseApi{

    public static EmployeeApi employeeApi;
    public EmployeeService employeeService;
    public EmployeeApi()
    {
        employeeService = HttpMethod.getInstance().createApi(EmployeeService.class);
    }
    public static EmployeeApi getInstance()
    {
        if(employeeApi == null)
        {
            employeeApi = new EmployeeApi();
        }
        return employeeApi;
    }


    public void apply(EmployeeApplyBean employeeApplyBean, HttpSubscriber<String> subscriber) {
        if(employeeService != null) {
            Observable observable = employeeService.apply(employeeApplyBean)
                    .map(new HttpResultFunc<String>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getApplyList(String accountId, int type, int isAudit, int status, int start, int rows, HttpSubscriber<PageBean<LeaveBean>> subscriber) {
        if(employeeService != null) {
            Observable observable = employeeService.getApplyList(accountId, type, isAudit, status, start, rows)
                    .map(new HttpResultFunc<PageBean<LeaveBean>>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getDetail(String accountId, String applyInfoId, HttpSubscriber<LeaveDetailBean> subscriber) {
        if(employeeService != null) {
            Observable observable = employeeService.getDetail(accountId, applyInfoId)
                    .map(new HttpResultFunc<LeaveDetailBean>());
            toSubscribe(observable, subscriber);
        }
    }

    public void getAuditors(String accountId, HttpSubscriber<List<DepartMentBean>> subscriber) {
        if(employeeService != null) {
            Observable observable = employeeService.getAuditors(accountId)
                    .map(new HttpResultFunc<List<DepartMentBean>>());
            toSubscribe(observable, subscriber);
        }
    }

    //司机审核 status : 7
    public void applyStatus(String accountId, String applyInfoId, int Status, String driver, String vehicle, String vehicleNo, String mobile, HttpSubscriber<List<DepartMentBean>> subscriber) {
        if(employeeService != null) {

            SchduleCarBodyBean schduleCarBodyBean = new SchduleCarBodyBean();
            schduleCarBodyBean.setApplyInfoId(applyInfoId);
            schduleCarBodyBean.setDriver(driver);
            schduleCarBodyBean.setVehicle(vehicle);
            schduleCarBodyBean.setVehicleNo(vehicleNo);
            schduleCarBodyBean.setMobile(mobile);
            Observable observable = employeeService.applyStatus(accountId, applyInfoId, Status, schduleCarBodyBean)
                    .map(new HttpResultFunc<List<DepartMentBean>>());
            toSubscribe(observable, subscriber);
        }
    }

    public void dinning(String accountId, String code, HttpSubscriber<String> subscriber) {
        if(employeeService != null) {
            Observable observable = employeeService.dinning(accountId, code)
                    .map(new HttpResultFunc<String>());
            toSubscribe(observable, subscriber);
        }
    }

    public void eatList(String accountId, int start, int rows, HttpSubscriber<PageBean<EatHistoryBean>> subscriber) {
        if(employeeService != null) {
            Observable observable = employeeService.eatList(accountId, start, rows)
                    .map(new HttpResultFunc<PageBean<EatHistoryBean>>());
            toSubscribe(observable, subscriber);
        }
    }

    public void schdueCar(String accountId, String applyInfoId, String driver, String vehicle, String vehicleNo, String mobile, HttpSubscriber<PageBean<EatHistoryBean>> subscriber) {
        if(employeeService != null) {
            SchduleCarBodyBean schduleCarBodyBean = new SchduleCarBodyBean();
            schduleCarBodyBean.setApplyInfoId(applyInfoId);
            schduleCarBodyBean.setDriver(driver);
            schduleCarBodyBean.setVehicle(vehicle);
            schduleCarBodyBean.setVehicleNo(vehicleNo);
            schduleCarBodyBean.setMobile(mobile);
            Observable observable = employeeService.schdueCar(accountId, schduleCarBodyBean)
                    .map(new HttpResultFunc<PageBean<EatHistoryBean>>());
            toSubscribe(observable, subscriber);
        }
    }

}

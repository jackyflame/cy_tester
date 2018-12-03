package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2017-7-27.
 */

public class SchduleCarBodyBean extends BaseBean{

    private String applyInfoId;
    private String driver;
    private String vehicle;
    private String vehicleNo;
    private String mobile;

    public String getApplyInfoId() {
        return applyInfoId;
    }

    public void setApplyInfoId(String applyInfoId) {
        this.applyInfoId = applyInfoId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

package com.cyztc.app.bean;

/**
 * Created by ywl on 2017-5-21.
 */

public class ApplyAuditorBean extends BaseBean{

    private String position;
    private String id;
    private String auditor;
    private String auditorPhoto;
    private String operateTime;
    private int status;
    private int no;
    private String auditorName;
    private String mobile;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditorPhoto() {
        return auditorPhoto;
    }

    public void setAuditorPhoto(String auditorPhoto) {
        this.auditorPhoto = auditorPhoto;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

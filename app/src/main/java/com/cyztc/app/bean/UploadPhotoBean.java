package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/11/27.
 */

public class UploadPhotoBean extends BaseBean{

    private int extParam;
    private String msg;
    private String status;
    private boolean success;
    private PhotoVpathBean data;

    public int getExtParam() {
        return extParam;
    }

    public void setExtParam(int extParam) {
        this.extParam = extParam;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PhotoVpathBean getData() {
        return data;
    }

    public void setData(PhotoVpathBean data) {
        this.data = data;
    }
}

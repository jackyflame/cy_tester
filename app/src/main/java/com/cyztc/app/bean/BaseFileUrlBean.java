package com.cyztc.app.bean;

/**
 * Created by ywl on 2017/1/18.
 */

public class BaseFileUrlBean extends BaseBean{

    private String data;
    private int extParam;
    private String msg;
    private String optionType;
    private String status;
    private boolean success;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

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

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
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
}

package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2017/1/8.
 */

public class CreateTrainOrderBodyBean extends BaseBean{

    private String accountId;
    private String trainInfoId;
    private int status;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTrainInfoId() {
        return trainInfoId;
    }

    public void setTrainInfoId(String trainInfoId) {
        this.trainInfoId = trainInfoId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

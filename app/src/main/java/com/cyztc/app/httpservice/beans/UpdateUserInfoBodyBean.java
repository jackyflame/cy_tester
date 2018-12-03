package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2017/2/13.
 */

public class UpdateUserInfoBodyBean extends BaseBean{

    private String accountId;
    private String nickName;
    private String picture;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

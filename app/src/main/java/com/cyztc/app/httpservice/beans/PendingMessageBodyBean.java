package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2017-7-23.
 */

public class PendingMessageBodyBean extends BaseBean{

    private String targetId;
    private int type;
    private String remark;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

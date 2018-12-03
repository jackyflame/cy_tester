package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/27.
 */

public class MessageBeanP extends BaseBean{

    private int totalCount;
    private List<MessageBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<MessageBean> getData() {
        return data;
    }

    public void setData(List<MessageBean> data) {
        this.data = data;
    }
}

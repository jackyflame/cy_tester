package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeTopicBeanP extends BaseBean{

    private int totalCount;
    private List<TribeTopicBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TribeTopicBean> getData() {
        return data;
    }

    public void setData(List<TribeTopicBean> data) {
        this.data = data;
    }
}

package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017/1/10.
 */

public class TrainHistoryBeanP extends BaseBean{

    private int totalCount;
    private List<TrainHistoryBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TrainHistoryBean> getData() {
        return data;
    }

    public void setData(List<TrainHistoryBean> data) {
        this.data = data;
    }
}

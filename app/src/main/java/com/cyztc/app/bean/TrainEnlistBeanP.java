package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017/1/8.
 */

public class TrainEnlistBeanP extends BaseBean{

    private int totalCount;
    private List<TrainEnlistBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TrainEnlistBean> getData() {
        return data;
    }

    public void setData(List<TrainEnlistBean> data) {
        this.data = data;
    }
}

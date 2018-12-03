package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeRecommendBeanP {

    private int totalCount;
    private List<TribeRecommendBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TribeRecommendBean> getData() {
        return data;
    }

    public void setData(List<TribeRecommendBean> data) {
        this.data = data;
    }
}

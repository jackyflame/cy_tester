package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017/2/15.
 */

public class GoodsBannerBeanP {

    private int totalCount;
    private List<GoodsBannerBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GoodsBannerBean> getData() {
        return data;
    }

    public void setData(List<GoodsBannerBean> data) {
        this.data = data;
    }
}

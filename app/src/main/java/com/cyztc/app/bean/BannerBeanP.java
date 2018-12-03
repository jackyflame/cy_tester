package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/3.
 */

public class BannerBeanP extends BaseBean{

    private int totalCount;
    private List<BannerBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<BannerBean> getData() {
        return data;
    }

    public void setData(List<BannerBean> data) {
        this.data = data;
    }
}

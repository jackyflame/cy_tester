package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/20.
 */

public class TopResponBeanP extends BaseBean{

    private List<TopResponBean> data;
    private int totalCount;

    public List<TopResponBean> getData() {
        return data;
    }

    public void setData(List<TopResponBean> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}

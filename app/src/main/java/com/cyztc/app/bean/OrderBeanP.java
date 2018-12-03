package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/18.
 */

public class OrderBeanP extends BaseBean{

    private int totalCount;
    private List<OrderBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<OrderBean> getData() {
        return data;
    }

    public void setData(List<OrderBean> data) {
        this.data = data;
    }
}

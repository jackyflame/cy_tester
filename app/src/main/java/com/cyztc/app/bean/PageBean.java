package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017-5-21.
 */

public class PageBean<T> extends BaseBean{

    private int totalCount;
    private List<T> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

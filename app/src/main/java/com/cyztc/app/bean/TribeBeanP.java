package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/20.
 */

public class TribeBeanP extends BaseBean{

    private int totalCount = 0;
    private List<TribeBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TribeBean> getData() {
        return data;
    }

    public void setData(List<TribeBean> data) {
        this.data = data;
    }
}

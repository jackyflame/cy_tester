package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/18.
 */

public class CorseBeanP extends BaseBean{

    private int totalCount;
    private List<CorseBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<CorseBean> getData() {
        return data;
    }

    public void setData(List<CorseBean> data) {
        this.data = data;
    }
}

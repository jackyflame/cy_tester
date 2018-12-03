package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/27.
 */

public class CuiYueCircleBeanP extends BaseBean{

    private int totalCount;
    private List<CuiYueCircleBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<CuiYueCircleBean> getData() {
        return data;
    }

    public void setData(List<CuiYueCircleBean> data) {
        this.data = data;
    }
}

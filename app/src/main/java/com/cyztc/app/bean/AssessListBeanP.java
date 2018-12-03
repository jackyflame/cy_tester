package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/23.
 */

public class AssessListBeanP extends BaseBean{

    private int totalCount;
    private List<AssessListBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<AssessListBean> getData() {
        return data;
    }

    public void setData(List<AssessListBean> data) {
        this.data = data;
    }
}

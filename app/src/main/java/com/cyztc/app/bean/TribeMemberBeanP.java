package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeMemberBeanP extends BaseBean{

    private int totalCount;
    private List<TribeMemberBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TribeMemberBean> getData() {
        return data;
    }

    public void setData(List<TribeMemberBean> data) {
        this.data = data;
    }
}

package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/8.
 */

public class TypeBeanP extends BaseBean{

    private int totalCount;
    private List<TypeBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TypeBean> getData() {
        return data;
    }

    public void setData(List<TypeBean> data) {
        this.data = data;
    }
}

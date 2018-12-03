package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/17.
 */

public class ClassMineBeanP extends BaseBean{

    private int totalCount;
    private List<ClassMineBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ClassMineBean> getData() {
        return data;
    }

    public void setData(List<ClassMineBean> data) {
        this.data = data;
    }
}

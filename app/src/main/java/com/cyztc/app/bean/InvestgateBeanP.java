package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017/2/18.
 */

public class InvestgateBeanP extends BaseBean{

    private int totalCount;
    private List<InvestgateBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<InvestgateBean> getData() {
        return data;
    }

    public void setData(List<InvestgateBean> data) {
        this.data = data;
    }
}

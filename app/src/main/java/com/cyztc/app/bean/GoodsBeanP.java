package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/11.
 */

public class GoodsBeanP extends BaseBean{

    private int totalCount;
    private List<GoodsBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GoodsBean> getData() {
        return data;
    }

    public void setData(List<GoodsBean> data) {
        this.data = data;
    }
}

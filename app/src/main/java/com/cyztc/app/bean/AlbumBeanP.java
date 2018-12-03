package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/30.
 */

public class AlbumBeanP extends BaseBean{

    private int totalCount;
    private List<AlbumBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<AlbumBean> getData() {
        return data;
    }

    public void setData(List<AlbumBean> data) {
        this.data = data;
    }
}

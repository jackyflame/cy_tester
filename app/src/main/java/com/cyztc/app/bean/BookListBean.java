package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/9.
 */

public class BookListBean extends BaseBean{

    private int totalCount;
    private List<BookBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<BookBean> getData() {
        return data;
    }

    public void setData(List<BookBean> data) {
        this.data = data;
    }
}

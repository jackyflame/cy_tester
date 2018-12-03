package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/11/9.
 */

public class VideoListBean extends BaseBean{

    private int totalCount;
    private List<VideoBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VideoBean> getData() {
        return data;
    }

    public void setData(List<VideoBean> data) {
        this.data = data;
    }
}

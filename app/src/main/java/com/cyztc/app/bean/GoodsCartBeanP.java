package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/13.
 */

public class GoodsCartBeanP extends BaseBean{

    private int totalCount;
    private List<GoodsCartBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GoodsCartBean> getData() {
        return data;
    }

    public void setData(List<GoodsCartBean> data) {
        this.data = data;
    }
}

package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/12/30.
 */

public class OrderDetailGoodsBeanP extends BaseBean{

    private int count;
    private OrderDetailGoodsBean goods;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OrderDetailGoodsBean getGoods() {
        return goods;
    }

    public void setGoods(OrderDetailGoodsBean goods) {
        this.goods = goods;
    }
}

package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

import java.util.HashMap;

/**
 * Created by ywl on 2016/12/30.
 */

public class CreateOrderBodyBean extends BaseBean{

    private int type;
    private HashMap<String, Integer> goods;
    private String address;
    private float cost;
    private int count;
    private int payType;
    private String picture;
    private String remark;
    private String subType;
    private String takeTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashMap<String, Integer> getGoods() {
        return goods;
    }

    public void setGoods(HashMap<String, Integer> goods) {
        this.goods = goods;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }
}

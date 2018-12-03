package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/12/11.
 */

public class GoodsBean extends BaseBean{

    private String itemtype;
    private int count;
    private String remark;
    private String serverPerson;
    private int serviceType;
    private String id;
    private String picture;
    private String title;
    private float price;
    private String thumbnail;
    private String subType;
    private int soldCount;
    private String productname;

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServerPerson() {
        return serverPerson;
    }

    public void setServerPerson(String serverPerson) {
        this.serverPerson = serverPerson;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        if(!TextUtils.isEmpty(picture)) {
            return picture.replace("\\", "/");
        }
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getThumbnail() {
        if(!TextUtils.isEmpty(thumbnail)) {
            return thumbnail.replace("\\", "/");
        }
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
}

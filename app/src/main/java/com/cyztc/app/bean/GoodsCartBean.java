package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/12/13.
 */

public class GoodsCartBean extends BaseBean{

    private String id;
    private String title;
    private String thumbnail;
    private float price;
    private int count;
    private String remark;
    private boolean isSelected;

    private int changeCount;

    public int getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(int changeCount) {
        this.changeCount = changeCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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
}

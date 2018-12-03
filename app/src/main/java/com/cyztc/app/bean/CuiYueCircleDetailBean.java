package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/12/27.
 */

public class CuiYueCircleDetailBean extends BaseBean{

    private String content;
    private String createTime;
    private String creator;
    private String creatorPhoto;
    private String id;
    private String picture;
    private int readCount;
    private String remark;
    private int status;
    private String thumbnail;
    private String title;
    private String updateTime;
    private String updator;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorPhoto() {
        if(!TextUtils.isEmpty(creatorPhoto))
            return creatorPhoto.replace("\\", "/");
        return "";
    }

    public void setCreatorPhoto(String creatorPhoto) {
        this.creatorPhoto = creatorPhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        if(!TextUtils.isEmpty(picture))
            return picture.replace("\\", "/");
        return "";
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThumbnail() {
        if(!TextUtils.isEmpty(thumbnail))
            return thumbnail.replace("\\", "/");
        return "";
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }
}

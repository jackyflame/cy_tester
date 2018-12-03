package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/11/17.
 */

public class ClassMineBean extends BaseBean{

    private String picture;
    private String content;
    private String id;
    private String createTime;
    private String title;
    private String thumbnail;
    private String updateTime;
    private String remark;
    private int readCount;
    private String updator;
    private String trainingInfoId;
    private String creator;
    private int type;
    private String photo;
    private String creatorName;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getPhoto() {
        if(TextUtils.isEmpty(photo))
            return "";
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPicture() {
        if(!TextUtils.isEmpty(picture)) {
            return picture.replace("\\", "/");
        }
        return "";
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
        return "";
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getTrainingInfoId() {
        return trainingInfoId;
    }

    public void setTrainingInfoId(String trainingInfoId) {
        this.trainingInfoId = trainingInfoId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

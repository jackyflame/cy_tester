package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2017/1/21.
 */

public class ClassPublishTopBodyBean extends BaseBean{

    private String content;
    private String creator;
    private String picture;
    private int readCount;
    private String remark;
    private String thumbnail;
    private String title;
    private String trainingInfoId;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPicture() {
        return picture;
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

    public String getThumbnail() {
        return thumbnail;
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

    public String getTrainingInfoId() {
        return trainingInfoId;
    }

    public void setTrainingInfoId(String trainingInfoId) {
        this.trainingInfoId = trainingInfoId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

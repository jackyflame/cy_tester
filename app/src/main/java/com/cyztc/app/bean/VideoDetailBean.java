package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/11/16.
 */

public class VideoDetailBean extends BaseBean{

    private String id;
    private String file;
    private String picture;
    private String remark;
    private int readCount;
    private int serviceType;
    private String subType;
    private String title;
    private int wordCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile() {
        return file.replace("\\", "/");
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPicture() {
        return picture.replace("\\", "/");
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

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}

package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/11/9.
 */

public class VideoBean extends BaseBean{

    private String id;
    private String picture;
    private String title;
    private int servicetype;
    private String remark;
    private String file;
    private int readCount;
    private String subtype;
    private int wordCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
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

    public int getServicetype() {
        return servicetype;
    }

    public void setServicetype(int servicetype) {
        this.servicetype = servicetype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFile() {
        if(TextUtils.isEmpty(file))
            return "";
        return file.replace("\\", "/");
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}

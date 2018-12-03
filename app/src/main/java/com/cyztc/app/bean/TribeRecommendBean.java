package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeRecommendBean extends BaseBean{

    private String id;
    private String createTime;
    private String thumbnail;
    private String remark;
    private int status;
    private String name;
    private int topicNum;
    private String coverPicture;
    private int memberNum;
    private String creator;

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

    public String getThumbnail() {
        if(TextUtils.isEmpty(thumbnail))
            return "";
        return thumbnail.replace("\\", "/");
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopicNum() {
        return topicNum;
    }

    public void setTopicNum(int topicNum) {
        this.topicNum = topicNum;
    }

    public String getCoverPicture() {
        return coverPicture.replace("\\", "/");
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}

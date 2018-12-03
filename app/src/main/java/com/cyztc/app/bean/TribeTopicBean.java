package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeTopicBean extends BaseBean{

    private String content;
    private String picture;
    private String createTime;
    private String title;
    private int replyCount;
    private int likeCount;
    private String studentId;
    private int readCount;
    private String topicId;
    private String studentName;
    private String photo;

    public String getContent() {
        if(TextUtils.isEmpty(content))
            return "";
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhoto() {
        if(!TextUtils.isEmpty(photo))
        return photo.replace("\\", "/");
        return "";
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

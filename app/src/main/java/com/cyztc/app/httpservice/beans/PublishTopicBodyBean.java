package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2016/12/26.
 */

public class PublishTopicBodyBean extends BaseBean{

    private String content;
    private String picture;
    private String studentId;
    private String title;
    private String tribleId;

    public String getContent() {
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTribleId() {
        return tribleId;
    }

    public void setTribleId(String tribleId) {
        this.tribleId = tribleId;
    }
}

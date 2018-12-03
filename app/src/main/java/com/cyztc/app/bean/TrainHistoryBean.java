package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2017/1/10.
 */

public class TrainHistoryBean extends BaseBean{

    private String id;
    private String createTime;
    private String startDate;
    private String teacherPhoto;
    private String endDate;
    private String teacherName;
    private String trainName;
    private float cost;
    private String teacherId;
    private String trainPicture;

    public String getTrainPicture() {
        if(!TextUtils.isEmpty(trainPicture)) {
            return trainPicture.replace("\\", "/");
        }
        return trainPicture;
    }

    public void setTrainPicture(String trainPicture) {
        this.trainPicture = trainPicture;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTeacherPhoto() {
        if(TextUtils.isEmpty(teacherPhoto))
            return "";
        return teacherPhoto;
    }

    public void setTeacherPhoto(String teacherPhoto) {
        this.teacherPhoto = teacherPhoto;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}

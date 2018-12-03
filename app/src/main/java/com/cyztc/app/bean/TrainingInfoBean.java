package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/11/17.
 */

public class TrainingInfoBean extends BaseBean{

    private String trainingInfoName;
    private String startDate;
    private float trainingCost;
    private String remark;
    private String teacherPhoto;
    private String trainingInfoId;
    private String endDate;
    private int isCheckIn;
    private int isAmount;
    private String teacherName;
    private String teacherId;

    public String getTrainingInfoName() {
        return trainingInfoName;
    }

    public void setTrainingInfoName(String trainingInfoName) {
        this.trainingInfoName = trainingInfoName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public float getTrainingCost() {
        return trainingCost;
    }

    public void setTrainingCost(float trainingCost) {
        this.trainingCost = trainingCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTeacherPhoto() {
        return teacherPhoto;
    }

    public void setTeacherPhoto(String teacherPhoto) {
        this.teacherPhoto = teacherPhoto;
    }

    public String getTrainingInfoId() {
        return trainingInfoId;
    }

    public void setTrainingInfoId(String trainingInfoId) {
        this.trainingInfoId = trainingInfoId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getIsCheckIn() {
        return isCheckIn;
    }

    public void setIsCheckIn(int isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public int getIsAmount() {
        return isAmount;
    }

    public void setIsAmount(int isAmount) {
        this.isAmount = isAmount;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}

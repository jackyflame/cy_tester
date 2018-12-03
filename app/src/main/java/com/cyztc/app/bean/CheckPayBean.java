package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/12/19.
 */

public class CheckPayBean extends BaseBean{

    private String trainingInfoName;
    private String startDate;
    private float trainingCost;
    private String phoneNum;
    private String idCard;
    private String studentName;
    private String endDate;
    private String trainingInfoId;
    private int isAmount;

    public String getTrainingInfoId() {
        return trainingInfoId;
    }

    public void setTrainingInfoId(String trainingInfoId) {
        this.trainingInfoId = trainingInfoId;
    }

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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getIsAmount() {
        return isAmount;
    }

    public void setIsAmount(int isAmount) {
        this.isAmount = isAmount;
    }
}

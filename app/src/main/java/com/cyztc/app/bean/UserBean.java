package com.cyztc.app.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by ywl on 2016/11/7.
 */

public class UserBean extends BaseBean{

    private List<DoorBean> doors;
    private String position;
    private String trainingInfoName;
    private String trainingInfoId;
    private String idCard;
    private String phone;
    private int sex;
    private String accountId;
    private String diningRoom;
    private String bedRoom;
    private String workUnit;
    private String photo;
    private String departMent;
    private String studentId;
    private String studentName;
    private String roomCode;
    private String account;
    private int isAmount;
    private String lockTime;
    private int dinnerCount;
    private String token;

    public int getDinnerCount() {
        return dinnerCount;
    }

    public void setDinnerCount(int dinnerCount) {
        this.dinnerCount = dinnerCount;
    }

    public List<DoorBean> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorBean> doors) {
        this.doors = doors;
    }

    public String getTrainingInfoId() {
        return trainingInfoId;
    }

    public void setTrainingInfoId(String trainingInfoId) {
        this.trainingInfoId = trainingInfoId;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public int getIsAmount() {
        return isAmount;
    }

    public void setIsAmount(int isAmount) {
        this.isAmount = isAmount;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTrainingInfoName() {
        return trainingInfoName;
    }

    public void setTrainingInfoName(String trainingInfoName) {
        this.trainingInfoName = trainingInfoName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDiningRoom() {
        return diningRoom;
    }

    public void setDiningRoom(String diningRoom) {
        this.diningRoom = diningRoom;
    }

    public String getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(String bedRoom) {
        this.bedRoom = bedRoom;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getPhoto() {
        if(!TextUtils.isEmpty(photo)) {
            return photo.replace("\\", "/");
        }
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDepartMent() {
        return departMent;
    }

    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

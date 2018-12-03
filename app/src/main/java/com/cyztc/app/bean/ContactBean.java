package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/12/20.
 */

public class ContactBean extends BaseBean{

    private int type;
    private String pinyin;
    private String position;
    private String phoneNum;
    private String bedRoom;
    private String studentName;
    private String workUnit;
    private String roomPhoneNum;
    private String photo;
    private String trainInfoName;
    private String roomCode;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getTrainInfoName() {
        return trainInfoName;
    }

    public void setTrainInfoName(String trainInfoName) {
        this.trainInfoName = trainInfoName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(String bedRoom) {
        this.bedRoom = bedRoom;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getRoomPhoneNum() {
        return roomPhoneNum;
    }

    public void setRoomPhoneNum(String roomPhoneNum) {
        this.roomPhoneNum = roomPhoneNum;
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
}

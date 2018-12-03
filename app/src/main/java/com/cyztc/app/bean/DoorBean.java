package com.cyztc.app.bean;

/**
 * Created by ywl on 2017-7-30.
 */

public class DoorBean extends BaseBean{

    private String doorCode;
    private String doorName;
    private int doorType;


    public String getDoorCode() {
        return doorCode;
    }

    public void setDoorCode(String doorCode) {
        this.doorCode = doorCode;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public int getDoorType() {
        return doorType;
    }

    public void setDoorType(int doorType) {
        this.doorType = doorType;
    }
}

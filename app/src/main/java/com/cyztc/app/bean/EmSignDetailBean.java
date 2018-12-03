package com.cyztc.app.bean;

/**
 * Created by ywl on 2017-6-4.
 */

public class EmSignDetailBean extends BaseBean{

    private String workDate;
    private String offTime;
    private String inTime;
    private boolean isClockOff;
    private boolean isClockIn;

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public boolean isClockOff() {
        return isClockOff;
    }

    public void setClockOff(boolean clockOff) {
        isClockOff = clockOff;
    }

    public boolean isClockIn() {
        return isClockIn;
    }

    public void setClockIn(boolean clockIn) {
        isClockIn = clockIn;
    }
}

package com.cyztc.app.bean;

/**
 * Created by ywl on 2017-6-4.
 */

public class EmSignBean extends BaseBean{

    private String workDate;
    private String offTime;
    private String inTime;
    private int isClockOff;
    private int isClockIn;
    private String week;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

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

    public int isClockOff() {
        return isClockOff;
    }

    public void setClockOff(int clockOff) {
        isClockOff = clockOff;
    }

    public int isClockIn() {
        return isClockIn;
    }

    public void setClockIn(int clockIn) {
        isClockIn = clockIn;
    }
}

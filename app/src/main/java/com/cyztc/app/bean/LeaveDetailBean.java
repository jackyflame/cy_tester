package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017-5-21.
 */

public class LeaveDetailBean extends BaseBean{

    private String startAddress;
    private String createTime;
    private int count;
    private String remark;
    private int status;
    private boolean isMyDuty;
    private int type;
    private String endTime;
    private List<ApplyAuditorBean> auditors;
    private String creator;
    private String id;
    private String content;
    private String startTime;
    private int subType;
    private String endAddress;
    private int userType;
    private String applyMan;

    public String getApplyMan() {
        return applyMan;
    }

    public void setApplyMan(String applyMan) {
        this.applyMan = applyMan;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isMyDuty() {
        return isMyDuty;
    }

    public void setMyDuty(boolean myDuty) {
        isMyDuty = myDuty;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ApplyAuditorBean> getAuditors() {
        return auditors;
    }

    public void setAuditors(List<ApplyAuditorBean> auditors) {
        this.auditors = auditors;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
}

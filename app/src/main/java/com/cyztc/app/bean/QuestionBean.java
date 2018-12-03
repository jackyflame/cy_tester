package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2016/12/26.
 */

public class QuestionBean extends BaseBean{

    private String id;
    private boolean multi;
    private int no;
    private List<QuestionOptionsBean> options;
    private String remark;
    private String subType1;
    private String subType2;
    private String subType3;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public List<QuestionOptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOptionsBean> options) {
        this.options = options;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubType1() {
        return subType1;
    }

    public void setSubType1(String subType1) {
        this.subType1 = subType1;
    }

    public String getSubType2() {
        return subType2;
    }

    public void setSubType2(String subType2) {
        this.subType2 = subType2;
    }

    public String getSubType3() {
        return subType3;
    }

    public void setSubType3(String subType3) {
        this.subType3 = subType3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

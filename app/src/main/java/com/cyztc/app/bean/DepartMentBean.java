package com.cyztc.app.bean;

import java.util.List;

/**
 * Created by ywl on 2017-5-26.
 */

public class DepartMentBean extends BaseBean{
    private String deptId;
    private String deptName;
    private List<DepartMentSubBean> employee;
    private List<DepartMentBean> subDept;

    public List<DepartMentBean> getSubDept() {
        return subDept;
    }

    public void setSubDept(List<DepartMentBean> subDept) {
        this.subDept = subDept;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<DepartMentSubBean> getEmployee() {
        return employee;
    }

    public void setEmployee(List<DepartMentSubBean> employee) {
        this.employee = employee;
    }
}

package com.cyztc.app.httpservice.beans;

import com.cyztc.app.bean.BaseBean;

/**
 * Created by ywl on 2017/1/17.
 */

public class LendBookBodyBean extends BaseBean{

    private String studentId;
    private String bookId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}

package com.cyztc.app.bean;

import android.text.TextUtils;

/**
 * Created by ywl on 2016/11/28.
 */

public class TribeMemberBean extends BaseBean{

    private String studentName;
    private String id;
    private String photo;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        if(!TextUtils.isEmpty(photo)) {
            return photo.replace("\\", "/");
        }
        return "";
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

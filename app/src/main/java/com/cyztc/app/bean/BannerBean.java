package com.cyztc.app.bean;

import android.text.TextUtils;

import com.cyztc.app.httpservice.service.HttpMethod;

/**
 * Created by ywl on 2016/11/3.
 */

public class BannerBean extends BaseBean{

    private String picture;
    private String id;
    private String title;

    public String getPicture() {
        if(TextUtils.isEmpty(picture))
            return  "";
        return HttpMethod.IMG_URL + picture.replace("\\", "/");
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

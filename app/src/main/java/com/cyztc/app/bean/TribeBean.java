package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/11/20.
 */

public class TribeBean extends BaseBean{

    private int type = 0;
    private String id;
    private String name;
    private String thumbnail;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail.replace("\\", "/");
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

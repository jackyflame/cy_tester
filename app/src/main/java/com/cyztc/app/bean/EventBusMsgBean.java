package com.cyztc.app.bean;


import java.util.Objects;

/**
 * Created by ywl on 2017/2/18.
 */

public class EventBusMsgBean {

    private int type;
    private Object object;

    public EventBusMsgBean(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

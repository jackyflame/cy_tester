package com.cyztc.app.bean;

/**
 * Created by ywl on 2017-4-29.
 */

public class EventBusBean extends BaseBean{

    private int type; //1:重置密码
    private Object object;

    public EventBusBean(int type, Object object) {
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

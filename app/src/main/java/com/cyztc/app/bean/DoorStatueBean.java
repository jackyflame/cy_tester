package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/12/22.
 */

public class DoorStatueBean extends BaseBean{

    private boolean isOpen;
    private String name;
    private String code;
    private int type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

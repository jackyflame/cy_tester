package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/11/15.
 */

public class HomeItemBean extends BaseBean{

    private String icon;
    private String name;
    private boolean isSelected;
    private boolean showDot;

    public boolean isShowDot() {
        return showDot;
    }

    public void setShowDot(boolean showDot) {
        this.showDot = showDot;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.cyztc.app.bean;

/**
 * Created by ywl on 2016/12/19.
 */

public class CheckPayedBean extends BaseBean{

    private String tradeNO;
    private String productDescription;
    private String payType;
    private int isAmount;
    private String payTime;
    private float cost;
    private String productName;

    public String getTradeNO() {
        return tradeNO;
    }

    public void setTradeNO(String tradeNO) {
        this.tradeNO = tradeNO;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getIsAmount() {
        return isAmount;
    }

    public void setIsAmount(int isAmount) {
        this.isAmount = isAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

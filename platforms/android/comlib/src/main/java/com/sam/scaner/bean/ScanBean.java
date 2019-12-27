package com.sam.scaner.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class ScanBean implements Serializable {

    /**
     * orderId : 22
     * expressOwnerTel : 13594516029
     * deliveryPhone : 13594516029
     * nickName : 王五
     * company : 圆通
     */

    private String orderId;
    private String expressOwnerTel;
    private String deliveryPhone;
    private String nickName;
    private String company;

    public ScanBean(String orderId, String expressOwnerTel, String deliveryPhone, String nickName, String company) {
        this.orderId = orderId;
        this.expressOwnerTel = expressOwnerTel;
        this.deliveryPhone = deliveryPhone;
        this.nickName = nickName;
        this.company = company;
    }

    public ScanBean(){}
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExpressOwnerTel() {
        return expressOwnerTel;
    }

    public void setExpressOwnerTel(String expressOwnerTel) {
        this.expressOwnerTel = expressOwnerTel;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

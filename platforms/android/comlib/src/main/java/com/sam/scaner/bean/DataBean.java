package com.sam.scaner.bean;

import java.io.Serializable;

public  class DataBean implements Serializable {
    /**
     * id : 82d99cad29374d388a640296671ab592
     * userId : 18716271713
     * orderId : 6
     * expressOwnerTel : 13594516029
     * orderStatus : 2
     * handleTime : 2019-08-06 15:40:25
     * nickName : 王四
     * ckTime : 2019-08-09 15:54:23
     * expressOwnerTelFour0 : 6029
     * smsStatus : 1
     * company : 圆通
     * shelves : 08060053
     * agentId : 13594516029
     * deliveryPhone : 13594516029
     * day : 72
     * agentAddress : 重庆
     * userName : 雷
     * orderRemark : 家人代取
     * orderIntegration : 2ea421dd-8c97-43f9-b732-60db971365de
     * userId 快递员电话 userName 快递员
     */

    private String id;
    private String userId;
    private String orderId;
    private String expressOwnerTel;
    private String orderStatus;
    private String handleTime;
    private String nickName;
    private String ckTime;
    private String expressOwnerTelFour0;
    private String smsStatus;
    private String company;
    private String shelves;
    private String agentId;
    private String deliveryPhone;
    private int day;
    private String agentAddress;
    private String userName;
    private String orderRemark;
    private String orderIntegration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCkTime() {
        return ckTime;
    }

    public void setCkTime(String ckTime) {
        this.ckTime = ckTime;
    }

    public String getExpressOwnerTelFour0() {
        return expressOwnerTelFour0;
    }

    public void setExpressOwnerTelFour0(String expressOwnerTelFour0) {
        this.expressOwnerTelFour0 = expressOwnerTelFour0;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getShelves() {
        return shelves;
    }

    public void setShelves(String shelves) {
        this.shelves = shelves;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderIntegration() {
        return orderIntegration;
    }

    public void setOrderIntegration(String orderIntegration) {
        this.orderIntegration = orderIntegration;
    }
}
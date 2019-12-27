package com.sam.scaner.bean;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class ExBean implements Serializable {

    /**
     * LogisticCode : 13594516029
     * Shippers : [{"ShipperName":"如风达","ShipperCode":"RFD"}]
     * EBusinessID : 1552450
     * Code : 100
     * Success : true
     */

    private String LogisticCode;
    private String EBusinessID;
    private String Code;
    private boolean Success;
    private ArrayList<ShippersBean> Shippers;

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public List<ShippersBean> getShippers() {
        return Shippers;
    }

    public void setShippers(ArrayList<ShippersBean> Shippers) {
        this.Shippers = Shippers;
    }


}

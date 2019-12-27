package com.sam.scaner.bean;


import java.io.Serializable;

public  class ShippersBean{
    /**
     * ShipperName : 如风达
     * ShipperCode : RFD
     */

    private String ShipperName;
    private String ShipperCode;

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String ShipperName) {
        this.ShipperName = ShipperName;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }
}

package com.sam.scaner.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class OrderScanerDataBean implements Serializable {

    /**
     * code : 200
     * data : {"id":"96a8e910e5dd4546adac5d46e03acd06","userId":"18716271713","orderId":"17","expressOwnerTel":"13594516029","orderStatus":"1","handleTime":"2019-08-09 09:59:50","nickName":"王五","ckTime":null,"expressOwnerTelFour":"6029","smsStatus":"2","company":"圆通","shelves":null,"agentId":null,"deliveryPhone":"13594516029","day":0,"agentAddress":null,"userName":"李四","orderRemark":null,"orderIntegration":"15ea5760-135f-4be8-b210-7828bddf2d61"}
     * size : 2
     * message : 08090056
     */

    private int code;
    private DataBean data;
    private int size;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

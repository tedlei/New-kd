package com.sam.scaner.bean;

import java.io.Serializable;
import java.util.List;

public class ScanerSearchBean implements Serializable {

    /**
     * daycount : 0
     * code : 200
     * data : [{"id":"82d99cad29374d388a640296671ab592","userId":"18716271713","orderId":"6","expressOwnerTel":"13594516029","orderStatus":"2","handleTime":"2019-08-06 15:40:25","nickName":"王四","ckTime":"2019-08-09 15:54:23","expressOwnerTelFour0":"6029","smsStatus":"1","company":"圆通","shelves":"08060053","agentId":"13594516029","deliveryPhone":"13594516029","day":72,"agentAddress":"重庆","userName":"雷","orderRemark":"家人代取","orderIntegration":"2ea421dd-8c97-43f9-b732-60db971365de"}]
     * outbound : 0
     * message : 操作成功
     * repertory : 0
     * day : 72在库小时数
     */

    private int daycount;
    private int code;
    private int outbound;
    private String message;
    private int repertory;
    private String day;
    private List<DataBean> data;

    public int getDaycount() {
        return daycount;
    }

    public void setDaycount(int daycount) {
        this.daycount = daycount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getOutbound() {
        return outbound;
    }

    public void setOutbound(int outbound) {
        this.outbound = outbound;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRepertory() {
        return repertory;
    }

    public void setRepertory(int repertory) {
        this.repertory = repertory;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

}

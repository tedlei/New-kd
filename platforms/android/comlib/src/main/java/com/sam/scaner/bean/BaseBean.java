package com.sam.scaner.bean;

import java.io.Serializable;
import java.util.List;

public class BaseBean implements Serializable {

    /**
     * daycount : 0
     * code : 200
     * data : [{"id":"864a042713f74e698ed2baf2f5ee8005","userId":null,"orderId":"1","expressOwnerTel":"13594516029","orderStatus":"3","handleTime":"2019-08-14 14:54:50","nickName":"王五","ckTime":null,"expressOwnerTelFour":"6029","smsStatus":"2","company":"圆通","shelves":"08140030","agentId":"13594516029","deliveryPhone":null,"day":0,"agentAddress":"观音桥代办点","userName":null,"orderRemark":null,"orderIntegration":null},{"id":"864a04271f74e698ed2baf2f5ee8005","userId":null,"orderId":"2","expressOwnerTel":"13983331251","orderStatus":"3","handleTime":"2019-08-14 14:54:50","nickName":"王五","ckTime":null,"expressOwnerTelFour":"6029","smsStatus":"2","company":"圆通","shelves":"08140031","agentId":"13594516029","deliveryPhone":null,"day":0,"agentAddress":"观音桥代办点","userName":null,"orderRemark":null,"orderIntegration":null}]
     * message : 操作成功
     */

    private int daycount;
    private int code;
    private String message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

}

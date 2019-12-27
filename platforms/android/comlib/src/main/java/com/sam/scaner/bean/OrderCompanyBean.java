package com.sam.scaner.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class OrderCompanyBean  implements Serializable {

    /**
     * code : 200
     * data :
     * message : 操作成功
     */

    private int code;
    private String data;
    private boolean isok;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public boolean isIsok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

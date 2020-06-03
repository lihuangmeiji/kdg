package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

/**
 * 存餐推送
 */
public class OpenBoxBean implements Serializable {

    private String code,guiNo,msg,type,boxNo,serialNo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGuiNo() {
        return guiNo;
    }

    public void setGuiNo(String guiNo) {
        this.guiNo = guiNo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}

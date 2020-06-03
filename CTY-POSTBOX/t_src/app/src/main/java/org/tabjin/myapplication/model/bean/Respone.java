package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

/**
 * Created by hspcadmin on 2018/9/30.
 */

public class Respone<T> implements Serializable{

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

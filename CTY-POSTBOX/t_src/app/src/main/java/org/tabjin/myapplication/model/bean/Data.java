package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

public class Data<T> implements Serializable {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

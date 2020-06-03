package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

/**
 * Created by hspcadmin on 2018/9/30.
 */

public class BoxBean<T> implements Serializable {

    private String gui_no,gui_name,gui_lat,gui_status,location,gui_lng;
    private T box_list;

    public String getGui_no() {
        return gui_no;
    }

    public void setGui_no(String gui_no) {
        this.gui_no = gui_no;
    }

    public String getGui_name() {
        return gui_name;
    }

    public void setGui_name(String gui_name) {
        this.gui_name = gui_name;
    }

    public String getGui_lat() {
        return gui_lat;
    }

    public void setGui_lat(String gui_lat) {
        this.gui_lat = gui_lat;
    }

    public String getGui_status() {
        return gui_status;
    }

    public void setGui_status(String gui_status) {
        this.gui_status = gui_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGui_lng() {
        return gui_lng;
    }

    public void setGui_lng(String gui_lng) {
        this.gui_lng = gui_lng;
    }

    public T getBox_list() {
        return box_list;
    }

    public void setBox_list(T box_list) {
        this.box_list = box_list;
    }
}

package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

public class Order implements Serializable {


    private String gui_no;
    private String taked_time;
    private String gui_name;
    private String postman_id;
    private String package_status;
    private String err_msg;
    private String err_code;
    private String stored_time;
    private String location;
    private String box_no;
    private String package_id;

    public String getGui_no() {
        return gui_no;
    }

    public void setGui_no(String gui_no) {
        this.gui_no = gui_no;
    }

    public String getTaked_time() {
        return taked_time;
    }

    public void setTaked_time(String taked_time) {
        this.taked_time = taked_time;
    }

    public String getGui_name() {
        return gui_name;
    }

    public void setGui_name(String gui_name) {
        this.gui_name = gui_name;
    }

    public String getPostman_id() {
        return postman_id;
    }

    public void setPostman_id(String postman_id) {
        this.postman_id = postman_id;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getStored_time() {
        return stored_time;
    }

    public void setStored_time(String stored_time) {
        this.stored_time = stored_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBox_no() {
        return box_no;
    }

    public void setBox_no(String box_no) {
        this.box_no = box_no;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }
}

package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

/**
 * Created by hspcadmin on 2018/9/30.
 */

public class GuiBean implements Serializable{

    private String gui_no,server_time,gui_name,location,server_url;

    public String getGui_no() {
        return gui_no;
    }

    public void setGui_no(String gui_no) {
        this.gui_no = gui_no;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getGui_name() {
        return gui_name;
    }

    public void setGui_name(String gui_name) {
        this.gui_name = gui_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServer_url() {
        return server_url;
    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }

    private  String deviceId;

    private  String manufacturer;

    private  int  number;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

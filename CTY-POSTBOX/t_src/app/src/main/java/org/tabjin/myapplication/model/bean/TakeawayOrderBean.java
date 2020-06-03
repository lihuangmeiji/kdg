package org.tabjin.myapplication.model.bean;

import com.google.gson.annotations.SerializedName;

public class TakeawayOrderBean {

    /**
     * package_no : 157571019041547268330280
     * box_no : C22
     * open_box_key : C221111
     */

    private int Id;
    private String packageNo;
    private String boxNo;
    private String openBoxKey;
    private String box_state;
    private String takeoutAt;

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getOpenBoxKey() {
        return openBoxKey;
    }

    public void setOpenBoxKey(String openBoxKey) {
        this.openBoxKey = openBoxKey;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBox_state() {
        return box_state;
    }

    public void setBox_state(String box_state) {
        this.box_state = box_state;
    }

    public String getTakeoutAt() {
        return takeoutAt;
    }

    public void setTakeoutAt(String takeoutAt) {
        this.takeoutAt = takeoutAt;
    }
}

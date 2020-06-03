package org.tabjin.myapplication.model.bean;

public class TakeawayOrderUpdateBean {
    private String packageNo;
    private String takeoutAt;

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getTakeoutAt() {

        return takeoutAt;
    }

    public void setTakeoutAt(String takeoutAt) {
        this.takeoutAt = takeoutAt;
    }
}

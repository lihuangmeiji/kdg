package org.tabjin.myapplication.model.bean;

import com.google.gson.annotations.SerializedName;

public class OrderAdminBean {

    /**
     * id : 5459
     * postmanMobile : 15857191954
     * customerMobile : 2536
     * tradeSeq : 182575590581546865119399
     * packageNo : 182575590581546865119842
     * guiNo : 310016001
     * boxNo : B11
     * openBoxKey : B112536
     * openBoxClient : CA
     * archive : 0
     * storeinAt : 2019-01-07 20:45:35
     * takeoutAt : null
     * takeoutBy : Delay
     * status : 1
     * location : null
     */

    @SerializedName("id")
    private int id;
    @SerializedName("postmanMobile")
    private String postmanMobile;
    @SerializedName("customerMobile")
    private String customerMobile;
    @SerializedName("tradeSeq")
    private String tradeSeq;
    @SerializedName("packageNo")
    private String packageNo;
    @SerializedName("guiNo")
    private String guiNo;
    @SerializedName("boxNo")
    private String boxNo;
    @SerializedName("openBoxKey")
    private String openBoxKey;
    @SerializedName("openBoxClient")
    private String openBoxClient;
    @SerializedName("archive")
    private int archive;
    @SerializedName("storeinAt")
    private String storeinAt;
    @SerializedName("takeoutAt")
    private Object takeoutAt;
    @SerializedName("takeoutBy")
    private String takeoutBy;
    @SerializedName("status")
    private int status;
    @SerializedName("location")
    private Object location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostmanMobile() {
        return postmanMobile;
    }

    public void setPostmanMobile(String postmanMobile) {
        this.postmanMobile = postmanMobile;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getTradeSeq() {
        return tradeSeq;
    }

    public void setTradeSeq(String tradeSeq) {
        this.tradeSeq = tradeSeq;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getGuiNo() {
        return guiNo;
    }

    public void setGuiNo(String guiNo) {
        this.guiNo = guiNo;
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

    public String getOpenBoxClient() {
        return openBoxClient;
    }

    public void setOpenBoxClient(String openBoxClient) {
        this.openBoxClient = openBoxClient;
    }

    public int getArchive() {
        return archive;
    }

    public void setArchive(int archive) {
        this.archive = archive;
    }

    public String getStoreinAt() {
        return storeinAt;
    }

    public void setStoreinAt(String storeinAt) {
        this.storeinAt = storeinAt;
    }

    public Object getTakeoutAt() {
        return takeoutAt;
    }

    public void setTakeoutAt(Object takeoutAt) {
        this.takeoutAt = takeoutAt;
    }

    public String getTakeoutBy() {
        return takeoutBy;
    }

    public void setTakeoutBy(String takeoutBy) {
        this.takeoutBy = takeoutBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }
}

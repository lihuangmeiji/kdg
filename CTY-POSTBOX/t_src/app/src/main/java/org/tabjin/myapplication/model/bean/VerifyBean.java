package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

/**
 * 取件验证接口返回得Data
 */
public class VerifyBean implements Serializable {

    private String customer_mobile,box_no,package_id,overpay_flag,expired_amt;


    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
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

    public String getOverpay_flag() {
        return overpay_flag;
    }

    public void setOverpay_flag(String overpay_flag) {
        this.overpay_flag = overpay_flag;
    }

    public String getExpired_amt() {
        return expired_amt;
    }

    public void setExpired_amt(String expired_amt) {
        this.expired_amt = expired_amt;
    }
}

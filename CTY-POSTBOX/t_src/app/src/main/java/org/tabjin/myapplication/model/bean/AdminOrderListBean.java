package org.tabjin.myapplication.model.bean;

import java.util.List;

public class AdminOrderListBean {
    private List<OrderAdminBean> list;

    public List<OrderAdminBean> getList() {
        return list;
    }

    public void setList(List<OrderAdminBean> list) {
        this.list = list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;
}

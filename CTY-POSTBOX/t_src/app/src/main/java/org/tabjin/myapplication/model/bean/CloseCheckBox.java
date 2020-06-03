package org.tabjin.myapplication.model.bean;

public class CloseCheckBox {
    byte boardId; //箱子编号
    byte boxId;//格子编号
    int second;//已经等待了多久的时间
    int status;//0=IDLE 当前格子未处于查询中，1=BUSY 当前格子出去查询中
    String serialNo;//编号
}

package org.tabjin.myapplication.model;

import android.os.Environment;



public class Server {

    public static final String registerFilePath = Environment.getExternalStoragePublicDirectory("") + "/linxian/";
    public static final String registerFileName = "linxianRegister";




    //服务期地址  http://postapi.lxstation.com
    public static final String serverUrl = "http://t-postapi.lxstation.com";


    //服务期地址
   // public static final String serverUrl = "http://postapi.lxstation.com";





    //public static final String serverUrl = "http://postapi.lxstation.com";

    //柜子注册
    public static final String cupRegister = "/elocker/register";

    //柜子注册takeawayOrder
    public static final String cupRegister1 = "/ipy/register";

    //柜体信息查询
    public static final String cupInfoQuery = "/elocker/info/query";

    //用户取件验证
    public static final String customerVerify = "/elocker/customer/verify";

    //用户订单查询
    public static final String customerQuery = "/elocker/customer/query";

    //格口信息查询
    public static final String boxQuery = "/elocker/box/query";

    //取件完成
    public static final String takeout = "/elocker/takeout";

    //取件完成
    public static final String takeoutGui = "/package/gui/takeout";

    //物品检测
    public static final String goodsInspection = "/package/gui/storein";

    //柜子未关门
    public static final String cupNoClose = "/package/gui/noClose";

    //柜子未关门
    public static final String cupTakeoutNoClose = "/package/h5/takeout";

    //错误日志收集
    public static final String crashCollect = "/uploadCrashMsg";

    //数据下载
    public static final String takeawayOrder = "/box/manage/download";

    //更新数据
    public static final String updateTakeawayOrder = "/box/manage/upload";

    //管理登录
    public static final String grantPolling = "/box/manage/grantPolling";


    //订单获取
    public static final String PACKAGELIST = "/admin/getPackageList";

    //配置信息获取
    public static final String CONFIGUREINFO = "/configure/get";

}

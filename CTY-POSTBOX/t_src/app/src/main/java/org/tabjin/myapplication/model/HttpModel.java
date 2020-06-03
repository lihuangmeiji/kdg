package org.tabjin.myapplication.model;

import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.model.bean.Order;
import org.tabjin.myapplication.model.bean.VerifyBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hspcadmin on 2018/9/30.
 */

public class HttpModel {

    public static final String secret = "b0b12569e3c4219c79717e2f2b09b7b5";

    public static final String appKey = "dcdzsoft_2018";

    public static final String signMethod = "md5";

    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("appKey", appKey);
        map.put("format", "JSON");
        map.put("signMethod", signMethod);
    }

    private static Map<String, String> methodMap = new HashMap<>();

    static {
        methodMap.put(Server.cupRegister, "dcdz.app.elocker.register");
        methodMap.put(Server.boxQuery, "dcdz.app.elocker.box.query");
        methodMap.put(Server.customerQuery, "dcdz.app.customer.query");
        methodMap.put(Server.customerVerify, "dcdz.app.customer.verify");
        methodMap.put(Server.cupInfoQuery, "dcdz.app.elocker.info.query");
        methodMap.put(Server.goodsInspection, "dcdz.app.elocker.goodsInspection");
        methodMap.put(Server.cupNoClose, "dcdz.app.elocker.cupNoClose");
        methodMap.put(Server.cupTakeoutNoClose, "dcdz.app.elocker.cupTakeoutNoClose");
    }

    /**
     * 柜子注册dcdz
     * @param params
     * @param callBack
     */
    public static void register(Map<String,String> params,HttpUtil.ResponeCallBack callBack){
        doPost(params,Server.cupRegister,callBack);
    }

    /**
     * 柜子注册py
     * @param params
     * @param callBack
     */
    public static void register1(Map<String,String> params,HttpUtil.ResponeCallBack callBack){
        doPost(params,Server.cupRegister1,callBack);
    }

    /**
     * 格口信息查询
     * @param boxNo
     * @param callBack
     */
    public static void boxQuery(String boxNo,HttpUtil.ResponeCallBack callBack) {
        if(Constant.guiBean == null){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("boxNo",boxNo);
        doPost(map,Server.boxQuery,callBack);
    }

    /**
     * 柜体信息查询
     * @param callBack
     */
    public static void infoQuery(HttpUtil.ResponeCallBack callBack) {
        if(Constant.guiBean == null){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("guiName", Constant.guiBean.getGui_name());
        map.put("location", Constant.guiBean.getLocation());
        doPost(map,Server.cupInfoQuery,callBack);
    }

    /**
     * 用户订单查询
     */
    public static void queryCustomer(String tail, String packageStatus,HttpUtil.ResponeCallBack callBack) {
        if(Constant.guiBean == null){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("customerMobile", tail);
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("packageStatus", Constant.guiBean.getGui_no());
        doPost(map,Server.customerQuery,callBack);
    }

    /**
     * 取件验证
     * openBoxKey 柜号加尾号
     */
    public static void customerVerify(String tradeSeq, String mobile, String openBoxKey, HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("tradeSeq", tradeSeq);
        map.put("customerMobile", mobile);
        map.put("openBoxKey", openBoxKey);
        doPost(map,Server.customerVerify,callBack);
    }
    /**
     * 取件验证不需要流水号
     * openBoxKey 柜号加尾号
     */
    public static void customerVerify(String mobile, String openBoxKey, HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("customerMobile", mobile);
        map.put("tradeSeq", "11");
        map.put("openBoxKey", openBoxKey);
        doPost(map,Server.customerVerify,callBack);
    }

    /**
     * 取件完成
     * @param callBack
     */
    public static void takeout(Order order,String tail,String packageStatus,HttpUtil.ResponeCallBack callBack){
        if(order == null) return;
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("tradeSeq", order.getPackage_id());
        map.put("postmanId", order.getPostman_id());
        map.put("packageId", order.getPackage_id());
        map.put("packageStatus", packageStatus);
        map.put("boxNo", order.getBox_no());
        map.put("customerMobile", tail);
        map.put("storedTime", order.getStored_time());
        map.put("occurTime", "2018-07-11 12:12:12");
        doPost(map,Server.takeout,callBack);
    }
 /**
     * 取件完成
     * @param callBack
     */
    public static void takeout(String openBoxKey,String boxNo, HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("openBoxKey", openBoxKey);
        map.put("boxNo", boxNo);
        doPost(map,Server.takeoutGui,callBack);
    }


    /**
     * 缓存数据
     * @param callBack
     */
    public static void takeawayOrder(HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        doPost(map,Server.takeawayOrder,callBack);
    }

    /**
     * 提交数据
     * @param callBack
     */
    public static void updatetakeawayOrder(String beans, HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("beans", beans);
        doPost(map,Server.updateTakeawayOrder,callBack);
    }


    /**
     * 管理登录
     * @param callBack
     */
    public static void grantPolling(long timeMillis,HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("timestamp", System.currentTimeMillis()+"");
        doGet(Server.grantPolling+"?guiNo="+Constant.guiBean.getGui_no()+"&timestamp="+timeMillis,callBack);
    }

    /**
     * 订单管理
     * @param callBack
     */
    public static void loadOrder(int pageNum,int pageSize,String postmanMobile,String customerMobile,String status,String date,HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("postmanMobile", postmanMobile);
        map.put("customerMobile", customerMobile);
        map.put("status", status);
        map.put("date", date);
        map.put("pageNum", pageNum+"");
        map.put("pageSize", pageSize+"");
        map.put("sourceType", 1+"");
        doPost(map,Server.PACKAGELIST,callBack);
    }

    /**
     * peizhi
     * @param callBack
     */
    public static void loadConfigureInfo(int type,HttpUtil.ResponeCallBack callBack){
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("type", type+"");
        doGet(Server.CONFIGUREINFO+"?guiNo="+Constant.guiBean.getGui_no()+"&type="+type,callBack);
    }

    /**
     * 取件开柜
     */
    public void getMealOpen(String open_seq,String open_user,String package_id,String box_no,String take_type,HttpUtil.ResponeCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("OPEN_SEQ", open_seq);
        map.put("OPEN_USER", open_user);
        map.put("PACKAGE_ID", package_id);
        map.put("BOX_NO", box_no);
        map.put("FORCE_OPEN", "0");
        map.put("TAKE_TYPE", take_type);
        map.put("OCCUR_TIME", HttpUtil.now());
        doPost(map,"dcdz.app.elocker.box.open2takeout",callBack);
    }
    /**
     * 取件完成
     */
    public void getMealComplete(String gui_no,String trade_seq,String postman_id,
                                String package_id,String package_status,String box_no,
                                String customer_mobile,String stored_time,HttpUtil.ResponeCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("GUI_NO", gui_no);
        map.put("TRADE_SEQ", trade_seq);
        map.put("POSTMAN_ID", postman_id);
        map.put("PACKAGE_ID", package_id);
        map.put("PACKAGE_STATUS", package_status);
        map.put("BOX_NO", box_no);
        map.put("CUSTOMER_MOBILE", customer_mobile);
        map.put("STORED_TIME", stored_time);
        map.put("OCCUR_TIME", HttpUtil.now());
        doPost(map,"dcdz.app.elocker.takeout",callBack);
    }





    public static void doPost(final Map<String, String> params, final String method, final HttpUtil.ResponeCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* params.putAll(map);

                params.put("method",methodMap.get(method));
                params.put("timeStamp", HttpUtil.now());

                try {
                    params.put("sign",HttpUtil.signRequest(params,secret,signMethod));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                callBack.respone(HttpUtil.post(method, params));
            }
        }).start();
    }

    public static void doGet(final String method, final HttpUtil.ResponeCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* params.putAll(map);

                params.put("method",methodMap.get(method));
                params.put("timeStamp", HttpUtil.now());

                try {
                    params.put("sign",HttpUtil.signRequest(params,secret,signMethod));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                callBack.respone(HttpUtil.get(method));
            }
        }).start();
    }

    /**
     * 信息收集
     * @param params
     * @param callBack
     */
    public static void crashCollect(Map<String,String> params,HttpUtil.ResponeCallBack callBack){
        doPost(params,Server.crashCollect,callBack);
    }



    /**
     * 存餐柜门关闭扫描
     * @param callBack
     */
    public static void goodsInspection(String serialNo,String status,HttpUtil.ResponeCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("serialNo", serialNo);
        map.put("status", status);
        doPost(map,Server.goodsInspection,callBack);
    }

    /**
     * 30秒柜门没有关闭通知
     * @param callBack
     */
    public static void cupNoClose(String serialNo,HttpUtil.ResponeCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("serialNo", serialNo);
        doPost(map,Server.cupNoClose,callBack);
    }


    /**
     * 取餐通知
     * @param callBack
     */
    public static void cupTakeoutNoClose(String mobile,String boxNo,String serialNo,HttpUtil.ResponeCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("guiNo", Constant.guiBean.getGui_no());
        map.put("boxNo", boxNo+"");
        map.put("serialNo", serialNo+"");
        doPost(map,Server.cupTakeoutNoClose,callBack);
    }
}

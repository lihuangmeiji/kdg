package org.tabjin.myapplication.base;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import org.tabjin.myapplication.model.bean.GuiBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Created by hspcadmin on 2018/8/25.
 */

public class Constant {

    // a1lkHlrHpfc  a1qGPxvs90L
    public static String pypwd = "a1qGPxvs90L";

    public static String dev = "/dev/ttyS4";

    public static boolean pytype = false;

    public static final String FRAGMENTID = "fragmentId";

    public static byte boardid = 1;

    public static byte boxId = 2;

    public static GuiBean guiBean = null;

    public static int guiType = 1;


    public static final String REBINDAIDL = "rebindaidl";

    public static final String TABLE_NAME = "takeawayOrder";

    // 列定义
    private final String[] ORDER_COLUMNS = new String[]{"Id", "package_no", "box_no", "open_box_key", "box_state"};

    //tcp连接的异常日志路径Tcp
    public static final String TCPEXPATH = Environment.getExternalStoragePublicDirectory("") + "/linxian/";
    //tcp连接的异常日志文件名
    public static final String TCPEXFILENAME = "tcpConnectionLog.txt";

    //打开的柜子
    public static String guiNum = "";

    //开或关
    public static int pyGuiJc = 1;

    //断网
    public static int brokenNetwork = 0;

    //打开的柜子KEY
    public static String openBoxKey = "";

    public static final String fileSavePath = getSDPath() != null ? (getSDPath() + "/linxian/") : null;

    /**
     * 若存在SD 则获取SD卡的路径 不存在则返回null
     */
    public static String getSDPath() {
        File sdDir = null;
        String path = null;
        //判断sd卡是否存在
        boolean sdCardExist = hasSDCard();
        if (sdCardExist) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
            path = sdDir.getPath();
        }
        return path;
    }

    /**
     * 判断是否有SD卡
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static String isNetworkConnected(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.getTypeName() + "   isAvailable：" + mNetworkInfo.isAvailable() + "   connected:" + mNetworkInfo.isConnected();
            }

        }
        return null;
    }

    public static boolean mReflectScreenState(Context context) {
        if (context != null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
            return isScreenOn;
        }
        return false;
    }

    /**
     * 用Ping的方法检测网络可行性
     *
     * @return
     */
    public static final boolean pingIsInternetConnect() {
        boolean result = false;
        try {
            String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题~
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping3次
// 读取ping的内容，可不加。
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.i("TTT", "result content : " + stringBuffer.toString());
// PING的状态
            int status = p.waitFor();
            if (status == 0) {
                Log.i( "有网","successful~");
                result =true;
            } else {
                Log.i( "没网","failed~ cannot reach the IP address");
                result = false;
            }
        } catch (IOException e) {
            Log.i( "ping错误","failed~ IOException");
            result = false;
        } catch (InterruptedException e) {
            Log.i( "ping错误","failed~ InterruptedException");
            result =false;
        } finally {
            Log.i("TTT", "result = " + result);
        }
        return result;
    }
}
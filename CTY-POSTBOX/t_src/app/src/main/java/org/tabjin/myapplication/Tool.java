package org.tabjin.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by hspcadmin on 2018/9/28.
 */

public class Tool {

    public static class L {

        public static final String TAG = "linxian";

        public static void e(String tag, String msg) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, msg);
            }
        }

        public static void e(String msg) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, msg);
            }
        }
    }

    public static class T{
        public static void showToast(Context context,String msg) {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

    public static class Kit{
        public static String getMacAddressFromIp(Context context) {
            String mac_s= "";
            StringBuilder buf = new StringBuilder();
            try {
                byte[] mac;
                NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
                mac = ne.getHardwareAddress();
                for (byte b : mac) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                mac_s = buf.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mac_s;
        }

        public static String getIpAddress(Context context){
            NetworkInfo info = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 3/4g网络
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    try {
                        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                            NetworkInterface intf = en.nextElement();
                            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                                InetAddress inetAddress = enumIpAddr.nextElement();
                                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                    return inetAddress.getHostAddress();
                                }
                            }
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }

                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    //  wifi网络
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                    return ipAddress;
                }  else if (info.getType() == ConnectivityManager.TYPE_ETHERNET){
                    // 有限网络
                    return getLocalIp();
                }
            }
            return null;
        }

        private static String intIP2StringIP(int ip) {
            return (ip & 0xFF) + "." +
                    ((ip >> 8) & 0xFF) + "." +
                    ((ip >> 16) & 0xFF) + "." +
                    (ip >> 24 & 0xFF);
        }


        // 获取有限网IP
        private static String getLocalIp() {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException ex) {

            }
            return "0.0.0.0";

        }

        protected void excutesucmd(String currenttempfilepath,Context context) {
            Process process = null;
            OutputStream out = null;
            InputStream in = null;
            try {
                // 请求root
                process = Runtime.getRuntime().exec("su");
                out = process.getOutputStream();
                // 调用安装
                out.write(("pm install -r " + currenttempfilepath + "\n").getBytes());
                in = process.getInputStream();
                int len = 0;
                byte[] bs = new byte[256];
                while (-1 != (len = in.read(bs))) {
                    String state = new String(bs, 0, len);
                    if (state.equals("success\n")) {
                        //安装成功后的操作

                        //静态注册自启动广播
                        Intent intent=new Intent();
                        //与清单文件的receiver的anction对应
                        intent.setAction("android.intent.action.PACKAGE_REPLACED");
                        //发送广播
                        context.sendBroadcast(intent);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static class SP{
        public static SharedPreferences sp;
        public static SharedPreferences.Editor editor;
        public static void init(Activity context){
            sp = context.getPreferences(Activity.MODE_PRIVATE);
            editor = sp.edit();
        }
    }

    public static class F{

        /**
         *
         * @param content 内容
         * @param filePath 文件路径
         * @param fileName 文件名称
         * @param maxlength 文件大小上限  单位MB
         */
        public static void appendTextTofile(String content, String filePath, String fileName, int maxlength) {
            //生成文件夹之后，再生成文件，不然会出错
            makeFilePath(filePath, fileName);

            String strFilePath = filePath+fileName;
            BufferedWriter out = null;
            try {
                File file = new File(strFilePath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                //如果大于上限，先清空
                if(file.length()>=maxlength*1024*1024){
                    FileWriter fileWriter =new FileWriter(file);
                    fileWriter.write("");
                    fileWriter.flush();
                    fileWriter.close();
                }
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(content);
            } catch (Exception e) {
                Log.e("TestFile", "Error on write File:" + e);
            } finally {
                try {
                    if(out!=null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // 将字符串写入到文本文件中
        public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
            //生成文件夹之后，再生成文件，不然会出错
            makeFilePath(filePath, fileName);

            String strFilePath = filePath+fileName;
            try {
                File file = new File(strFilePath);
                if (!file.exists()) {
                    Log.d("TestFile", "Create the file:" + strFilePath);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
//                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
//                raf.seek(file.length());
//                raf.write(strcontent.getBytes());
//                raf.close();
                FileWriter fileWriter =new FileWriter(file);
                fileWriter.write("");
                fileWriter.write(strcontent);
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                Log.e("TestFile", "Error on write File:" + e);
            }
        }

        // 生成文件
        public static File makeFilePath(String filePath, String fileName) {
            File file = null;
            makeRootDirectory(filePath);
            try {
                file = new File(filePath + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }

        // 生成文件夹
        public static void makeRootDirectory(String filePath) {
            File file = null;
            try {
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdir();
                }
            } catch (Exception e) {
                Log.i("error:", e+"");
            }
        }
    }

    public static class D{
        /**
         * 获取当前时间年月日时分秒
         * @return 当前时间年月日时分秒
         */
        public static String getNowYMD_HMS(){
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }
    }

}

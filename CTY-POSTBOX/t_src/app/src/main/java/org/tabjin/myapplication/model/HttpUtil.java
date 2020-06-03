package org.tabjin.myapplication.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hspcadmin on 2018/9/29.
 */

public class HttpUtil {

//    public static final String serverUrl = "http://api8080.ximuok.com";
//    public static final String serverUrl = "http://211.155.225.116:58882/elockerapi/terminalservice";

    public static String post(String method,Map<String,String> params) {

        Log.i("PostGetUtil","方法名称："+method);

        HttpURLConnection conn = null;

        String backcontent = "";

        OutputStream outputStream = null;

        InputStream in = null;
        try {
            String content = getRequestData(params,"utf-8").toString();
            StringBuffer urlStr = new StringBuffer(Server.serverUrl+method);
            urlStr.append("?");
            urlStr.append(content);
            URL url = new URL(urlStr.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "android");
            conn.setRequestProperty("Content-Type", "application/json");
            byte[] contents = content.getBytes();
//            conn.setRequestProperty("Content-Length", String.valueOf(contents.length));
            conn.setUseCaches(false);
            conn.setConnectTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            /*outputStream = conn.getOutputStream();
            outputStream.write(contents);
            outputStream.flush();
            outputStream.close();*/
            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
                Log.i("PostGetUtil","post请求成功"+content);
                in=conn.getInputStream();
                backcontent = dealResponseResult(in);
            }else {
                Log.i("PostGetUtil","post请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return backcontent;

        }

    }


    public static String get(String method) {

        try {
            URL url = new URL(Server.serverUrl+method);
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            //只是建立一个连接, 并不会发送真正http请求  (可以不调用)
            urlConnection.connect();
            if(200 == urlConnection.getResponseCode()){
                Log.i("PostGetUtil","get请求成功");
                //得到输入流
                InputStream is =urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = is.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }else{
                Log.i("PostGetUtil","get请求失败");
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
    * Function  :   封装请求体信息
    * Param     :   params请求体内容，encode编码格式
    */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[30];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    public interface ResponeCallBack{
        void respone(String respone);
    }

    public static String now() {
        Date data = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return format.format(data);
    }

    public static final String SIGN_METHOD_MD5 = "md5";
    public static final String SIGN_METHOD_HMAC = "hmac";
    public static final String CHARSET_UTF8     = "utf-8";
    public static String signRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            bytes = md5.digest(data.getBytes(CHARSET_UTF8));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            throw new IOException(e.toString());
        }
        return bytes;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }


}

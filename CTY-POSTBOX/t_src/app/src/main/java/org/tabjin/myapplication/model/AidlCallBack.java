package org.tabjin.myapplication.model;

import com.hzdongcheng.drivers.bean.Result;

/**
 * Created by hspcadmin on 2018/9/28.
 */

public interface AidlCallBack {

    void callback(Result result);

    void handlerException(Exception e);

}

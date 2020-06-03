package org.tabjin.myapplication.model;

/**
 * Created by hspcadmin on 2018/9/28.
 */

public abstract class AidlSuccessCallBack implements AidlCallBack {

    @Override
    public void handlerException(Exception e) {
        System.out.println(e.getMessage());
    }
}

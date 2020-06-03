package org.tabjin.myapplication.model;

import android.os.RemoteException;

import org.tabjin.myapplication.Tool;

/**
 * Created by hspcadmin on 2018/9/28.
 */

public abstract class RemoteExceptionRunnable implements Runnable {

    @Override
    public void run() {
        try {
            doTask();
        } catch (RemoteException e) {
            Tool.L.e("aidl",e.getMessage());
            exception(e);
        }
    }

    abstract void doTask() throws RemoteException;

    abstract void exception(RemoteException e);
}

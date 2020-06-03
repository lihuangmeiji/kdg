package org.tabjin.myapplication.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.hzdongcheng.drivers.IDriverManager;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.locker.IMasterController;
import com.hzdongcheng.drivers.locker.ISlaveController;
import com.hzdongcheng.drivers.peripheral.cardreader.ICardReaderController;
import com.hzdongcheng.drivers.peripheral.scanner.IScannerController;
import com.hzdongcheng.drivers.system.ISystemController;

import org.tabjin.myapplication.base.Constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by hspcadmin on 2018/9/27.
 */

public class AidlModel {

    static IMasterController masterController;
    static ISlaveController slaveController;
    static IScannerController scannerController;
    static ICardReaderController cardreaderController;
    static ISystemController systemController;

    private static Activity mContext;

    //执行aidl的线程池
    private static ExecutorService singleThreadPool;

    private static AidlModel instance;

    public static AidlModel getInstance() {
        return instance;
    }

    public static void init(IDriverManager driverManager,Activity context){
        instance = new AidlModel(driverManager, context);
    }

    private AidlModel(IDriverManager driverManager,Activity context) {
        mContext = context;
        try {
            masterController =
                    IMasterController.Stub.asInterface(driverManager.getMasterService());
            slaveController =
                    ISlaveController.Stub.asInterface(driverManager.getSlaveService());
            systemController =
                    ISystemController.Stub.asInterface(driverManager.getSystemService());
            singleThreadPool = Executors.newSingleThreadExecutor();
        } catch (RemoteException e) {
            Log.e("==初始化主控服务失败==", e.getLocalizedMessage());
            sendReBind(context);
        }
    }

    /**
     * 发送重连aidl广播
     */
    private static void sendReBind(Context context) {
        Intent intent = new Intent(Constant.REBINDAIDL);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 判断aidl是否正常
     * @return true 正常  false 不正常
     */
    private static boolean isAidlConnected(){
        return instance != null&&slaveController != null;
    }

    /**
     * 进行aidl操作时，检查aidl连接是否正常，不正常就发送重连的广播
     * @return true 正常  false 不正常
     */
    private static boolean checkAidl(){
        if(isAidlConnected()){
            return true;
        }else{
            if(mContext !=null) {
                sendReBind(mContext);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"连接异常，请重试！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return false;
        }
    }

    /**
     * 根据名字打开柜
     */
    public static void openBoxById(final byte boardId,final byte boxId, final AidlCallBack callback) {
        if(!checkAidl()){
            return;
        }
        singleThreadPool.execute(new RemoteExceptionRunnable() {
            @Override
            public void doTask() throws RemoteException{
                Result result = slaveController.openBoxById(boardId,boxId);
                callback.callback(result);
            }

            @Override
            void exception(RemoteException e) {
                callback.handlerException(e);
            }
        });
    }

    /**
     * 查询柜子状态
     * @param boxId
     */
    public static void  queryBoxStatusById(final byte boardId, final byte boxId, final AidlCallBack callBack){
        if(!checkAidl()){
            return;
        }
        singleThreadPool.execute(new RemoteExceptionRunnable() {
            @Override
            void doTask() throws RemoteException {
                Result result = slaveController.queryBoxStatusById(boardId,boxId);
                callBack.callback(result);
            }

            @Override
            void exception(RemoteException e) {
                callBack.handlerException(e);
            }
        });
    }

    public static void openAllBox(final byte boardId, AidlCallBack callBack) {
        if(!checkAidl()){
            return;
        }
        singleThreadPool.execute(new RemoteExceptionRunnable() {
            @Override
            void doTask() throws RemoteException {
                Result result = slaveController.openAllBox(boardId);
            }

            @Override
            void exception(RemoteException e) {

            }
        });
    }


}

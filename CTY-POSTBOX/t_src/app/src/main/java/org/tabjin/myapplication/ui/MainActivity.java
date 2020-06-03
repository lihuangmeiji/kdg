package org.tabjin.myapplication.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hzdongcheng.drivers.IDriverManager;
import com.hzdongcheng.drivers.bean.Result;
import com.tencent.map.geolocation.TencentLocation;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.autolayout.AutoLayoutActivity;


import net.ipinyuan.communication.error.ErrorCode;
import net.ipinyuan.communication.out.CabinetTransaction;
import net.ipinyuan.communication.out.CabinetTransactionEventListener;
import net.ipinyuan.communication.out.DoorStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tabjin.myapplication.BuildConfig;
import org.tabjin.myapplication.R;
import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.LogcatFileManager;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.kit.CrashHandler;
import org.tabjin.myapplication.model.AidlModel;
import org.tabjin.myapplication.model.AidlSuccessCallBack;
import org.tabjin.myapplication.model.DataBaseOpenHelper;
import org.tabjin.myapplication.model.HttpModel;
import org.tabjin.myapplication.model.HttpUtil;
import org.tabjin.myapplication.model.Server;
import org.tabjin.myapplication.model.SpUtil;
import org.tabjin.myapplication.model.bean.BannerBean;
import org.tabjin.myapplication.model.bean.BoxBean;
import org.tabjin.myapplication.model.bean.GuiBean;
import org.tabjin.myapplication.model.bean.GuiTypeBean;
import org.tabjin.myapplication.model.bean.OpenBoxBean;
import org.tabjin.myapplication.model.bean.Respone;
import org.tabjin.myapplication.model.bean.TakeawayOrderBean;
import org.tabjin.myapplication.model.bean.TakeawayOrderUpdateBean;
import org.tabjin.myapplication.model.tcpnet.TCPSocketCallback;
import org.tabjin.myapplication.model.tcpnet.TCPSocketConnect;
import org.tabjin.myapplication.model.tcpnet.TcpLog;
import org.tabjin.myapplication.ui.fragment.CloseDoorFragment;
import org.tabjin.myapplication.ui.fragment.DoorNotClosedFragment;
import org.tabjin.myapplication.ui.fragment.GetMealFragment;
import org.tabjin.myapplication.ui.fragment.GetMealSelectFragment;
import org.tabjin.myapplication.ui.fragment.GetMealSuccessFragment;
import org.tabjin.myapplication.ui.fragment.HomeFragment;
import org.tabjin.myapplication.ui.fragment.PutMealFragment;
import org.tabjin.myapplication.ui.server.AlarmReceiver;
import org.tabjin.myapplication.ui.widget.CustomDialog;
import org.tabjin.myapplication.ui.widget.LocationHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class MainActivity extends AppCompatActivity implements IRouter {

    private final int permissionCode = 202;

    private final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    private int fragmentId = R.id.frame_content;

    private CustomDialog dialog;


    //private CountDownTimer timer;

    LogcatFileManager logcatFileManager;

    private FragmentManager mFragmentManager;


    int maxCloseCheckBox = 72;

    final int MSG_CLOSE_CHECK_PENDING = 0;

    CloseCheckBox boxesWaitingList[] = new CloseCheckBox[maxCloseCheckBox];//等待被检测 是否关门的格子号码

    CloseCheckBox boxesPendingList[] = new CloseCheckBox[maxCloseCheckBox];//等待被放入到 waitingList中去的格子


    private final int CELL_COUNT = 0;

    private GuiTypeBean guiTypeBean;

    //tcp连接
    private TCPSocketConnect connect;

    //首页fragment
    private HomeFragment mHomeFragment;
    //存餐fragment
    private PutMealFragment mPutMealFragment;
    //取餐fragment
    private GetMealSelectFragment mealSelectFragment;
    //取餐fragment
    private GetMealFragment mGetMealFragment;
    //取餐成功fragment
    private GetMealSuccessFragment mGetMealSuccessFragment;
    //关闭柜门fragment
    private CloseDoorFragment mCloseDoorFragment;
    //未关闭柜门fragment
    private DoorNotClosedFragment mDoorNotClosedFragment;

    private CabinetTransaction cabinetTransaction;

    int videotype = 0;

    Banner banner;

    ImageView iv_top;

    Banner bannerapawait;

    RelativeLayout rel_top;

    //设置图片资源:url或本地资源
    List<String> stringList;

    //设置图片资源:url或本地资源
    List<String> stringListapawait;

    //设置视频资源
    List<BannerBean> topVideoList;

    private LocationHelper mLocationHelper;

    private double lat = 0.0;
    private double lng = 0.0;

    /* 上一次User有动作的Time Stamp */
    private Date takeawayOrderlastUpdateTime;

    private Handler mHandler01 = new Handler();
    private Handler mHandler02 = new Handler();

    /* 上一次User有动作的Time Stamp */
    private Date lastUpdateTime;
    /* 计算User有几秒没有动作的 */
    private long timePeriod;

    /* 静止超过N秒将自动进入屏保 */
    private float mHoldStillTime = 20;
    /*标识当前是否进入了屏保*/
    private boolean isRunScreenSaver;

    /*时间间隔*/
    private long intervalScreenSaver = 1000;
    private long intervalKeypadeSaver = 1000;

    /*可以进入待机状态*/
    private boolean isTrue = true;

    /* 上一次User有动作的Time Stamp */
    private Date lastDkUpdateTime=null;

    private String initPass = "";

    //aidl断线重连广播接收器
    private BroadcastReceiver receiver;

    //绑定service
    private Intent lockerIntent;

    TextView tvloginfo;

    AlarmManager am;
    PendingIntent pi;

    JCVideoPlayerStandard videoplayer;

    FrameLayout frame_content;

    final int PLAY_VIDEO = 11;

    final int PLAY_BANNER = 12;

    final int PLAY_BANNERAPAWAIT = 13;

    final int PLAY_VIDEOHEIGHT = 14;

    final int PY_START = 15;

    //时间间隔
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    DataBaseOpenHelper dataBaseOpenHelper;

    Timer timeDataBase = new Timer();

    Timer timerAutomaticClose;

    Timer timeralarmRestart;

    //aidl连接
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IDriverManager driverManager = IDriverManager.Stub.asInterface(service);
            AidlModel.init(driverManager, MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("==初始化主控服务失败==", "连接失败");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.e("==初始化主控服务失败==", "onBindingDied");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < maxCloseCheckBox; i++) {
            boxesPendingList[i] = new CloseCheckBox();
            boxesWaitingList[i] = new CloseCheckBox();
        }
        CrashHandler.getInstance().init(MainActivity.this);
        dialog = new CustomDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ImageView iv_top = (ImageView) findViewById(R.id.iv_top);
        tvloginfo = (TextView) findViewById(R.id.tvloginfo);
      /*  tvloginfo.setOnClickListener(new View.OnClickListener() {
        /* 初始取得User可触碰屏幕的时间 */
        lastUpdateTime = new Date(System.currentTimeMillis());

        dialog = new CustomDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        banner = (Banner) findViewById(R.id.banner);
        bannerapawait = (Banner) findViewById(R.id.bannerapawait);
        tvloginfo = (TextView) findViewById(R.id.tvloginfo);
        rel_top = (RelativeLayout) findViewById(R.id.rel_top);
        tvloginfo.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                cabinetTransaction.openCell(1);
            }
        });
        File mFile = new File(Constant.fileSavePath + "take.png");
        DisplayMetrics displayMetrics = getDisplayMetrics(MainActivity.this);
        tvloginfo.setText("---printDisplayMetrics---" +
                "widthPixels=" + displayMetrics.widthPixels
                + ", heightPixels=" + displayMetrics.heightPixels
                + ", density=" + displayMetrics.density
                + ", densityDpi=" + displayMetrics.densityDpi);
        stringList = new ArrayList<>();
        stringList.add(Constant.fileSavePath + "banner1.jpg");
        stringList.add(Constant.fileSavePath + "banner2.jpg");
        stringList.add(Constant.fileSavePath + "banner3.jpg");
        loadTestDatas();
        stringListapawait = new ArrayList<>();
        stringListapawait.add(Constant.fileSavePath + "bannerapawait1.jpg");
        stringListapawait.add(Constant.fileSavePath + "bannerapawait2.jpg");
        stringListapawait.add(Constant.fileSavePath + "bannerapawait3.jpg");
        loadTestApawaitDatas();
        videoplayer = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        topVideoList = new ArrayList<>();
        videoplayer.setUp(Constant.fileSavePath + "lx1.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频播放");
        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
        videoplayer.loadingProgressBar.setVisibility(View.GONE);
        videoplayer.backButton.setVisibility(View.GONE);
        videoplayer.bottomProgressBar.setVisibility(View.GONE);
        videoplayer.bottomProgressBar.setAlpha(0);
        videoplayer.currentTimeTextView.setVisibility(View.GONE);
        videoplayer.fullscreenButton.setVisibility(View.GONE);
        videoplayer.bottomContainer.setVisibility(View.GONE);
        videoplayer.startButton.setVisibility(View.GONE);
        videoplayer.startButton.setAlpha(0);
        frame_content = (FrameLayout) findViewById(R.id.frame_content);
        cabinetTransaction = CabinetTransaction.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissions[0])
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, permissionCode);
            } else {
                checkRegistered();
            }
        } else {
            checkRegistered();
        }

        List<String> tableSqls = new ArrayList<>();
        String sql = "create table if not exists " + Constant.TABLE_NAME + " (Id integer primary key, package_no text,box_no text,open_box_key text,box_state text,takeoutAt text)";
        tableSqls.add(sql);
        dataBaseOpenHelper = DataBaseOpenHelper.getInstance(MainActivity.this, Constant.TABLE_NAME, 1, stringList);
        mLocationHelper = new LocationHelper(this);
        intiFragmentContent();
        startLogcatManager();
        doMyLoc();
        alarmRestart();
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        } else {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

    /**
     * 初始化重连aidl广播接收器
     */
    private void initReceiver() {
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Constant.REBINDAIDL.equals(action)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            unBindAidl();
                            initAidl();
                        }
                    }).start();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter(Constant.REBINDAIDL);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);
    }


    /**
     * 初始化aidl 和柜子的连接
     */
    private void initAidl() {
        lockerIntent = new Intent("hzdongcheng.intent.action.DRIVER");
        lockerIntent.setPackage("com.hzdongcheng.drivers");
        bindService(lockerIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解决aidl绑定
     */
    private void unBindAidl() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }


    private void intiFragmentContent() {
        mFragmentManager = getSupportFragmentManager();
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance();
        }
        mFragmentManager.beginTransaction().replace(fragmentId, mHomeFragment).commit();
    }

    @Override
    public void gotoFragment(String windowId) {
        switch (windowId) {
            case WindowId.HomeFragment:
                isTrue = true;
                if (Constant.brokenNetwork == 1) {
                    timeDataBase.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (Constant.pingIsInternetConnect()) {
                                String box_state = "1";
                                Cursor cursor = dataBaseOpenHelper.query(Constant.TABLE_NAME, " where box_state =" + box_state);
                                if (cursor.moveToFirst() == false) {
                                    List<TakeawayOrderUpdateBean> takeawayOrderUpdateBeanList = new ArrayList<>();
                                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                                        String package_no = cursor.getString(cursor.getColumnIndex("package_no"));
                                        String takeoutAt = cursor.getString(cursor.getColumnIndex("takeoutAt"));
                                        TakeawayOrderUpdateBean takeawayOrderUpdateBean = new TakeawayOrderUpdateBean();
                                        takeawayOrderUpdateBean.setPackageNo(package_no);
                                        takeawayOrderUpdateBean.setTakeoutAt(takeoutAt);
                                        takeawayOrderUpdateBeanList.add(takeawayOrderUpdateBean);
                                    }
                                    String bean = new Gson().toJson(takeawayOrderUpdateBeanList);
                                    updateTakeawayOrder(bean);
                                }
                            }
                        }
                    }, 0, 1000);
                }
                lastUpdateTime = new Date(System.currentTimeMillis());
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mHomeFragment).commit();
                break;
            case WindowId.PutMealFragment:
                isTrue = false;
                if (mPutMealFragment == null) {
                    mPutMealFragment = PutMealFragment.newInstance();
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mPutMealFragment).commit();
                break;
            case WindowId.GetMealSelectFragment:
                isTrue = false;
                if (mealSelectFragment == null) {
                    mealSelectFragment = GetMealSelectFragment.newInstance();
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mealSelectFragment).commit();
                break;
            case WindowId.GetMealFragment:
                isTrue = false;
                if (mGetMealFragment == null) {
                    mGetMealFragment = GetMealFragment.newInstance(cabinetTransaction, dataBaseOpenHelper);
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mGetMealFragment).commit();
                break;
            case WindowId.GetMealSuccessFragment:
                isTrue = false;
                if (mGetMealSuccessFragment == null) {
                    mGetMealSuccessFragment = GetMealSuccessFragment.newInstance();
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mGetMealSuccessFragment).commit();
                break;
            case WindowId.CloseDoorFragment:
                isTrue = false;
                if (mCloseDoorFragment == null) {
                    mCloseDoorFragment = CloseDoorFragment.newInstance();
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mCloseDoorFragment).commit();
                break;
            case WindowId.DoorNotClosedFragment:
                isTrue = false;
                if (mDoorNotClosedFragment == null) {
                    mDoorNotClosedFragment = DoorNotClosedFragment.newInstance();
                }
                mFragmentManager.beginTransaction().replace(fragmentId, mDoorNotClosedFragment).commit();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            } else {
                checkRegistered();
            }
        }
    }

    /**
     * 检查是否需要注册
     */
    private void checkRegistered() {
        final File file = new File(Server.registerFilePath + Server.registerFileName);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                String content = HttpUtil.dealResponseResult(fis);
                if (!TextUtils.isEmpty(content)) {
                    Constant.guiBean = new Gson().fromJson(content, GuiBean.class);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (null != Constant.guiBean) {
            CrashHandler.getInstance().init(MainActivity.this);
            loadBanner();
            loadBannerApawait();
            loadTopVideo();
            if (Constant.guiBean.getManufacturer().equals("dc")) {
                initReceiver();
                initAidl();
                infoQuery();
                //打开长连接接收推送指令
                OpenTCPClient(Constant.guiBean.getGui_no());
                automaticCloseTheDoor();
                ViewGroup.LayoutParams params = rel_top.getLayoutParams();
                params.height = 506;
                rel_top.setLayoutParams(params);
            } else if (Constant.guiBean.getManufacturer().equals("py")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = PY_START;
                        msg.arg1 = 1;
                        mUIHandler.sendMessage(msg);
                    }
                }, 5000);
                ViewGroup.LayoutParams params = rel_top.getLayoutParams();
                params.height = 607;
                rel_top.setLayoutParams(params);
            }
        } else {
            doRegister();
        }
    }

    /**
     * 查询柜子
     */
    private void infoQuery() {
        HttpModel.infoQuery(new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {

            }
        });
    }

    /**
     * 注册安装
     */
    private void doRegister() {
        dialog.setYesOnclickListener("确认", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                Toast.makeText(MainActivity.this, "开始注册！", Toast.LENGTH_SHORT).show();
                int guiType = dialog.getGuiTypeStr();
                if (guiType == 1) {
                    initPass = dialog.getMessageStr();
                    register();
                } else {
                    String guiNo = dialog.getMessageStr();
                    String number = dialog.getNumberStr();
                    String location = dialog.getLocationStr();
                    register1(guiNo, number, location);
                }

            }
        });
        dialog.setNoOnclickListener("退出", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
                finish();
            }
        });

        dialog.show();
    }


    /**
     * 注册dcdz
     */
    private void register() {
        Map<String, String> map = new HashMap<>();
        map.put("initPassword", initPass);
        map.put("signMac", Tool.Kit.getMacAddressFromIp(this));
        map.put("softwareVersion", BuildConfig.VERSION_NAME);
        map.put("signIp", Tool.Kit.getIpAddress(this));
        map.put("latitude", lat + "");
        map.put("longitude", lng + "");
        HttpModel.register(map, new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {
//                respone = "{\"code\":0,\"msg\":\"\",\"data\":{\"gui_no\":\"180921002\",\"server_time\":\"2018-10-08 17:23:17\",\"gui_name\":\"CYT02\",\"location\":\"城驿通\",\"server_url\":\"http://postapi.lxstation.com:80\"}}";
                try {
                    Type type = new TypeToken<Respone<GuiBean>>() {
                    }.getType();
                    Respone<GuiBean> bean = new Gson().fromJson(respone, type);
                    if (bean != null && bean.getCode() == 0) {
                        GuiBean guiBeanzc = bean.getData();
                        guiBeanzc.setManufacturer("dc");
                        Constant.guiBean = guiBeanzc;
                        if (Constant.guiBean == null) {
                            return;
                        }
                        CrashHandler.getInstance().init(MainActivity.this);
                        loadBanner();
                        loadBannerApawait();
                        loadTopVideo();
                        Tool.F.writeTxtToFile(new Gson().toJson(bean.getData()), Server.registerFilePath, Server.registerFileName);
                        initReceiver();
                        initAidl();
                        OpenTCPClient(Constant.guiBean.getGui_no());
                        automaticCloseTheDoor();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = PLAY_VIDEOHEIGHT;
                                msg.arg1 = 1;
                                mUIHandler.sendMessage(msg);
                            }
                        }).start();
                        dialog.dismiss();
                    } else {
                        dialog.clearMessage();
                        Looper.prepare();
                        Tool.T.showToast(MainActivity.this, "注册东城失败,请重试！");
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Tool.T.showToast(MainActivity.this, "注册----解析json异常");
                    Looper.loop();
                    Tool.L.e("注册----解析json异常");
                }
            }
        });
    }


    /**
     * 注册py
     */
    private void register1(final String guiNo, final String number, final String location) {
        Map<String, String> map = new HashMap<>();
        map.put("guiNo", guiNo);
        map.put("deviceId", cabinetTransaction.getDeviceName());
        map.put("location", location);
        map.put("manufacturer", "py");
        map.put("lat", lat + "");
        map.put("lon", lng + "");
        HttpModel.register1(map, new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {
                try {
                    Type type = new TypeToken<Respone<GuiBean>>() {
                    }.getType();
                    Respone<GuiBean> bean = new Gson().fromJson(respone, type);
                    if (bean != null && bean.getCode() == 0) {
                        GuiBean guiBeanzc = new GuiBean();
                        guiBeanzc.setGui_no(guiNo);
                        guiBeanzc.setLocation(location);
                        guiBeanzc.setDeviceId(cabinetTransaction.getDeviceName());
                        guiBeanzc.setManufacturer("py");
                        guiBeanzc.setNumber(Integer.valueOf(number).intValue());
                        Constant.guiBean = guiBeanzc;
                        if (Constant.guiBean == null) {
                            return;
                        }
                        CrashHandler.getInstance().init(MainActivity.this);
                        loadBanner();
                        loadBannerApawait();
                        loadTopVideo();
                        Tool.F.writeTxtToFile(new Gson().toJson(guiBeanzc), Server.registerFilePath, Server.registerFileName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cabinetTransaction.start(Constant.pypwd, Constant.dev, Constant.pytype, 1, Integer.valueOf(number).intValue(), cabinetTransactionEventListener);
                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = PLAY_VIDEOHEIGHT;
                                msg.arg1 = 2;
                                mUIHandler.sendMessage(msg);
                            }
                        }).start();
                        dialog.dismiss();
                    } else {
                        dialog.clearMessage();
                        Looper.prepare();
                        Tool.T.showToast(MainActivity.this, "注册品元失败,请重试！");
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Tool.T.showToast(MainActivity.this, "注册----解析json异常");
                    Looper.loop();
                    Log.i("json异常", "异常信息:" + e.getMessage());
                }

            }
        });
    }

    public void loadBanner() {
       /* HttpModel.loadConfigureInfo(4, new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {
                Log.e("bannerBeanRespone", respone);
                try {
                    Type type = new TypeToken<Respone<List<BannerBean>>>() {
                    }.getType();
                    Respone<List<BannerBean>> bannerBeanRespone = new Gson().fromJson(respone, type);
                    Log.e("bannerBeanResponedata", bannerBeanRespone.getData().toString());
                    if (bannerBeanRespone != null && bannerBeanRespone.getCode() == 0 && bannerBeanRespone.getData().size() > 0) {
                        List<BannerBean> bannerBean = bannerBeanRespone.getData();
                        for (int i = 0; i < bannerBean.size(); i++) {
                            stringList.add(bannerBean.get(i).getPath());
                            Log.e("bannergetPath", bannerBean.get(i).getPath());
                        }
                    } else {
                        stringList.add("");
                    }
                } catch (Exception e) {
                    stringList.add("");
                    Log.e("bannerexception", e.toString());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = PLAY_BANNER;
                        mUIHandler.sendMessage(msg);
                    }
                }).start();
            }
        });*/
    }

    public void loadBannerApawait() {
      /*  HttpModel.loadConfigureInfo(5, new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {
                try {
                    Type type = new TypeToken<Respone<List<BannerBean>>>() {
                    }.getType();
                    Respone<List<BannerBean>> bannerBeanRespone = new Gson().fromJson(respone, type);
                    if (bannerBeanRespone != null && bannerBeanRespone.getCode() == 0 && bannerBeanRespone.getData().size() > 0) {
                        List<BannerBean> bannerBean = bannerBeanRespone.getData();
                        for (int i = 0; i < bannerBean.size(); i++) {
                            stringListapawait.add(bannerBean.get(i).getPath());
                        }
                    } else {
                        stringListapawait.add("");
                    }
                } catch (Exception e) {
                    stringListapawait.add("");
                    Log.e("bannerexception", e.toString());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = PLAY_BANNERAPAWAIT;
                        mUIHandler.sendMessage(msg);
                    }
                }).start();
            }
        });*/
    }

    public void loadTopVideo() {
        /*HttpModel.loadConfigureInfo(8, new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {
                try {
                    Type type = new TypeToken<Respone<List<BannerBean>>>() {
                    }.getType();
                    Respone<List<BannerBean>> bannerBeanRespone = new Gson().fromJson(respone, type);
                    if (bannerBeanRespone != null && bannerBeanRespone.getCode() == 0&&bannerBeanRespone.getData().size()>0) {
                        topVideoList.addAll(bannerBeanRespone.getData());
                    } else {
                        BannerBean bannerBean = new BannerBean();
                        bannerBean.setPath(Constant.fileSavePath + "lx1.mp4");
                        topVideoList.add(bannerBean);
                    }
                } catch (Exception e) {
                    BannerBean bannerBean = new BannerBean();
                    bannerBean.setPath(Constant.fileSavePath + "lx1.mp4");
                    topVideoList.add(bannerBean);
                    Log.e("bannerexception", e.toString());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = PLAY_VIDEO;
                        mUIHandler.sendMessage(msg);
                    }
                }).start();
            }
        });*/
    }

    boolean openBoxAll;

    int numTCPClient=0;

    /*
      开启TCPClient连接
     */
    private void OpenTCPClient(String guiNo) {
        connect = new TCPSocketConnect(new TCPSocketCallback() {
            @Override
            public void tcp_connected() {
                TcpLog.d(">TCP连接服务器成功<");
                numTCPClient=0;
            }

            @Override
            public void tcp_receive(byte[] buffer) {
                //接收到服务器指令，这里进行解析处理业务动作
                System.out.println("---" + new String(buffer));
                String str = new String(buffer);
                Log.e("TCP", str);

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String type = jsonObject.getString("type");
                    if (type == null) {
                        return;
                    }
                    OpenBoxBean openBoxBean = new Gson().fromJson(str, OpenBoxBean.class);
                    switch (type) {
                        case "storein":
                            if (openBoxBean != null && openBoxBean.getBoxNo() != null) {
                                String boxNo = openBoxBean.getBoxNo();
                                CaculateId caculateId = new CaculateId(boxNo).invoke();
                                byte boardId = caculateId.getBoardId();
                                byte boxId = caculateId.getBoxId();
                                if (openBoxTcp(str, boardId, boxId) == true) {
                                    try {
                                        String serialNo = jsonObject.getString("serialNo");
                                        CloseCheckBox closeCheckBox = new CloseCheckBox();
                                        closeCheckBox.boxId = (int) boxId;
                                        closeCheckBox.boardId = (int) boardId;
                                        closeCheckBox.boxNo = boxNo;
                                        closeCheckBox.serialNo = serialNo;
                                        closeCheckBox.storeInOrTakeOut = CloseCheckBox.STOREIN;
                                        closeCheckBox.second = 0;
                                        closeCheckBox.status = CloseCheckBox.IDLE;
                                        closeCheckBox.mobile = null;
                                        //关箱检测
                                        Message msg = mUIHandler.obtainMessage(MSG_CLOSE_CHECK_PENDING);
                                        msg.obj = closeCheckBox;
                                        msg.sendToTarget();
                                        String retunstr = new String(buffer);
                                        retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"0\"");
                                        connect.write(retunstr.getBytes());
                                    } catch (Exception e) {
                                        String retunstr = new String(buffer);
                                        retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"-2\"");
                                        connect.write(retunstr.getBytes());
                                    }
                                } else {
                                    String retunstr = new String(buffer);
                                    retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"-1\"");
                                    connect.write(retunstr.getBytes());
                                }
                            }
                            break;
                        case "takeout":
                            if (openBoxBean != null && openBoxBean.getBoxNo() != null) {
                                String boxNo = openBoxBean.getBoxNo();
                                CaculateId caculateId = new CaculateId(boxNo).invoke();
                                byte boardId = caculateId.getBoardId();
                                byte boxId = caculateId.getBoxId();
                                if (openBoxTcp(str, boardId, boxId) == true) {
                                    try {
                                        String mobile = jsonObject.getString("msg");
                                        String serialNo = jsonObject.getString("serialNo");
                                        CloseCheckBox closeCheckBox = new CloseCheckBox();
                                        closeCheckBox.boxId = (int) boxId;
                                        closeCheckBox.boardId = (int) boardId;
                                        closeCheckBox.serialNo = serialNo;
                                        closeCheckBox.storeInOrTakeOut = CloseCheckBox.TAKEOUT;
                                        closeCheckBox.boxNo = boxNo;
                                        closeCheckBox.second = 0;
                                        closeCheckBox.status = CloseCheckBox.IDLE;
                                        closeCheckBox.mobile = mobile;
                                        Message msg = mUIHandler.obtainMessage(MSG_CLOSE_CHECK_PENDING);
                                        msg.obj = closeCheckBox;
                                        msg.sendToTarget();
                                        String retunstr = new String(buffer);
                                        retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"0\"");
                                        connect.write(retunstr.getBytes());
                                    } catch (Exception e) {
                                        String retunstr = new String(buffer);
                                        retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"-2\"");
                                        connect.write(retunstr.getBytes());
                                    }
                                } else {
                                    String retunstr = new String(buffer);
                                    retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"-1\"");
                                    connect.write(retunstr.getBytes());
                                }
                            }
                            break;
                        case "test":
                            connect.write(str.getBytes());
                            break;
                        case "open":
                            if (openBoxBean != null && openBoxBean.getBoxNo() != null) {
                                String boxNo = openBoxBean.getBoxNo();
                                CaculateId caculateId = new CaculateId(boxNo).invoke();
                                byte boardId = caculateId.getBoardId();
                                byte boxId = caculateId.getBoxId();
                                if (openBoxTcp(str, boardId, boxId) == true) {
                                    String retunstr = new String(buffer);
                                    retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"0\"");
                                    connect.write(retunstr.getBytes());
                                } else {
                                    String retunstr = new String(buffer);
                                    retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"-1\"");
                                    connect.write(retunstr.getBytes());
                                }
                            } else {
                                String retunstr = new String(buffer);
                                retunstr = retunstr.replaceAll("\"code\":\"\"", "\"code\":\"-1\"");
                                connect.write(retunstr.getBytes());
                            }
                            break;
                        case "openAll":
                            byte boardid1 = 0;
                            AidlModel.openAllBox(boardid1, new AidlSuccessCallBack() {
                                @Override
                                public void callback(Result result) {

                                }
                            });
                            byte boardid2 = 1;
                            AidlModel.openAllBox(boardid2, new AidlSuccessCallBack() {
                                @Override
                                public void callback(Result result) {

                                }
                            });

                            byte boardid3 =2;
                            AidlModel.openAllBox(boardid3, new AidlSuccessCallBack() {
                                @Override
                                public void callback(Result result) {

                                }
                            });

                            byte boardid4 = 3;
                            AidlModel.openAllBox(boardid4, new AidlSuccessCallBack() {
                                @Override
                                public void callback(Result result) {

                                }
                            });
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void tcp_disconnect() {
                TcpLog.d(">TCP连接断开<断开:原因");
                TcpLog.d(">网络连接状态:" + Constant.isNetworkConnected(MainActivity.this));
                TcpLog.d(">屏幕状态:" + Constant.mReflectScreenState(MainActivity.this));
                TcpLog.d(">能否打开网址:" + Constant.pingIsInternetConnect());
                numTCPClient++;
                if(numTCPClient*6>=5*60){
                    Log.d("关机", "alarm onReceive reboot");
                    try {
                        Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});  //关机
                        proc.waitFor();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        //String SrvIP = "106.15.176.115";

        String SrvIP = "t-postapi.lxstation.com";
        int SrvPort = 9226;
        //region 根据编译环境获取TCP通讯服务器地址&端口
        /*
        if (BuildConfig.DEBUG) {
            //本地测试环境
            SrvIP="t-postapi.lxstation.com"";
            SrvPort=8080;
        } else {
            //生产环境
            SrvIP="106.15.176.115";
            SrvPort=9226;
        }
        */
        //endregion

        connect.setAddress(SrvIP, SrvPort);
        connect.setGuiNo(guiNo);
        new Thread(connect).start();
    }

    /**
     * 取餐完成
     *
     * @param str
     */
    private void getMealDone(String str) {
        OpenBoxBean openBoxBean = new Gson().fromJson(str, OpenBoxBean.class);
        Intent intent = new Intent("getMealDone");
        intent.putExtra("openBoxBean", openBoxBean);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    boolean bReuslt = true;

    /**
     * tcp连接指令打开箱子
     *
     * @param str
     */

    private boolean openBoxTcp(String str, byte boardId, byte boxId) {

        if (boardId == -1) {
            return false;
        }

        AidlModel.openBoxById(boardId, boxId, new AidlSuccessCallBack() {
            @Override
            public void callback(Result result) {
                if (result.getCode() == 0) {
                    bReuslt = true;
                } else {
                    bReuslt = false;
                }
            }
        });
        return true;
    }


    /**
     * 根据boxNo计算相关id
     */
    private class CaculateId {
        private String boxNo;
        private byte boardId;
        private byte boxId;

        public CaculateId(String boxNo) {
            this.boxNo = boxNo;
        }

        public byte getBoardId() {
            return boardId;
        }

        public byte getBoxId() {
            return boxId;
        }

        public CaculateId invoke() {
            boardId = -1;
            switch (boxNo.toUpperCase().charAt(0)) {
                case 'A':
                    boardId = 0;
                    boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(1))));
                    break;
                case 'B':
                    boardId = 0;
                    boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(1))) + 9);
                    break;
                case 'C':
                    boardId = 1;
                    boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(1))));
                    break;
                case 'D':
                    boardId = 1;
                    boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(1))) + 9);
                    break;

                case 'E':
                    boardId = 2;
                    boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(1))));
                    break;
                case 'F':
                    boardId = 2;
                    boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(1))) + 9);
                    break;
                case 'G':
                    boardId = 3;
                    boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(1))));
                    break;
                case 'H':
                    boardId = 3;
                    boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(1))) + 9);
                    break;

            }
            return this;
        }
    }

   /* *//**
     * 根据boxNo计算相关id
     *//*

    private class CaculateId {
        private String boxNo;
        private byte boardId;
        private byte boxId;

        public CaculateId(String boxNo) {
            this.boxNo = boxNo;
        }

        public byte getBoardId() {
            return boardId;
        }

        public byte getBoxId() {
            return boxId;
        }

        public CaculateId invoke() {
            boardId = -1;
            switch (boxNo.charAt(0)) {
                case '1':
                    switch (boxNo.charAt(1)) {
                        case '1':
                            boardId = 0;
                            boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(2))));
                            break;
                        case '2':
                            boardId = 0;
                            boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(2))) + 9);
                            break;
                        case '3':
                            boardId = 1;
                            boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(2))));
                            break;
                        case '4':
                            boardId = 1;
                            boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(2))) + 9);
                            break;
                    }
                    break;
                case '2':
                    switch (boxNo.charAt(1)) {
                        case '1':
                            boardId = 2;
                            boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(2))));
                            break;
                        case '2':
                            boardId = 2;
                            boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(2))) + 9);
                            break;
                        case '3':
                            boardId = 3;
                            boxId = (byte) ((int) Integer.valueOf(String.valueOf(boxNo.charAt(2))));
                            break;
                        case '4':
                            boardId = 3;
                            boxId = (byte) (Integer.valueOf(String.valueOf(boxNo.charAt(2))) + 9);
                            break;
                    }
                    break;
            }
            return this;
        }
    }*/


    private void startLogcatManager() {
        String folderPath = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// save in SD card first
            folderPath = Constant.fileSavePath;
        } else {// If the SD card does not exist, save in the directory of application.

            folderPath = this.getFilesDir().getAbsolutePath() + File.separator + "BDT-Logcat";
        }
        logcatFileManager = LogcatFileManager.getInstance();
        logcatFileManager.start(folderPath);
    }

    private void stopLogcatManager() {
        logcatFileManager.stop();
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }


    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    //Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    //Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    //Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    //Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    //Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    //Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = PLAY_VIDEO;
                            mUIHandler.sendMessage(msg);
                        }
                    }).start();
                    //Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    //Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    //Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    //Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    //Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    //Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    //Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;

                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    //Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    //Log.i("USER_EVENT", "unknow");
                    break;
            }
        }
    }


    /**
     * 计时线程
     */
    private Runnable mTask01 = new Runnable() {

        @Override
        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            /* 计算User静止不动作的时间间距 */
            /**当前的系统时间 - 上次触摸屏幕的时间 = 静止不动的时间**/
            timePeriod = (long) timeNow.getTime() - (long) lastUpdateTime.getTime();

            /*将静止时间毫秒换算成秒*/
            float timePeriodSecond = ((float) timePeriod / 1000);

            if (timePeriodSecond > mHoldStillTime && isTrue) {
                if (isRunScreenSaver == false) {  //说明没有进入屏保
                    /* 启动线程去显示屏保 */
                    mHandler02.postAtTime(mTask02, intervalScreenSaver);
                    /*显示屏保置为true*/
                    isRunScreenSaver = true;
                } else {
                    /*屏保正在显示中*/
                }
            } else {
                /*说明静止之间没有超过规定时长*/
                isRunScreenSaver = false;
            }
            /*反复调用自己进行检查*/
            mHandler01.postDelayed(mTask01, intervalKeypadeSaver);
        }
    };

    /**
     * 持续屏保显示线程
     */
    private Runnable mTask02 = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isRunScreenSaver == true) {  //如果屏保正在显示，就计算不断持续显示
//				hideOriginalLayout();
                showScreenSaver();
                mHandler02.postDelayed(mTask02, intervalScreenSaver);
            } else {
                mHandler02.removeCallbacks(mTask02);  //如果屏保没有显示则移除线程
            }
        }
    };

    /**
     * 显示屏保
     */
    private void showScreenSaver() {
        frame_content.setVisibility(View.GONE);
        bannerapawait.setVisibility(View.VISIBLE);
        banner.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        updateUserActionTime();
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        updateUserActionTime();
        return super.onTouchEvent(event);
    }

    /*用户有操作的时候不断重置静止时间和上次操作的时间*/
    public void updateUserActionTime() {
        frame_content.setVisibility(View.VISIBLE);
        bannerapawait.setVisibility(View.GONE);
        banner.setVisibility(View.VISIBLE);
        Date timeNow = new Date(System.currentTimeMillis());
        timePeriod = timeNow.getTime() - lastUpdateTime.getTime();
        lastUpdateTime.setTime(timeNow.getTime());
    }


    private CabinetTransactionEventListener cabinetTransactionEventListener = new CabinetTransactionEventListener() {
        @Override
        public void onIotConnectStatusChanged(boolean isConnected) {
            Log.i("TAG", "iot 连接状态：" + (isConnected ? "已连接" : "未连接（断开连接）") + ",设备号：" + cabinetTransaction.getDeviceName());
            //tvloginfo.setText("iot 连接状态：" + (isConnected ? "已连接" : "未连接（断开连接）") + ",设备号：" + cabinetTransaction.getDeviceName());
            if(!isConnected){
                if(lastDkUpdateTime==null){
                    lastDkUpdateTime = new Date(System.currentTimeMillis());
                }else{
                    Date timeNow = new Date(System.currentTimeMillis());
                    /**当前的系统时间 - 上次触摸屏幕的时间 = 静止不动的时间**/
                    timePeriod = (long) timeNow.getTime() - (long) lastDkUpdateTime.getTime();
                    /*将静止时间毫秒换算成秒*/
                    float timePeriodSecond = ((float) timePeriod / 1000);
                    if(timePeriodSecond>=5*60){
                        Log.d("关机", "alarm onReceive reboot");
                        try {
                            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});  //关机
                            proc.waitFor();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }else{
                lastDkUpdateTime=null;
            }
        }

        @Override
        public void onDoorStatusChanged(int i, DoorStatus doorStatus) {
            Log.i("onDoorStatusChanged", "onDoorStatusChanged: 开门");
            //门状态变化时会调用该方法
            if (!Constant.guiNum.equals("") && Constant.guiNum != null) {
                if (Constant.pyGuiJc == 1) {
                    if (doorStatus(doorStatus).equals("已开门")) {
                        gotoFragment(WindowId.GetMealSuccessFragment);
                        getMealDone();
                    } else {
                        Tool.T.showToast(MainActivity.this, "打开柜子失败");
                    }
                } else if (Constant.pyGuiJc == 2) {
                    if (doorStatus(doorStatus).equals("已关门")) {
                        gotoFragment(WindowId.CloseDoorFragment);
                    }
                }
            }
            takeawayOrderlastUpdateTime = new Date(System.currentTimeMillis());
           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Date timeNow = new Date(System.currentTimeMillis());
                    long takeawayOrder = (long) timeNow.getTime() - (long) takeawayOrderlastUpdateTime.getTime();
                    float timePeriodSecond = ((float) takeawayOrder / 1000);
                    if (timePeriodSecond >= 3) {
                        getTakeawayOrder();
                    } else {
                        Log.i("takeawayOrder", "两次开柜时间间隔没有超过3秒不缓存数据，时间差" + takeawayOrder);
                    }
                }
            }, 3000);*/
        }

        @Override
        public void onBoardChecked(List<Integer> existBoardNumberList) {
            //控制板检查后会调用该方法，默认控制板检查时间间隔为5分钟
        }

        @Override
        public void onError(int cellNo, ErrorCode errorCode) {
            //发生错误时调用该方法
            Log.e("TAG", "onError : cellNo = " + cellNo + ", error = " + errorCode.getErrorMessage());
        }
    };


    private String doorStatus(DoorStatus doorStatus) {
        switch (doorStatus) {
            case DOOR_STATUS_OPENED:
                return "已开门";

            default:
                return "已关门";
        }
    }

    /**
     * 数据缓存
     */
    private void getTakeawayOrder() {
        HttpModel.takeawayOrder(
                new HttpUtil.ResponeCallBack() {
                    @Override
                    public void respone(String respone) {
                        Respone respone1 = new Gson().fromJson(respone, Respone.class);
                        if (respone1 == null) {
                            return;
                        }
                        if (0 == respone1.getCode()) {
                            //暂不做处理
                            Type type = new TypeToken<List<TakeawayOrderBean>>() {
                            }.getType();
                            List<TakeawayOrderBean> takeawayOrderBeans = new Gson().fromJson(respone1.getData().toString(), type);
                            for (int i = 0; i < takeawayOrderBeans.size(); i++) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("Id", i);
                                contentValues.put("package_no", takeawayOrderBeans.get(i).getPackageNo());
                                contentValues.put("box_no", takeawayOrderBeans.get(i).getBoxNo());
                                contentValues.put("open_box_key", takeawayOrderBeans.get(i).getOpenBoxKey());
                                contentValues.put("box_state", "0");
                                contentValues.put("takeoutAt", "");
                                dataBaseOpenHelper.insert(Constant.TABLE_NAME, contentValues);
                            }
                        }
                    }
                });
    }


    /*
     * 数据同步
     * */
    private void updateTakeawayOrder(String beans) {
        HttpModel.updatetakeawayOrder(beans,
                new HttpUtil.ResponeCallBack() {
                    @Override
                    public void respone(String respone) {
                        Respone respone1 = new Gson().fromJson(respone, Respone.class);
                        if (respone1 == null) {
                            return;
                        }
                        if (0 == respone1.getCode() && timeDataBase != null) {
                            timeDataBase.cancel();
                            timeDataBase.purge();
                            timeDataBase = null;
                        }
                    }
                });
    }

    /**
     * 取件完成
     */
    private void getMealDone() {
        HttpModel.takeout(
                Constant.openBoxKey,
                Constant.guiNum,
                new HttpUtil.ResponeCallBack() {
                    @Override
                    public void respone(String respone) {
                        Respone respone1 = new Gson().fromJson(respone, Respone.class);
                        if (respone1 == null) {
                            return;
                        }
                        if (0 == respone1.getCode()) {
                            //暂不做处理
                        }
                    }
                });
    }

    /*
     * 主页轮播图
     * */
    private void loadTestDatas() {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //new UserOfficialView(MainActivity.this,frame_content);
            }
        });
        //设置轮播时间
        banner.setDelayTime(10000);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ForegroundToBackground);
        //设置图片集合
        banner.setImages(stringList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        //Glide.with(getBaseContext()).load("http://cyt-resource.oss-cn-shanghai.aliyuncs.com/imgupload/154884208943147888.jpg").into(iv_top);
    }

    /*
     * 待机轮播图
     * */
    private void loadTestApawaitDatas() {
        bannerapawait.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                lastUpdateTime = new Date(System.currentTimeMillis());
                frame_content.setVisibility(View.VISIBLE);
                bannerapawait.setVisibility(View.GONE);
                banner.setVisibility(View.VISIBLE);
            }
        });
        //设置轮播时间
        bannerapawait.setDelayTime(10000);
        //设置banner样式
        bannerapawait.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        bannerapawait.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //设置banner动画效果
        bannerapawait.setBannerAnimation(Transformer.ZoomOut);
        //设置图片集合
        bannerapawait.setImages(stringListapawait);
        //banner设置方法全部调用完毕时最后调用
        bannerapawait.start();
    }


    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY_VIDEO:
                    JCVideoPlayer.releaseAllVideos();
                   /* if (topVideoList.size() == 1) {
                        videoplayer.setUp(topVideoList.get(videotype).getPath()
                                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频播放");
                    } else {
                        videoplayer.setUp(topVideoList.get(videotype).getPath()
                                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频播放");
                        if (videotype == topVideoList.size() - 1) {
                            videotype = 0;
                        } else {
                            videotype++;
                        }
                    }*/
                    if (videotype == 0) {
                        videoplayer.setUp(Constant.fileSavePath + "lx2.mp4"
                                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频播放");
                        videotype = 1;
                    } else {
                        videoplayer.setUp(Constant.fileSavePath + "lx1.mp4"
                                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频播放");
                        videotype = 0;
                    }
                    videoplayer.startVideo();
                    break;
                case PLAY_BANNER:
                    loadTestDatas();
                    break;
                case PLAY_BANNERAPAWAIT:
                    loadTestApawaitDatas();
                    break;
                case PLAY_VIDEOHEIGHT:
                    ViewGroup.LayoutParams params = rel_top.getLayoutParams();
                    if (msg.arg1 == 1) {
                        params.height = 506;
                    } else if (msg.arg1 == 2) {
                        params.height = 607;
                    }
                    rel_top.setLayoutParams(params);
                    break;
                case PY_START:
                    cabinetTransaction.start(Constant.pypwd, Constant.dev, Constant.pytype, 1, Constant.guiBean.getNumber(), cabinetTransactionEventListener);
                    break;
                case 12345:
                    tvloginfo.setText(msg.obj.toString());
                    break;
                case MSG_CLOSE_CHECK_PENDING:
                    Log.i("TCP", "开始添加");
                    synchronized (boxesPendingList) {
                        for (int i = 0; i < boxesPendingList.length; i++) {
                            if (boxesPendingList[i].boxId == 0) {//无效的格子编号，当前位置 可以存储格子数据
                                try {
                                    Thread.sleep(500);
                                    CloseCheckBox closeCheckBox = (CloseCheckBox) msg.obj;
                                    boxesPendingList[i] = closeCheckBox;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    Log.i("TCP", ">锁boxesPendingList2<" + e.toString());
                                }
                                break;
                            }
                        }
                    }
                    Log.i("TCP", "结束添加");
                    break;
                default:
                    break;
            }
        }
    };


    /*
     * 定位
     * */
    private void doMyLoc() {
        if (mLocationHelper.getLastLocation() != null) {
            animateTo(mLocationHelper.getLastLocation()); // 已有最新位置
        } else if (mLocationHelper.isStarted()) {
            Log.i("isStarted", "正在定位"); // 当前正在定位
        } else {
            Log.i("Started", "开始定位");
            mLocationHelper.start(new Runnable() {
                public void run() {
                    animateTo(mLocationHelper.getLastLocation());
                }
            });
        }
    }

    private void animateTo(TencentLocation location) {
        if (location == null) {
            return;
        }
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    /*
     * 闹钟
     * */
    private void alarmRestart() {
        //获取telephonyManager
        Calendar calendar = Calendar.getInstance();

        /*** 定制每日5:00执行方法 ***/

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date date=calendar.getTime(); //第一次执行定时任务的时间
        System.out.println(date);
        System.out.println("before 方法比较："+date.before(new Date()));
        //如果第一次执行定时任务的时间 小于 当前的时间
        //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
        if (date.before(new Date())) {
            date = addDay(date, 1);
            System.out.println(date);
        }
        timeralarmRestart = new Timer();
        timeralarmRestart.schedule(new TimerTask() {
            @Override
            public void run() {
                    Log.d("关机", "alarm onReceive reboot");
                    try {
                        finish();
                        Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});  //关机
                        proc.waitFor();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            }
        }, date, PERIOD_DAY);
       /* Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("activity.MyWeathe.alarm");
        pi = PendingIntent.getService(this, 0, intent, 0);

        long firstTime = System.currentTimeMillis();    //获取系统当前时间
        Log.i("elapsedRealtime", "elapsedRealtime: "+stampToDate(firstTime));
        long systemTime = System.currentTimeMillis();//java.lang.System.currentTimeMillis()，它返回从 UTC 1970 年 1 月 1 日午夜开始经过的毫秒数。
        Log.i("currentTimeMillis", "currentTimeMillis: "+stampToDate(systemTime));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); //  这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 20);//设置为8：00点提醒
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //选择的定时时间
        long selectTime = calendar.getTimeInMillis();    //计算出设定的时间
        Log.i("selectTime", "selectTime: "+stampToDate(selectTime));
        //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
            Log.i("selectTimeUpdate", "selectTime: "+stampToDate(selectTime));
        }

        long time = selectTime - systemTime;// 计算现在时间到设定时间的时间差
        Log.i("time", "time: "+stampToDate(time));
        long my_Time = firstTime + time;//系统 当前的时间+时间差
        Log.i("my_Time", "my_Time: "+stampToDate(my_Time));
        // 进行闹铃注册
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, selectTime, AlarmManager.INTERVAL_DAY, pi);*/
    }


    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static void main(String[] args) {
        //long firstTime = SystemClock.elapsedRealtime();    //获取系统当前时间
        long systemTime = System.currentTimeMillis();//java.lang.System.currentTimeMillis()，它返回从 UTC 1970 年 1 月 1 日午夜开始经过的毫秒数。
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); //  这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 20);//设置为8：00点提醒
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //选择的定时时间
        long selectTime = calendar.getTimeInMillis();    //计算出设定的时间
        System.out.println("设定的时间" + calendar.getTime());
        //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }

        long time = selectTime - systemTime;// 计算现在时间到设定时间的时间差
        System.out.println("现在时间到设定时间的时间差" + time);
        //long my_Time = firstTime + time;//系统 当前的时间+时间差
        System.out.println("系统");
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
        bannerapawait.startAutoPlay();
    }


    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
        bannerapawait.stopAutoPlay();
    }

    @Override
    protected void onResume() {
        /*activity显示的时候启动线程*/
        mHandler01.postAtTime(mTask01, intervalKeypadeSaver);
        super.onResume();
        if (videoplayer != null) {
            videoplayer.startVideo();
        }
    }

    //private CloseCheckBox closeCheckBox;
    //private Iterator<CloseCheckBox> it;


    /*
     * 关门检查
     * */
    public void automaticCloseTheDoor() {
        timerAutomaticClose= new Timer();
        timerAutomaticClose.schedule(new TimerTask() {
            @Override
            public void run() {
                //PendingList中的数据挪过来；
                Log.i("TCP", "开始定时任务");
                synchronized (boxesPendingList) {
                    try {
                        Thread.sleep(500);
                        for (int i = 0; i < boxesWaitingList.length; i++) {
                            if (boxesWaitingList[i].boxId == 0) {
                                for (int j = 0; j < boxesPendingList.length; j++) {
                                    if (boxesPendingList[j].boxId != 0) {
                                        boxesWaitingList[i].storeInOrTakeOut = boxesPendingList[j].storeInOrTakeOut;
                                        boxesWaitingList[i].boxId = boxesPendingList[j].boxId;
                                        boxesWaitingList[i].boardId = boxesPendingList[j].boardId;
                                        boxesWaitingList[i].serialNo = boxesPendingList[j].serialNo;
                                        boxesWaitingList[i].second = boxesPendingList[j].second;
                                        boxesWaitingList[i].status = boxesPendingList[j].status;
                                        boxesWaitingList[i].boxNo = boxesPendingList[j].boxNo;
                                        boxesWaitingList[i].mobile = boxesPendingList[j].mobile;
                                        boxesPendingList[j].boxId = 0;
                                        //Log.i("TCP", ">信息挪移成功<" + boxesWaitingList[i].serialNo);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        Log.i("TCP", ">锁boxesPendingList1<" + e.toString());
                        e.printStackTrace();
                    }
                }
               /* Log.i("TCP", ">boxesWaitingList开始<");
                Log.i("TCP", ">存还是取<" + boxesWaitingList[0].boxId);*/
                for (int k = 0; k < boxesWaitingList.length; k++) {
                  /*  Log.i("TCP", ">存还是取<" + boxesWaitingList[k].storeInOrTakeOut);
                    Log.i("TCP", ">箱子号<" + boxesWaitingList[k].boardId);
                    Log.i("TCP", ">格子号<" + boxesWaitingList[k].boxId);
                    Log.i("TCP", ">格子状态<" + boxesWaitingList[k].status);*/
                    if (boxesWaitingList[k].boxId != 0 && boxesWaitingList[k].status == CloseCheckBox.IDLE) {
                        boxesWaitingList[k].status = CloseCheckBox.BUSY;
                        if (boxesWaitingList[k].second == 30) {
                            HttpModel.cupNoClose(boxesWaitingList[k].serialNo, new HttpUtil.ResponeCallBack() {
                                @Override
                                public void respone(String respone) {

                                }
                            });
                            boxesWaitingList[k].status = CloseCheckBox.IDLE;
                            boxesWaitingList[k].second = boxesWaitingList[k].second + 5;
                            Log.i("TCP", ">30描消息反馈<" + boxesWaitingList[k].serialNo);
                        } else {
                            final int location = k;
                            AidlModel.queryBoxStatusById((byte) boxesWaitingList[k].boardId, (byte) boxesWaitingList[location].boxId, new AidlSuccessCallBack() {
                                @Override
                                public void callback(Result result) {
                                    if (0 == result.getCode()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result.getData());
                                            if ("0".equals(jsonObject.getString("openStatus"))) {
                                                if (boxesWaitingList[location].storeInOrTakeOut == CloseCheckBox.STOREIN) {//存件并且存在物品
                                                    Log.i("TCP", ">STOREIN 存并且有<" + boxesWaitingList[location].serialNo);
                                                    HttpModel.goodsInspection(boxesWaitingList[location].serialNo, "1", new HttpUtil.ResponeCallBack() {
                                                        @Override
                                                        public void respone(String respone) {

                                                        }
                                                    });
                                                    boxesWaitingList[location].boxId = 0;
                                                } else if (boxesWaitingList[location].storeInOrTakeOut == CloseCheckBox.TAKEOUT) {//取餐并且物品不存在
                                                    Log.i("TCP", ">TAKEOUT 取并且有<" + boxesWaitingList[location].serialNo);
                                                    HttpModel.cupTakeoutNoClose(boxesWaitingList[location].mobile, boxesWaitingList[location].boxNo, boxesWaitingList[location].serialNo, new HttpUtil.ResponeCallBack() {
                                                        @Override
                                                        public void respone(String respone) {

                                                        }
                                                    });
                                                    boxesWaitingList[location].boxId = 0;
                                                }
                                            } else {
                                                boxesWaitingList[location].status = CloseCheckBox.IDLE;
                                                boxesWaitingList[location].second = boxesWaitingList[location].second + 5;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        boxesWaitingList[location].status = CloseCheckBox.IDLE;
                                        boxesWaitingList[location].second = boxesWaitingList[location].second + 5;
                                    }
                                }
                            });
                        }
                    } else {
                        boxesWaitingList[k].status = CloseCheckBox.IDLE;
                        boxesWaitingList[k].second = boxesWaitingList[k].second + 5;
                    }
                }
                Log.i("TCP", ">boxesWaitingList<结束");
            }
        }, 0, 1000 * 5);

    }


    private class CloseCheckBox {
        final static int STOREIN = 0;
        final static int TAKEOUT = 1;

        final static int IDLE = 0;
        final static int BUSY = 1;

        int storeInOrTakeOut;//0 存件， 1 取出
        int id;
        int boxId;//格子编号
        int boardId; //箱子编号
        String serialNo;//编号
        String mobile;//手机号
        String boxNo;//格子编号服务端
        int second;//已经等待了多久的时间
        int status;//0=IDLE 当前格子未处于查询中，1=BUSY 当前格子出去查询中
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogcatManager();
        if (connect != null) {
            connect.disconnect();
        }

        if (am != null && pi != null) {
            am.cancel(pi);
        }

        if(timerAutomaticClose!=null){
            timerAutomaticClose.cancel();
            timerAutomaticClose.purge();
        }

        if(timeralarmRestart!=null){
            timeralarmRestart.cancel();
            timeralarmRestart.purge();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        mHandler01.removeCallbacks(mTask01);
        mHandler02.removeCallbacks(mTask02);
    }

    public class UserOfficialView extends PopupWindow {

        public UserOfficialView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.view_user_service, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setText("顶部：（宽：" + rel_top.getWidth() + "高:" + rel_top.getHeight() + "）/n"
                    + "中间：（宽：" + frame_content.getWidth() + "高：" + frame_content.getHeight() + ")/n"
                    + "底部：（宽" + banner.getWidth() + "高：" + banner.getHeight() + ")");
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }
    }
}


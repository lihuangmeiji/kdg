package org.tabjin.myapplication.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.ipinyuan.communication.library.utils.SharedPreferenceHelper;

import org.tabjin.myapplication.R;

import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.base.Constant;

import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.model.NetUtils;
import org.tabjin.myapplication.ui.IRouter;
import org.tabjin.myapplication.ui.MainActivity;
import org.tabjin.myapplication.ui.admin.AdminHomeActivity;
import org.tabjin.myapplication.ui.admin.AdminLoginActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;


/**
 * Created by hspcadmin on 2018/8/23.
 * 首页fragemnt
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View getMeal, putMeal;
    private TextView tv_date;
    private TextView tv_location;
    private IRouter mRouter;
    private ImageView iv_wifiinfo;
    private ImageView iv_homelogo;

    final int SHOW_TIME = 12;
    Timer timer = null;
    int index = 1;

    public TelephonyManager mTelephonyManager;
    public PhoneStatListener mListener;

    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.home_fragment, container, false);

        if (getActivity() instanceof IRouter) {
            mRouter = (IRouter) getActivity();
        }
        showTime();
        getMeal = contentView.findViewById(R.id.iv_get);
        putMeal = contentView.findViewById(R.id.iv_put);
        tv_date = contentView.findViewById(R.id.tv_date);
        iv_homelogo = contentView.findViewById(R.id.iv_homelogo);
        tv_location = contentView.findViewById(R.id.tv_location);
        if (Constant.guiBean != null) {
            tv_location.setText(Constant.guiBean.getLocation());
        }
        iv_wifiinfo = contentView.findViewById(R.id.iv_wifiinfo);
        int wifiLevel = NetUtils.getNetworkWifiLevel(getContext());
      /*  if (wifiLevel == 1) {
            iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg2);
        } else if (wifiLevel == 2) {
            iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg3);
        } else if (wifiLevel == 3) {
            iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg4);
        } else if (wifiLevel == 4) {
            iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg5);
        } else if (wifiLevel == 5) {
            iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg6);
        } else {
            iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg);
        }*/
        //获取telephonyManager
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        //开始监听
        mListener = new PhoneStatListener();
        //监听信号强度
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
        getMeal.setOnClickListener(this);
        putMeal.setOnClickListener(this);
        iv_homelogo.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mTelephonyManager!=null&&mListener!=null){
            mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //用户不在当前页面时，停止监听
        if(mTelephonyManager!=null&&mListener!=null){
            mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_NONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_get:
                if (mRouter != null) {
                    mRouter.gotoFragment(WindowId.GetMealSelectFragment);
                }
                break;
            case R.id.iv_put:
                if (mRouter != null) {
                    mRouter.gotoFragment(WindowId.PutMealFragment);
                }
                break;
            case R.id.iv_homelogo:
                if (index >= 5) {
                    index = 1;
                    Intent intent = new Intent(getContext(), AdminLoginActivity.class);
                    startActivity(intent);
                } else {
                    index++;
                    //Tool.T.showToast(getContext(),"开启后台管理需要连续点击5次，已点击"+index);
                }
                break;
        }
    }

    public void showTime() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = mUIHandler.obtainMessage(SHOW_TIME);
                    msg.sendToTarget();
                }
            }, 0, 1000);
        }
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_TIME:
                    tv_date.setText(simpleDateFormat2.format(new Date()));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }


    @SuppressWarnings("deprecation")
    private class PhoneStatListener extends PhoneStateListener {
        //获取信号强度


        @Override
        public void onSignalStrengthChanged(int asu) {
            super.onSignalStrengthChanged(asu);
            Log.i("SignalStrength", "asu===========" + asu);
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            Log.i("SignalStrength", "获取信号强度===========");
            //获取网络信号强度
            //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int level = signalStrength.getLevel();
                Log.i("SignalStrength", "level====" + level);
            }
            int cdmaDbm = signalStrength.getCdmaDbm();
            int evdoDbm = signalStrength.getEvdoDbm();
            System.out.println("cdmaDbm=====" + cdmaDbm);
            System.out.println("evdoDbm=====" + evdoDbm);
            Log.i("SignalStrength", "cdmaDbm=====" + cdmaDbm);
            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
            int dbm = -113 + 2 * gsmSignalStrength;
            System.out.println("dbm===========" + dbm);
            Log.i("SignalStrength", "dbm===========" + dbm);
            if (dbm >= 65&&dbm <=85) {
                iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg3);
            } else if (dbm>85&&dbm <= 95) {
                iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg4);
            } else if (dbm>95&&dbm <= 105) {
                iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg5);
            } else if (dbm>105&&dbm <=115) {
                iv_wifiinfo.setBackgroundResource(R.mipmap.homewifiimg6);
            } else {
                iv_wifiinfo.setVisibility(View.GONE);
            }
        }
    }




}

package org.tabjin.myapplication.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzdongcheng.drivers.bean.Result;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.base.fragment.TitleBaseFragment;
import org.tabjin.myapplication.model.AidlCallBack;
import org.tabjin.myapplication.model.AidlModel;
import org.tabjin.myapplication.ui.IRouter;

/**
 * Created by hspcadmin on 2018/8/25.
 * 柜门未关闭
 */

public class DoorNotClosedFragment extends TitleBaseFragment {

    private Handler doorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                AidlModel.queryBoxStatusById(Constant.boardid,Constant.boxId, new AidlCallBack(){

                    @Override
                    public void callback(Result result) {
                        if(0==result.getCode()){
                            if (mRouter != null) {
                                mRouter.gotoFragment(WindowId.HomeFragment);
                            }
                        }else{
                            sendEmptyMessageDelayed(0, 1000);
                        }
                    }

                    @Override
                    public void handlerException(Exception e) {
                        sendEmptyMessageDelayed(0, 1000);
                    }
                });
            }
        }
    };

   private IRouter mRouter;

    public static DoorNotClosedFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DoorNotClosedFragment fragment = new DoorNotClosedFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_door_not_closed, container, false);
        if(getActivity() instanceof IRouter){
            mRouter = (IRouter) getActivity();
        }
        ((TextView)titleView.findViewById(R.id.tv_title_right)).setText("");
        ((TextView)titleView.findViewById(R.id.tv_title)).setText("取餐");
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AidlModel.getInstance() != null) {
            doorHandler.sendEmptyMessageAtTime(0, 1000);
        }
    }

    @Override
    protected void clickLeft(View v) {
        if (mRouter != null) {
            mRouter.gotoFragment(WindowId.HomeFragment);
        }
    }

    @Override
    protected void clickRightTv(View v) {

    }
}

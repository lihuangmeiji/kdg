package org.tabjin.myapplication.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.base.fragment.TitleBaseFragment;
import org.tabjin.myapplication.ui.IRouter;

import java.util.Locale;

/**
 * Created by hspcadmin on 2018/8/25.
 * 取餐成功
 */

public class CloseDoorFragment extends TitleBaseFragment {

    private IRouter mRouter;

    private CountDownTimer mTimer;

    private Button btnHome;

    public static CloseDoorFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CloseDoorFragment fragment = new CloseDoorFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_close_door, container, false);
        if(getActivity() instanceof IRouter) {
            mRouter = (IRouter) getActivity();
        }
        contentView.findViewById(R.id.goon_get_meal).setOnClickListener(this);
        contentView.findViewById(R.id.go_home).setOnClickListener(this);
        ((TextView)titleView.findViewById(R.id.tv_title_right)).setText("");
        ((TextView)titleView.findViewById(R.id.tv_title)).setText("取餐");

        btnHome = contentView.findViewById(R.id.go_home);

        if(mTimer == null) {
            mTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btnHome.setText(String.format(Locale.getDefault(),"返回首页%dS",millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    mRouter.gotoFragment(WindowId.HomeFragment);
                }
            };
        }
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mTimer!=null){
            mTimer.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.goon_get_meal:
                if (mRouter != null) {
                    mRouter.gotoFragment(WindowId.GetMealFragment);
                }
                break;
            case R.id.go_home:
                if (mRouter != null) {
                    mRouter.gotoFragment(WindowId.HomeFragment);
                }
                break;
        }
    }

    @Override
    protected void clickLeft(View v) {

    }

    @Override
    protected void clickRightTv(View v) {

    }
}

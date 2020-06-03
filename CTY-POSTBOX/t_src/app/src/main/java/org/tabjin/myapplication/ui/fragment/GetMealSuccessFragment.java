package org.tabjin.myapplication.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hzdongcheng.drivers.bean.Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.base.fragment.TitleBaseFragment;
import org.tabjin.myapplication.model.AidlModel;
import org.tabjin.myapplication.model.AidlSuccessCallBack;
import org.tabjin.myapplication.ui.IRouter;

/**
 * Created by hspcadmin on 2018/8/25.
 * 取餐成功
 */

public class GetMealSuccessFragment extends TitleBaseFragment {

    private IRouter mRouter;

    private CountDownTimer timer;

    private TextView tvSecond;

    public static GetMealSuccessFragment newInstance() {

        Bundle args = new Bundle();

        GetMealSuccessFragment fragment = new GetMealSuccessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_get_meal_success, container, false);
        if(getActivity() instanceof IRouter) {
            mRouter = (IRouter) getActivity();
        }
        TextView numTv = contentView.findViewById(R.id.tv_cupboard_num);
        numTv.setOnClickListener(this);
        numTv.setText(Constant.guiNum);
        ((TextView)titleView.findViewById(R.id.tv_title)).setText(R.string.get_meal);
        ((TextView)titleView.findViewById(R.id.tv_title_right)).setText("");
        tvSecond = contentView.findViewById(R.id.tv_second);
        if(timer == null) {
            if(timer == null) {
                timer = new CountDownTimer(15000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvSecond.setText(millisUntilFinished / 1000 + "S");
                        if(Constant.guiBean.getManufacturer().equals("dc")){
                            AidlModel.queryBoxStatusById(Constant.boardid, Constant.boxId, new AidlSuccessCallBack() {
                                @Override
                                public void callback(Result result) {
                                    if (0 == result.getCode()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result.getData());
                                            if("0".equals(jsonObject.getString("openStatus "))){
                                                mRouter.gotoFragment(WindowId.CloseDoorFragment);
                                                timer.cancel();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }else if(Constant.guiBean.getManufacturer().equals("py")){
                            Constant.pyGuiJc=2;
                            Log.e("millisUntilFinished", "开门成功以后倒计时"+millisUntilFinished);
                        }
                    }

                    @Override
                    public void onFinish() {
                        tvSecond.setText("");
                        mRouter.gotoFragment(WindowId.DoorNotClosedFragment);
                    }
                };
            }
        }
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(timer!=null) {
            timer.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cupboard_num:
                if (mRouter != null) {
                    mRouter.gotoFragment(WindowId.CloseDoorFragment);
                }
                break;
            case R.id.iv_title_left:
                if (mRouter != null) {
                    mRouter.gotoFragment(WindowId.GetMealFragment);
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

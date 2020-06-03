package org.tabjin.myapplication.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzdongcheng.drivers.bean.Result;

import net.ipinyuan.communication.out.CabinetTransaction;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.base.fragment.TitleBaseFragment;
import org.tabjin.myapplication.model.AidlModel;
import org.tabjin.myapplication.model.AidlSuccessCallBack;
import org.tabjin.myapplication.model.DataBaseOpenHelper;
import org.tabjin.myapplication.model.HttpModel;
import org.tabjin.myapplication.model.HttpUtil;
import org.tabjin.myapplication.model.bean.BoxBean;
import org.tabjin.myapplication.model.bean.Data;
import org.tabjin.myapplication.model.bean.Order;
import org.tabjin.myapplication.model.bean.Respone;
import org.tabjin.myapplication.model.bean.VerifyBean;
import org.tabjin.myapplication.ui.IRouter;
import org.tabjin.myapplication.ui.adapter.CupboardAdapter;
import org.tabjin.myapplication.ui.widget.NumClickListener;
import org.tabjin.myapplication.ui.widget.WheelView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by hspcadmin on 2018/8/25.
 * 取餐
 */

public class GetMealFragment extends TitleBaseFragment {

    private IRouter mRouter;

    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list3 = new ArrayList<>();


    private WheelAdapter adapter1;

    private WheelAdapter adapter2;

    private WheelAdapter adapter3;

    private WheelView wheelView1;
    private WheelView wheelView2;
    private WheelView wheelView3;

    private int selected1 = 1, selected2 = 0, selected3 = 0;

    private CountDownTimer timer;

    private TextView tvRight;

    private TextView tvError;

    //手机尾号
    private String phoneTail = "";

    //展示手机尾号的Tv
    private TextView[] tailTvs = new TextView[4];

    //匹配到得order订单
    private VerifyBean mOrder;

    private CabinetTransaction cabinetTransaction;
    private DataBaseOpenHelper dataBaseOpenHelper;

    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    private static BroadcastReceiver mReceiver;

    public static GetMealFragment newInstance(CabinetTransaction cabinetTransaction, DataBaseOpenHelper dataBaseOpenHelper) {

        Bundle args = new Bundle();
        GetMealFragment fragment = new GetMealFragment();
        fragment.setArguments(args);
        fragment.cabinetTransaction = cabinetTransaction;
        fragment.dataBaseOpenHelper = dataBaseOpenHelper;
        return fragment;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_get_meal, container, false);
        if (getActivity() instanceof IRouter) {
            mRouter = (IRouter) getActivity();
        }
        ((TextView) titleView.findViewById(R.id.tv_title)).setText("取餐");
        contentView.findViewById(R.id.btn_sure).setOnClickListener(this);
        tvRight = titleView.findViewById(R.id.tv_title_right);
        tvError = contentView.findViewById(R.id.tv_error);

        //初始化倒计时
        initTimer();

        if (Constant.guiBean.getManufacturer().equals("dc")) {
            //初始化柜子选择控件
            initWheelView(contentView);
        } else if (Constant.guiBean.getManufacturer().equals("py")) {
            initWheelViewPy(contentView);
        }
        //初始化键盘
        initKeyboard(contentView);

        return contentView;
    }

    /**
     * 倒计时
     */
    private void initTimer() {
        if (timer == null) {
            timer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRight.setText(millisUntilFinished / 1000 + "S");
                }

                @Override
                public void onFinish() {
                    tvRight.setText("");
                    mRouter.gotoFragment(WindowId.HomeFragment);
                }
            };
        }
    }

    /**
     * 键盘
     *
     * @param contentView
     */
    private void initKeyboard(View contentView) {
        tailTvs[0] = contentView.findViewById(R.id.tv_tail1);
        tailTvs[1] = contentView.findViewById(R.id.tv_tail2);
        tailTvs[2] = contentView.findViewById(R.id.tv_tail3);
        tailTvs[3] = contentView.findViewById(R.id.tv_tail4);

        phoneTail = "";
        setKeyBoardListener(contentView, new NumClickListener() {
            @Override
            public void onNumClick(View view, String num) {
                inputTail(num);
                if (phoneTail.length() < 4) {
                    handleInputErro(false);
                }
            }

            @Override
            public void onClearClick(View view) {
                inputTail("C");
                if (tvError.getVisibility() == View.VISIBLE) {
                    handleInputErro(false);
                }
            }

            @Override
            public void onBackClick(View view) {
                inputTail("-1");
                if (tvError.getVisibility() == View.VISIBLE) {
                    handleInputErro(false);
                }
            }
        });
    }

    /**
     * 柜子选择滑轮控件
     *
     * @param contentView
     */
    private void initWheelView(View contentView) {
        Constant.guiNum = "";
        selected1 = 0;
        selected2 = 0;
        wheelView1 = contentView.findViewById(R.id.wv_cupboard_picker1);
        wheelView2 = contentView.findViewById(R.id.wv_cupboard_picker2);
        wheelView3 = contentView.findViewById(R.id.wv_cupboard_picker3);
        wheelView3.setVisibility(View.GONE);
        list1.clear();
        list1.add("A");
        list1.add("B");
        list1.add("C");
        list1.add("D");
        list2.clear();
        for (int i = 1; i < 10; i++) {
            list2.add(String.valueOf(i));
//            list2.add(String.format(Locale.getDefault(),"%02d",i));
        }
        adapter1 = new CupboardAdapter<>(list1);
        adapter2 = new CupboardAdapter<>(list2);
        wheelView1.setDataSources(list1);
        wheelView2.setDataSources(list2);
        wheelView1.setSelectedLineColor(getResources().getColor(R.color.colorPrimary));
        wheelView2.setSelectedLineColor(getResources().getColor(R.color.colorPrimary));
        wheelView1.setNormalTextColor(getResources().getColor(R.color.colorPrimary));
        wheelView1.setNormalTextColor(getResources().getColor(R.color.bgAA));
        wheelView2.setNormalTextColor(getResources().getColor(R.color.bgAA));
        wheelView1.setSelectPosition(selected1);
        wheelView2.setSelectPosition(selected2);
        wheelView1.setTextSize(36);
        wheelView2.setTextSize(36);
        wheelView1.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
        wheelView2.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
        wheelView1.setVisibilityCount(3);
        wheelView2.setVisibilityCount(3);

        wheelView1.setTextGravity(Gravity.CENTER);
        wheelView2.setTextGravity(Gravity.CENTER);
        wheelView1.setCallBack(new WheelView.CallBack() {
            @Override
            public void onPositionSelect(int index) {
                selected1 = index;
            }
        });
        wheelView2.setCallBack(new WheelView.CallBack() {
            @Override
            public void onPositionSelect(int index) {
                selected2 = index;
            }
        });
    }


    /**
     * 柜子选择滑轮控件
     *
     * @param contentView
     */
    private void initWheelViewPy(View contentView) {
        Constant.guiNum = "";
        selected1 = 1;
        selected2 = 0;
        selected2 = 0;
        wheelView1 = contentView.findViewById(R.id.wv_cupboard_picker1);
        wheelView2 = contentView.findViewById(R.id.wv_cupboard_picker2);
        wheelView3 = contentView.findViewById(R.id.wv_cupboard_picker3);
        wheelView3.setVisibility(View.VISIBLE);
        list1.clear();
        list1.add("A");
        list1.add("B");
        list1.add("C");

        list2.clear();
        for (int i = 1; i < 5; i++) {
            list2.add(String.valueOf(i));
        }

        list3.clear();
        for (int i = 1; i < 8; i++) {
            list3.add(String.valueOf(i));
        }
        adapter1 = new CupboardAdapter<>(list1);
        adapter2 = new CupboardAdapter<>(list2);
        adapter3 = new CupboardAdapter<>(list3);

        wheelView1.setDataSources(list1);
        wheelView2.setDataSources(list2);
        wheelView3.setDataSources(list3);

        wheelView1.setSelectedLineColor(getResources().getColor(R.color.colorPrimary));
        wheelView2.setSelectedLineColor(getResources().getColor(R.color.colorPrimary));
        wheelView3.setSelectedLineColor(getResources().getColor(R.color.colorPrimary));

        wheelView1.setNormalTextColor(getResources().getColor(R.color.colorPrimary));
        wheelView1.setNormalTextColor(getResources().getColor(R.color.bgAA));
        wheelView2.setNormalTextColor(getResources().getColor(R.color.bgAA));
        wheelView3.setNormalTextColor(getResources().getColor(R.color.bgAA));

        wheelView1.setSelectPosition(selected1);
        wheelView2.setSelectPosition(selected2);
        wheelView3.setSelectPosition(selected3);

        wheelView1.setTextSize(30);
        wheelView2.setTextSize(30);
        wheelView3.setTextSize(30);

        wheelView1.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
        wheelView2.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
        wheelView3.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));

        wheelView1.setVisibilityCount(3);
        wheelView2.setVisibilityCount(3);
        wheelView3.setVisibilityCount(3);

        wheelView1.setTextGravity(Gravity.CENTER);
        wheelView2.setTextGravity(Gravity.CENTER);
        wheelView3.setTextGravity(Gravity.CENTER);

        wheelView1.setCallBack(new WheelView.CallBack() {
            @Override
            public void onPositionSelect(int index) {
                selected1 = index;
            }
        });
        wheelView2.setCallBack(new WheelView.CallBack() {
            @Override
            public void onPositionSelect(int index) {
                selected2 = index;
            }
        });
        wheelView3.setCallBack(new WheelView.CallBack() {
            @Override
            public void onPositionSelect(int index) {
                selected3 = index;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (timer != null) {
            timer.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sure:
                checkPhoneTail();
                break;
        }
    }

    private void handleInputErro(boolean isError) {
        if (isError) {
            for (TextView tv : tailTvs) {
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            tvError.setVisibility(View.VISIBLE);
            wheelView1.setSelectedTextColor(getResources().getColor(R.color.colorAccent));
            wheelView2.setSelectedTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            for (TextView tv : tailTvs) {
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            tvError.setVisibility(View.GONE);
            wheelView1.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
            wheelView2.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    /**
     * 检查手机尾号和柜门编号
     */
    private void checkPhoneTail() {

        if (phoneTail.length() < 4) {
            handleInputErro(true);
            return;
        }

        if (Constant.guiBean.getManufacturer().equals("dc")) {
            //计算箱号id
            caculateBoxId();
            customerVerify(list1.get(selected1) + list2.get(selected2) + phoneTail);
        } else if (Constant.guiBean.getManufacturer().equals("py")) {
            //caculatePyBoxId();
            Constant.openBoxKey = list1.get(selected1) + list2.get(selected2) + list3.get(selected3) + phoneTail;
            //Constant.pingIsInternetConnect()
            if (1==1) {
                customerVerify(list1.get(selected1) + list2.get(selected2) + list3.get(selected3) + phoneTail);
            } else {
                Constant.brokenNetwork=1;
                Cursor cursor = dataBaseOpenHelper.query(Constant.TABLE_NAME, " where open_box_key =" + list1.get(selected1) + list2.get(selected2) + list3.get(selected3) + phoneTail);
                if (cursor.moveToFirst() == false)
                {
                    tvError.post(new Runnable() {
                        @Override
                        public void run() {
                            Constant.guiNum = "";
                            Tool.T.showToast(tvError.getContext(), "验证失败");
                            handleInputErro(true);
                        }
                    });
                }else{
                    ContentValues cv = new ContentValues();
                    cv.put("box_state","1");
                    cv.put("takeoutAt",simpleDateFormat2.format(new Date()));
                    dataBaseOpenHelper.update(Constant.TABLE_NAME,cv," where open_box_key =" + list1.get(selected1) + list2.get(selected2) + list3.get(selected3) + phoneTail,null);
                    Constant.pyGuiJc = 1;
                    Constant.guiNum = list1.get(selected1) + list2.get(selected2) + list3.get(selected3);
                    cabinetTransaction.openCell(caculatePyBoxId());
                }
            }
        }
        //先查询用户订单
//        queryOrder();
        //直接验证
    }

    /**
     * 查询用户订单
     */
    private void queryOrder() {
        HttpModel.queryCustomer(phoneTail, "0", new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {
                Type type = new TypeToken<Respone<Data<List<Order>>>>() {
                }.getType();
                Respone<Data<List<Order>>> bean = new Gson().fromJson(respone, type);
                if (bean == null) {
                    return;
                }
                if (0 == bean.getCode()) {
                    if (bean.getData() == null) return;
                    List<Order> orders = bean.getData().getData();
                    //箱号验证是否通过
                    Boolean isRight = false;
                    if (orders != null && orders.size() > 0) {
                        for (Order order : orders) {
                            if (order == null) {
                                continue;
                            }
                            //判断箱号选择是否正确   且是未取件状态
                            if ("0".equals(order.getPackage_status()) && String.valueOf(Constant.boxId).equals(order.getBox_no())) {
//                                customerVerify(order);
                                isRight = true;
                                break;
                            }
                        }
                        if (!isRight) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Tool.T.showToast(getActivity(), "箱号选择错误！");
                                }
                            });
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleInputErro(true);
                            }
                        });
                    }


                } else {
//                    Tool.L.e("查询用户信息失败");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleInputErro(true);
                        }
                    });
                }
            }
        });

        
    }

    /**
     * 取件验证
     */
    private void customerVerify(String openBoxKey) {
        HttpModel.customerVerify(phoneTail, openBoxKey, new HttpUtil.ResponeCallBack() {
            @Override
            public void respone(String respone) {

                Log.e("VerifyBean",respone);
                Type typeToken = new TypeToken<Respone<VerifyBean>>() {
                }.getType();

                Respone<VerifyBean> respone1 = new Gson().fromJson(respone, typeToken);
                if (respone1 == null) {
                    tvError.post(new Runnable() {
                        @Override
                        public void run() {
                            handleInputErro(true);
                        }
                    });
                    return;
                }
                if (respone1.getCode() == 0) {
                    Tool.L.e("查询成功");
                    if (Constant.guiBean.getManufacturer().equals("dc")) {
                        openBox();
                    } else if (Constant.guiBean.getManufacturer().equals("py")) {
                        Log.e("caculatePyBoxId", "柜号 = " + caculatePyBoxId() + "selected1:" + selected1 + "selected2" + selected2 + "selected3" + selected3);
                        Constant.pyGuiJc = 1;
                        Constant.guiNum = list1.get(selected1) + list2.get(selected2) + list3.get(selected3);
                        cabinetTransaction.openCell(caculatePyBoxId());
                    }
                } else {
                    tvError.post(new Runnable() {
                        @Override
                        public void run() {
                            Constant.guiNum = "";
                            Tool.T.showToast(tvError.getContext(), "验证失败");
                            handleInputErro(true);
                        }
                    });
                }
            }
        });
    }

    /**
     * aidl 打开柜子
     */
    private void openBox() {

        AidlModel.openBoxById(Constant.boardid, Constant.boxId, new AidlSuccessCallBack() {
            @Override
            public void callback(final Result result) {

                tvError.post(new Runnable() {
                    @Override
                    public void run() {
                        if (0 == (result.getCode())) {
                            Constant.guiNum = list1.get(selected1) + list2.get(selected2);
                            mRouter.gotoFragment(WindowId.GetMealSuccessFragment);
                            //取件完成
                            getMealDone();
                        } else {
                            Tool.T.showToast(tvError.getContext(), String.format(Locale.getDefault(), "打开柜子失败%d", result.getCode()));
//                            mRouter.gotoFragment(WindowId.GetMealSuccessFragment);
                        }
                    }
                });

            }
        });
    }

    /**
     * 取件完成
     */
    private void getMealDone() {
        HttpModel.takeout(
                list1.get(selected1) + list2.get(selected2) + phoneTail,
                list1.get(selected1) + list2.get(selected2),
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

    private void caculateBoxId() {
        if (selected1 > 1) {
            Constant.boardid = 1;
            Constant.boxId = (byte) ((selected1 - 2) * 9 + selected2 + 1);
        } else {
            Constant.boardid = 0;
            Constant.boxId = (byte) ((selected1) * 9 + selected2 + 1);
        }
    }

    private int caculatePyBoxId() {
        int guinum = 0;
        switch (selected1) {
            case 0:
                guinum = 28 + selected3;
                break;
            case 1:
                guinum = selected2 * 7 + (selected3 + 1);
                break;
            case 2:
                guinum = 31 + selected2 * 7 + (selected3 + 1);
                break;
        }
        return guinum;
    }

    public static void main(String[] args) {
        int aa = View.MeasureSpec.getSize(27);
       /* //c11 200
        int selected1 = 2;
        int selected2 = 0;
        int selected3 = 0;
        int guinum = 0;
        switch (selected1) {
            case 0:
                guinum = 28 + selected3;
                break;
            case 1:
                guinum = selected2* 7 + (selected3+1);
                break;
            case 2:
                guinum = 31 + selected2* 7 + (selected3+1);
                break;
        }*/
        System.out.println("guinum= " + aa);
    }

    @Override
    protected void clickLeft(View v) {
        if (null != mRouter) {
            mRouter.gotoFragment(WindowId.HomeFragment);
        }
    }

    @Override
    protected void clickRightTv(View v) {

    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setKeyBoardListener(View view, final NumClickListener listener) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = ((TextView) v).getText().toString();
                listener.onNumClick(v, num);
            }
        };
        view.findViewById(R.id.keyboard1).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard2).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard3).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard4).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard5).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard6).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard7).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard8).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard9).setOnClickListener(clickListener);
        view.findViewById(R.id.keyboard0).setOnClickListener(clickListener);

        //清除
        View.OnClickListener clearListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClearClick(v);
            }
        };
        view.findViewById(R.id.keyboard_clear).setOnClickListener(clearListener);

        //删除
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackClick(v);
            }
        };
        view.findViewById(R.id.keyboard_back).setOnClickListener(backListener);
    }

    /**
     * 0-9 正常输入
     * -1 删除最后一位
     * C 清除所有
     *
     * @param num
     */
    private void inputTail(String num) {
        if (num.matches("\\d")) {
            if (phoneTail.length() < 4) {
                tailTvs[phoneTail.length()].setText(num);
                phoneTail = phoneTail.concat(num);
            }
        } else if ("-1".equals(num)) {
            if (phoneTail.length() > 0 && phoneTail.length() < 5) {
                tailTvs[phoneTail.length() - 1].setText("");
                phoneTail = phoneTail.substring(0, phoneTail.length() - 1);
            }
        } else if ("C".equals(num)) {
            for (TextView tv : tailTvs) {
                tv.setText("");
            }
            phoneTail = "";
        }

    }

}

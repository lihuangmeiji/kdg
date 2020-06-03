package org.tabjin.myapplication.ui.admin;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.model.HttpModel;
import org.tabjin.myapplication.model.HttpUtil;
import org.tabjin.myapplication.model.bean.AdminOrderListBean;
import org.tabjin.myapplication.model.bean.BannerBean;
import org.tabjin.myapplication.model.bean.GuiBean;
import org.tabjin.myapplication.model.bean.Order;
import org.tabjin.myapplication.model.bean.OrderAdminBean;
import org.tabjin.myapplication.model.bean.Respone;
import org.tabjin.myapplication.model.bean.TakeawayOrderBean;
import org.tabjin.myapplication.ui.MainActivity;
import org.tabjin.myapplication.ui.adapter.AdapterOrder;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class AdminOrderActivity extends AppCompatActivity {
    private ListView list_view_order;
    List<OrderAdminBean> orderList;
    AdapterOrder adapterOrder;
    private EditText et_postmanMobile;
    private TextView et_storeinAt;
    private EditText et_customerMobile;
    private TextView tv_ss;
    private TextView tv_title;
    private TextView tv_title_right;
    private ImageView iv_title_left;
    private PullToRefreshLayout prl_order;
    private Spinner spinnerstatus;
    TimePickerView pvTime;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int pageNum = 1;
    private static final int pageSize = 20;
    private String status=null;
    private  String[] ctype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        list_view_order = findViewById(R.id.list_view_order);
        et_postmanMobile = findViewById(R.id.et_postmanMobile);
        et_storeinAt = findViewById(R.id.et_storeinAt);
        et_customerMobile = findViewById(R.id.et_customerMobile);
        tv_ss = findViewById(R.id.tv_ss);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单管理");
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.GONE);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prl_order=findViewById(R.id.prl_order);
        prl_order.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                pageNum = 1;
                orderList.clear();
                loadOrder(pageNum,pageSize);
                prl_order.finishRefresh();
            }

            @Override
            public void loadMore() {
                pageNum++;
                loadOrder(pageNum,pageSize);
                prl_order.finishLoadMore();
            }
        });
        spinnerstatus=findViewById(R.id.spinnerstatus);
        ctype = new String[]{"全部", "待取", "已取"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinnerstatus.setAdapter(adapter);
        spinnerstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    status=null;
                }else if(i==1){
                    status="0";
                }else if(i==2){
                    status="1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        orderList = new ArrayList<>();
        adapterOrder = new AdapterOrder(orderList, AdminOrderActivity.this);
        list_view_order.setAdapter(adapterOrder);
        setPvTime();
        et_storeinAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
        tv_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderList.clear();
                pageNum = 1;
                hideInputManager();
                loadOrder(pageNum,pageSize);
            }
        });

        loadOrder(pageNum,pageSize);
    }


    private void loadOrder(int pageNum,int pageSize) {
        HttpModel.loadOrder(pageNum,pageSize,et_postmanMobile.getText().toString(), et_customerMobile.getText().toString(), status, et_storeinAt.getText().toString(),
                new HttpUtil.ResponeCallBack() {
                    @Override
                    public void respone(String respone) {
                        Log.e("orderrespone", respone);
                        try {
                            Type type = new TypeToken<Respone<AdminOrderListBean>>() {
                            }.getType();
                            Respone<AdminOrderListBean> orderRespone = new Gson().fromJson(respone, type);
                            Log.i("orderResponeMap", "orderRespone: " + orderRespone);
                            if (orderRespone != null && orderRespone.getCode() == 0 && orderRespone.getData() != null) {
                                AdminOrderListBean data = orderRespone.getData();
                                Log.i("orderMapData", "orderMapData: " + data.getCount());
                                orderList.addAll(data.getList());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message msg = new Message();
                                        msg.what = 1;
                                        mUIHandler.sendMessage(msg);
                                    }
                                }).start();
                            } else {
                                Looper.prepare();
                                Tool.T.showToast(AdminOrderActivity.this, "暂无数据！");
                                Looper.loop();
                            }
                        }catch (Exception e){
                            Looper.prepare();
                            Tool.T.showToast(AdminOrderActivity.this, "数据异常！");
                            Looper.loop();
                        }
                    }
                });
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapterOrder.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public void setPvTime() {
        Calendar defaultDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2010, 1, 1);
        final Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 1, 1);
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String startTime = formatter.format(date);
                et_storeinAt.setText(startTime);
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(Color.rgb(240, 255, 255))//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRange(defaultDate.get(Calendar.YEAR) - 20, defaultDate.get(Calendar.YEAR) + 20)//默认是1900-2100年
                .setRangDate(startDate, endDate)
                .setDate(defaultDate)// 默认是系统时间*/
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
    }

    /**
     * 隐藏输入软键盘
     *
     * @param
     * @param
     */
    public void hideInputManager() {
        View view = getWindow().peekDecorView();
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

package org.tabjin.myapplication.ui.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.ipinyuan.communication.error.ErrorCode;
import net.ipinyuan.communication.out.CabinetTransaction;
import net.ipinyuan.communication.out.CabinetTransactionEventListener;
import net.ipinyuan.communication.out.CellStatus;
import net.ipinyuan.communication.out.DoorStatus;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.Tool;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.ui.MainActivity;
import org.tabjin.myapplication.ui.adapter.CellStatusAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private ListView listView;
    private CabinetTransaction cabinetTransaction;
    private CellStatusAdapter cellStatusAdapter;

    private TextView tv_title;
    private TextView tv_title_right;
    private ImageView iv_title_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText("箱子管理");
        tv_title_right=(TextView)findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.GONE);
        iv_title_left=(ImageView)findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView=findViewById(R.id.list_view);
        cabinetTransaction = CabinetTransaction.getInstance();
        //cabinetTransaction.start(Constant.pypwd, Constant.dev, Constant.pytype, Constant.guiBean.getNumber(), cabinetTransactionEventListener);
        //cabinetTransaction.start(Constant.pypwd, Constant.dev, Constant.pytype, Constant.guiBean.getNumber(), cabinetTransactionEventListener);
        List<CellStatus> cellStatuses = new ArrayList<>();
        for (CellStatus cellStatus : cabinetTransaction.getCellStatusList()) {
            cellStatuses.add(cellStatus);
        }
        cellStatusAdapter = new CellStatusAdapter(AdminMainActivity.this, cellStatuses, CellStatusAdapter.TYPE_DOOR);
        listView.setAdapter(cellStatusAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cabinetTransaction.openCell(i+1);
            }
        });
    }

    private CabinetTransactionEventListener cabinetTransactionEventListener = new CabinetTransactionEventListener() {
        @Override
        public void onIotConnectStatusChanged(boolean isConnected) {
            Log.i("TAG", "iot 连接状态：" + (isConnected ? "已连接" : "未连接（断开连接）") + ",设备号：" + cabinetTransaction.getDeviceName());
            //tvloginfo.setText("iot 连接状态：" + (isConnected ? "已连接" : "未连接（断开连接）") + ",设备号：" + cabinetTransaction.getDeviceName());
        }

        @Override
        public void onDoorStatusChanged(int i, DoorStatus doorStatus) {
            //门状态变化时会调用该方法
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
}

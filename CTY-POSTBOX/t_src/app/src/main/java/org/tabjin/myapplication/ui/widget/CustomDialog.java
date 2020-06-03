package org.tabjin.myapplication.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;

public class CustomDialog extends Dialog implements RadioGroup.OnCheckedChangeListener {

    private Button yes;
    private Button no;
    private TextView titleTv;
    private EditText messageTv;
    private EditText et_number;
    private EditText et_location;
    private LinearLayout lin_number;
    private LinearLayout lin_location;
    private String titleStr;
    private String messageStr;
    private RadioGroup rg_type;
    private String yesStr, noStr;
    int guiType=1;

    /*  -------------------------------- 接口监听 -------------------------------------  */

    private onNoOnclickListener noOnclickListener;
    private onYesOnclickListener yesOnclickListener;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_type1:
                titleTv.setText("请输入安装密码");
                lin_number.setVisibility(View.GONE);
                lin_location.setVisibility(View.GONE);
                guiType=1;
                break;
            case R.id.rb_type2:
                titleTv.setText("请输入柜子编号");
                lin_number.setVisibility(View.VISIBLE);
                lin_location.setVisibility(View.VISIBLE);
                guiType=2;
                break;
        }
    }

    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }

    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    /*  ---------------------------------- 构造方法 -------------------------------------  */

    public CustomDialog(Context context) {
        super(context, R.style.MyDialog);//风格主题
    }


    /*  ---------------------------------- onCreate-------------------------------------  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwrod_input);//自定义布局
        //按空白处不能取消动画
//        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        titleTv = (TextView) findViewById(R.id.title);
        messageTv = findViewById(R.id.et);
        rg_type = (RadioGroup) findViewById(R.id.rg_type);
        rg_type.setOnCheckedChangeListener(this);
        et_number = findViewById(R.id.et_number);
        et_location = findViewById(R.id.et_location);
        lin_number=findViewById(R.id.lin_number);
        lin_location=findViewById(R.id.lin_location);
    }

    public String getMessageStr() {
        return messageTv.getText().toString();
    }

    public String getLocationStr() {
        return et_location.getText().toString();
    }

    public String getNumberStr() {
        return et_number.getText().toString();
    }

    public int getGuiTypeStr() {
        return guiType;
    }
    /*  ---------------------------------- set方法 传值-------------------------------------  */
//为外界设置一些public 公开的方法，来向自定义的dialog传递值
    public void setTitle(String title) {
        titleStr = title;
    }

    public void setMessage(String message) {
        messageStr = message;
    }

    public void clearMessage() {
        messageTv.setText("");
    }


}
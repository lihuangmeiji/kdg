package org.tabjin.myapplication.ui.admin;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.model.HttpModel;
import org.tabjin.myapplication.model.HttpUtil;
import org.tabjin.myapplication.model.Server;
import org.tabjin.myapplication.model.bean.Respone;
import org.tabjin.myapplication.ui.fragment.PutMealFragment;

public class AdminLoginActivity extends AppCompatActivity {

    private CountDownTimer timer;
    private ImageView iv_login_qr_code;
    private TextView tv_title;
    private TextView tv_title_right;
    private ImageView iv_title_left;
    long timeMillis=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        iv_login_qr_code=(ImageView)findViewById(R.id.iv_login_qr_code);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText("后台登录");
        tv_title_right=(TextView)findViewById(R.id.tv_title_right);
        iv_title_left=(ImageView)findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        timeMillis=System.currentTimeMillis();
        Glide.with(AdminLoginActivity.this).load(Server.serverUrl+"/wx/common/qrcode?guiNo="+Constant.guiBean.getGui_no()+"&type=2&timestamp="+timeMillis).into(iv_login_qr_code);
        if (timer == null) {
            timer = new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv_title_right.setText(millisUntilFinished / 1000 + "S");
                    grantPolling();
                }

                @Override
                public void onFinish() {
                    finish();
                }
            };
        }
    }

    private void grantPolling() {
        HttpModel.grantPolling(timeMillis,
                new HttpUtil.ResponeCallBack() {
                    @Override
                    public void respone(String respone) {
                        Respone respone1 = new Gson().fromJson(respone, Respone.class);
                        if (respone1 == null) {
                            return;
                        }
                        Log.i("responeAdminLogin", "respone: "+respone1.getCode());
                        if (0 == respone1.getCode()) {
                            finish();
                            Intent intent=new Intent(AdminLoginActivity.this,AdminHomeActivity.class);
                            startActivity(intent);
                        }
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
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

}

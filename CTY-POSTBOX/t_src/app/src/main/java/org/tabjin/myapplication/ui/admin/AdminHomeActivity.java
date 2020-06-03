package org.tabjin.myapplication.ui.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.model.FileChooseUtil;
import org.tabjin.myapplication.model.OpenFileUtil;
import org.tabjin.myapplication.model.Server;

import java.io.File;

public class AdminHomeActivity extends AppCompatActivity {
    private TextView tv_function1;
    private TextView tv_function2;
    private TextView tv_function3;
    private TextView tv_function4;

    private TextView tv_title;
    private TextView tv_title_right;
    private ImageView iv_title_left;

    public final int REQUEST_CHOOSEFILE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        tv_function1=findViewById(R.id.tv_function1);

        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText("后台管理");
        tv_title_right=(TextView)findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.GONE);
        iv_title_left=(ImageView)findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_function1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this,AdminMainActivity.class);
                startActivity(intent);
            }
        });
        tv_function2=findViewById(R.id.tv_function2);
        tv_function2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this,AdminOrderActivity.class);
                startActivity(intent);
            }
        });
        tv_function3=findViewById(R.id.tv_function3);
        tv_function3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(AdminHomeActivity.this,AdminFileManagementActivity.class);
                startActivity(intent);*/
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                //获取文件下载路径

                File dir = new File(Server.registerFilePath + Server.registerFileName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //调用系统文件管理器打开指定路径目录
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.fromFile(dir.getParentFile()), "*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CHOOSEFILE);

            }
        });
        tv_function4=findViewById(R.id.tv_function4);
        tv_function4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("关机", "alarm onReceive reboot");
             /*   SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getContext().getApplicationContext());
                Calendar calendar = Calendar.getInstance();
                sharedPreferenceHelper.saveLong(AlarmUtils.LAST_REBOOT_TIMESTAMP_PREFERENCE_KEY, calendar.getTimeInMillis());*/
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});  //关机
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//选择文件返回
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case REQUEST_CHOOSEFILE:
                    Uri uri=data.getData();
                    //String chooseFilePath= FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
                    OpenFileUtil.getAllIntent( uri.getPath());
                    Log.d("chooseFilePath","选择文件返回："+ uri.getPath());
                    //sendFileMessage(chooseFilePath);
                    break;
            }
        }
    }
}

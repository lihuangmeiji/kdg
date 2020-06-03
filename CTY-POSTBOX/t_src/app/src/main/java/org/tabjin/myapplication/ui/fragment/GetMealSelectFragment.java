package org.tabjin.myapplication.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.base.fragment.TitleBaseFragment;
import org.tabjin.myapplication.model.Server;
import org.tabjin.myapplication.ui.IRouter;

import java.io.File;

/**
 * Created by hspcadmin on 2018/8/23.
 * 存餐
 */

public class GetMealSelectFragment extends TitleBaseFragment {

    private IRouter mRouter;

    private TextView tvRight;

    private CountDownTimer timer;

    public static GetMealSelectFragment newInstance() {

        Bundle args = new Bundle();

        GetMealSelectFragment fragment = new GetMealSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.get_meal_select_fragment, container,false);
        if(getActivity() instanceof IRouter) {
            mRouter = (IRouter) getActivity();
        }
        ((TextView) titleView.findViewById(R.id.tv_title)).setText("取餐");
        ImageView iv_saveimg=(ImageView)contentView.findViewById(R.id.iv_takeimg);
       /* File mFile=new File(Constant.fileSavePath+"take.png");
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap= BitmapFactory.decodeFile(Constant.fileSavePath+"take.png");
            iv_saveimg.setImageBitmap(bitmap);
        }*/
        Glide.with(GetMealSelectFragment.this).load(Server.serverUrl+"/wx/common/qrcode?guiNo="+Constant.guiBean.getGui_no()+"&type=1").into(iv_saveimg);
        TextView tv_imgpath=(TextView)contentView.findViewById(R.id.tv_imgpath);
        tv_imgpath.setText(Server.serverUrl+"/wx/common/qrcode?guiNo="+Constant.guiBean.getGui_no()+"&type=1");
        TextView tv_takeapp=(TextView)contentView.findViewById(R.id.tv_takeapp);
        tv_takeapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRouter.gotoFragment(WindowId.GetMealFragment);
            }
        });
        tvRight = titleView.findViewById(R.id.tv_title_right);
        if(timer == null) {
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
    protected void clickLeft(View v) {
        if (mRouter != null) {
            mRouter.gotoFragment(WindowId.HomeFragment);
        }
    }

    @Override
    protected void clickRightTv(View v) {

    }
}

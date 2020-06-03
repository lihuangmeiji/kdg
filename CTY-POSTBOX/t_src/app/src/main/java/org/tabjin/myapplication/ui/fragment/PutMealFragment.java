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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.tabjin.myapplication.R;
import org.tabjin.myapplication.base.Constant;
import org.tabjin.myapplication.base.WindowId;
import org.tabjin.myapplication.base.fragment.TitleBaseFragment;
import org.tabjin.myapplication.model.Server;
import org.tabjin.myapplication.ui.IRouter;
import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by hspcadmin on 2018/8/23.
 * 存餐
 */

public class PutMealFragment extends TitleBaseFragment {

    private IRouter mRouter;

    private TextView tvRight;

    private CountDownTimer timer;

    public static PutMealFragment newInstance() {

        Bundle args = new Bundle();

        PutMealFragment fragment = new PutMealFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.put_meal_fragment, container, false);
        if (getActivity() instanceof IRouter) {
            mRouter = (IRouter) getActivity();
        }
        ((TextView) titleView.findViewById(R.id.tv_title)).setText("存餐");
        ImageView iv_saveimg = (ImageView) contentView.findViewById(R.id.iv_saveimg);
     /*   File mFile=new File(Constant.fileSavePath+"save.jpg");
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap= BitmapFactory.decodeFile(Constant.fileSavePath+"save.jpg");
            iv_saveimg.setImageBitmap(bitmap);
        }*/
        TextView tv_imgpath = (TextView) contentView.findViewById(R.id.tv_imgpath);
        tv_imgpath.setText(Server.serverUrl+"/wx/common/qrcode?guiNo=" + Constant.guiBean.getGui_no()+"&type=0");
        Glide.with(PutMealFragment.this).load(Server.serverUrl+"/wx/common/qrcode?guiNo=" + Constant.guiBean.getGui_no()+"&type=0").into(iv_saveimg);
        tvRight = titleView.findViewById(R.id.tv_title_right);
        ImageView iv_title_left = titleView.findViewById(R.id.iv_title_left);
        iv_title_left.setImageResource(R.mipmap.go_backsave);
        RelativeLayout relBg = titleView.findViewById(R.id.rel_bg);
        relBg.setBackgroundColor(getContext().getResources().getColor(R.color.iv_put));
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
        return contentView;
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
    protected void clickLeft(View v) {
        if (mRouter != null) {
            mRouter.gotoFragment(WindowId.HomeFragment);
        }
    }

    @Override
    protected void clickRightTv(View v) {

    }
}

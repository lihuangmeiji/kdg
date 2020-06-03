package org.tabjin.myapplication.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tabjin.myapplication.R;

/**
 * Created by hspcadmin on 2018/9/20.
 */

public class NumKeyBoard extends ViewGroup {

    public NumKeyBoard(Context context) {
        super(context,null);
    }

    public NumKeyBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.num_keyboard,this);
    }

    public NumKeyBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * 数字点击监听接口
     */
    public interface NumClickListener{
        void onNumClick(View view, String num);

        void onClearClick(View view);

        void onBackClick(View view);
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setKeyBoardListener(final NumClickListener listener) {
        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String num = ((TextView)v).getText().toString();
                listener.onNumClick(v, num);
            }
        };
        findViewById(R.id.keyboard1).setOnClickListener(clickListener);
        findViewById(R.id.keyboard2).setOnClickListener(clickListener);
        findViewById(R.id.keyboard3).setOnClickListener(clickListener);
        findViewById(R.id.keyboard4).setOnClickListener(clickListener);
        findViewById(R.id.keyboard5).setOnClickListener(clickListener);
        findViewById(R.id.keyboard6).setOnClickListener(clickListener);
        findViewById(R.id.keyboard7).setOnClickListener(clickListener);
        findViewById(R.id.keyboard8).setOnClickListener(clickListener);
        findViewById(R.id.keyboard9).setOnClickListener(clickListener);
        findViewById(R.id.keyboard0).setOnClickListener(clickListener);

        //清除
        View.OnClickListener clearListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onClearClick(v);
            }
        };
        findViewById(R.id.keyboard_clear).setOnClickListener(clearListener);

        //删除
        View.OnClickListener backListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onBackClick(v);
            }
        };
        findViewById(R.id.keyboard_back).setOnClickListener(backListener);
    }


}

package org.tabjin.myapplication.ui.widget;

import android.view.View;

/**
 * 数字点击监听接口
 */
public interface NumClickListener{
    void onNumClick(View view, String num);

    void onClearClick(View view);

    void onBackClick(View view);
}
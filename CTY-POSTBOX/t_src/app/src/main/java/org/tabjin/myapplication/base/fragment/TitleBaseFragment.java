package org.tabjin.myapplication.base.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhy.autolayout.utils.AutoUtils;

import org.tabjin.myapplication.R;

/**
 * title写死，提供出来点击事件处理方法
 * 内容抽离，提供抽象方法实现
 */

public abstract class TitleBaseFragment extends Fragment implements View.OnClickListener{

    //标题
    protected View titleView;

    //内容
    protected View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(initTitle(inflater,container),0);
        contentView = onCreateContentView(inflater,container,savedInstanceState);
        //AutoUtils.auto(contentView);
        linearLayout.addView(contentView,1);
        //AutoUtils.auto(linearLayout);
        return linearLayout;
    }

    /**
     * 初始化标题
     */
    private View initTitle(LayoutInflater inflater, @Nullable ViewGroup container) {
        titleView = inflater.inflate(R.layout.base_fragment_title, container,false);
        titleView.findViewById(R.id.iv_title_left).setOnClickListener(this);
        titleView.findViewById(R.id.tv_title).setOnClickListener(this);
        titleView.findViewById(R.id.tv_title_right).setOnClickListener(this);
        //AutoUtils.auto(titleView);
        return titleView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                clickLeft(v);
                break;
            case R.id.tv_title_right:
                clickRightTv(v);
                break;
        }
    }



    /**
     * 初始化fragment的内容视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract @Nullable View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    /**
     * 左边的图片按钮
     * @param v
     */
    protected abstract void clickLeft(View v);

    /**
     * 右边的文字按钮
     * @param v
     */
    protected abstract void clickRightTv(View v);
}

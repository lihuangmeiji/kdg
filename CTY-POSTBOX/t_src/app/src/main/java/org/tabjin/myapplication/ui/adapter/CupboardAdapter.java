package org.tabjin.myapplication.ui.adapter;

import android.support.annotation.NonNull;

import com.contrarywind.adapter.WheelAdapter;

import java.util.List;

/**
 * Created by hspcadmin on 2018/8/26.
 */

public class CupboardAdapter<T> implements WheelAdapter {

    private List<T> list;

    public CupboardAdapter(@NonNull List<T> list) {
        this.list = list;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public T getItem(int index) {
        return list.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }
}

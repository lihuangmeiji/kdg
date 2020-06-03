package org.tabjin.myapplication.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import net.ipinyuan.communication.out.CellStatus;
import net.ipinyuan.communication.out.DoorStatus;
import net.ipinyuan.communication.out.IndexPath;
import net.ipinyuan.communication.out.SectionRowAdapter;
import net.ipinyuan.communication.out.SectionRowViewHolder;

import org.tabjin.myapplication.R;

import java.util.List;

public class CellStatusAdapter extends SectionRowAdapter<CellStatusAdapter.CellStatusViewHolder> {

    public static final int TYPE_DOOR = 1;
    public static final int TYPE_GOODS = 2;
    public static final int TYPE_ALL = 3;

    private List<CellStatus> cellStatusList;
    private int type;

    public CellStatusAdapter(Context context, List<CellStatus> cellStatusList, int type) {
        super(context);

        this.cellStatusList = cellStatusList;
        this.type = type;
    }

    @Override
    protected int numberOfRowsInSection(int section) {
        return cellStatusList.size();
    }

    @Override
    protected int cellForRowAtIndexPath(IndexPath indexPath) {
        return R.layout.cell_status_adapter;
    }

    @Override
    protected CellStatusViewHolder initViewHolder(View view, IndexPath indexPath) {
        CellStatusViewHolder holder = new CellStatusViewHolder();
        holder.statusValue = view.findViewById(R.id.status_value);
        return holder;
    }

    private String doorStatus(DoorStatus doorStatus) {
        switch (doorStatus) {
            case DOOR_STATUS_OPENED:
                return "已开门";

            default:
                return "已关门";
        }
    }

    @Override
    protected void fillViewHolder(CellStatusViewHolder holder, IndexPath indexPath) {
        CellStatus cellStatus = cellStatusList.get(indexPath.getRow());
        if (type == TYPE_DOOR) {
            holder.statusValue.setText("No." + (indexPath.getRow()+1) + "号格子门状态为：" + doorStatus(cellStatus.getDoorStatus()));
        }
    }

    public class CellStatusViewHolder extends SectionRowViewHolder {
        TextView statusValue;
    }
}

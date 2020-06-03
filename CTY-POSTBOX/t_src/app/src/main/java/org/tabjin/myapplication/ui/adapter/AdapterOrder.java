package org.tabjin.myapplication.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.tabjin.myapplication.R;
import org.tabjin.myapplication.model.bean.Order;
import org.tabjin.myapplication.model.bean.OrderAdminBean;

import java.util.List;

public class AdapterOrder extends BaseAdapter {

	private List<OrderAdminBean> list;
	private Context context;
	// 记录当前展开项的索引
	private int expandPosition = -1;
	private String recharge;
	private int clickTemp = 0;

	public AdapterOrder(List<OrderAdminBean> list, Context context) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == list ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSeclection(int position) {
		clickTemp = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if (null == convertView) {
			LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.order_adapter, null);
			holder = new ViewHolder();
			holder.tv_postmanMobile = (TextView) convertView.findViewById(R.id.tv_postmanMobile);
			holder.customerMobile=(TextView)convertView.findViewById(R.id.customerMobile);
			holder.tv_taked_time=(TextView)convertView.findViewById(R.id.tv_taked_time);
			holder.tv_gui_no=(TextView)convertView.findViewById(R.id.tv_gui_no);
			holder.tv_wh=(TextView)convertView.findViewById(R.id.tv_wh);
			holder.tv_status=(TextView)convertView.findViewById(R.id.tv_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		OrderAdminBean order = (OrderAdminBean) list.get(position);
		if(order!=null){
			holder.tv_postmanMobile.setText(order.getPostmanMobile());
			holder.customerMobile.setText(order.getCustomerMobile());
			holder.tv_taked_time.setText(order.getStoreinAt());
			holder.tv_gui_no.setText(order.getPackageNo());
			holder.tv_wh.setText(order.getBoxNo());
			if(order.getStatus()==0){
				holder.tv_status.setText("待取");
			}else if(order.getStatus()==1){
				holder.tv_status.setText("已取");
			}else{
				holder.tv_status.setText("未知");
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_postmanMobile;
		TextView customerMobile;
		TextView tv_taked_time;
		TextView tv_gui_no;
		TextView tv_wh;
		TextView tv_status;
	}
}

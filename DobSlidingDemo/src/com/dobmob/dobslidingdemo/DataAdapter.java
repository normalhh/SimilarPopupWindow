package com.dobmob.dobslidingdemo;

import java.util.ArrayList;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {

	ArrayList<String> data = new ArrayList<String>();
	

    private int selectedPosition = 0;// 选中的位置  
	Context context;
	public DataAdapter(Context context,ArrayList<String> data ) {
		this.context = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setSelectedPosition(int position) {  
        selectedPosition = position;  
    }  
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_select_view, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.tv);
		tv.setText(data.get(position));
		
		if (selectedPosition == position) {
			convertView.setBackgroundResource(R.color.lightgray);
//			convertView.setBackground(context.getResources().getDrawable(R.color.lightgray));
		}else{
			convertView.setBackgroundResource(R.color.white);
		}
		
		return convertView;
	}

}

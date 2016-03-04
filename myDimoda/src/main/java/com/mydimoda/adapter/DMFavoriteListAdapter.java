package com.mydimoda.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.constant;
import com.parse.ParseObject;

public class DMFavoriteListAdapter extends BaseAdapter {
	private Context 		m_Context;
	private LayoutInflater 	layoutInflater;  
	private List<ParseObject> mList;
	
	public DMFavoriteListAdapter(Context context,  List<ParseObject> list) {
	           	 	
		m_Context=context;
		layoutInflater = LayoutInflater.from(m_Context);  
		mList = list;
	}
	 
	@Override
	public int getCount() {
		return mList.size();
	}
	 
	@Override
	public Object getItem(int position) {
		return null;
	}
	 
	@Override
	public long getItemId(int position) {
		return position;
	}
	 
	public View getView( final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
	
		if (convertView == null) {
			
			convertView = layoutInflater.inflate(R.layout.list_favorite_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.item_txt);
			convertView.setTag(holder);
			
		} else {        	
			holder = (ViewHolder) convertView.getTag();         
		}  
		
		holder.name.setText(constant.getCustomDate(mList.get(position).getString("DateTime")));
	
		return convertView;         
	}
	 
	static class ViewHolder {
		TextView  name;
	}    
}



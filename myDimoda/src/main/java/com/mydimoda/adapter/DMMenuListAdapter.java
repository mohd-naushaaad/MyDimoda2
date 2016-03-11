package com.mydimoda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMMenuListAdapter extends BaseAdapter {

	private Context m_Context;
	private LayoutInflater layoutInflater;
	private List<String> mList = new ArrayList<String>();

	public DMMenuListAdapter(Context context, List<String> list) {

		m_Context = context;
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

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.list_menu_item, null);
			holder = new ViewHolder();
			holder.menuItem = (TextView) convertView
					.findViewById(R.id.menu_itemname);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FontsUtil.setExistenceLight(m_Context, holder.menuItem);

//		holder.menuItem.setTypeface(constant.fontface);
		holder.menuItem.setText(mList.get(position));
		return convertView;
	}

	static class ViewHolder {
		TextView menuItem;
	}
}

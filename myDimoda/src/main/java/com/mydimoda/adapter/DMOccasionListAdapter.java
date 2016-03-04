package com.mydimoda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMOccasionListAdapter extends BaseAdapter {
	private Context m_Context;
	private LayoutInflater layoutInflater;
	private int[] mImageIds = { R.drawable.list_favorites,
			R.drawable.list_casual, R.drawable.list_after,
			R.drawable.list_formal };
	private String[] mNames = { "FAVORITES", "CASUAL", "AFTER 5/NIGHT OUT",
			"FORMAL" };

	public DMOccasionListAdapter(Context context) {

		m_Context = context;
		layoutInflater = LayoutInflater.from(m_Context);
	}

	@Override
	public int getCount() {
		return mNames.length;
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

			convertView = layoutInflater.inflate(R.layout.list_occasion_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.item_view);
			holder.name = (TextView) convertView.findViewById(R.id.item_txt);
			FontsUtil.setExistenceLight(m_Context, holder.name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(mImageIds[position]);
		holder.name.setText(mNames[position]);

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView name;
	}
}

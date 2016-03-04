package com.mydimoda.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.ParseApplication;
import com.mydimoda.R;
import com.mydimoda.object.DMProductObject;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DMProductAdapter extends BaseAdapter {

	private Context m_Context;
	private LayoutInflater layoutInflater;
	private List<DMProductObject> mList;

	public DMProductAdapter(Context context, List<DMProductObject> list) {

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

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.grid_product_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.item_img);
			holder.txtPrice = (TextView) convertView
					.findViewById(R.id.price_txt);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FontsUtil.setExistenceLight(m_Context, holder.txtPrice);
		holder.txtPrice.setText(mList.get(position).Price);
		// display image
		ParseApplication.getInstance().mImageLoader.displayImage(
				mList.get(position).MediumImage.url, holder.imageView,
				ParseApplication.getInstance().options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {

					}
				});

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView txtPrice;
	}
}

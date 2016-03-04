package com.mydimoda.adapter;

import java.util.ArrayList;
import java.util.List;

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
import com.mydimoda.constant;
import com.mydimoda.model.OrderClothModel;
import com.mydimoda.widget.CircularProgressBar;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DMFashionGridAdapter extends BaseAdapter {

	private Context m_Context;
	private LayoutInflater layoutInflater;
	private List<OrderClothModel> mList = new ArrayList<OrderClothModel>();

	public DMFashionGridAdapter(Context context, List<OrderClothModel> list) {

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

		final ViewHolder holder;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.grid_fashion_layout,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.item_view);
			holder.progress = (CircularProgressBar) convertView
					.findViewById(R.id.item_progress);
			holder.typeText = (TextView) convertView
					.findViewById(R.id.typeText);
			FontsUtil.setExistenceLight(m_Context, holder.typeText);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// holder.imageView.setTag(position);
		// display image
		ParseApplication.getInstance().mImageLoader.displayImage(
				mList.get(position).getImageUrl(), holder.imageView,
				ParseApplication.getInstance().options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						holder.progress.setProgress(0);
						holder.progress.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						holder.progress.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						holder.progress.setVisibility(View.GONE);
						if (mList.get(position).getPosition() == constant.SUIT)
							holder.typeText.setText("suit");
						else
							holder.typeText.setText("");
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						int progress = Math.round(100.0f * current / total);
						holder.progress.setProgress(progress);
						holder.progress.setTitle(String.valueOf(progress) + "%");
					}
				});
		// new
		// ImageDownloaderTask1(holder.imageView,holder.progress,m_Context,"",position).execute(mList.get(position));

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		CircularProgressBar progress;
		TextView typeText;
	}
}

package com.mydimoda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.mydimoda.ParseApplication;
import com.mydimoda.R;
import com.mydimoda.image.ImageLoader;
import com.mydimoda.widget.CircularProgressBar;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DMDeleteGridAdapter extends BaseAdapter {
	
	private Context 		m_Context;
	private LayoutInflater 	layoutInflater;  
	private List<String>    mList = new ArrayList<String>();
	private ImageLoader  	mLoader;
	private DMListItemCallback mCallback;
	
	public DMDeleteGridAdapter(Context context,List<String> list,DMListItemCallback callback) {
	           	 	
		m_Context=context;
		layoutInflater = LayoutInflater.from(m_Context);
		mList = list;
		mLoader = new ImageLoader(m_Context);
		mCallback = callback;
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
		
		final ViewHolder holder;
	
		if (convertView == null) {
			
			convertView = layoutInflater.inflate(R.layout.grid_cloth_delete_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_view);
			holder.progress  = (CircularProgressBar) convertView.findViewById(R.id.item_progress);
			holder.deleteBtn  = (Button) convertView.findViewById(R.id.btn_delete);
			holder.formalBtn  = (Button) convertView.findViewById(R.id.btn_formal);
			
			convertView.setTag(holder);
			
		} else {        	
			holder = (ViewHolder) convertView.getTag();         
		}  
		holder.deleteBtn.setFocusable(false);
		holder.formalBtn.setFocusable(false);	
		holder.deleteBtn.setVisibility(View.GONE);
		holder.formalBtn.setVisibility(View.GONE);
		
		 ParseApplication.getInstance().mImageLoader.displayImage(mList.get(position), holder.imageView,ParseApplication.getInstance().options, new SimpleImageLoadingListener() {
			 @Override
			 public void onLoadingStarted(String imageUri, View view) {
				 System.out.println("DMDeleteGridAdapter.onLoadingStarted : "+imageUri);
				 holder.progress.setProgress(0);
				 holder.progress.setVisibility(View.VISIBLE);
			 }

			 @Override
			 public void onLoadingFailed(String imageUri, View view,
					 FailReason failReason) {
				 holder.progress.setVisibility(View.GONE);
			 }

			 @Override
			 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				 System.out.println("DMDeleteGridAdapter.onLoadingStarted : loadedImage Bitmap : "+imageUri);
				// saveFile(loadedImage);


				 holder.progress.setVisibility(View.GONE);
				
			 }
		 }, new ImageLoadingProgressListener() {
			 @Override
			 public void onProgressUpdate(String imageUri, View view, int current,
					 int total) {
				 int progress = Math.round(100.0f * current / total);
				 holder.progress.setProgress(progress);
				 holder.progress.setTitle(String.valueOf(progress) + "%");
			 }
		});
		
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				holder.deleteBtn.setVisibility(View.VISIBLE);
				holder.formalBtn.setVisibility(View.VISIBLE);
			}
		});
	
		holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCallback != null)
					mCallback.selectButton(position, 1);
				
				holder.deleteBtn.setVisibility(View.GONE);
				holder.formalBtn.setVisibility(View.GONE);
			}
		});
		
		holder.formalBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCallback != null)
					mCallback.selectButton(position, 2);
				
				holder.deleteBtn.setVisibility(View.GONE);
				holder.formalBtn.setVisibility(View.GONE);
			}
		});
		
        return convertView;         
	}
	void saveFile(Bitmap bitmap){
		String filename = "pippo.png";
		File sd = Environment.getExternalStorageDirectory();
		File dest = new File(sd, filename);
		try {
			FileOutputStream out = new FileOutputStream(dest);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	static class ViewHolder {
		ImageView imageView;
		CircularProgressBar progress;
		Button deleteBtn, formalBtn;
	}    
}




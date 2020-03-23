package com.mydimoda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.collection.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mydimoda.R;
import com.mydimoda.image.ImageLoader;
import com.mydimoda.model.DialogImagesModel;
import com.mydimoda.widget.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

public class DMDialogGridAdapter extends BaseAdapter {
    int THUMBSIZE;
    private Context m_Context;
    private LayoutInflater layoutInflater;
    private List<DialogImagesModel> mList = new ArrayList<>();
    private ImageLoader mLoader;
    private LruCache<String, Bitmap> mMemoryCache;


    public DMDialogGridAdapter(Context context, List<DialogImagesModel> list) {

        m_Context = context;
        layoutInflater = LayoutInflater.from(m_Context);
        mList = list;
        mLoader = new ImageLoader(m_Context);
        THUMBSIZE = MediaStore.Images.Thumbnails.MINI_KIND;

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

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

            convertView = layoutInflater.inflate(R.layout.item_gal_dialog, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_gal_imageView);
            holder.mSelectedIv = (ImageView) convertView.findViewById(R.id.item_gal_selected_iv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.imageView.setImageBitmap(getThumb(mList.get(position).getOrigId()));
        if (mList.get(position).isSelected()) {
            holder.mSelectedIv.setImageResource(R.drawable.selected);

        } else {
            holder.mSelectedIv.setImageResource(R.drawable.unselected);
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView imageView;
        CircularProgressBar progress;
        ImageView mSelectedIv;
    }


    public Bitmap getThumb(long origId) {
        try {
            if (mMemoryCache.get(String.valueOf(origId)) == null) {
              /*  BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;*/
                mMemoryCache.put(String.valueOf(origId),
                        MediaStore.Images.Thumbnails.getThumbnail(m_Context.getContentResolver(),
                                origId, MediaStore.Images.Thumbnails.MINI_KIND, null));
            }
            return mMemoryCache.get(String.valueOf(origId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;//@todo handle this
        }
    }

}



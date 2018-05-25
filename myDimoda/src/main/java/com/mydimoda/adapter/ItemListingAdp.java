package com.mydimoda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mydimoda.ParseApplication;
import com.mydimoda.R;
import com.mydimoda.image.ImageLoader;
import com.mydimoda.widget.CircularProgressBar;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemListingAdp extends RecyclerView.Adapter<ItemListingAdp.ItemListingHolder> {

    private Context context;
    private List<String> mList = new ArrayList<String>();
    private ImageLoader mLoader;
    private onclickOfSingleItemListner listner;

    public interface onclickOfSingleItemListner {
        void onClickOfitem(int pos);
    }

    public ItemListingAdp(Context context, List<String> mList, onclickOfSingleItemListner listner) {
        this.context = context;
        this.mList = mList;
        mLoader = new ImageLoader(context);
        this.listner = listner;
    }

    @Override
    public ItemListingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_fashion_layout, parent, false);
        return new ItemListingHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemListingHolder holder, int position) {
        holder.setData(position);
// display image


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ItemListingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view)
        ImageView itemView;
        @BindView(R.id.item_progress)
        CircularProgressBar itemProgress;
        @BindView(R.id.item_layout)
        RelativeLayout itemLayout;
        @BindView(R.id.RelativeLayout1)
        RelativeLayout RelativeLayout1;

        public ItemListingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.RelativeLayout1)
        public void onViewClicked() {
            listner.onClickOfitem(getAdapterPosition());

        }

        public void setData(int position) {
            ParseApplication.getInstance().mImageLoader.displayImage(mList.get(position), itemView, ParseApplication.getInstance().options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    itemProgress.setProgress(0);
                    itemProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    itemProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    itemProgress.setVisibility(View.GONE);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current,
                                             int total) {
                    int progress = Math.round(100.0f * current / total);
                    itemProgress.setProgress(progress);
                    itemProgress.setTitle(String.valueOf(progress) + "%");

                }
            });
        }
    }
}

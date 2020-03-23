package com.mydimoda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Parth on 3/5/2018.
 */

public class SubItemOfLookAdp extends RecyclerView.Adapter<SubItemOfLookAdp.SubItemHolder> {

    private List<OrderClothModel> listOfCloth;
    private Context mContext;

    public SubItemOfLookAdp(List<OrderClothModel> listOfCloth, Context mContext) {
        this.listOfCloth = listOfCloth;
        this.mContext = mContext;
    }

    @Override
    public SubItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcloth, parent, false);
        return new SubItemHolder(view);
    }

    @Override
    public void onBindViewHolder(SubItemHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return listOfCloth.size();
    }

    public class SubItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view)
        ImageView itemView;
        @BindView(R.id.item_progress)
        CircularProgressBar itemProgress;
        @BindView(R.id.item_layout)
        RelativeLayout itemLayout;
        @BindView(R.id.RelativeLayout1)
        RelativeLayout RelativeLayout1;
        @BindView(R.id.typeText)
        TextView typeText;

        public SubItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            FontsUtil.setExistenceLight(mContext, typeText);
            // display image
            ParseApplication.getInstance().mImageLoader.displayImage(
                    listOfCloth.get(position).getImageUrl(), itemView,
                    ParseApplication.getInstance().options,
                    new SimpleImageLoadingListener() {
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
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {

                            itemProgress.setVisibility(View.GONE);
                            if (listOfCloth.get(position).getPosition() == constant.SUIT)
                                typeText.setText("suit");
                            else
                                typeText.setText("");
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view,
                                                     int current, int total) {
                            int progress = Math.round(100.0f * current / total);
                            itemProgress.setProgress(progress);
                            itemProgress.setTitle(String.valueOf(progress) + "%");
                        }
                    });
        }
    }
}

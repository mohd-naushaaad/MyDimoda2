package com.mydimoda.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mydimoda.R;

import java.util.ArrayList;

/**
 * Created by Mainank on 15-03-2016.
 */
public class DMCropActRecycAdapter extends RecyclerView.Adapter<DMCropActRecycAdapter.ListItemViewHolder> {

    private ArrayList<Bitmap> items;

    public DMCropActRecycAdapter(ArrayList<Bitmap> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.items = modelData;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_crop_recyclr_vw, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            ListItemViewHolder viewHolder, int position) {
        viewHolder.mCroppedImage.setImageBitmap(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mCroppedImage;
        public ListItemViewHolder(View itemView) {
            super(itemView);
            mCroppedImage = (ImageView) itemView.findViewById(R.id.item_crop_imagevw);
        }
    }
}
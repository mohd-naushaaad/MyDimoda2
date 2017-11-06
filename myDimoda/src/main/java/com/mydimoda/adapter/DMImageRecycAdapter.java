package com.mydimoda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.model.CropListModel;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.util.ArrayList;

/**
 * Created by Mainank on 15-03-2016.
 */
public class DMImageRecycAdapter extends RecyclerView.Adapter<DMImageRecycAdapter.ListItemViewHolder> {

    private ArrayList<CropListModel> items;
    private CropListCallBacks mCallbacks;
    Context mContext;

    public DMImageRecycAdapter(ArrayList<CropListModel> modelData, CropListCallBacks mCallbacks) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.items = modelData;
        this.mCallbacks = mCallbacks;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_crop_list, viewGroup, false);
        mContext = viewGroup.getContext();
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            ListItemViewHolder viewHolder, final int position) {
        viewHolder.mCroppedImage.setImageBitmap(items.get(position).getmImage());
        if (!TextUtils.isEmpty(items.get(position).getmType())) {
            viewHolder.mType.setText(items.get(position).getmType());
        } else {
            viewHolder.mType.setText("Type");
        }
        if (TextUtils.isEmpty(items.get(position).getmCategory())) {
            viewHolder.mCategory.setText("Category");
        } else {
            viewHolder.mCategory.setText(items.get(position).getmCategory());
        }
        /*viewHolder.mType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.type(position);
            }
        });*/
        viewHolder.mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.category(position);
            }
        });
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.delete(position);
            }
        });

        viewHolder.mErrorTv.setVisibility(items.get(position).isError() ? View.VISIBLE : View.INVISIBLE);

        FontsUtil.setExistenceLight(mContext, viewHolder.mErrorTv);
        FontsUtil.setExistenceLight(mContext, viewHolder.mCategory);
        FontsUtil.setExistenceLight(mContext, viewHolder.mType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mCroppedImage, mDelete;
        TextView mCategory, mType, mErrorTv;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mCroppedImage = (ImageView) itemView.findViewById(R.id.item_capture_image);
            mCategory = (TextView) itemView.findViewById(R.id.item_cap_category_spnr);
            mType = (TextView) itemView.findViewById(R.id.item_cap_type_spnr);
            mErrorTv = (TextView) itemView.findViewById(R.id.item_cap_error_tv);
            mDelete = (ImageView) itemView.findViewById(R.id.item_cap_delete);
        }
    }

    public interface CropListCallBacks {

        void type(int position);

        void category(int position);

        void delete(int position);
    }
}
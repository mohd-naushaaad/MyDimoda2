package com.mydimoda.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydimoda.R;
import com.mydimoda.customView.SqureImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookImagesAdp extends RecyclerView.Adapter<LookImagesAdp.LookImageHolder> {

    private List list = new ArrayList();

    public LookImagesAdp(List list) {
        this.list = list;
    }

    @Override
    public LookImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new LookImageHolder(view);
    }

    @Override
    public void onBindViewHolder(LookImageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LookImageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        SqureImageView img;

        public LookImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

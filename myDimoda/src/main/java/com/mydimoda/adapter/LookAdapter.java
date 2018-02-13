package com.mydimoda.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydimoda.R;
import com.mydimoda.model.DemoModelForLook;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookAdapter extends RecyclerView.Adapter<LookAdapter.LookHolder> {

    private List<DemoModelForLook> lookList = new ArrayList<>();
    private LookImagesAdp lookImagesAdp;
    private Context mContext;

    public LookAdapter(List<DemoModelForLook> lookList, Context mContext) {
        this.lookList = lookList;
        this.mContext = mContext;
    }

    @Override
    public LookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_looks, parent, false);
        return new LookHolder(view);
    }

    @Override
    public void onBindViewHolder(LookHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return lookList.size();
    }

    public class LookHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_img_look)
        RecyclerView rvImgLook;

        public LookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            lookImagesAdp = new LookImagesAdp(lookList.get(position).getList());
            rvImgLook.setLayoutManager(new GridLayoutManager(mContext, 2));
            rvImgLook.setAdapter(lookImagesAdp);
        }
    }
}

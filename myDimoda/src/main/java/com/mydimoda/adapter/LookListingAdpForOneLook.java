package com.mydimoda.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.mydimoda.R;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.model.ModelLookListing;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/28/2018.
 */

public class LookListingAdpForOneLook extends RecyclerView.Adapter<LookListingAdpForOneLook.MyHolder> {
    private List<ModelLookListing> listOfCloth;
    private Context mContext;

    public LookListingAdpForOneLook(List<ModelLookListing> listOfCloth, Context mContext) {
        this.listOfCloth = listOfCloth;
        this.mContext = mContext;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look_listing_one_look, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return listOfCloth.size();
    }

    @OnClick({R.id.like_btn, R.id.dissmiss_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.like_btn:
                break;
            case R.id.dissmiss_btn:
                break;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fashion_gridview)
        GridView fashionGridview;
        @BindView(R.id.like_btn)
        Button likeBtn;
        @BindView(R.id.dissmiss_btn)
        Button dissmissBtn;
        @BindView(R.id.tv_clothtype)
        Existence_Light_TextView tvClothtype;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            fashionGridview.setAdapter(new DMFashionGridAdapter(mContext, listOfCloth.get(position).getList()));
            tvClothtype.setText(listOfCloth.get(position).getClothType());
//            tvClothtype.setTextColor(ContextCompat.getColor(mContext, listOfCloth.get(position).getColorCode()));
        }
    }
}

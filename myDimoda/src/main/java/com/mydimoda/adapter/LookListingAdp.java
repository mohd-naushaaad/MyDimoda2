package com.mydimoda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class LookListingAdp extends RecyclerView.Adapter<LookListingAdp.MyHolder> {

    public interface ClickListnerOfLook {
        void onClickOfLike(int pos);

        void onClickofDisLike(int pos);
    }

    private List<ModelLookListing> listOfCloth;
    private Context mContext;
    private ClickListnerOfLook listner;
    private boolean isFromTrip = false;

    public LookListingAdp(List<ModelLookListing> listOfCloth, Context mContext, ClickListnerOfLook listner) {
        this.listOfCloth = listOfCloth;
        this.mContext = mContext;
        this.listner = listner;
    }

    public void updateFlag(boolean b) {
        isFromTrip = b;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look_listing_one_look, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        SubItemOfLookAdp subItemOfLookAdp = new SubItemOfLookAdp(listOfCloth.get(position).getList(), mContext);
        if (isFromTrip) {
            holder.dissmissBtn.setVisibility(View.GONE);
        } else {
            holder.dissmissBtn.setVisibility(View.VISIBLE);
        }
        if (listOfCloth.get(position).getClothType().equalsIgnoreCase("after5")) {
            holder.tvClothtype.setText("business");
        } else {
            holder.tvClothtype.setText(listOfCloth.get(position).getClothType());
        }
        if (listOfCloth.get(position).isIsliked()) {
            holder.likeBtn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_liked));
        }

        /*if (listOfCloth.get(position).getClothType().equalsIgnoreCase("casual")) {
            holder.tvClothtype.setTextColor(Color.GREEN);
        } else if (listOfCloth.get(position).getClothType().equalsIgnoreCase("formal")) {
            holder.tvClothtype.setTextColor(Color.RED);

        } else if (listOfCloth.get(position).getClothType().equalsIgnoreCase("after5")) {
            holder.tvClothtype.setText("business");
            holder.tvClothtype.setTextColor(Color.BLUE);
        }*/

        holder.rvSubitem.setAdapter(subItemOfLookAdp);
    }

    @Override
    public int getItemCount() {
        return listOfCloth.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clothtype)
        Existence_Light_TextView tvClothtype;
        @BindView(R.id.rv_subitem)
        RecyclerView rvSubitem;
        @BindView(R.id.like_btn)
        Button likeBtn;
        @BindView(R.id.dissmiss_btn)
        Button dissmissBtn;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rvSubitem.setLayoutManager(new GridLayoutManager(mContext, 2));
        }

        @OnClick({R.id.like_btn, R.id.dissmiss_btn})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.like_btn:
                    listner.onClickOfLike(getAdapterPosition());
                    break;
                case R.id.dissmiss_btn:
                    listner.onClickofDisLike(getAdapterPosition());
                    break;
            }
        }

    }
}

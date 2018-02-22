package com.mydimoda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.swipe.SwipeLayout;
import com.mydimoda.R;
import com.mydimoda.customView.BrixtonLightText;
import com.mydimoda.customView.Existence_Light_TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/12/2018.
 */

public class ReviewTripAdp extends RecyclerView.Adapter<ReviewTripAdp.ReviewTripHolder> {
    public interface tripClickListner {
        void onClickofTrip(int pos);
    }

    private Context mContext;
    private List list = new ArrayList();
    private tripClickListner tripClickListner;

    public ReviewTripAdp(Context mContext, List list, ReviewTripAdp.tripClickListner tripClickListner) {
        this.mContext = mContext;
        this.list = list;
        this.tripClickListner = tripClickListner;
    }

    @Override
    public ReviewTripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_trip_planned, parent, false);
        return new ReviewTripHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewTripHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ReviewTripHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;
        @BindView(R.id.tv_trip_ttl)
        Existence_Light_TextView tvTripTtl;
        /*@BindView(R.id.tv_trip_status)
        Existence_Light_TextView tvTripStatus;*/
        @BindView(R.id.tv_trip_looks)
        Existence_Light_TextView tvTripLooks;
        @BindView(R.id.swiproot)
        SwipeLayout swiproot;

        public ReviewTripHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ll_delete, R.id.swiproot})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.ll_delete:
                    break;
                case R.id.swiproot:
                    break;
            }
        }
    }
}

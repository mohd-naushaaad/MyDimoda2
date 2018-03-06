package com.mydimoda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.swipe.SwipeLayout;
import com.mydimoda.R;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.model.ReviewTripData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/12/2018.
 */

public class ReviewTripAdp extends RecyclerView.Adapter<ReviewTripAdp.ReviewTripHolder> {


    public interface TripClickListner {
        void onClickofTrip(int pos);

        void onClickOfDeleteIcon(int pos);
    }

    public ReviewTripAdp(Context mContext, List<ReviewTripData> list, TripClickListner tripClickListner) {
        this.mContext = mContext;
        this.list = list;
        this.tripClickListner = tripClickListner;
    }

    private Context mContext;
    private List<ReviewTripData> list = new ArrayList();
    private TripClickListner tripClickListner;

    @Override
    public ReviewTripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_trip_planned, parent, false);
        return new ReviewTripHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewTripHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ReviewTripHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;
        @BindView(R.id.tv_date_in_dd)
        Existence_Light_TextView tvDateInDd;
        @BindView(R.id.tv_mnt_with_yr)
        Existence_Light_TextView tvMntWithYr;
        @BindView(R.id.tv_trip_ttl)
        Existence_Light_TextView tvTripTtl;
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
                    tripClickListner.onClickOfDeleteIcon(getAdapterPosition());
                    break;
                case R.id.swiproot:
                    tripClickListner.onClickofTrip(getAdapterPosition());
                    break;
            }
        }

        public void setData(int position) {
            tvTripTtl.setText(list.get(position).getTripTitle());
            tvTripLooks.setText(String.format(mContext.getString(R.string.number_of_look), list.get(position).getTotalLookList().size()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(list.get(position).getStartDate());
            tvDateInDd.setText(String.valueOf(cal.get(Calendar.DATE)));
            tvMntWithYr.setText(constant.getMonth(cal.get(Calendar.MONTH) + 1) + " " + cal.get(Calendar.YEAR));
        }

    }
}

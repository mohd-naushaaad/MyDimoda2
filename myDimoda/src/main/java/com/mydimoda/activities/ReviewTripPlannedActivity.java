package com.mydimoda.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.adapter.ReviewTripAdp;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/9/2018.
 */

public class ReviewTripPlannedActivity extends AppCompatActivity implements ReviewTripAdp.tripClickListner{
    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.back_txt)
    TextView backTxt;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.back_layout)
    RelativeLayout backLayout;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.rv_trip_planned)
    RecyclerView rvTripPlanned;
    private ReviewTripAdp reviewTripAdp;
    private List list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_trip_listing);
        ButterKnife.bind(this);
        applyCustomFont();
        setUpAdp();
        makeStaticList(15);
    }

    private void setUpAdp() {
        list = new ArrayList();
        reviewTripAdp = new ReviewTripAdp(this, list, this);
        rvTripPlanned.setLayoutManager(new LinearLayoutManager(this));
        rvTripPlanned.setAdapter(reviewTripAdp);
    }

    private void makeStaticList(int i) {
        for (int j = 0; j < i; j++) {
            list.add(j);
        }
        reviewTripAdp.notifyDataSetChanged();
    }

    private void applyCustomFont() {
        FontsUtil.setExistenceLight(this, titleView);
        FontsUtil.setExistenceLight(this, backTxt);
    }

    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClickofTrip(int pos) {

    }
}

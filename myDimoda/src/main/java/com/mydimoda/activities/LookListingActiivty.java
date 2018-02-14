package com.mydimoda.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mydimoda.R;
import com.mydimoda.adapter.LookAdapter;
import com.mydimoda.customView.BrixtonLightText;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.model.DemoModelForLook;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookListingActiivty extends AppCompatActivity {
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.title_view)
    Existence_Light_TextView titleView;
    @BindView(R.id.back_txt)
    Existence_Light_TextView backTxt;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.tv_for_trip)
    BrixtonLightText tvForTrip;
    @BindView(R.id.rv_looklisting)
    RecyclerView rvLooklisting;
    private List<DemoModelForLook> lookList;
    private LookAdapter lookAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_listing);
        ButterKnife.bind(this);
        setUpAdp();
        setStaticListing(10);
    }

    private void setUpAdp() {
        lookList = new ArrayList<>();
        lookAdapter = new LookAdapter(lookList, this);
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        rvLooklisting.setAdapter(lookAdapter);
    }

    private void setStaticListing(int i) {
        DemoModelForLook demoModelForLook;
        List sublist = new ArrayList();
        for (int j = 0; j < i; j++) {
            sublist.clear();
            for (int k = 0; k < 2; k++) {
                sublist.add(k);
            }
            demoModelForLook = new DemoModelForLook(sublist);
            lookList.add(demoModelForLook);
        }
        lookAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_share, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_share:
                break;
            case R.id.back_layout:
                break;
        }
    }
}

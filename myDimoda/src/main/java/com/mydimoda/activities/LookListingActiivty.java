package com.mydimoda.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.SharedPreferenceUtil;
import com.mydimoda.adapter.LookAdapter;
import com.mydimoda.constant;
import com.mydimoda.customView.BrixtonLightText;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.model.DemoModelForLook;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookListingActiivty extends AppCompatActivity implements LookAdapter.ClickListnerOfLook {
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
    Existence_Light_TextView tvForTrip;
    @BindView(R.id.rv_looklisting)
    RecyclerView rvLooklisting;
    @BindView(R.id.rl_coach_look_listing)
    RelativeLayout rlCoachLookListing;
    private List<DemoModelForLook> lookList;
    private LookAdapter lookAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_listing);
        ButterKnife.bind(this);
//        showShowcaseView();
        setUpAdp();
        setStaticListing(10);
    }

    private void setUpAdp() {
        lookList = new ArrayList<>();
        lookAdapter = new LookAdapter(lookList, this, this);
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        rvLooklisting.setAdapter(lookAdapter);
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_LOOK_LISTING, false)) {
            rlCoachLookListing.setVisibility(View.VISIBLE);
            rlCoachLookListing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlCoachLookListing.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_LOOK_LISTING, true);
                }
            });
        }

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
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClickOfLike(int pos) {
        /*lookList.get(pos).setLiked(!lookList.get(pos).isLiked());
        lookAdapter.notifyItemChanged(pos);
        if (lookList.get(pos).isLiked()) {
            showSimilarDialog();
        }*/
    }

    public void showSimilarDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        FontsUtil.setExistenceLight(this, title);
        title.setText(R.string.alert_added);

        Button okBtn = (Button) dialog.findViewById(R.id.okbtn);
        FontsUtil.setExistenceLight(this, okBtn);

        okBtn.setText("OK");
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onClickofClose(int pos) {
        /*lookList.get(pos).setColsed(!lookList.get(pos).isColsed());
        lookAdapter.notifyItemChanged(pos);*/
    }
}

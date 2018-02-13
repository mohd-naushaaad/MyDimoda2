package com.mydimoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mydimoda.R;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class TripHelpMeActivity extends Activity {

    @BindView(R.id.back_txt)
    Existence_Light_TextView backTxt;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_view)
    Existence_Light_TextView titleView;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.restore_btn)
    Button restoreBtn;
    @BindView(R.id.shirt_icon)
    ImageView shirtIcon;
    @BindView(R.id.shirt_txt)
    Existence_Light_TextView shirtTxt;
    @BindView(R.id.restore_layout)
    RelativeLayout restoreLayout;
    @BindView(R.id.pants_btn)
    Button pantsBtn;
    @BindView(R.id.pants_icon)
    ImageView pantsIcon;
    @BindView(R.id.pants_txt)
    Existence_Light_TextView pantsTxt;
    @BindView(R.id.pants_layout)
    RelativeLayout pantsLayout;
    @BindView(R.id.coat_btn)
    Button coatBtn;
    @BindView(R.id.coat_icon)
    ImageView coatIcon;
    @BindView(R.id.coat_txt)
    Existence_Light_TextView coatTxt;
    @BindView(R.id.coat_layout)
    RelativeLayout coatLayout;
    @BindView(R.id.tie_btn)
    Button tieBtn;
    @BindView(R.id.tie_icon)
    ImageView tieIcon;
    @BindView(R.id.tie_txt)
    Existence_Light_TextView tieTxt;
    @BindView(R.id.tie_layout)
    RelativeLayout tieLayout;
    @BindView(R.id.suit_btn)
    Button suitBtn;
    @BindView(R.id.suit_icon)
    ImageView suitIcon;
    @BindView(R.id.suit_txt)
    Existence_Light_TextView suitTxt;
    @BindView(R.id.suit_layout)
    RelativeLayout suitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me_selection);
        ButterKnife.bind(this);
    }

    private void showShowcaseView() {
       /* if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_HOME_SHOWN, false)) {
            mCoachmark.setVisibility(View.VISIBLE);
            mCoachmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachmark.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_HOME_SHOWN,true);
                }
            });
        }*/

    }


    @OnClick({R.id.back_layout, R.id.restore_btn, R.id.pants_btn, R.id.coat_btn, R.id.tie_btn, R.id.suit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.restore_btn:
            case R.id.pants_btn:
            case R.id.coat_btn:
            case R.id.tie_btn:
            case R.id.suit_btn:
                Intent intent = new Intent(this, TripSuggestedItemActivity.class);
                startActivity(intent);
                break;
        }
    }
}

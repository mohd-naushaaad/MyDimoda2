package com.mydimoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mydimoda.DMHelpActivity;
import com.mydimoda.R;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class TripHelpMeActivity extends Activity {

    ArrayList<Integer> list;
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

    @BindView(R.id.shirt_icon)
    ImageView shirtIcon;
    @BindView(R.id.shirt_txt)
    Existence_Light_TextView shirtTxt;
    @BindView(R.id.rl_shirts)
    RelativeLayout rlShirts;
    @BindView(R.id.pants_btn)
    Button pantsBtn;
    @BindView(R.id.pants_icon)
    ImageView pantsIcon;
    @BindView(R.id.pants_txt)
    Existence_Light_TextView pantsTxt;
    @BindView(R.id.rl_pants)
    RelativeLayout rlPants;
    @BindView(R.id.coat_btn)
    Button coatBtn;
    @BindView(R.id.coat_icon)
    ImageView coatIcon;
    @BindView(R.id.coat_txt)
    Existence_Light_TextView coatTxt;
    @BindView(R.id.rl_coats)
    RelativeLayout rlCoats;
    @BindView(R.id.tie_btn)
    Button tieBtn;
    @BindView(R.id.tie_icon)
    ImageView tieIcon;
    @BindView(R.id.tie_txt)
    Existence_Light_TextView tieTxt;
    @BindView(R.id.rl_tie)
    RelativeLayout rlTie;
    @BindView(R.id.suit_btn)
    Button suitBtn;
    @BindView(R.id.suit_icon)
    ImageView suitIcon;
    @BindView(R.id.suit_txt)
    Existence_Light_TextView suitTxt;
    @BindView(R.id.rl_suit)
    RelativeLayout rlSuit;
    @BindView(R.id.shirt_btn)
    Button shirtBtn;
    @BindView(R.id.menu_btn)
    Button menuBtn;
    @BindView(R.id.menu_list)
    ListView menuList;
    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me_selection);
        ButterKnife.bind(this);
        manageListingOfClothes();
        showMenu();
        sideMenuClickListner();
    }

    public void showMenu() {
        System.out.println("Setting" + constant.gMenuList);
        menuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
    }

    private void sideMenuClickListner() {
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                constant.selectMenuItem(TripHelpMeActivity.this, position,
                        true);
            }
        });
    }

    public void slideMenu() {
        if (drawerLayout.isDrawerOpen(menuLayout)) {
            drawerLayout.closeDrawer(menuLayout);
        } else
            drawerLayout.openDrawer(menuLayout);
    }

    private void manageListingOfClothes() {
        list = (ArrayList<Integer>) getIntent().getSerializableExtra(constant.BUNDLE_LOOKLISTING);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == constant.casual) {
                showCasualLook();
            }
            if (list.get(i) == constant.formal) {
                showFormalLook();
            }
            if (list.get(i) == constant.business) {
                showBusinessLook();
            }
        }
    }

    private void showCasualLook() {
        rlShirts.setVisibility(View.VISIBLE);
        rlPants.setVisibility(View.VISIBLE);
    }

    private void showFormalLook() {
        rlShirts.setVisibility(View.VISIBLE);
        rlPants.setVisibility(View.VISIBLE);
        rlCoats.setVisibility(View.VISIBLE);
        rlTie.setVisibility(View.VISIBLE);
        rlSuit.setVisibility(View.VISIBLE);
    }

    private void showBusinessLook() {
        rlShirts.setVisibility(View.VISIBLE);
        rlPants.setVisibility(View.VISIBLE);
        rlCoats.setVisibility(View.VISIBLE);
        rlSuit.setVisibility(View.VISIBLE);
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


    @OnClick({R.id.back_layout, R.id.shirt_btn, R.id.pants_btn, R.id.coat_btn, R.id.tie_btn, R.id.suit_btn, R.id.menu_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.shirt_btn:
                navigatetoClothListing("shirt");
                break;
            case R.id.pants_btn:
                navigatetoClothListing("trousers");

                break;
            case R.id.coat_btn:
                navigatetoClothListing("jacket");

                break;
            case R.id.tie_btn:
                navigatetoClothListing("tie");

                break;
            case R.id.suit_btn:
                navigatetoClothListing("suit");
                break;
            case R.id.menu_btn:
                slideMenu();
                break;
        }
    }

    private void navigatetoClothListing(String type) {
        Intent intent = new Intent(this, DMHelpActivity.class);
        intent.putExtra(constant.BUNDLE_ISFROMPLANNEWTRIP, true);
        intent.putExtra("type", type);
        intent.putExtra("look", getIntent().getStringExtra("look"));
        startActivity(intent);
    }
}

package com.mydimoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.DMHomeActivity;
import com.mydimoda.R;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.constant;
import com.mydimoda.customView.BrixtonLightText;
import com.mydimoda.widget.cropper.util.FontsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/8/2018.
 */

public class PlanANewTripActivity extends Activity {


    @BindView(R.id.menu_btn)
    Button menuBtn;
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
    @BindView(R.id.ed_name_trip)
    EditText edNameTrip;
    @BindView(R.id.tv_lbl_date_range)
    BrixtonLightText tvLblDateRange;
    @BindView(R.id.tv_lbl_need_pkg)
    BrixtonLightText tvLblNeedPkg;
    @BindView(R.id.rb_more_look)
    RadioButton rbMoreLook;
    @BindView(R.id.rb_less_look)
    RadioButton rbLessLook;
    @BindView(R.id.tv_lbl_que_looks)
    BrixtonLightText tvLblQueLooks;
    @BindView(R.id.tv_stylr_me)
    BrixtonLightText tvStylrMe;
    @BindView(R.id.tv_help_me)
    BrixtonLightText tvHelpMe;
    @BindView(R.id.tv_trip)
    BrixtonLightText tvTrip;
    @BindView(R.id.ll_plan_a_new_trip)
    LinearLayout llPlanANewTrip;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.menu_list)
    ListView menuList;
    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_content_layout)
    RelativeLayout mainContentLayout;
    @BindView(R.id.RelativeLayout1)
    RelativeLayout RelativeLayout1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannewtrip);
        ButterKnife.bind(this);
        rbMoreLook.setChecked(true);
        init();
        setFontToTextView();
        sideMenuClickListner();
    }

    private void setFontToTextView() {
        FontsUtil.setExistenceLight(this, titleView);
        FontsUtil.setExistenceLight(this, backTxt);
        FontsUtil.setLight(this, rbLessLook);
        FontsUtil.setLight(this, rbMoreLook);
        FontsUtil.setLight(this, edNameTrip);
    }

    private void sideMenuClickListner() {
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 5) {
                    slideMenu();
                } else {
                    constant.selectMenuItem(PlanANewTripActivity.this, position,
                            true);
                }
            }
        });
    }

    @OnClick({R.id.menu_btn, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_btn:
                slideMenu();
                break;
            case R.id.back_layout:
                Intent intent = new Intent(this,
                        DMHomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void slideMenu() {
        if (drawerLayout.isDrawerOpen(menuLayout)) {
            drawerLayout.closeDrawer(menuLayout);
        } else
            drawerLayout.openDrawer(menuLayout);
    }

    public void init() {
        showMenu();
        showShowcaseView();
    }

    public void showMenu() {
        System.out.println("Setting" + constant.gMenuList);
        menuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
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

}

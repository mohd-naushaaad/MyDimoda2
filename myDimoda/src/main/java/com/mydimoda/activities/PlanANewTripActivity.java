package com.mydimoda.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.R;
import com.mydimoda.SharedPreferenceUtil;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/8/2018.
 */

public class PlanANewTripActivity extends Activity {
    int val_causal = 0, val_formal = 0, val_business = 0;
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
    Existence_Light_TextView tvLblDateRange;
    @BindView(R.id.iv_calender)
    ImageView ivCalender;
    @BindView(R.id.tv_start_dd)
    Existence_Light_TextView tvStartDd;
    @BindView(R.id.tv_start_mm)
    Existence_Light_TextView tvStartMm;
    @BindView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @BindView(R.id.tv_start_date)
    Existence_Light_TextView tvStartDate;
    @BindView(R.id.tv_end_dd)
    Existence_Light_TextView tvEndDd;
    @BindView(R.id.tv_end_mm)
    Existence_Light_TextView tvEndMm;
    @BindView(R.id.ll_end_date)
    LinearLayout llEndDate;
    @BindView(R.id.tv_end_date)
    Existence_Light_TextView tvEndDate;
    @BindView(R.id.tv_lbl_need_pkg)
    Existence_Light_TextView tvLblNeedPkg;
    @BindView(R.id.rb_more_look)
    RadioButton rbMoreLook;
    @BindView(R.id.rb_less_look)
    RadioButton rbLessLook;
    @BindView(R.id.tv_lbl_que_looks)
    Existence_Light_TextView tvLblQueLooks;
    @BindView(R.id.tv_casual_minus)
    Existence_Light_TextView tvCasualMinus;
    @BindView(R.id.tv_casual_val)
    Existence_Light_TextView tvCasualVal;
    @BindView(R.id.tv_casual_plus)
    Existence_Light_TextView tvCasualPlus;
    @BindView(R.id.tv_formal_minus)
    Existence_Light_TextView tvFormalMinus;
    @BindView(R.id.tv_formal_val)
    Existence_Light_TextView tvFormalVal;
    @BindView(R.id.tv_formal_plus)
    Existence_Light_TextView tvFormalPlus;
    @BindView(R.id.tv_business_minus)
    Existence_Light_TextView tvBusinessMinus;
    @BindView(R.id.tv_business_val)
    Existence_Light_TextView tvBusinessVal;
    @BindView(R.id.tv_business_plus)
    Existence_Light_TextView tvBusinessPlus;
    @BindView(R.id.rl_styleme)
    RelativeLayout rlStyleme;
    @BindView(R.id.rl_helpme)
    RelativeLayout rlHelpme;
    @BindView(R.id.btn_trip)
    Button btnTrip;
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
    @BindView(R.id.rl_coach_plan_new_trip)
    RelativeLayout rlCoachPlanNewTrip;
    @BindView(R.id.RelativeLayout1)
    RelativeLayout RelativeLayout1;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannewtrip);
        ButterKnife.bind(this);
        init_view();
        init();
        setFontToTextView();
        sideMenuClickListner();
    }

    private void init_view() {
        rbMoreLook.setChecked(true);
        tvCasualVal.setText(String.valueOf(val_causal));
        tvFormalVal.setText(String.valueOf(val_formal));
        tvBusinessVal.setText(String.valueOf(val_business));
        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvStartDate.setVisibility(View.GONE);
                llStartDate.setVisibility(View.VISIBLE);
                tvStartDd.setText(String.valueOf(dayOfMonth));
                tvStartMm.setText(constant.getMonth(monthOfYear + 1));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tvEndDate.setVisibility(View.GONE);
                llEndDate.setVisibility(View.VISIBLE);
                tvEndDd.setText(String.valueOf(dayOfMonth));
                tvEndMm.setText(constant.getMonth(monthOfYear + 1));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setFontToTextView() {
        FontsUtil.setExistenceLight(this, titleView);
        FontsUtil.setExistenceLight(this, btnTrip);
        FontsUtil.setExistenceLight(this, backTxt);
        FontsUtil.setExistenceLight(this, rbLessLook);
        FontsUtil.setExistenceLight(this, rbMoreLook);
        FontsUtil.setExistenceLight(this, edNameTrip);
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

    @OnClick({R.id.menu_btn, R.id.back_layout, R.id.tv_casual_minus, R.id.tv_casual_plus, R.id.tv_formal_minus, R.id.tv_formal_plus
            , R.id.tv_business_minus, R.id.tv_business_plus, R.id.tv_start_date, R.id.tv_end_date, R.id.ll_start_date, R.id.ll_end_date
            , R.id.btn_trip, R.id.rl_styleme, R.id.rl_helpme, R.id.iv_calender})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_btn:
                slideMenu();
                break;
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.tv_casual_minus:
                if (val_causal > 0) {
                    tvCasualVal.setText(String.valueOf(--val_causal));
                }
                break;
            case R.id.tv_casual_plus:
                tvCasualVal.setText(String.valueOf(++val_causal));
                break;
            case R.id.tv_formal_minus:
                if (val_formal > 0) {
                    tvFormalVal.setText(String.valueOf(--val_formal));
                }
                break;
            case R.id.tv_formal_plus:
                tvFormalVal.setText(String.valueOf(++val_formal));
                break;
            case R.id.tv_business_minus:
                if (val_business > 0) {
                    tvBusinessVal.setText(String.valueOf(--val_business));
                }
                break;
            case R.id.tv_business_plus:
                tvBusinessVal.setText(String.valueOf(++val_business));
                break;
            case R.id.tv_start_date:
            case R.id.ll_start_date:
                startDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                startDatePickerDialog.show();
                break;
            case R.id.tv_end_date:
            case R.id.ll_end_date:
            case R.id.iv_calender:
                endDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                endDatePickerDialog.show();
                break;
            case R.id.btn_trip:
                Intent intent = new Intent(this, ReviewTripPlannedActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_styleme:
                Intent styleMeintent = new Intent(this, LookListingActiivty.class);
                startActivity(styleMeintent);
                break;
            case R.id.rl_helpme:
                passListing();
                break;
        }
    }

    private void passListing() {
        ArrayList<Integer> list = new ArrayList<>();
        if (val_causal == 0 && val_formal == 0 && val_business == 0) {
            Toast.makeText(this, "Please Select alteast one Cloth", Toast.LENGTH_SHORT).show();
        } else {
            if (val_causal > 0) {
                list.add(constant.casual);
            }
            if (val_formal > 0) {
                list.add(constant.formal);
            }
            if (val_business > 0) {
                list.add(constant.business);
            }
            Intent helpMeintent = new Intent(this, TripHelpMeActivity.class);
            helpMeintent.putExtra(constant.BUNDLE_LOOKLISTING, list);
            startActivity(helpMeintent);
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
//        showShowcaseView();
    }

    public void showMenu() {
        System.out.println("Setting" + constant.gMenuList);
        menuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_PLAN_NEW_TRIP_SHOWN, false)) {
            rlCoachPlanNewTrip.setVisibility(View.VISIBLE);
            rlCoachPlanNewTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlCoachPlanNewTrip.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_PLAN_NEW_TRIP_SHOWN, true);
                }
            });
        }

    }

}

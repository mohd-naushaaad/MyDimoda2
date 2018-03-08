package com.mydimoda.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.mydimoda.social.google.GoogleIAP;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/8/2018.
 */

public class PlanANewTripActivity extends Activity {
    int val_causal = 0, val_formal = 0, val_business = 0;
    private ArrayList<String> listTypeSelection = new ArrayList<>();
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
    private Calendar calendar;
    private Date startDate, endDate;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private long gapDiffbetweenDate = 0;

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (ev.getAction() == MotionEvent.ACTION_DOWN &&
                !getLocationOnScreen(edNameTrip).contains(x, y)) {
            InputMethodManager input = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(edNameTrip.getWindowToken(), 0);
        }

        return super.dispatchTouchEvent(ev);
    }

    protected Rect getLocationOnScreen(EditText mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }

    private void init_view() {
        rbMoreLook.setChecked(true);
        tvCasualVal.setText(String.valueOf(val_causal));
        tvFormalVal.setText(String.valueOf(val_formal));
        tvBusinessVal.setText(String.valueOf(val_business));
        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar = Calendar.getInstance();
//                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

                calendar.set(year, monthOfYear, dayOfMonth);
                /*calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);*/

                startDate = calendar.getTime();

                tvStartDate.setVisibility(View.GONE);
                llStartDate.setVisibility(View.VISIBLE);
                tvStartDd.setText(String.valueOf(dayOfMonth));
                tvStartMm.setText(constant.getMonth(monthOfYear + 1));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar = Calendar.getInstance();
//                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

                calendar.set(year, monthOfYear, dayOfMonth);
               /* calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);*/

                endDate = calendar.getTime();

                tvEndDate.setVisibility(View.GONE);
                llEndDate.setVisibility(View.VISIBLE);
                tvEndDd.setText(String.valueOf(dayOfMonth));
                tvEndMm.setText(constant.getMonth(monthOfYear + 1));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private boolean hasPurchase() {

        final ParseUser user = ParseUser.getCurrentUser();
        boolean bPurchased = user.getBoolean("Buy");
        int maxCount = user.getInt(constant.USER_MAX_COUNT); // new max count according to new policy

        if (!bPurchased) {
            int count = user.getInt("Count");

            if (SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase(
                    "1")) {
                return true;
            } else if (count >= (maxCount >= 5 ? maxCount : constant.maxCount)) {
                /*
                 * if(SharedPreferenceUtil.getString("inApp",
				 * "0").equalsIgnoreCase("1")) { gotoAlgorithmActivity(); } else
				 * {
				 */
                showPurchaseAlert();
                /* } */
            } else {
                return true;

            }
        } else {
            return true;

        }
        return false;
    }

    private void showPurchaseAlert() {

        new AlertDialog.Builder(this)
                .setTitle("Upgrade for myDiModa!")
                .setMessage(
                        "To get more Style Me mode, please buy unlimited styling license at 1.99$")
                .setCancelable(false)
                .setNegativeButton("Buy",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                GoogleIAP.buyFeature(0);

						/*
                         * showProgressBar("");
						 *
						 * ParseUser user = ParseUser.getCurrentUser();
						 * user.put("Buy", true);
						 * user.saveInBackground(new SaveCallback() {
						 *
						 * @Override public void done(ParseException e1)
						 * {
						 *
						 * hideProgressBar();
						 *
						 * if(e1 == null) { gotoAlgorithmActivity(); }
						 * else { Toast.makeText(DMStyleActivity.this,
						 * e1.toString(), Toast.LENGTH_LONG).show(); } }
						 * });
						 */
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }


    private boolean isvalidate() {
        if (edNameTrip.getText().toString().trim().length() > 0) {
            if (val_causal == 0 && val_formal == 0 && val_business == 0) {
                Toast.makeText(this, "Please Provide atleast one look for this trip.", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (startDate != null && endDate != null) {
                    if (dateFormatter.format(startDate).equalsIgnoreCase(dateFormatter.format(endDate))) {
                        if ((val_causal + val_formal + val_business) > 1) {
                            Toast.makeText(this, "Number of looks you have requested do not match with trip dates. You could only select one pair for each day of your trip.", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (startDate.compareTo(endDate) > 0) {
                            Toast.makeText(this, "Trip end date should be later than trip start date", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            long different = endDate.getTime() - startDate.getTime();
                            long secondsInMilli = 1000;
                            long minutesInMilli = secondsInMilli * 60;
                            long hoursInMilli = minutesInMilli * 60;
                            long daysInMilli = hoursInMilli * 24;
                            gapDiffbetweenDate = different / daysInMilli;
                            if (gapDiffbetweenDate > 14) {
                                Toast.makeText(this, "Date Range should be less than 2 weeks for a trip", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                if ((val_causal + val_formal + val_business) > gapDiffbetweenDate) {
                                    Toast.makeText(this, "Number of looks you have requested do not match with trip dates. You could only select one pair for each day of your trip.", Toast.LENGTH_SHORT).show();
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Please provide start and end date for this trip", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else {
            Toast.makeText(this, "Please provide a meaningful name for this trip", Toast.LENGTH_SHORT).show();
            return false;
        }
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
                if (isvalidate()) {
                    if (hasPurchase()) {

                        Intent styleMeintent = new Intent(this, LookListingActiivty.class);
                        listTypeSelection.clear();
                        for (int i = 0; i < val_causal; i++) {
                            listTypeSelection.add("casual");
                        }
                        for (int i = 0; i < val_formal; i++) {
                            listTypeSelection.add("formal");
                        }
                        for (int i = 0; i < val_business; i++) {
                            listTypeSelection.add("after5");
                        }
                        styleMeintent.putExtra(constant.BUNDLE_LIST_OF_SELECTION, listTypeSelection);
                        styleMeintent.putExtra(constant.BUNDLE_START_DATE, startDate);
                        styleMeintent.putExtra(constant.BUNDLE_TRIP_NAME, edNameTrip.getText().toString().trim());
                        startActivity(styleMeintent);
                        /*Intent styleMeintent = new Intent(this, LooklistingActivityForOneLook.class);
                        constant.BUNDLE_TRIP_NAME = edNameTrip.getText().toString();
                        styleMeintent.putExtra(constant.BUNDLE_CATEGORY, "casual");
                        styleMeintent.putExtra(constant.BUNDLE_MODE, "style me");
                        startActivity(styleMeintent);*/
//                        saveDataintoParseDb();
                    }
                }
                break;
            case R.id.rl_helpme:
                if (isvalidate()) {
                    if (hasPurchase()) {
                        constant.BUNDLE_TRIP_NAME = edNameTrip.getText().toString().trim();
                        passListing();
                    }
                }
                break;
        }
    }

    private void saveDataintoParseDb() {
        List<ParseObject> lookListing = new ArrayList<>();
        List<ParseObject> subItemListing = new ArrayList<>();

        List<String> list = new ArrayList<>();
        list.add("test by Parth");
        ParseObject testObject = new ParseObject("TripData");
        testObject.put("Title", "Test Trip");
        testObject.put("Start_date", startDate);
        testObject.put("User", ParseUser.getCurrentUser());
        testObject.put("Looks", list);
        testObject.saveInBackground();
    }

    private void goToLookListingActivity() {
        /*if (constant.gIsCloset) {
            ParseUser user = ParseUser.getCurrentUser();
            int count = user.getInt("Count");
            count++;
            user.put("Count", count);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }

            });
            Intent intent1 = new Intent(PlanANewTripActivity.this, LooklistingActivityForOneLook.class);
            startActivity(intent1);
        } else {
            Intent intent1 = new Intent(PlanANewTripActivity.this, LooklistingActivityForOneLook.class);
            startActivity(intent1);
        }*/
    }


    private void passListing() {
        ArrayList<Integer> list = new ArrayList<>();

        /*if (val_causal > 0) {
            list.add(constant.casual);
        }
        if (val_formal > 0) {
            list.add(constant.formal);
        }
        if (val_business > 0) {
            list.add(constant.business);
        }*/
        list.add(constant.casual);
        constant.STARTDATE = startDate;
        constant.TRIPNAME = edNameTrip.getText().toString().trim();

        Intent helpMeintent = new Intent(this, TripHelpMeActivity.class);
        helpMeintent.putExtra(constant.BUNDLE_LOOKLISTING, list);
        helpMeintent.putExtra(constant.BUNDLE_TRIP_NAME, edNameTrip.getText().toString().trim());
        startActivity(helpMeintent);

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

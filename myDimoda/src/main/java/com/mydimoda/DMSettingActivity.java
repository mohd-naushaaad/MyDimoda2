package com.mydimoda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.social.google.GoogleIAP;
import com.mydimoda.social.google.util.IabHelper;
import com.mydimoda.social.google.util.IabHelper.OnIabSetupFinishedListener;
import com.mydimoda.social.google.util.IabHelper.QueryInventoryFinishedListener;
import com.mydimoda.social.google.util.IabResult;
import com.mydimoda.social.google.util.Inventory;
import com.mydimoda.social.google.util.Purchase;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DMSettingActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;
    @BindView(R.id.setting_notification_layout)
    RelativeLayout mNotificationRL;
    TextView mNotificationTxt;
    @BindView(R.id.setting_notification_toggleButton)
    SwitchCompat mNotificationToggleBtn;
    @BindView(R.id.style_me_label_txt)
    TextView mStyleMeLAbel;
    @BindView(R.id.act_setting_stylme_point_tv)
    TextView mStylemePointTv;
    @BindView(R.id.act_sett_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;

    // / Layout variables
    TextView vTxtRestore, vTxtLogout, vTxtIntro, VTxtRate;
    RelativeLayout vLytRestore, vLytLogout, vLytIntro, vLyRate;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        final ParseUser user = ParseUser.getCurrentUser();
        boolean bPurchased = user.getBoolean(constant.RATED_APP);
        System.out.println("HomePurchased" + bPurchased);

        if (bPurchased == true) {
            System.out.println("TRUE" + bPurchased);

        } else {
            if (AppUtils.getPref("pro", DMSettingActivity.this) != null) {
                if (AppUtils.getPref("pro", DMSettingActivity.this).equalsIgnoreCase("false")) {
                    AppRater.app_launched(DMSettingActivity.this);
                } else {
                    AppUtils.putPref("pro", "true", DMSettingActivity.this);
                }
            } else if (AppUtils.getPref("pro", DMSettingActivity.this) == null) {
                AppUtils.putPref("pro", "false", DMSettingActivity.this);
                AppRater.app_launched(DMSettingActivity.this);
            } else {
                AppUtils.putPref("pro", "false", DMSettingActivity.this);
                AppRater.app_launched(DMSettingActivity.this);
            }
        }
        // / menu
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);
        vTxtTitle = (TextView) findViewById(R.id.title_view);
        FontsUtil.setExistenceLight(this, vTxtTitle);

        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        // / layout
        vTxtRestore = (TextView) findViewById(R.id.restore_txt);
        FontsUtil.setExistenceLight(this, vTxtRestore);

        vTxtLogout = (TextView) findViewById(R.id.logout_txt);
        FontsUtil.setExistenceLight(this, vTxtLogout);

        vTxtIntro = (TextView) findViewById(R.id.intro_txt);
        FontsUtil.setExistenceLight(this, vTxtIntro);

        VTxtRate = (TextView) findViewById(R.id.rate_txt);
        FontsUtil.setExistenceLight(this, VTxtRate);

        mNotificationTxt = (TextView) findViewById(R.id.noti_txt);

        FontsUtil.setExistenceLight(this, mNotificationTxt);
        FontsUtil.setExistenceLightAnyView(this, mNotificationToggleBtn);

        FontsUtil.setExistenceLight(this, mStyleMeLAbel);
        FontsUtil.setExistenceLight(this, mStylemePointTv);
        setStyleCount(user);


        mNotificationToggleBtn.setChecked(SharedPreferenceUtil.getBoolean(constant.PREF_IS_NOTI_ENABLE, true));

        vLytRestore = (RelativeLayout) findViewById(R.id.restore_layout);
        vLytLogout = (RelativeLayout) findViewById(R.id.logout_layout);
        vLytIntro = (RelativeLayout) findViewById(R.id.intro_layout);
        vLyRate = (RelativeLayout) findViewById(R.id.rate_layout);

        vBtnMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                slideMenu();
            }
        });

        vMenuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 6) {
                    slideMenu();
                } else {
                    constant.selectMenuItem(DMSettingActivity.this, position,
                            true);
                }
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        vLytRestore.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                GoogleIAP.initiate(DMSettingActivity.this,
                        getResources().getString(R.string.google_iap_base64));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DMSettingActivity.this);
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder
                        .setMessage(
                                "Buy me unlimited style, Already purchased go for restore instead.")
                        .setCancelable(false)
                        .setPositiveButton("Restore",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        restore();
                                        // finish();
                                    }
                                })
                        .setNegativeButton("Purchase",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        purchase();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //restore();
            }
        });

        vLytIntro.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DMSettingActivity.this,
                        DMIntroActivity.class);
                intent.putExtra("isStart", false);
                startActivity(intent);
            }
        });

        vLytLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                logout();
            }
        });


        vLyRate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(DMSettingActivity.this,
                        Activity_RateApp.class);
                startActivity(intent);

            }
        });

        mNotificationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationToggleBtn.setChecked(!mNotificationToggleBtn.isChecked());
                SharedPreferenceUtil.putValue(constant.PREF_IS_NOTI_ENABLE, mNotificationToggleBtn.isChecked());
                SharedPreferenceUtil.save();
            }
        });
        showShowcaseView();

    }

    // / --------------------------------- slide menu section
    // ------------------------------
    public void slideMenu() {
        if (vDrawerLayout.isDrawerOpen(vMenuLayout)) {
            vDrawerLayout.closeDrawer(vMenuLayout);
        } else
            vDrawerLayout.openDrawer(vMenuLayout);
    }

    public void init() {
        showMenu();
        setStyleCount(ParseUser.getCurrentUser());

        // setViewWithFont();
    }

    // / --------------------------------- show menu list
    // --------------------------------------
    public void showMenu() {
        vMenuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
    }


    // / -------------------------------------------- logout
    // -----------------------------
    public void logout() {
        SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME,
                0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("username", "");
        editor.putString("email", "");
        editor.putString("password", "");
        editor.putString("userid", "");

        editor.commit();

        //Keyur
        //canceling notifications if any
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1234);
        mNotificationManager.cancelAll();

        //mayur resetting checks
        AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, false, this);
        SharedPreferenceUtil.putValue(constant.USER_MAX_COUNT_INITILISED, false);
        SharedPreferenceUtil.putValue(constant.PREF_MAX_COUNT_CONFIGURED, false);

//Now call logout
        ParseUser.logOutInBackground();

        Intent intent = new Intent(this, DMLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }

    protected void purchase(){
        ParseUser user = ParseUser.getCurrentUser();
        boolean userPurchased = user.getBoolean("Buy");
        if(!userPurchased){
            GoogleIAP.buyFeature(0);
        }else{
            showToast("You have already purchased this before, Please Try Restoring it.");
        }

    }

    protected void restore() {

        final IabHelper iabHelper = new IabHelper(this, getResources()
                .getString(R.string.google_iap_base64));
        iabHelper.enableDebugLogging(true);

        iabHelper.startSetup(new OnIabSetupFinishedListener() {

            @Override
            public void onIabSetupFinished(IabResult result) {
                Log.e("IAB", "SETUP FINISHED");
                if (!result.isSuccess()) {
                    Log.e("IAB", "SETUP NOT OK");
                    return;
                } else
                    Log.e("IAB", "SETUP OK");

                iabHelper
                        .queryInventoryAsync(new QueryInventoryFinishedListener() {
                            @Override
                            public void onQueryInventoryFinished(
                                    IabResult result, Inventory inv) {
                                Log.e("IAB", "Query inventory finished.");
                                if (result.isFailure()) {
                                    Log.e("IAB", "Failed to query inventory: "
                                            + result);
                                    return;
                                }
                                Log.e("IAB", "Query inventory was successful.");

                                // Do we have the premium upgrade?
                                boolean mIsPremium = inv
                                        .hasPurchase(GoogleIAP.SKU_PAID);
                                Purchase p = inv
                                        .getPurchase(GoogleIAP.SKU_PAID);
                                if (p != null) {
                                    Log.e("IAB PURCHASE STATE", IabHelper
                                            .getResponseDesc(p
                                                    .getPurchaseState()));
                                } else {
                                    Log.e("IAB PURCHASE STATE",
                                            "Purchase is null");
                                }

                                Log.e("IAB", "User is "
                                        + (mIsPremium ? "PREMIUM"
                                        : "NOT PREMIUM"));

                                Log.e("", "mIsPremium == " + mIsPremium);
                                ParseUser user = ParseUser.getCurrentUser();
                                boolean userPurchased = user.getBoolean("Buy");
                                if (mIsPremium||userPurchased) {
                                    Log.e("", "In App from base activity == 1");
                                    try {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                showToast("Item restored successfully");
                                                SharedPreferenceUtil.putValue(
                                                        "inApp", "1");
                                                SharedPreferenceUtil.save();
                                                Intent intent = new Intent(
                                                        DMSettingActivity.this,
                                                        DMHomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            showToast("Failure to consume since item is not owned.");
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setStyleCount(ParseUser user) {

        if (user.getBoolean(constant.RATED_APP) || user.getBoolean("Buy") ||
                SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase("1")) {
            mStylemePointTv.setText(getResources().getString(R.string.unlimited));
        } else {
            try {
                int i = user.getInt(constant.USER_MAX_COUNT) - user.getInt("Count");
                mStylemePointTv.setText((i >= 0 ? i : 0) + " " + (i > 1 ? "Styles left" : "Style left"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_SETTING_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_SETTING_SHOWN, true);
                }
            });
        }
    }
}

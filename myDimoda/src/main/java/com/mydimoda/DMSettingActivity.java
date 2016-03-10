package com.mydimoda;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.social.google.GoogleIAP;
import com.mydimoda.social.google.util.IabHelper;
import com.mydimoda.social.google.util.IabHelper.OnIabSetupFinishedListener;
import com.mydimoda.social.google.util.IabHelper.QueryInventoryFinishedListener;
import com.mydimoda.social.google.util.IabResult;
import com.mydimoda.social.google.util.Inventory;
import com.mydimoda.social.google.util.Purchase;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DMSettingActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;
    @Bind(R.id.setting_notification_layout)
    RelativeLayout mNotificationRL;
    TextView mNotificationTxt;
    @Bind(R.id.setting_notification_toggleButton)
    ToggleButton mNotificationToggleBtn;

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
        boolean bPurchased = user.getBoolean("ratedMyDimoda");
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
        FontsUtil.setExistenceLightAnyView(this,mNotificationToggleBtn);
        mNotificationToggleBtn.setChecked(SharedPreferenceUtil.getBoolean(constant.PREF_IS_NOTI_ENABLE,true));

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
                constant.selectMenuItem(DMSettingActivity.this, position, true);
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
                restore();
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

        Intent intent = new Intent(this, DMLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
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

                                if (mIsPremium) {
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

}

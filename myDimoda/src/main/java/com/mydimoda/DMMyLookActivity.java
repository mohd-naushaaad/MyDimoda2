package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DMMyLookActivity extends Activity implements OnClickListener {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    RelativeLayout vFavoritesLayout;
    RelativeLayout vCasualLayout;
    RelativeLayout vAfterLayout;
    RelativeLayout vFormalLayout;
    TextView vFavoritesTextView;
    TextView vCausualTextView;
    TextView vAfterTextView;
    TextView vFormalTextView;
    // ListView vOccasionList;
    int n[] = {1, 2, 3};
    int type;
    @BindView(R.id.act_ocsn_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occasion);
        ButterKnife.bind(this);
        // / layout
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);

        final ParseUser user = ParseUser.getCurrentUser();
        boolean bPurchased = user.getBoolean("ratedMyDimoda");
        System.out.println("HomePurchased" + bPurchased);

        if (bPurchased == true) {
            System.out.println("TRUE" + bPurchased);
        } else {
            if (AppUtils.getPref("pro", DMMyLookActivity.this) != null) {
                if (AppUtils.getPref("pro", DMMyLookActivity.this).equalsIgnoreCase("false")) {
                    AppRater.app_launched(DMMyLookActivity.this);
                } else {
                    AppUtils.putPref("pro", "true", DMMyLookActivity.this);
                }
            } else if (AppUtils.getPref("pro", DMMyLookActivity.this) == null) {
                AppUtils.putPref("pro", "false", DMMyLookActivity.this);
                AppRater.app_launched(DMMyLookActivity.this);
            } else {
                AppUtils.putPref("pro", "false", DMMyLookActivity.this);
                AppRater.app_launched(DMMyLookActivity.this);
            }
        }


        Random random = new Random();
        System.out.println(n[random.nextInt(n.length)]);
        type = n[random.nextInt(n.length)];
        if (getIntent() != null) {

            try {
                if (getIntent().hasExtra("notification_look")) {
                    if (getIntent().getExtras().getString("notification_look") != null) {
                        System.out.println("Type--Look" + type);
                        goStyleActivity_8(type);


                    }
                }
            } catch (Exception e) {

            }
        }


        vTxtTitle = (TextView) findViewById(R.id.title_view);
        FontsUtil.setExistenceLight(this, vTxtTitle);

        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        vFavoritesLayout = (RelativeLayout) findViewById(R.id.layout_favorites);
        vFavoritesLayout.setOnClickListener(this);

        vCasualLayout = (RelativeLayout) findViewById(R.id.layout_casual);
        vCasualLayout.setOnClickListener(this);

        vAfterLayout = (RelativeLayout) findViewById(R.id.layout_after5);
        vAfterLayout.setOnClickListener(this);

        vFormalLayout = (RelativeLayout) findViewById(R.id.layout_formal);
        vFormalLayout.setOnClickListener(this);

        vFavoritesTextView = (TextView) findViewById(R.id.tv_favorites);
        FontsUtil.setExistenceLight(this, vFavoritesTextView);

        vCausualTextView = (TextView) findViewById(R.id.tv_causual);
        FontsUtil.setExistenceLight(this, vCausualTextView);
        vAfterTextView = (TextView) findViewById(R.id.tv_after);
        FontsUtil.setExistenceLight(this, vAfterTextView);
        vFormalTextView = (TextView) findViewById(R.id.tv_formal);
        FontsUtil.setExistenceLight(this, vFormalTextView);

        init();
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
                if (position == 1) {
                    slideMenu();
                } else {
                    constant.selectMenuItem(DMMyLookActivity.this, position,
                            true);
                }
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DMMyLookActivity.this,
                        DMHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void init() {

        showMenu();
        // setViewWithFont();
        showShowcaseView();
    }

    // / --------------------------------- set font
    // -------------------------------------

    // / --------------------------------- show menu list
    // --------------------------------------
    public void showMenu() {
        vMenuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
    }

    // / --------------------------------- slide menu section
    // ------------------------------
    public void slideMenu() {
        if (vDrawerLayout.isDrawerOpen(vMenuLayout)) {
            vDrawerLayout.closeDrawer(vMenuLayout);
        } else
            vDrawerLayout.openDrawer(vMenuLayout);
    }

    // / --------------------------------------------- go style activity
    // ----------------
    public void goStyleActivity(int position) {
        if (position == 1)
            constant.gCategory = "casual";
        else if (position == 2)
            constant.gCategory = "after5";
        else if (position == 3)
            constant.gCategory = "formal";


        Intent intent = new Intent(DMMyLookActivity.this, DMStyleActivity.class);
        startActivity(intent);
    }

    public void goStyleActivity_8(int position) {
        if (position == 1)
            constant.gCategory = "casual";
        else if (position == 2)
            constant.gCategory = "after5";
        else if (position == 3)
            constant.gCategory = "formal";


        Intent intent = new Intent(DMMyLookActivity.this, DMStyleActivity.class);
        intent.putExtra("notification_look", "true");
        intent.putExtra("notification_category", constant.gCategory);
        startActivity(intent);
    }

    public void goFavoriteActivity() {
        Intent intent = new Intent(DMMyLookActivity.this,
                DMFavoriteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (AppUtils.isConnectingToInternet(DMMyLookActivity.this)) {
            switch (v.getId()) {
                case R.id.layout_favorites:
                    goFavoriteActivity();
                    break;

                case R.id.layout_casual:
                    goStyleActivity(1);
                    break;

                case R.id.layout_after5:
                    goStyleActivity(2);
                    break;

                case R.id.layout_formal:
                    goStyleActivity(3);
                    break;
            }
        } else {
            Toast.makeText(DMMyLookActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
        }

    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_OCCASION_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_OCCASION_SHOWN, true);
                }
            });
        }
    }
}

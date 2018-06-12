package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DMHangUpActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;

    TextView vTxtBack, vTxtTitle, vTxtShirt, vTxtPant, vTxtCoat, vTxtTie,
            vTxtSuit, vTxtShoes;
    RelativeLayout vBackLayout;

    Button vBtnShirt, vBtnPant, vBtnCoat, vBtnTie, vBtnSuit;
    RelativeLayout vCoatLayout, vTieLayout, vSuitLayout, vShoesLayout;

    boolean mFromMain;
    @BindView(R.id.act_hang_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangup);
        ButterKnife.bind(this);

        // / layout
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);

        vTxtTitle = (TextView) findViewById(R.id.title_view);
        FontsUtil.setExistenceLight(this, vTxtTitle);

        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);

        vTxtShirt = (TextView) findViewById(R.id.shirt_txt);
        FontsUtil.setExistenceLight(this, vTxtShirt);

        vTxtPant = (TextView) findViewById(R.id.pants_txt);
        FontsUtil.setExistenceLight(this, vTxtPant);

        vTxtCoat = (TextView) findViewById(R.id.coat_txt);
        FontsUtil.setExistenceLight(this, vTxtCoat);

        vTxtTie = (TextView) findViewById(R.id.tie_txt);
        FontsUtil.setExistenceLight(this, vTxtTie);

        vTxtSuit = (TextView) findViewById(R.id.suit_txt);
        FontsUtil.setExistenceLight(this, vTxtSuit);

        vTxtShoes = (TextView) findViewById(R.id.shoes_txt);
        FontsUtil.setExistenceLight(this, vTxtShoes);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        vBtnShirt = (Button) findViewById(R.id.restore_btn);
        vBtnPant = (Button) findViewById(R.id.pants_btn);
        vBtnCoat = (Button) findViewById(R.id.coat_btn);
        vBtnTie = (Button) findViewById(R.id.tie_btn);
        vBtnSuit = (Button) findViewById(R.id.suit_btn);
        vCoatLayout = (RelativeLayout) findViewById(R.id.coat_layout);
        vTieLayout = (RelativeLayout) findViewById(R.id.tie_layout);
        vSuitLayout = (RelativeLayout) findViewById(R.id.suit_layout);
        vShoesLayout = (RelativeLayout) findViewById(R.id.shoes_layout);

        final ParseUser user = ParseUser.getCurrentUser();
        boolean bPurchased = user.getBoolean("ratedMyDimoda");
        System.out.println("HomePurchased" + bPurchased);

        if (bPurchased == true) {
            System.out.println("TRUE" + bPurchased);
        } else {

            if (AppUtils.getPref("pro", DMHangUpActivity.this) != null) {
                if (AppUtils.getPref("pro", DMHangUpActivity.this).equalsIgnoreCase("false")) {
                    AppRater.app_launched(DMHangUpActivity.this);
                } else {
                    AppUtils.putPref("pro", "true", DMHangUpActivity.this);
                }
            } else if (AppUtils.getPref("pro", DMHangUpActivity.this) == null) {
                AppUtils.putPref("pro", "false", DMHangUpActivity.this);
                AppRater.app_launched(DMHangUpActivity.this);
            } else {
                AppUtils.putPref("pro", "false", DMHangUpActivity.this);
                AppRater.app_launched(DMHangUpActivity.this);
            }
        }
        vBtnMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                slideMenu();
            }
        });

        vMenuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                vDrawerLayout.closeDrawer(vMenuLayout);
                if (position != 4)
                    constant.selectMenuItem(DMHangUpActivity.this, position,
                            true);
            }
        });
        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            /*	if (!mFromMain) {
                    Intent intent = new Intent(DMHangUpActivity.this,
							DMHomeActivity.class);
					startActivity(intent);
				}
*/
                finish();
            }
        });

        vBtnShirt.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                goCaptureActivity("shirt");
            }
        });

        vBtnPant.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                goCaptureActivity("trousers");
            }
        });

        vBtnSuit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goCaptureActivity("suit");
            }
        });

        vBtnCoat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (constant.gCategory.equals("after5")
                        || constant.gCategory.equals("formal")) {
                    goCaptureActivity("jacket");
                }
            }
        });

        vBtnTie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                goCaptureActivity("tie");
            }
        });

    }

    public void init() {
        showMenu();
        // setViewWithFont();
        Intent in = getIntent();
        mFromMain = in.getBooleanExtra("FromMain", true);
        if (mFromMain == true) {
            vTxtTitle.setText("What to add");
            showShowcaseView();
        } else {
            showHelpShowcaseView();
            vTxtTitle.setText("Where to start");
        }
        showButtonWithCategory();
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont()
    // {
    // vTxtTitle.setTypeface(constant.fontface);
    // vTxtBack.setTypeface(constant.fontface);
    // vTxtShirt.setTypeface(constant.fontface);
    // vTxtCoat.setTypeface(constant.fontface);
    // vTxtTie.setTypeface(constant.fontface);
    // vTxtPant.setTypeface(constant.fontface);
    // vTxtSuit.setTypeface(constant.fontface);
    // vTxtShoes.setTypeface(constant.fontface);
    // }
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

    // / --------------------------------------- show layout according category
    // ------------
    public void showButtonWithCategory() {
        if (constant.gCategory.equals("casual")) {
            vCoatLayout.setVisibility(View.GONE);
            vTieLayout.setVisibility(View.GONE);
            vSuitLayout.setVisibility(View.GONE);
            vShoesLayout.setVisibility(View.GONE);
        } else if (constant.gCategory.equals("after5")) {
            vTieLayout.setVisibility(View.GONE);
            vShoesLayout.setVisibility(View.GONE);
        }
    }

    public void goCaptureActivity(String type) {
		/*Intent intent = new Intent(DMHangUpActivity.this, DMCaptureOptionActivity.class);
		intent.putExtra("FromMain", mFromMain);
		intent.putExtra("type", type);
		startActivity(intent);*/
        if (mFromMain == true) {
            Intent intent = new Intent(DMHangUpActivity.this, DMCaptureOptionActivity.class);
            intent.putExtra("FromMain", mFromMain);
            intent.putExtra("type", type);
            intent.putExtra("isCapture", true);
            startActivity(intent);
        } else {
            Intent intent = new Intent(DMHangUpActivity.this, DMHelpActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    public void goFindMeActivity() {
        Intent intent = new Intent(DMHangUpActivity.this,
                DMFindMeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_HANGUP_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_HANGUP_SHOWN, true);
                }
            });
        }
    }

    private void showHelpShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_HANGUP_HELP_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setImageResource(R.drawable.help_style_me);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_HANGUP_HELP_SHOWN, true);
                }
            });
        }
    }
}

package com.mydimoda;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.adapter.DMOptionsListAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DMExactActivity extends FragmentActivity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    Button vBtnFind;
    EditText vEdtMax, vEdtDesign;
    TextView vTxtMax, vTxtShirt, vTxtTrouser, vTxtJacket, vTxtTie, vTxtDesign;
    ImageView vChkShirt, vChkTrouser, vChkJacket, vChkTie;
    RelativeLayout vLytShirt, vLytTrouser, vLytJacket, vLytTie;

    int mType = 0;
    boolean mFShirt, mFTrouser, mFJacket, mFTie;
    Intent intent = getIntent();
    String zone = "";

    String[] mShopList;
    HashMap<String, String> mShopMap = new HashMap<>();
    @BindView(R.id.shop_edit)
    TextView mShopNameTv;
    HashMap<String, String> mSort_Map = new HashMap<>();
    @BindView(R.id.sort_edit)
    TextView mSortTypeTv;
    String[] mSortList;

    @BindView(R.id.sort_lyt_title)
    TextView mSortTitle;
    @BindView(R.id.shop_lyt_title)
    TextView mShopTitle;
    @BindView(R.id.act_exact_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exact);
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

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        vBtnFind = (Button) findViewById(R.id.btn_findme);
        FontsUtil.setExistenceLight(this, vBtnFind);

        vEdtMax = (EditText) findViewById(R.id.max_edit);
        FontsUtil.setExistenceLight(this, vEdtMax);

        vTxtMax = (TextView) findViewById(R.id.max_price_txt);
        FontsUtil.setExistenceLight(this, vTxtMax);

        vEdtDesign = (EditText) findViewById(R.id.design_edit);
        FontsUtil.setExistenceLight(this, vEdtDesign);

        vTxtDesign = (TextView) findViewById(R.id.design_price_txt);
        FontsUtil.setExistenceLight(this, vTxtDesign);

        vTxtShirt = (TextView) findViewById(R.id.shirt_txt);
        FontsUtil.setExistenceLight(this, vTxtShirt);

        vTxtTrouser = (TextView) findViewById(R.id.pants_txt);
        FontsUtil.setExistenceLight(this, vTxtTrouser);

        vTxtJacket = (TextView) findViewById(R.id.coat_txt);
        FontsUtil.setExistenceLight(this, vTxtJacket);

        vTxtTie = (TextView) findViewById(R.id.tie_txt);
        FontsUtil.setExistenceLight(this, vTxtTie);

        vChkShirt = (ImageView) findViewById(R.id.shirt_check);
        vChkTrouser = (ImageView) findViewById(R.id.pants_check);
        vChkJacket = (ImageView) findViewById(R.id.coat_check);
        vChkTie = (ImageView) findViewById(R.id.tie_check);
        vLytShirt = (RelativeLayout) findViewById(R.id.restore_layout);
        vLytTrouser = (RelativeLayout) findViewById(R.id.pants_layout);
        vLytJacket = (RelativeLayout) findViewById(R.id.coat_layout);
        vLytTie = (RelativeLayout) findViewById(R.id.tie_layout);

        vEdtMax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    vEdtMax.setHint("");
                } else {
                    vEdtMax.setHint("$1000");
                }
            }
        });

        vEdtMax.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("00")){
                    vEdtMax.setText("0");
                    vEdtMax.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                constant.selectMenuItem(DMExactActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppUtils.brand = "";
                vEdtDesign.setText("");
                vEdtDesign.setHint("ALL");
                finish();
            }
        });

        vBtnFind.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                findMeProduct();
            }
        });

        vLytShirt.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectType(1);
                AppUtils.brand = "";
                vEdtDesign.setText("");
                vEdtDesign.setHint("ALL");
            }
        });

        vLytTrouser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectType(2);
                AppUtils.brand = "";
                vEdtDesign.setText("");
                vEdtDesign.setHint("ALL");
            }
        });

        vLytJacket.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectType(3);
                AppUtils.brand = "";
                vEdtDesign.setText("");
                vEdtDesign.setHint("ALL");
            }
        });

        vLytTie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectType(4);
                AppUtils.brand = "";
                vEdtDesign.setText("");
                vEdtDesign.setHint("ALL");
            }
        });

        mShopNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShopDialog();
            }
        });
        mShopList = getResources().getStringArray(R.array.shop_filter);
        mSortList = getResources().getStringArray(R.array.sort_filter);
        mShopMap = fillShopmap(mShopMap, mShopList);
        mSort_Map = fillSortmap(mSort_Map, mSortList);
        mSortTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortDialog();
            }
        });
        mSortTypeTv.setText(mSortList[0]);

        mShopNameTv.setText(mShopList[0]);
        FontsUtil.setExistenceLight(this, mShopNameTv);
        FontsUtil.setExistenceLight(this, mSortTypeTv);
        FontsUtil.setExistenceLight(this, mSortTitle);
        FontsUtil.setExistenceLight(this, mShopTitle);

        showShowcaseView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();

		/*
         * Bundle extras = getIntent().getExtras(); if (extras != null) { zone =
		 * extras.getString("zone"); }
		 */
        vEdtDesign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("TYPE" + mType);
                AppUtils.putPref("type", String.valueOf(mType),
                        DMExactActivity.this);
                Intent m_intent = new Intent(DMExactActivity.this,
                        MainActivity.class);
                m_intent.putExtra("type", mType);
                System.out.println("Main" + mType);
                startActivity(m_intent);

            }
        });

        if (!TextUtils.isEmpty(AppUtils.brand)) {
            vEdtDesign.setText(AppUtils.brand);
            AppUtils.yes = "false";
        } else {
            vEdtDesign.setText("");
            AppUtils.yes = "true";
            vEdtDesign.setHint("ALL");
        }

		/*
         * if(AppUtils.getPref("zone", DMExactActivity.this) != null) {
		 * 
		 * vEdtDesign.setText(AppUtils.getPref("zone", DMExactActivity.this));
		 * AppUtils.yes ="false"; } else { AppUtils.yes ="true"; }
		 */

    }

    public void init() {
        showMenu();
        // setViewWithFont();
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont()
    // {
    // vTxtTitle.setTypeface(constant.fontface);
    // vTxtBack.setTypeface(constant.fontface);
    // vTxtMax.setTypeface(constant.fontface);
    // vTxtDesign.setTypeface(constant.fontface);
    // vTxtShirt.setTypeface(constant.fontface);
    // vTxtTrouser.setTypeface(constant.fontface);
    // vTxtJacket.setTypeface(constant.fontface);
    // vTxtTie.setTypeface(constant.fontface);
    // vBtnFind.setTypeface(constant.fontface);
    //
    //
    //
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

    // / -------------------------------- when items is clicked, show checked
    // state --------
    public void selectType(int type) {
        if (type == 1) {
            // Shirts
            mFShirt = !mFShirt;
            mFTrouser = false;
            mFJacket = false;
            mFTie = false;
        } else if (type == 2) {
            // Trousers
            mFTrouser = !mFTrouser;
            mFShirt = false;
            mFJacket = false;
            mFTie = false;
        } else if (type == 3) {
            // Jacket
            mFJacket = !mFJacket;
            mFTrouser = false;
            mFShirt = false;
            mFTie = false;
        } else if (type == 4) {
            // Tie
            mFTie = !mFTie;
            mFJacket = false;
            mFTrouser = false;
            mFShirt = false;
        }

        vChkShirt.setImageResource(R.drawable.edit_yel_line_bg);
        vChkTrouser.setImageResource(R.drawable.edit_yel_line_bg);
        vChkJacket.setImageResource(R.drawable.edit_yel_line_bg);
        vChkTie.setImageResource(R.drawable.edit_yel_line_bg);

        if (mFShirt) {
            vChkShirt.setImageResource(R.drawable.img_check_sel);
            mType = constant.SHIRT;
            AppUtils.putPref("type", String.valueOf(mType),
                    DMExactActivity.this);
            AppUtils.brand = "";
            vEdtDesign.setHint("ALL");
        } else if (mFTrouser) {
            vChkTrouser.setImageResource(R.drawable.img_check_sel);
            mType = constant.TROUSERS;
            AppUtils.brand = "";
            AppUtils.putPref("type", String.valueOf(mType),
                    DMExactActivity.this);
            vEdtDesign.setHint("ALL");
        } else if (mFJacket) {
            vChkJacket.setImageResource(R.drawable.img_check_sel);
            mType = constant.JACKET;
            AppUtils.brand = "";
            AppUtils.putPref("type", String.valueOf(mType),
                    DMExactActivity.this);
            vEdtDesign.setHint("ALL");
        } else if (mFTie) {
            vChkTie.setImageResource(R.drawable.img_check_sel);
            mType = constant.TIE;
            AppUtils.brand = "";
            AppUtils.putPref("type", String.valueOf(mType),
                    DMExactActivity.this);
            vEdtDesign.setHint("ALL");
        } else {
            mType = constant.NONE;
            vEdtDesign.setHint("ALL");
        }
    }

    // / --------------------------- find products with price from server
    // ------------
    public void findMeProduct() {
        String maxPrice = vEdtMax.getText().toString();

        if (maxPrice.equals("")) {
            constant.alertbox("Warning!", "Max Price Empty.", this);
        } else {
            Intent intent = new Intent(this, DMAutoActivity.class);
            intent.putExtra("from", "exact");
            intent.putExtra("closet", mType);
            intent.putExtra(constant.SHOP_NAME, mShopMap.get(mShopNameTv.getText()));
            intent.putExtra(constant.SORT_BY_KEY, mSort_Map.get(mSortTypeTv.getText()));
            AppUtils.putPref("type", String.valueOf(mType),
                    DMExactActivity.this);
            System.out.println("closet" + mType);
            intent.putExtra("price", maxPrice);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        AppUtils.brand = "";
        vEdtDesign.setText("");
        super.onBackPressed();
    }

    private AlertDialog mTypeDialog;

    public void showShopDialog() {
        if (mTypeDialog == null) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(DMExactActivity.this, android.R.style.Theme_Holo));
            View mmain = this.getLayoutInflater().inflate(R.layout.dialog_options, null);

            ListView mOptions = (ListView) mmain.findViewById(R.id.dialog_options_lstvw);
            final DMOptionsListAdapter mAdapter = new DMOptionsListAdapter(this, mShopList);

            mOptions.setAdapter(mAdapter);

            mOptions.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mShopNameTv.setText(mShopList[position]);
                    if (mTypeDialog != null && mTypeDialog.isShowing()) {
                        mTypeDialog.dismiss();
                    }
                }
            });
            mBuilder.setView(mmain);
            mTypeDialog = mBuilder.create();
        }

        mTypeDialog.show();
    }

    private AlertDialog mSortByDialog;

    public void showSortDialog() {
        if (mSortByDialog == null) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(DMExactActivity.this, android.R.style.Theme_Holo));
            View mmain = this.getLayoutInflater().inflate(R.layout.dialog_options, null);

            ListView mOptions = (ListView) mmain.findViewById(R.id.dialog_options_lstvw);
            final DMOptionsListAdapter mAdapter = new DMOptionsListAdapter(this, mSortList);

            mOptions.setAdapter(mAdapter);

            mOptions.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSortTypeTv.setText(mSortList[position]);
                    if (mSortByDialog != null && mSortByDialog.isShowing()) {
                        mSortByDialog.dismiss();
                    }
                }
            });
            mBuilder.setView(mmain);
            mSortByDialog = mBuilder.create();
        }
        mSortByDialog.show();
    }

    /**
     * @param fillMe    - the target hashmap to be populated
     * @param mShopList make sure sequence in shop list syncs with api functions here
     * @return
     */
    private HashMap<String, String> fillShopmap(@NonNull HashMap<String, String> fillMe, @NonNull String[] mShopList) {
        if (mShopList == null) {
            throw new NullPointerException("Shop list cannot be null");
        }
        fillMe.put(mShopList[0], constant.All_OPTION);
        fillMe.put(mShopList[1], constant.AMAZON_SHOP);
        fillMe.put(mShopList[2], constant.SHOPSTYLE_SHOP);
        fillMe.put(mShopList[3], constant.ASOS_SHOP);
        return fillMe;
    }

    /**
     * @param fillMe    - the target hashmap to be populated
     * @param mSortList make sure sequence in sort list syncs with api values here
     * @return
     */
    private HashMap<String, String> fillSortmap(HashMap<String, String> fillMe, String[] mSortList) {
        if (mSortList == null) {
            throw new NullPointerException("Sort list cannot be null");
        }
        fillMe.put(mSortList[0], constant.SORT_RELEVANCE);
        fillMe.put(mSortList[1], constant.SORT_HI_LO);
        fillMe.put(mSortList[2], constant.SORT_LO_HI);
        return fillMe;
    }

    private void showShowcaseView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppUtils.hideSoftKeyBoard(DMExactActivity.this);
            }
        },500);
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_EXACT_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_EXACT_SHOWN, true);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            AppUtils.brand = "";
            vEdtDesign.setText("");
            vEdtDesign.setHint("ALL");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

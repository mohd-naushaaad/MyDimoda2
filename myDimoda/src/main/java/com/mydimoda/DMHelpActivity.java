package com.mydimoda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.activities.LookListingActiivty;
import com.mydimoda.adapter.DMHelpGridAdapter;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.model.ModelCatWithMode;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.social.google.GoogleIAP;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DMHelpActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;
    GridView vClothGrid;
    String mType;
    DMHelpGridAdapter mAdapter;
    List<ParseObject> mClothList = null;
    List<String> mUrlList = null;
    private boolean isFromPlanNewTrip = false;
    private ProgressDialog progressbar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
        vClothGrid = (GridView) findViewById(R.id.cloths_grid);
        vBtnMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                slideMenu();
            }
        });
        vMenuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                constant.selectMenuItem(DMHelpActivity.this, position, true);
            }
        });
        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        vClothGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                constant.gItemList = makeItemList(mClothList.get(position));
                constant.gItemListTemp = makeItemList(mClothList.get(position));//mayur added for fixinf cloths swap issue
                if (!isFromPlanNewTrip) {
                    checkPermissions();
                } else {
                    if (mType.equalsIgnoreCase(constant.L_SHIRT) || mType.equalsIgnoreCase(constant.L_TROUSERS)) {
                        makeListOfModeWithCat(0);
                    } else if (mType.equalsIgnoreCase(constant.L_JACKET)) {

                    } else if (mType.equalsIgnoreCase(constant.L_TIE)) {

                    } else if (mType.equalsIgnoreCase(constant.L_SUIT)) {

                    }
                    /*Intent styleMeintent = new Intent(DMHelpActivity.this, LooklistingActivityForOneLook.class);
                    styleMeintent.putExtra(constant.BUNDLE_CATEGORY, "casual");
                    styleMeintent.putExtra(constant.BUNDLE_MODE, "help me");
                    startActivity(styleMeintent);*/
                }
            }
        });
    }

    private void makeListOfModeWithCat(int index) {
        List<ModelCatWithMode> listOfCatwithMode = new ArrayList<>();
        for (int i = 0; i < constant.listTypeSelection.size(); i++) {
            ModelCatWithMode modelCatWithMode;
            if (i == index) {
                modelCatWithMode = new ModelCatWithMode(constant.listTypeSelection.get(i), constant.helpME);
            } else {
                modelCatWithMode = new ModelCatWithMode(constant.listTypeSelection.get(i), constant.styleME);
            }
            listOfCatwithMode.add(modelCatWithMode);
        }
        Intent styleMeintent = new Intent(DMHelpActivity.this, LookListingActiivty.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(constant.BUNDLE_LIST_OF_SELECTION, Parcels.wrap(listOfCatwithMode));
        styleMeintent.putExtras(bundle);
        startActivity(styleMeintent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        showMenu();
        // setViewWithFont();

        Intent in = getIntent();
        mType = in.getStringExtra("type");
        if (in.getBooleanExtra(constant.BUNDLE_ISFROMPLANNEWTRIP, false)) {
            isFromPlanNewTrip = true;
        }
        showTitle();
        if (AppUtils.isConnectingToInternet(DMHelpActivity.this)) {
            getClothFP();
        } else {
            Toast.makeText(DMHelpActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
        }
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont()
    // {
    // vTxtTitle.setTypeface(constant.fontface);
    // vTxtBack.setTypeface(constant.fontface);
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

    public void showTitle() {
        if (mType.equals("shirt")) {
            vTxtTitle.setText("Shirt");
        } else if (mType.equals("trousers")) {
            vTxtTitle.setText("Trousers");
        } else if (mType.equals("jacket")) {
            vTxtTitle.setText("Jacket");
        } else if (mType.equals("tie")) {
            vTxtTitle.setText("Tie");
        } else if (mType.equals("suit")) {
            vTxtTitle.setText("Suit");
        }
    }

    private void goAlgorithmActivity() {
        ParseUser user = ParseUser.getCurrentUser();
        int count = user.getInt("Count");
        count++;
        user.put("Count", count);
        user.saveInBackground();

        Intent intent = new Intent(DMHelpActivity.this,
                DMAlgorithmActivity.class);
        startActivity(intent);
    }

    private void procRate() {

        SharedPreferences setting = getSharedPreferences("mydimoda_setting", 0);
        boolean never = setting.getBoolean("never", false);
        int count = setting.getInt("rate_count", 1);

        if (!never) {
            if (count == 3) {
                showRateAlert();
            } else {
                procPurchase();
            }
        } else {
            procPurchase();
        }
    }

    private void procPurchase() {
        final ParseUser user = ParseUser.getCurrentUser();
        boolean bPurchased = user.getBoolean("Buy");
        int maxCount = user.getInt(constant.USER_MAX_COUNT); // new max count according to new policy
        if (!bPurchased) {
            int count = user.getInt("Count");

            if (SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase(
                    "1")) {
                goAlgorithmActivity();
            } else if (count >= (maxCount >= constant.maxCount ? maxCount : constant.maxCount)) {
                showPurchaseAlert();

            } else {
                goAlgorithmActivity();
            }
        } else {
            goAlgorithmActivity();
        }
    }

    private void checkPermissions() {
        final ParseUser user = ParseUser.getCurrentUser();
        boolean bRated = user.getBoolean("ratedmyDiModa");
        if (bRated) {
            procPurchase();
        } else {
            procRate();
        }
    }

    private void showRateAlert() {

        new AlertDialog.Builder(this)
                .setTitle("Rate us!")
                .setMessage(
                        "Would you like to rate us? Please rate us 5 stars!")
                .setCancelable(false)
                .setNegativeButton("Never",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Editor editor = getSharedPreferences(
                                        "mydimoda_setting", 0).edit();
                                editor.putBoolean("never", true);
                                editor.commit();
                                procPurchase();
                            }
                        })
                .setNeutralButton("Rate Now",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("ratedmyDiModa", true);
                                user.saveInBackground();
                                rateApp();
                            }
                        })
                .setPositiveButton("Not Now",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                SharedPreferences setting = getSharedPreferences(
                                        "mydimoda_setting", 0);
                                int count = setting.getInt("rate_count", 1);
                                count++;
                                if (count == 4)
                                    count = 1;
                                Editor editor = getSharedPreferences(
                                        "mydimoda_setting", 0).edit();
                                editor.putInt("rate_count", count);
                                editor.commit();
                                procPurchase();
                            }
                        }).show();
    }

    @SuppressWarnings("unused")
    private void showRateAlert1() {

        new AlertDialog.Builder(this)
                .setTitle("Upgrade for free!")
                .setMessage(
                        "Would you like to get more Style Me mode? Please rate us 5 stars!")
                .setCancelable(false)
                .setNegativeButton("Yes!",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("ratedmyDiModa", true);
                                user.saveInBackground();
                                rateApp();
                            }
                        })
                .setPositiveButton("No thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
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
								 * if(e1 == null) { goAlgorithmActivity(); }
								 * else { Toast.makeText(DMHelpActivity.this,
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

    private void rateApp() {

        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());

        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "unable to find market app", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @SuppressWarnings("unused")
    private void showProgressBar(String message) {
        if (progressbar != null)
            return;
        progressbar = ProgressDialog.show(this, null, message);
    }

    @SuppressWarnings("unused")
    private void hideProgressBar() {

        if (progressbar == null)
            return;

        progressbar.dismiss();
        progressbar = null;
    }

    // / ------------------------------------- get cloth from parse
    // -------------------------------------
    public void getClothFP() {
        constant.showProgress(this, "Loading...");
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = null;
        if (constant.gIsCloset) {
            query = ParseQuery.getQuery("Clothes");
            query.whereEqualTo("Type", mType);
            query.whereEqualTo("User", user);
            query.orderByDescending("createdAt");
            query.setLimit(constant.RESULT_SIZE);//mayur increased limit to 1000
        } else {
            query = ParseQuery.getQuery("DemoCloset");
            query.whereEqualTo("Type", mType);
            query.orderByDescending("createdAt");
        }

        if (constant.gCategory.equals("after5")
                || constant.gCategory.equals("formal"))
            query.whereNotEqualTo("Category", "casual");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                constant.hideProgress();
                if (e == null) {
                    makeClothList(clothList);
                } else {
                    Toast.makeText(DMHelpActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // / --------------------------------------------------------- make photo
    // list from parse object list --------------
    public void makeClothList(List<ParseObject> clothList) {
        ParseUser user = ParseUser.getCurrentUser();
        if (clothList != null) {
            mClothList = new ArrayList<ParseObject>();
            mUrlList = new ArrayList<String>();
            for (int i = 0; i < clothList.size(); i++) {
                if (constant.gIsCloset) {
                    ParseUser itemUser = clothList.get(i).getParseUser("User");
                    if (itemUser.getObjectId().equals(user.getObjectId())) {
                        mClothList.add(clothList.get(i));
                        ParseFile urlObject = (ParseFile) clothList.get(i).get(
                                "ImageContent");
                        String url = urlObject.getUrl();
                        mUrlList.add(url);
                    }
                } else {
                    mClothList.add(clothList.get(i));
                    ParseFile urlObject = (ParseFile) clothList.get(i).get(
                            "ImageContent");
                    String url = urlObject.getUrl();
                    mUrlList.add(url);
                }
            }
            showClothList();
        }
    }

    // / ------------------------------------- show cloth
    // ---------------------------------------
    public void showClothList() {
        mAdapter = new DMHelpGridAdapter(this, mUrlList);
        vClothGrid.setAdapter(mAdapter);
    }

    // / ------------------------------------ make JSONArray
    // -------------------------------------
    public List<DMItemObject> makeItemList(ParseObject parseObj) {
        List<DMItemObject> list = new ArrayList<DMItemObject>();

        DMItemObject item = new DMItemObject();
        item.index = parseObj.getObjectId();
        item.type = parseObj.getString("Type");

        System.out.println("item.index ->" + item.index);
        System.out.println("item.type ->" + item.type);

        AppUtils.putPref("index", item.index, this);
        AppUtils.putPref("type", item.type, this);
        list.add(item);
        return list;
    }
}

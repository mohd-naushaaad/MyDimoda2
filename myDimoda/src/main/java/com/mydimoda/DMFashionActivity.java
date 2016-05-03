package com.mydimoda;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMFashionGridAdapter;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;
import com.mydimoda.model.OrderClothModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DMFashionActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    GridView vFashionGrid;
    TextView vRememberDate, vTxtRemember;
    Button vBtnLike, vBtnDismiss, vBtnStamp, vBtnHome;
    ImageButton vBtnRemember;
    RelativeLayout vRemeberLayout, vHomeBtnLayout;
    LinearLayout vBtnLayout;
    ProgressDialog vProgress;

    boolean mIsRemember = false;
    List<ParseObject> mClothList = null;
    List<String> mIdList = null;

    String mBaseUrl;
    JSONObject mSendData;

    boolean mIsDislike = false;
    DatabaseModel m_DatabaseModel;
    DbAdapter mDbAdapter;
    final public static String ONE_TIME = "onetime";
    @Bind(R.id.act_fash_fb_share)
    TextView mFbShareBtn;
    @Bind(R.id.act_fash_tw_share)
    TextView mTwShareBtn;
    @Bind(R.id.act_fash_In_share)
    TextView mInShareBtn;
    List<OrderClothModel> mClothModellist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion);
        ButterKnife.bind(this);
        mDbAdapter = new DbAdapter(DMFashionActivity.this);
        mDbAdapter.createDatabase();
        mDbAdapter.open();
        m_DatabaseModel = new DatabaseModel();
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

        vFashionGrid = (GridView) findViewById(R.id.fashion_gridview);
        vBtnLike = (Button) findViewById(R.id.like_btn);
        vBtnDismiss = (Button) findViewById(R.id.dissmiss_btn);
        vBtnStamp = (Button) findViewById(R.id.stamp_btn);
        vBtnHome = (Button) findViewById(R.id.btn_home);
        FontsUtil.setExistenceLight(this, vBtnHome);

        vBtnRemember = (ImageButton) findViewById(R.id.remember_btn);
        vRememberDate = (TextView) findViewById(R.id.remember_date);
        vTxtRemember = (TextView) findViewById(R.id.remember_view);
        vRemeberLayout = (RelativeLayout) findViewById(R.id.remember_layout);
        vHomeBtnLayout = (RelativeLayout) findViewById(R.id.homebtn_layout);
        vBtnLayout = (LinearLayout) findViewById(R.id.button_layout);

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
                constant.selectMenuItem(DMFashionActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        vBtnHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DMFashionActivity.this,
                        DMHomeActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        vBtnLike.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new DownloadTaskRunner().execute();
                String favoritelist = "", showlayout = "";
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    favoritelist = extras.getString("favoritelist");
                    showlayout = extras.getString("showlayout");
                }

                if (showlayout == null) {

                }
                if (showlayout != null
                        && showlayout.equalsIgnoreCase("showlayout")) {
                    showRemeberLayout("");

                } else {
                    hideBtnLayout();
                    likeCloth();
                }
            }
        });

        vBtnDismiss.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dislikeCloth();
                constant.clearClothBitmapList();
            }
        });

        vBtnStamp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                constant.gBlockedList.add(constant.gFashion);
                goAlgorithmActivity();
                constant.clearClothBitmapList();

            }
        });

        vBtnRemember.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mIsRemember) {
                    // constant.alertbox("Warning",
                    // "Do you want to remove this look from Favorites?",
                    // DMFashionActivity.this);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            DMFashionActivity.this);
                    alertDialogBuilder.setTitle("Warning");
                    alertDialogBuilder
                            .setMessage(
                                    "Do you want to remove this look from Favorites?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            ParseObject.createWithoutData(
                                                    "Favorite",
                                                    constant.gFashionID)
                                                    .deleteEventually();
                                            // finish();
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    mIsRemember = true;
                    vBtnRemember.setBackgroundResource(R.drawable.remember_checked_bg);
                    FavoriteCloths();
                }
            }
        });

        init();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    public void init() {
        showMenu();
//		setViewWithFont();

        Intent in = getIntent();
        String favorite = in.getStringExtra("favorite");
        if (favorite.equals("yes")) {
            // favorite
            showRemeberLayout(in.getStringExtra("date"));
            // two lines commented
            mIsRemember = true;
            vBtnRemember.setBackgroundResource(R.drawable.remember_checked_bg);

        } else {
            // general
            showBtnLayout();
            vRemeberLayout.setVisibility(View.GONE);
        }

        // init id list to get cloths
        initIdList();
    }

    // / --------------------------------- set font
    // -------------------------------------
//	public void setViewWithFont() {
//		vTxtTitle.setTypeface(constant.fontface);
//		vTxtBack.setTypeface(constant.fontface);
//		vRememberDate.setTypeface(constant.fontface);
//		vTxtRemember.setTypeface(constant.fontface);
//		vBtnHome.setTypeface(constant.fontface);
//	}

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
        } else {
            vDrawerLayout.openDrawer(vMenuLayout);
        }
    }

    // / -------------------------------------- go algorithmActivity
    // ------------
    public void goAlgorithmActivity() {
        Intent intent = new Intent(DMFashionActivity.this,
                DMAlgorithmActivity.class);
        startActivity(intent);
        finish();
    }

    // / ------------------------------------- show button layout, hide button
    // layout ---------
    public void showBtnLayout() {
        vHomeBtnLayout.setVisibility(View.GONE);
        vBtnLayout.setVisibility(View.VISIBLE);
    }

    public void hideBtnLayout() {
        vHomeBtnLayout.setVisibility(View.VISIBLE);
        vBtnLayout.setVisibility(View.GONE);
    }

    // / ------------------------------------- show cloth
    // ---------------------------------------
    public void showFashionList(List<OrderClothModel> list) {
        vFashionGrid.setAdapter(new DMFashionGridAdapter(this, list));
    }

    // / ------------------------------------- show remember layout
    // ------------------------
    public void showRemeberLayout(String time) {
        hideBtnLayout();
        vRemeberLayout.setVisibility(View.VISIBLE);

        if (time.equals(""))
            vRememberDate.setText(constant.getCurrentTime());
        else
            vRememberDate.setText(time);
    }

    // / -------------------------------------- initialize id list
    // ---------------------------
    public void initIdList() {
        if (constant.gFashion != null) {
            mIdList = new ArrayList<String>();
            for (int i = 0; i < constant.gFashion.blockedList.size(); i++) {
                mIdList.add(constant.gFashion.blockedList.get(i).index);
            }
        }
        if (AppUtils.isConnectingToInternet(DMFashionActivity.this)) {
            if (mIdList != null && mIdList.size() > 0)
                getClothsFP(mIdList);
        } else {
            Toast.makeText(DMFashionActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
        }

    }

    // / -------------------------------------- get cloth from parse.com
    // ----------------------
    public void getClothsFP(List<String> list) {
        showProgress("Loading...");

        ParseQuery<ParseObject> query = null;
        if (constant.gIsCloset) {
            query = ParseQuery.getQuery("Clothes");
            m_DatabaseModel.setName("Clothes");

        } else {
            query = ParseQuery.getQuery("DemoCloset");
            m_DatabaseModel.setName("DemoCloset");
        }

        query.whereContainedIn("objectId", list);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                hideProgress();
                if (e == null) {
                    makeClothList(clothList);
                } else {
                    Toast.makeText(DMFashionActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // / --------------------------------------------------------- make photo
    // list from parse object list --------------
    public void makeClothList(List<ParseObject> clothList) {
        if (clothList != null) {
            mClothList = clothList;
            List<OrderClothModel> photoList = new ArrayList<OrderClothModel>();
            for (int i = 0; i < mClothList.size(); i++) {
                OrderClothModel model = new OrderClothModel();
                ParseFile urlObject = (ParseFile) mClothList.get(i).get(
                        "ImageContent");
                String url = urlObject.getUrl();
                Log.e("URL", mClothList.get(i).get("ImageContent").toString());

                model.setImageUrl(url);
                model.setType(AppUtils.asUpperCaseFirstChar(mClothList.get(i).getString("Type")));
                if (mClothList.get(i).getString("Type")
                        .equalsIgnoreCase("shirt")) {
                    model.setPosition(constant.SHIRT);

                } else if (mClothList.get(i).getString("Type")
                        .equalsIgnoreCase("jacket")) {
                    model.setPosition(constant.JACKET);

                } else if (mClothList.get(i).getString("Type")
                        .equalsIgnoreCase("trousers")) {
                    model.setPosition(constant.TROUSERS);
                } else if (mClothList.get(i).getString("Type")
                        .equalsIgnoreCase("tie")) {
                    model.setPosition(constant.TIE);
                } else if (mClothList.get(i).getString("Type")
                        .equalsIgnoreCase("suit")) {
                    model.setPosition(constant.SUIT);
                }
                photoList.add(model);
            }

            // sort user list by action count
            Collections.sort(photoList, new Comparator<OrderClothModel>() {
                @Override
                public int compare(OrderClothModel s1, OrderClothModel s2) {
                    return s1.getPosition() - s2.getPosition();
                }
            });
            showFashionList(photoList);
            mClothModellist = photoList;
        }
    }

    // / --------------------------------------------------------- save favorite
    // with date to parse.com database -----------
    public void FavoriteCloths() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject favorite = new ParseObject("Favorite");
        favorite.put("User", user);
        favorite.put("DateTime", constant.getCurrentDate());

        for (int i = 0; i < mClothList.size(); i++) {
            ParseObject obj = mClothList.get(i);

            String type = obj.getString("Type");
            if (type.equals("shirt"))
                favorite.put("Shirt", obj);
            else if (type.equals("tie"))
                favorite.put("Tie", obj);
            else if (type.equals("jacket"))
                favorite.put("Jacket", obj);
            else if (type.equals("trousers"))
                favorite.put("Trousers", obj);
            else if (type.equals("suit"))
                favorite.put("Suit", obj);
        }

        constant.showProgress(this, "Favoriting");

        favorite.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                // TODO Auto-generated method stub
                if (e == null)
                    Toast.makeText(DMFashionActivity.this, "Favorited",
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(DMFashionActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
                            Toast.LENGTH_LONG).show();

                constant.hideProgress();
            }
        });
    }

    // / ------------------------------------------- like cloths
    // ------------------
    public void likeCloth() {
        mBaseUrl = "http://54.69.61.15:/resp_attire";// "http://54.191.209.169:/newa";
        // commented
        // mBaseUrl="http://54.191.209.169:/newa";//"http://54.69.61.15:/newa";
        // mBaseUrl="http://52.11.139.58:/newa";//"http://54.69.61.15:/newa";
        makeSendData("like");

        MyAsyncTask task1 = new MyAsyncTask();
        task1.execute();

    }

    public void dislikeCloth() {
        mIsDislike = true;
        constant.gBlockedList.add(constant.gFashion);
        mBaseUrl = "http://54.69.61.15:/resp_attire";// "http://54.191.209.169:/newa";
        // commented
        // mBaseUrl="http://54.191.209.169:/newa";//"http://54.69.61.15:/newa";
        // mBaseUrl="http://52.11.139.58:/newa" ;//"http://54.69.61.15:/newa";
        makeSendData("dislike");
        MyAsyncTask task1 = new MyAsyncTask();
        task1.execute();
    }

    // / ----------------------------------------------- make send data with
    // json format ------------
    public void makeSendData(String feedback) {
        mSendData = new JSONObject();
        try {
            mSendData.put("version", "2");
            mSendData.put("category", constant.gCategory);
            mSendData.put("feedback", feedback);
            mSendData.put("target", makeTargetJSONArray());
            mSendData.put("name", "genparams");
            mSendData.put("value", "1");

            Log.e("data-----upd24liked---", mSendData.toString());

            if (feedback.equalsIgnoreCase("dislike")) {
                m_DatabaseModel.setFeedback(feedback);
            } else {
                String favoritelist = "";
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    favoritelist = extras.getString("favoritelist");
                }

                if (favoritelist == null) {

                }
                if (favoritelist != null
                        && favoritelist.equalsIgnoreCase("favoritelist")) {
                    System.out.println("favorite" + favoritelist);

                } else {
                    m_DatabaseModel.setFeedback(feedback);
                    m_DatabaseModel.setVersion(2);
                    m_DatabaseModel.setCategory(constant.gCategory);
                    m_DatabaseModel
                            .setTarget("" + makeTargetJSONArrayid_type());
                    // m_DatabaseModel.setName("genparams");

                    Random r = new Random();
                    int id = r.nextInt();
                    m_DatabaseModel.setValue("" + id);
                    m_DatabaseModel.setFeedback(feedback);

                    mDbAdapter.add(m_DatabaseModel);

                    AlarmManager am = (AlarmManager) DMFashionActivity.this
                            .getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(DMFashionActivity.this,
                            AlarmReciever.class);
                    intent.putExtra(ONE_TIME, Boolean.TRUE);
                    intent.putExtra("id", "" + id);
                    // PendingIntent pi =
                    // PendingIntent.getBroadcast(DMFashionActivity.this, 0,
                    // intent, 0);

                    PendingIntent pi = PendingIntent.getBroadcast(this, id,
                            intent, PendingIntent.FLAG_ONE_SHOT);

                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  // mayur
                            SystemClock.elapsedRealtime() + 24 * 60 * 60 * 1000, pi);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // / ---------------------------------------------- make target json array
    // -----------
    public JSONArray makeTargetJSONArray() {
        JSONArray arr = new JSONArray();
        if (mClothList != null) {
            for (int i = 0; i < mClothList.size(); i++) {
                ParseObject parseObj = mClothList.get(i);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("color", parseObj.getString("Color"));

                    obj.put("pattern", parseObj.getString("Pattern"));
                    obj.put("type", parseObj.getString("Type"));
                    obj.put("id", parseObj.getObjectId());

                    m_DatabaseModel.setType(parseObj.getString("Type")
                            .toString());
                    m_DatabaseModel.setColor(mClothList.get(i).getString(
                            "Color"));
                    m_DatabaseModel.setPattern(mClothList.get(i).getString(
                            "Pattern"));

                    arr.put(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            System.out
                    .println("TYPE:::" + m_DatabaseModel.getType().toString());
        }
        return arr;
    }

    public JSONArray makeTargetJSONArrayid_type() {
        JSONArray arr = new JSONArray();
        if (mClothList != null) {
            for (int i = 0; i < mClothList.size(); i++) {
                ParseObject parseObj = mClothList.get(i);
                JSONObject obj = new JSONObject();
                try {

                    obj.put("id", parseObj.getObjectId());
                    obj.put("type", parseObj.getString("Type"));

                    arr.put(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return arr;
    }

    // / -------------------------------------------- When like, dislike , parse
    // response --------
    public void parseResponse(JSONObject data) {

        if (data != null) {
            Log.e("data", data.toString());

            try {
                String status = data.getString("status");

                if (status.equals("ok")) {
                    if (mIsDislike) {
                        Toast.makeText(this, "Disliked", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(this, "Liked", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                // Toast.makeText(this, "Unknown server error.",
                // Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            // Toast.makeText(this, "You can not like",
            // Toast.LENGTH_LONG).show();
        }

        if (mIsDislike) {
            mIsDislike = false;
            goAlgorithmActivity();
        } else {
            initItemList();
            if (constant.gMode.equals("help me")) {
                constant.gLikeNum++;
                if (constant.gCategory.equals("casual")) {
                    constant.gLikeNum = 0;
                } else if (constant.gCategory.equals("after5")) {
                    if (constant.gLikeNum == 2) {
                        constant.gLikeNum = 0;
                    } else {
                        constant.gItemList = getItemList();
                        goAlgorithmActivity();
                    }
                } else if (constant.gCategory.equals("formal")) {
                    if (constant.gLikeNum == 3) {
                        constant.gLikeNum = 0;
                    } else {
                        constant.gItemList = getItemList();
                        goAlgorithmActivity();
                    }
                }
            } else {
                constant.gLikeNum = 0;
            }
        }
    }

    // / --------------------------------------- When mode is help me, make item
    // list -------------
    public List<DMItemObject> getItemList() {
        List<DMItemObject> list = new ArrayList<DMItemObject>();
        if (mClothList != null) {
            for (int i = 0; i < mClothList.size(); i++) {
                DMItemObject item = new DMItemObject();
                item.index = mClothList.get(i).getObjectId();
                item.type = mClothList.get(i).getString("Type");

                list.add(item);
            }
        }
        return list;
    }

    public void initItemList() {
        constant.gItemList = new ArrayList<DMItemObject>();
        constant.gBlockedList = new ArrayList<DMBlockedObject>();
    }

    public class MyAsyncTask extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        // / server communicate using asyncTask

        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
        JSONObject mResponseData;

        @Override
        protected void onPreExecute() {
            constant.showProgress(DMFashionActivity.this, "Loading...");
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(
                String... params) {

            // Creating JSON Parser instance
            JSONPostParser jParser = new JSONPostParser();
            // getting JSON string from URL
            mResponseData = jParser.getJSONFromUrl(mBaseUrl,
                    mSendData.toString());

            return UploadsList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

            // / when get response, call parser response function of the class
            constant.hideProgress();
            parseResponse(mResponseData);

            super.onPostExecute(result);
        }
    }

    public void showProgress(String message) {
        vProgress = new ProgressDialog(this);
        vProgress.setMessage(message);
        vProgress.setCancelable(true);
        vProgress.show();
    }

    public void hideProgress() {
        vProgress.dismiss();
    }

    public boolean makeImage() {
        View v = LayoutInflater.from(getApplication()).inflate(R.layout.collage_view, new RelativeLayout(getApplication()), false);
        AlertDialog.Builder builder = new AlertDialog.Builder(DMFashionActivity.this);
        builder.setView(v);
        //    builder.create().show();

        ImageView mImage_1, mImage_2, mImage_3, mImage_4;
        TextView mTxtVw_1, mTxtVw_2, mTxtVw_3, mTxtVw_4;
        FrameLayout mFl_1, mFl_2, mFl_3, mFl_4;
        mImage_1 = (ImageView) v.findViewById(R.id.collage_image_1);
        mImage_2 = (ImageView) v.findViewById(R.id.collage_image_2);
        mImage_3 = (ImageView) v.findViewById(R.id.collage_image_3);
        mImage_4 = (ImageView) v.findViewById(R.id.collage_image_4);

        mTxtVw_1 = (TextView) v.findViewById(R.id.collage_1_tv);
        mTxtVw_2 = (TextView) v.findViewById(R.id.collage_2_tv);
        mTxtVw_3 = (TextView) v.findViewById(R.id.collage_3_tv);
        mTxtVw_4 = (TextView) v.findViewById(R.id.collage_4_tv);

        mFl_1 = (FrameLayout) v.findViewById(R.id.collage_1);
        mFl_2 = (FrameLayout) v.findViewById(R.id.collage_2);
        mFl_3 = (FrameLayout) v.findViewById(R.id.collage_3);
        mFl_4 = (FrameLayout) v.findViewById(R.id.collage_4);
        FontsUtil.setExistenceLight(this, mTxtVw_1);
        FontsUtil.setExistenceLight(this, mTxtVw_2);
        FontsUtil.setExistenceLight(this, mTxtVw_3);
        FontsUtil.setExistenceLight(this, mTxtVw_4);

        switch (mClothModellist.size()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:

                try {
                    mImage_1.setImageBitmap(constant.getclothsBitmapLst().get(0));
                    mImage_2.setImageBitmap(constant.getclothsBitmapLst().get(1));

                    //  ParseApplication.getInstance().mImageLoader.displayImage(
                    //        mClothModellist.get(1).getImageUrl(), mImage_2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTxtVw_1.setText(mClothModellist.get(0).getType());

                mTxtVw_2.setText(mClothModellist.get(1).getType());

                mFl_1.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_2.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_3.setVisibility(View.GONE);
                mFl_4.setVisibility(View.GONE);
                break;
            case 3:
                mImage_1.setImageBitmap(constant.getclothsBitmapLst().get(0));
                mImage_2.setImageBitmap(constant.getclothsBitmapLst().get(1));
                mImage_3.setImageBitmap(constant.getclothsBitmapLst().get(2));

                mTxtVw_1.setText(mClothModellist.get(0).getType());
                mTxtVw_2.setText(mClothModellist.get(1).getType());
                mTxtVw_3.setText(mClothModellist.get(2).getType());
                mFl_1.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_2.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_3.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_4.setVisibility(View.GONE);
                break;
            case 4:
                mImage_1.setImageBitmap(constant.getclothsBitmapLst().get(0));
                mImage_2.setImageBitmap(constant.getclothsBitmapLst().get(1));
                mImage_3.setImageBitmap(constant.getclothsBitmapLst().get(2));
                mImage_4.setImageBitmap(constant.getclothsBitmapLst().get(3));
                mTxtVw_1.setText(mClothModellist.get(0).getType());
                mTxtVw_2.setText(mClothModellist.get(1).getType());
                mTxtVw_3.setText(mClothModellist.get(2).getType());
                mTxtVw_4.setText(mClothModellist.get(3).getType());
                mFl_1.setRotation(AppUtils.generatRandomPositiveNegitiveValue(30, 5));
                mFl_2.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_3.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_4.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                break;
            default:
                try {
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            mClothModellist.get(0).getImageUrl(), mImage_1);
                    mTxtVw_1.setText(mClothModellist.get(0).getType());
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            mClothModellist.get(1).getImageUrl(), mImage_2);
                    mTxtVw_2.setText(mClothModellist.get(1).getType());
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            mClothModellist.get(2).getImageUrl(), mImage_3);
                    mTxtVw_3.setText(mClothModellist.get(2).getType());
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            mClothModellist.get(3).getImageUrl(), mImage_4);
                    mTxtVw_4.setText(mClothModellist.get(3).getType());
                    mFl_1.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                    mFl_2.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                    mFl_3.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                    mFl_4.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        // this is to give attribute
        int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
        // if (v.getHeight() == 0) {
        v.measure(specWidth, specWidth);

        //}


        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        try {

            AppUtils.savebitmap(b);
            AppUtils.showShareDialog(b, DMFashionActivity.this);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void downloadBitmaps() {
        try {
            if (constant.getclothsBitmapLst().size() != mClothModellist.size()) {
                for (int i = 0; i < mClothModellist.size(); i++) {

                    constant.getclothsBitmapLst().add(i, BitmapFactory.decodeStream
                            (new URL(mClothModellist.get(i).getImageUrl()).openConnection().getInputStream()));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //     Toast.makeText(DMFashionActivity.this,"Loading images please wait",Toast.LENGTH_SHORT);
        }
    }


    @Override
    protected void onDestroy() {
        constant.clearClothBitmapList();
        super.onDestroy();

    }


    private class DownloadTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  constant.showProgress(DMFashionActivity.this, "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isFinishing()) {
                return null;
            }
            downloadBitmaps();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (!isFinishing() && AppUtils.isOnline(DMFashionActivity.this)) {
                try {
                    makeImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            constant.hideProgress();

        }

    }


}

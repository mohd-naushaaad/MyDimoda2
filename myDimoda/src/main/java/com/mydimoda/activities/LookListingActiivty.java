package com.mydimoda.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mydimoda.AlarmReciever;
import com.mydimoda.AppUtils;
import com.mydimoda.JSONPostParser;
import com.mydimoda.ParseApplication;
import com.mydimoda.R;
import com.mydimoda.SharedPreferenceUtil;
import com.mydimoda.adapter.LookListingAdp;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;
import com.mydimoda.model.ModelCatWithMode;
import com.mydimoda.model.ModelLookListing;
import com.mydimoda.model.OrderClothModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.widget.ProgressWheel;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.nostra13.universalimageloader.core.assist.ImageSize;
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
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookListingActiivty extends AppCompatActivity implements LookListingAdp.ClickListnerOfLook {
    final public static String ONE_TIME = "onetime";
    /*@BindView(R.id.iv_share)
    ImageView ivShare;*/
    @BindView(R.id.back_txt)
    Existence_Light_TextView backTxt;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.tv_for_trip)
    Existence_Light_TextView tvForTrip;
    @BindView(R.id.rv_looklisting)
    RecyclerView rvLooklisting;
    @BindView(R.id.rl_coach_look_listing)
    RelativeLayout rlCoachLookListing;
    @BindView(R.id.progress_view)
    ProgressWheel progressView;
    @BindView(R.id.progress_text)
    Existence_Light_TextView progressText;
    @BindView(R.id.title_view)
    Existence_Light_TextView titleView;
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;
    @BindView(R.id.ll_save)
    LinearLayout llSave;
    List<ModelLookListing> listLooks = new ArrayList<>();
    boolean mIsDislike = false;
    DatabaseModel m_DatabaseModel;
    DbAdapter mDbAdapter;
    /**
     * for some use idk
     */
    int id;
    private List<ModelCatWithMode> listModelWithCat = new ArrayList<>();
    private CountDownTimer timer;
    private JSONObject mSendData;
    private List<ParseObject> listOfClothFromParceDB;
    private int apicounter = 0;
    private List<DMItemObject> listOfAllresultItem = new ArrayList<>();
    private List<ModelLookListing> listResultingLook;
    private ModelLookListing modelLookListing;
    private LookListingAdp adapter;
    private Date startDate;
    private String tripName;
    private ProgressDialog dialog;
    private String likeDislikeUrl = "http://54.69.61.15:/resp_attire";
    private List<OrderClothModel> listOfSelectedCloth = new ArrayList<>();
    private ModelCatWithMode modelCatWithMode = new ModelCatWithMode();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_listing);
        ButterKnife.bind(this);

        mDbAdapter = new DbAdapter(LookListingActiivty.this);
        mDbAdapter.createDatabase();
        mDbAdapter.open();
        m_DatabaseModel = new DatabaseModel();
        dialog = new ProgressDialog(this);
        showShowcaseView();
        getBundleData();
    }

    private void setUpAdb() {
        listResultingLook = new ArrayList<>();
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LookListingAdp(listResultingLook, this, this);
        rvLooklisting.setAdapter(adapter);
    }

    public void likeCloth(String clothType) {
        mIsDislike = false;
        /*makeSendData("like", clothType);
        LikeAndDislikeAsynk likeAndDislikeAsynk = new LikeAndDislikeAsynk();
        likeAndDislikeAsynk.execute();*/
        initItemList();
        if (constant.gMode.equals("help me")) {

        } else {
            constant.gLikeNum = 0;
            new DownloadTaskRunner().execute();
        }
        constant.gItemListTemp = getItemList();
    }

    // / --------------------------------------- When mode is help me, make item
    // list -------------
    public List<DMItemObject> getItemList() {
        List<DMItemObject> list = new ArrayList<DMItemObject>();
        if (listOfSelectedCloth != null) {
            for (int i = 0; i < listOfSelectedCloth.size(); i++) {
                DMItemObject item = new DMItemObject();
                item.index = listOfSelectedCloth.get(i).getId();
                item.type = listOfSelectedCloth.get(i).getType();

                list.add(item);
            }
        }
        return list;
    }

    public void dislikeCloth(String clothType) {
        mIsDislike = true;
        constant.gBlockedList.add(constant.gFashion);
        makeSendData("dislike", clothType);
        LikeAndDislikeAsynk likeAndDislikeAsynk = new LikeAndDislikeAsynk();
        likeAndDislikeAsynk.execute();
    }

    // / ----------------------------------------------- make send data with
    // json format ------------
    public void makeSendData(String feedback, String type) {
        mSendData = new JSONObject();
        try {
            mSendData.put("version", "2");
            mSendData.put("category", type);
            mSendData.put("feedback", feedback);
            mSendData.put("target", makeTargetJSONArray(listOfSelectedCloth));
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

                if (favoritelist != null
                        && favoritelist.equalsIgnoreCase("favoritelist")) {
                    System.out.println("favorite" + favoritelist);

                } else {
                    m_DatabaseModel.setFeedback(feedback);
                    m_DatabaseModel.setVersion(2);
                    m_DatabaseModel.setCategory(constant.gCategory);
                    m_DatabaseModel
                            .setTarget("" + makeTargetJSONArrayid_type(listOfSelectedCloth));
                    // m_DatabaseModel.setName("genparams");

                    Random r = new Random();
                    id = r.nextInt();
                    m_DatabaseModel.setValue("" + id);
                    m_DatabaseModel.setFeedback(feedback);

                    mDbAdapter.add(m_DatabaseModel);
//                    setAlarm(id);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray makeTargetJSONArrayid_type(List<OrderClothModel> subClothList) {
        JSONArray arr = new JSONArray();
        if (subClothList != null) {
            for (int i = 0; i < subClothList.size(); i++) {
                JSONObject obj = new JSONObject();
                try {

                    obj.put("id", subClothList.get(i).getId());
                    obj.put("type", subClothList.get(i).getType());

                    arr.put(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return arr;
    }

    // / ---------------------------------------------- make target json array
    // -----------
    public JSONArray makeTargetJSONArray(List<OrderClothModel> mClothList) {
        JSONArray arr = new JSONArray();

        for (int i = 0; i < mClothList.size(); i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("color", mClothList.get(i).getColor());
                obj.put("pattern", mClothList.get(i).getPattern());
                obj.put("type", mClothList.get(i).getType());
                obj.put("id", mClothList.get(i).getId());

                arr.put(obj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return arr;
    }

    private void ParseLikeandDislikeResponse(JSONObject mResponseData) {
        String status = null;
        try {
            status = mResponseData.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equals("ok")) {
            if (mIsDislike) {
                Toast.makeText(this, "Disliked", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(this, "Liked", Toast.LENGTH_LONG).show();
            }
        }
        if (mIsDislike) {
            mIsDislike = false;
//            goAlgorithmActivity();
        } //moved this code to share dialog dismiss//moved this code to share dialog dismiss
        else {
            initItemList();
            if (constant.gMode.equals("help me")) {

            } else {
                constant.gLikeNum = 0;
                new DownloadTaskRunner().execute();
            }
        }
    }

    void setAlarm(int id) {

        final int ALARM_REQUEST_CODE = 5503;

        AlarmManager am = (AlarmManager) LookListingActiivty.this
                .getSystemService(Context.ALARM_SERVICE);


        Intent alarmIntent = new Intent(LookListingActiivty.this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            am.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(LookListingActiivty.this,
                AlarmReciever.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra("id", "" + id);
        // PendingIntent pi =
        // PendingIntent.getBroadcast(DMFashionActivity.this, 0,
        // intent, 0);

        PendingIntent pi = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  // mayur
                SystemClock.elapsedRealtime() + 24 * 60 * 60 * 1000, pi);
         /*           am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  // for testing mayur
                            SystemClock.elapsedRealtime() +  10000, pi);*/

    }

    int getId() {
        if (id == 0) {
            id = new Random().nextInt();
        }
        return id;
    }

    public boolean makeImage() {
        View v = LayoutInflater.from(getApplication()).inflate(R.layout.collage_view, new RelativeLayout(getApplication()), false);
        AlertDialog.Builder builder = new AlertDialog.Builder(LookListingActiivty.this);
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

        switch (listOfSelectedCloth.size()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:

                try {
                    mImage_1.setImageBitmap(constant.getclothsBitmapLst().get(0));
                    mImage_2.setImageBitmap(constant.getclothsBitmapLst().get(1));

                    //  ParseApplication.getInstance().mImageLoader.displayImage(
                    //        listOfSelectedCloth.get(1).getImageUrl(), mImage_2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTxtVw_1.setText(listOfSelectedCloth.get(0).getType());

                mTxtVw_2.setText(listOfSelectedCloth.get(1).getType());

                mFl_1.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_2.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_3.setVisibility(View.GONE);
                mFl_4.setVisibility(View.GONE);
                break;
            case 3:
                mImage_1.setImageBitmap(constant.getclothsBitmapLst().get(0));
                mImage_2.setImageBitmap(constant.getclothsBitmapLst().get(1));
                mImage_3.setImageBitmap(constant.getclothsBitmapLst().get(2));

                mTxtVw_1.setText(listOfSelectedCloth.get(0).getType());
                mTxtVw_2.setText(listOfSelectedCloth.get(1).getType());
                mTxtVw_3.setText(listOfSelectedCloth.get(2).getType());
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
                mTxtVw_1.setText(listOfSelectedCloth.get(0).getType());
                mTxtVw_2.setText(listOfSelectedCloth.get(1).getType());
                mTxtVw_3.setText(listOfSelectedCloth.get(2).getType());
                mTxtVw_4.setText(listOfSelectedCloth.get(3).getType());
                mFl_1.setRotation(AppUtils.generatRandomPositiveNegitiveValue(30, 5));
                mFl_2.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_3.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                mFl_4.setRotation(AppUtils.generatRandomPositiveNegitiveValue());
                break;
            default:
                try {
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            listOfSelectedCloth.get(0).getImageUrl(), mImage_1);
                    mTxtVw_1.setText(listOfSelectedCloth.get(0).getType());
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            listOfSelectedCloth.get(1).getImageUrl(), mImage_2);
                    mTxtVw_2.setText(listOfSelectedCloth.get(1).getType());
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            listOfSelectedCloth.get(2).getImageUrl(), mImage_3);
                    mTxtVw_3.setText(listOfSelectedCloth.get(2).getType());
                    ParseApplication.getInstance().mImageLoader.displayImage(
                            listOfSelectedCloth.get(3).getImageUrl(), mImage_4);
                    mTxtVw_4.setText(listOfSelectedCloth.get(3).getType());
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

            AppUtils.showShareDialog(b, LookListingActiivty.this, new AppUtils.onShareDialogDismissListener() {
                @Override
                public void onDismiss() {

                   /* initItemList();
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
                    }*/
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void downloadBitmaps() {
        try {
            if (constant.getclothsBitmapLst().size() != listOfSelectedCloth.size()) {
                for (int i = 0; i < listOfSelectedCloth.size(); i++) {
                    //      InputStream mInputStr = new URL(listOfSelectedCloth.get(i).getImageUrl()).openConnection().getInputStream();
                    try {
                        //            BitmapFactory.Options options = new BitmapFactory.Options();
                        //              options.inJustDecodeBounds = true;
                        //                BitmapFactory.decodeStream(mInputStr, null, options);

                        //                  options.inSampleSize = calculateInSampleSize(options, 500, 500);
                        //                    options.inJustDecodeBounds = false;
                        //                      mInputStr.reset();
                        constant.getclothsBitmapLst().add(i, ParseApplication.getInstance().mImageLoader.loadImageSync(listOfSelectedCloth.get(i).getImageUrl(), new ImageSize(500, 500)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //        if (mInputStr != null) {
                        //          mInputStr.close();
                        //    }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(DMFashionActivity.this,"Loading images please wait",Toast.LENGTH_SHORT);
        }
    }

    public void initItemList() {
        constant.gItemList = new ArrayList<DMItemObject>();
        constant.gBlockedList = new ArrayList<DMBlockedObject>();
    }

    private void storeinParseDb(List<ModelLookListing> listResultingLook) {
        showProgressDialog();
        ParseObject testObject = new ParseObject("TripData");
        Gson gson = new Gson();

        String listString = gson.toJson(
                listResultingLook,
                new TypeToken<ArrayList<ModelLookListing>>() {
                }.getType());

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(listString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                JSONArray ja = jsonObj.getJSONArray("listOfCloth");
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jsonSubObj = ja.getJSONObject(j);
                    jsonSubObj.remove("position");
                }
            }
            System.out.println("jsonArray" + jsonArray);

            testObject.put("Title", tripName);
            testObject.put("Start_date", startDate);
            testObject.put("User", ParseUser.getCurrentUser());
            testObject.put("OsType", 1);
            testObject.put("Looks", jsonArray);

            testObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    hideProgressDialog();
                    if (e == null) {
                        Intent intent = new Intent(LookListingActiivty.this, ReviewTripPlannedActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void hideProgress() {
        llProgress.setVisibility(View.GONE);
    }

    private void showProgress() {
        llProgress.setVisibility(View.VISIBLE);
    }

    private void showProgressDialog() {
        dialog.setMessage("Please wait...");
        dialog.show();
    }

    private void hideProgressDialog() {
        dialog.hide();
    }

    public void getClothsFP() {
        modelCatWithMode = listModelWithCat.get(apicounter);
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = null;

        SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME, 0);
        constant.gIsCloset = settings.getBoolean("isCloset", false);
        if (constant.gIsCloset) {

            System.out.println("" + constant.gIsCloset);

            query = ParseQuery.getQuery("Clothes");
            m_DatabaseModel.setName("Clothes");
            query.whereEqualTo("User", user);
            query.setLimit(constant.RESULT_SIZE);//mayur increased limit to 1000
        } else {
            query = ParseQuery.getQuery("DemoCloset");
            m_DatabaseModel.setName("DemoCloset");

        }

        if (modelCatWithMode.getCategory().equals("after5")
                || (modelCatWithMode.getCategory().equals("formal")))
            query.whereNotEqualTo("Category", "casual");
        if ((modelCatWithMode.getCategory().equals("casual"))) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("shirt");
            list.add("trousers");
            query.whereContainedIn("Type", list);
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                if (e == null) {
                    listOfClothFromParceDB.clear();
                    listOfClothFromParceDB.addAll(clothList);
                    removeDublicateItem();
                    makeSendData(makeJSONArray(listOfClothFromParceDB));
                    sendClothsTS();
                } else {
                    Toast.makeText(LookListingActiivty.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void removeDublicateItem() {
        if (apicounter - 1 != 0) {
            for (int i = 0; i < listOfAllresultItem.size(); i++) {
                for (int j = 0; j < listOfClothFromParceDB.size(); j++) {
                    if (listOfClothFromParceDB.get(j).getObjectId().equalsIgnoreCase(listOfAllresultItem.get(i).index)) {
                        listOfClothFromParceDB.remove(j);
                        break;
                    }
                }
            }
        }
    }

    // / ------------------------------------------------ send json data to
    // server ------------------
    public void sendClothsTS() {
        MyAsyncTask task1 = new MyAsyncTask();
        task1.execute();
    }

    public void parseResponse(JSONObject data) {
        if (data != null) {
            try {
                JSONArray arr = data.getJSONArray("selection");
                if (arr != null) {
                    List<DMItemObject> listSubItemLsit = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        DMItemObject item = new DMItemObject(obj);
                        listSubItemLsit.add(item);
                        listOfAllresultItem.add(item);
                    }
                    modelLookListing = new ModelLookListing(convertInParceObject(listSubItemLsit), modelCatWithMode.getCategory());
                    listResultingLook.add(modelLookListing);
                    if (listModelWithCat.size() - 1 > apicounter) {
                        apicounter++;
                        getClothsFP();
                    } else {
                        hideProgress();
                        adapter.notifyDataSetChanged();
                        llSave.setVisibility(View.VISIBLE);

                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "You can not get clothes",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private List<OrderClothModel> convertInParceObject(List<DMItemObject> listSubItemLsit) {
        List<OrderClothModel> resultingList = new ArrayList<>();
        List<ParseObject> ParceobjectList = new ArrayList<>();
        for (int i = 0; i < listSubItemLsit.size(); i++) {
            for (int j = 0; j < listOfClothFromParceDB.size(); j++) {
                if (listOfClothFromParceDB.get(j).getObjectId().equalsIgnoreCase(listSubItemLsit.get(i).index)) {
                    ParceobjectList.add(listOfClothFromParceDB.get(j));
                }
            }
        }
        for (int i = 0; i < ParceobjectList.size(); i++) {
            OrderClothModel model = new OrderClothModel();
            ParseFile urlObject = (ParseFile) ParceobjectList.get(i).get(
                    "ImageContent");
            String url = urlObject.getUrl();
            Log.e("URL", ParceobjectList.get(i).get("ImageContent").toString());

            model.setImageUrl(url);
            model.setType(ParceobjectList.get(i).getString("Type"));
            model.setId(ParceobjectList.get(i).getObjectId());
            model.setColor(ParceobjectList.get(i).getString("Color"));
            model.setPattern(ParceobjectList.get(i).getString("Pattern"));
            if (ParceobjectList.get(i).getString("Type")
                    .equalsIgnoreCase("shirt")) {
                model.setPosition(constant.SHIRT);

            } else if (ParceobjectList.get(i).getString("Type")
                    .equalsIgnoreCase("jacket")) {
                model.setPosition(constant.JACKET);

            } else if (ParceobjectList.get(i).getString("Type")
                    .equalsIgnoreCase("trousers")) {
                model.setPosition(constant.TROUSERS);
            } else if (ParceobjectList.get(i).getString("Type")
                    .equalsIgnoreCase("tie")) {
                model.setPosition(constant.TIE);
            } else if (ParceobjectList.get(i).getString("Type")
                    .equalsIgnoreCase("suit")) {
                model.setPosition(constant.SUIT);
            }
            resultingList.add(model);
            // sort user list by action count
            Collections.sort(resultingList, new Comparator<OrderClothModel>() {
                @Override
                public int compare(OrderClothModel s1, OrderClothModel s2) {
                    return s1.getPosition() - s2.getPosition();
                }
            });
        }
        return resultingList;
    }

    // / --------------------------------------------------------- make
    // JSONObject to send server ----------
    public void makeSendData(JSONArray clothArr) {
        mSendData = new JSONObject();
        try {
            mSendData.put("version", "2");
            mSendData.put("category", modelCatWithMode.getCategory());//causal,formal,after5
            mSendData.put("mode", modelCatWithMode.getMode());//style me or help me
            mSendData.put("closet", clothArr);
            mSendData.put("name", "genparams");
            mSendData.put("value", "1");
            mSendData.put("blocked", makeBlockedJSONArray(constant.gBlockedList));

            if (constant.gMode.equals("help me")) {
                mSendData.put("items", makeItemJSONArray(constant.gItemList));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray makeBlockedJSONArray(List<DMBlockedObject> list) {
        JSONArray arr = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                arr.put(makeItemJSONArray(list.get(i).blockedList));
            }
        }
        return arr;
    }

    // / ------------------------------------------------------ make blocked or
    // item json arr --------------
    public JSONArray makeItemJSONArray(List<DMItemObject> list) {
        JSONArray arr = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", list.get(i).index);
                    obj.put("type", list.get(i).type);

                    arr.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    // JSONArray from clothlist --------------
    public JSONArray makeJSONArray(List<ParseObject> clothList) {
        ParseUser user = ParseUser.getCurrentUser();
        JSONArray clothArr = new JSONArray();
        if (clothList != null) {
            for (int i = 0; i < clothList.size(); i++) {
                if (constant.gIsCloset) {
                    ParseUser itemUser = clothList.get(i).getParseUser("User");
                    if (itemUser.getObjectId().equals(user.getObjectId())) {
                        ParseObject parseObj = clothList.get(i);
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("type", parseObj.get("Type"));
                            jsonObj.put("id", parseObj.getObjectId());
                            // jsonObj.put("id", AppUtils.getPref("index",
                            // this));
                            jsonObj.put("color", parseObj.get("Color"));
                            jsonObj.put("pattern", parseObj.get("Pattern"));

                            clothArr.put(jsonObj);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    ParseObject parseObj = clothList.get(i);
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("type", parseObj.get("Type"));
                        jsonObj.put("id", parseObj.getObjectId());
                        // jsonObj.put("id", AppUtils.getPref("index", null));
                        jsonObj.put("color", parseObj.get("Color"));
                        jsonObj.put("pattern", parseObj.get("Pattern"));
                        clothArr.put(jsonObj);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return clothArr;
    }

    private void getBundleData() {
        tripName = getIntent().getStringExtra(constant.BUNDLE_TRIP_NAME);
        tvForTrip.setText(String.format(getString(R.string.suggested_look_for_trip), tripName));
        startDate = (Date) getIntent().getSerializableExtra(constant.BUNDLE_START_DATE);
        setUpAdb();
        if (Parcels.unwrap(getIntent().getParcelableExtra(constant.BUNDLE_LIST_OF_SELECTION)) != null) {
//            listTypeSelection = (ArrayList<String>) getIntent().getSerializableExtra(constant.BUNDLE_LIST_OF_SELECTION);
            listModelWithCat = Parcels.unwrap(getIntent().getParcelableExtra(constant.BUNDLE_LIST_OF_SELECTION));
            initBasedOnSelection();
            getClothsFP();
        } else {
            listLooks = Parcels.unwrap(getIntent().getParcelableExtra(constant.BUNDLE_TRIP_LIST_LOOKS));
            listResultingLook.addAll(listLooks);
            adapter.notifyDataSetChanged();
        }

    }

    private void initBasedOnSelection() {

        listOfClothFromParceDB = new ArrayList<>();


        showProgress();
        progressView.spin();

        final int[] mTime = {0};
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.e("onTick: ", "Tick");
                mTime[0]++;
                if (mTime[0] == 5) {
                    progressText.setText("myEnergy. myStyle. \nmyDiModa");
                    timer.cancel();
                }
            }

            public void onFinish() {
                Log.d("test", "Timer last tick");
            }

        }.start();
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_LOOK_LISTING, false)) {
            rlCoachLookListing.setVisibility(View.VISIBLE);
            rlCoachLookListing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlCoachLookListing.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_LOOK_LISTING, true);
                }
            });
        }

    }

    @OnClick({R.id.back_layout, R.id.ll_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.ll_save:
                storeinParseDb(listResultingLook);
                break;
        }
    }

    @Override
    public void onClickOfLike(int pos) {
        listOfSelectedCloth.clear();
        listOfSelectedCloth.addAll(listResultingLook.get(pos).getList());
        likeCloth(listResultingLook.get(pos).getClothType());
    }

   /* private void setUpAdp() {
        lookList = new ArrayList<>();
        lookAdapter = new LookAdapter(lookList, this, this);
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        rvLooklisting.setAdapter(lookAdapter);
    }*/

    public void showSimilarDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        FontsUtil.setExistenceLight(this, title);
        title.setText(R.string.alert_added);

        Button okBtn = (Button) dialog.findViewById(R.id.okbtn);
        FontsUtil.setExistenceLight(this, okBtn);

        okBtn.setText("OK");
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onClickofDisLike(int pos) {
        listOfSelectedCloth.clear();
        listOfSelectedCloth.addAll(listResultingLook.get(pos).getList());
        dislikeCloth(listResultingLook.get(pos).getClothType());
        constant.clearClothBitmapList();
    }

    public class LikeAndDislikeAsynk extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        // / server communicate using asyncTask

        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
        JSONObject mResponseData;

        @Override
        protected void onPreExecute() {
            constant.showProgress(LookListingActiivty.this, "Loading...");
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(
                String... params) {

            // Creating JSON Parser instance
            JSONPostParser jParser = new JSONPostParser();
            // getting JSON string from URL
            mResponseData = jParser.getJSONFromUrl(likeDislikeUrl,
                    mSendData.toString());

            return UploadsList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

            // / when get response, call parser response function of the class
            constant.hideProgress();
            ParseLikeandDislikeResponse(mResponseData);

            super.onPostExecute(result);
        }
    }

    private class DownloadTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setAlarm(getId());
            constant.showProgress(LookListingActiivty.this, "Please wait...");
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
            if (!isFinishing() && AppUtils.isOnline(LookListingActiivty.this)) {
                constant.hideProgress();
                try {
                    makeImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class MyAsyncTask extends
            AsyncTask<String, Integer, JSONObject> {

        // / server communicate using asyncTask
        JSONObject mResponseData;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(
                String... params) {

            // Creating JSON Parser instance
            JSONPostParser jParser = new JSONPostParser();
            // getting JSON string from URL
            mResponseData = jParser.getJSONFromUrl(constant.base_url,
                    mSendData.toString());

//            return UploadsList;
            return mResponseData;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            parseResponse(result);
            super.onPostExecute(result);
        }
    }
}

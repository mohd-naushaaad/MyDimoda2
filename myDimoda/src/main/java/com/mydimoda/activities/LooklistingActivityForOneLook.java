package com.mydimoda.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mydimoda.AppUtils;
import com.mydimoda.JSONPostParser;
import com.mydimoda.R;
import com.mydimoda.adapter.LookListingAdp;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.model.ModelLookListing;
import com.mydimoda.model.OrderClothModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.widget.ProgressWheel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/28/2018.
 */

public class LooklistingActivityForOneLook extends Activity {
    @BindView(R.id.title_view)
    Existence_Light_TextView titleView;
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
    @BindView(R.id.progress_view)
    ProgressWheel progressView;
    @BindView(R.id.progress_text)
    Existence_Light_TextView progressText;
    @BindView(R.id.rl_coach_look_listing)
    RelativeLayout rlCoachLookListing;
    JSONObject mSendData;
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;
    private String category = "";
    private String mode = "";
    private List<ParseObject> listOfClothFromParceDB;
    private ModelLookListing modelLookListing;
    private List<ModelLookListing> listResultingLook;
    private LookListingAdp adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_listing_for_one_look);
        ButterKnife.bind(this);
        init();
        getClothsFP();
    }

    private void init() {
        listOfClothFromParceDB = new ArrayList<>();
        setUpAdb();
        tvForTrip.setText(String.format(getString(R.string.suggested_look_for_trip), constant.BUNDLE_TRIP_NAME));
        getBundleData();
    }

    private void getBundleData() {
        category = getIntent().getStringExtra(constant.BUNDLE_CATEGORY);
        mode = getIntent().getStringExtra(constant.BUNDLE_MODE);
    }

    private void setUpAdb() {
        listResultingLook = new ArrayList<>();
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LookListingAdp(listResultingLook, this);
        rvLooklisting.setAdapter(adapter);
    }

    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        onBackPressed();
    }

    // / ---------------------------------- get cloth set from parse
    // ------------------------
    public void getClothsFP() {
        showProgress();
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = null;
        //Keyur -> to get Preference value for isCloset
        SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME, 0);
        constant.gIsCloset = settings.getBoolean("isCloset", false);
        if (constant.gIsCloset) {

            System.out.println("" + constant.gIsCloset);

            query = ParseQuery.getQuery("Clothes");
            query.whereEqualTo("User", user);
            query.setLimit(constant.RESULT_SIZE);//mayur increased limit to 1000
        } else {
            query = ParseQuery.getQuery("DemoCloset");
        }

        if (category.equals("after5")
                || category.equals("formal"))
            query.whereNotEqualTo("Category", "casual");
        if (category.equals("casual")) {
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
                    makeSendData(makeJSONArray(listOfClothFromParceDB));
                    sendClothsTS();
                } else {
                    Toast.makeText(LooklistingActivityForOneLook.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendClothsTS() {
        MyAsyncTask task1 = new MyAsyncTask();
        task1.execute();
    }

    public class MyAsyncTask extends
            AsyncTask<String, Integer, JSONObject> {

        // / server communicate using asyncTask

        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
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

    // / ----------------------------------------------- parse response
    // -------------------------
    //@todo mayur 23-8 add check here
    public void parseResponse(JSONObject data) {
        hideProgress();
        constant.gFashion = new DMBlockedObject();
        if (data != null) {
            try {
                JSONArray arr = data.getJSONArray("selection");
                if (arr != null) {
                    List<DMItemObject> listSubItemLsit = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        DMItemObject item = new DMItemObject(obj);
                        listSubItemLsit.add(item);
                    }
                    modelLookListing = new ModelLookListing(convertInParceObject(listSubItemLsit), category);
                    listResultingLook.add(modelLookListing);
                    adapter.notifyDataSetChanged();
                    storeinParseDb(listResultingLook);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "You can not get clothes",
                        Toast.LENGTH_LONG).show();
                constant.gLikeNum = 0;// mayur
                e.printStackTrace();
            }
        }
        //do smthing
    }

    private void storeinParseDb(List<ModelLookListing> listResultingLook) {

        ParseObject testObject = new ParseObject("TripData");
        Gson gson = new Gson();
        String listString = gson.toJson(
                listResultingLook,
                new TypeToken<ArrayList<ModelLookListing>>() {
                }.getType());
        try {
            JSONArray jsonArray = new JSONArray(listString);
            testObject.put("Title", constant.TRIPNAME);
            testObject.put("Start_date", constant.STARTDATE);
            testObject.put("User", ParseUser.getCurrentUser());
            testObject.put("OsType", 1);
            testObject.put("Looks", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        testObject.saveInBackground();
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
            model.setType(AppUtils.asUpperCaseFirstChar(ParceobjectList.get(i).getString("Type")));
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
            mSendData.put("category", category);
            mSendData.put("mode", mode);
            mSendData.put("closet", clothArr);
            mSendData.put("name", "genparams");
            mSendData.put("value", "1");

            if (mode.equals("help me")) {
                mSendData.put("items", makeItemJSONArray(constant.gItemList));
            }

            mSendData.put("blocked",
                    makeBlockedJSONArray(constant.gBlockedList));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                    System.out.println("list.get(i).index ->"
                            + list.get(i).index);
                    System.out
                            .println("list.get(i).type ->" + list.get(i).type);

                    arr.put(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return arr;
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

    // / --------------------------------------------------------- make
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

    public void showProgress() {
        llProgress.setVisibility(View.VISIBLE);
        progressView.spin();
    }

    public void hideProgress() {
        llProgress.setVisibility(View.INVISIBLE);
    }
}

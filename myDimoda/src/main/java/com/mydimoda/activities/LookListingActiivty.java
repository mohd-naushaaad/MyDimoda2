package com.mydimoda.activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mydimoda.AppUtils;
import com.mydimoda.JSONPostParser;
import com.mydimoda.R;
import com.mydimoda.SharedPreferenceUtil;
import com.mydimoda.adapter.LookAdapter;
import com.mydimoda.adapter.LookListingAdp;
import com.mydimoda.constant;
import com.mydimoda.customView.Existence_Light_TextView;
import com.mydimoda.model.ClothDetails;
import com.mydimoda.model.DemoModelForLook;
import com.mydimoda.model.ModelLookListing;
import com.mydimoda.model.OrderClothModel;
import com.mydimoda.model.TripLookListingModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.widget.ProgressWheel;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookListingActiivty extends AppCompatActivity implements LookAdapter.ClickListnerOfLook {
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
    private List<DemoModelForLook> lookList;
    private LookAdapter lookAdapter;
    private ArrayList<String> listTypeSelection;
    private CountDownTimer timer;
    private JSONObject mSendData;
    private List<ParseObject> listOfClothFromParceDB;
    private int apicounter = 0;
    private List<DMItemObject> listOfAllresultItem = new ArrayList<>();
    private String currentCat = "";
    private List<ModelLookListing> listResultingLook;
    private ModelLookListing modelLookListing;
    private LookListingAdp adapter;
    private Date startDate;
    List<ModelLookListing> listLooks = new ArrayList<>();
    private String tripName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_listing);
        ButterKnife.bind(this);
//        showShowcaseView();
        getBundleData();

//        setStaticListing(10);
    }

    private void setUpAdb() {
        listResultingLook = new ArrayList<>();
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LookListingAdp(listResultingLook, this);
        rvLooklisting.setAdapter(adapter);
    }

    private void storeinParseDb(List<ModelLookListing> listResultingLook) {
        /*List<TripLookListingModel> totalLookList = new ArrayList<>();
        for (int i = 0; i < listResultingLook.size(); i++) {
            TripLookListingModel tripLookListingModel = new TripLookListingModel();
            List<ClothDetails> list_Cloth = new ArrayList<>();
            for (int j = 0; j < listResultingLook.get(i).getList().size(); j++) {
                OrderClothModel orderClothModel = listResultingLook.get(i).getList().get(j);
                ClothDetails clothDetails = new ClothDetails(orderClothModel.getImageUrl(), orderClothModel.getType());
                list_Cloth.add(clothDetails);
            }
            tripLookListingModel.setClothType(listResultingLook.get(i).getClothType());
            tripLookListingModel.setListOfCloth(list_Cloth);
            totalLookList.add(tripLookListingModel);
        }*/

        ParseObject testObject = new ParseObject("TripData");
        Gson gson = new Gson();
        String listString = gson.toJson(
                listResultingLook,
                new TypeToken<ArrayList<ModelLookListing>>() {
                }.getType());
        try {
            JSONArray jsonArray = new JSONArray(listString);
            testObject.put("Title", tripName);
            testObject.put("Start_date", startDate);
            testObject.put("User", ParseUser.getCurrentUser());
            testObject.put("OsType", 1);
            testObject.put("Looks", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        testObject.saveInBackground();

    }


    private int giveMeColorCode(String currentCat) {
        if (currentCat.equalsIgnoreCase("casual")) {
            return Color.GREEN;
        } else if (currentCat.equalsIgnoreCase("formal")) {
            return Color.RED;
        } else if (currentCat.equalsIgnoreCase("after5")) {
            return Color.BLUE;
        }
        return 0;
    }

    public void hideProgress() {
        llProgress.setVisibility(View.GONE);
    }

    private void showProgress() {
        llProgress.setVisibility(View.VISIBLE);
    }

    public void getClothsFP() {
        currentCat = listTypeSelection.get(apicounter);
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = null;

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

        if (currentCat.equals("after5")
                || currentCat.equals("formal"))
            query.whereNotEqualTo("Category", "casual");
        if (currentCat.equals("casual")) {
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
                    modelLookListing = new ModelLookListing(convertInParceObject(listSubItemLsit), currentCat);
                    listResultingLook.add(modelLookListing);
                    if (listTypeSelection.size() - 1 > apicounter) {
                        apicounter++;
                        getClothsFP();
                    } else {
                        hideProgress();
                        adapter.notifyDataSetChanged();
                        storeinParseDb(listResultingLook);
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
            mSendData.put("category", currentCat);//causal,formal,after5
            mSendData.put("mode", "style me");//style me or help me
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
        if (getIntent().getSerializableExtra(constant.BUNDLE_LIST_OF_SELECTION) != null) {
            listTypeSelection = (ArrayList<String>) getIntent().getSerializableExtra(constant.BUNDLE_LIST_OF_SELECTION);
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

   /* private void setUpAdp() {
        lookList = new ArrayList<>();
        lookAdapter = new LookAdapter(lookList, this, this);
        rvLooklisting.setLayoutManager(new LinearLayoutManager(this));
        rvLooklisting.setAdapter(lookAdapter);
    }*/

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

    private void setStaticListing(int i) {
        DemoModelForLook demoModelForLook;
        List sublist = new ArrayList();
        for (int j = 0; j < i; j++) {
            sublist.clear();
            for (int k = 0; k < 2; k++) {
                sublist.add(k);
            }
            demoModelForLook = new DemoModelForLook(sublist);
            lookList.add(demoModelForLook);
        }
        lookAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_layout:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClickOfLike(int pos) {
        /*lookList.get(pos).setLiked(!lookList.get(pos).isLiked());
        lookAdapter.notifyItemChanged(pos);
        if (lookList.get(pos).isLiked()) {
            showSimilarDialog();
        }*/
    }

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
    public void onClickofClose(int pos) {
        /*lookList.get(pos).setColsed(!lookList.get(pos).isColsed());
        lookAdapter.notifyItemChanged(pos);*/
    }
}

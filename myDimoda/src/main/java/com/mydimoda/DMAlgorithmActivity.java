package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.object.DMItemObjectDatabase;
import com.mydimoda.widget.ProgressWheel;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DMAlgorithmActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    ProgressWheel vProgress;
    TextView vProgressTxt;

    String mBaseUrl;
    JSONObject mSendData;
    int mTime = 0;
    DatabaseModel m_DatabaseModel;
    ArrayList<DatabaseModel> m_DatabaseModels = new ArrayList<DatabaseModel>();
    ArrayList<DatabaseModel> m_notDatabaseModels = new ArrayList<DatabaseModel>();
    DbAdapter mDbAdapter;
    String yes = "";
    String m_notDatabaseModel = "";
    String m_notDatabaseModel_Category = "";
    String Fashion8 = "";
    boolean isFromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);
        constant mConstant = new constant();
        mDbAdapter = new DbAdapter(DMAlgorithmActivity.this);
        mDbAdapter.createDatabase();
        mDbAdapter.open();
        m_DatabaseModel = new DatabaseModel();
        m_DatabaseModels = mDbAdapter.getAllCity();

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

        vProgressTxt = (TextView) findViewById(R.id.progress_text);
        FontsUtil.setExistenceLight(this, vProgressTxt);

        vProgress = (ProgressWheel) findViewById(R.id.progress_view);

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
                constant.selectMenuItem(DMAlgorithmActivity.this, position,
                        true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
        processIntent(getIntent());
        if (getIntent() != null) {

            if (getIntent().hasExtra("isFromNotification"))
                isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);

            if ((getIntent().hasExtra("category"))) {
                constant.gCategory = getIntent().getExtras().getString("category");
                System.out.println("Category Resume" + constant.gCategory);
                if (constant.gCategory.equalsIgnoreCase("")) {
                    constant.gCategory = "casual";
                    Fashion8 = "true";
                }

                if (Fashion8.equalsIgnoreCase("")) {
                    Fashion8 = "true";
                }

                Fashion8 = "true";


            }

            if (getIntent().hasExtra("notification_counter")) {
                getIntent().getExtras().getString("notification_counter");
                getIntent().getExtras().getString("notification_yes");
                System.out.println("Algorithm"
                        + getIntent().getExtras().getString(
                        "notification_counter"));
                System.out.println("Intent"
                        + getIntent().getExtras().getString(
                        "notification_counter"));
                mDbAdapter.getAllCityOneSelected(getIntent().getExtras()
                        .getString("notification_counter"));
                m_notDatabaseModels = mDbAdapter
                        .getAllCityOneSelected(getIntent().getExtras()
                                .getString("notification_counter"));
                System.out.println("NOTSIZE"
                        + m_notDatabaseModels.get(0).getTarget());
                m_notDatabaseModel = m_notDatabaseModels.get(0).getTarget()
                        .toString();
                System.out.println("ID"
                        + m_notDatabaseModels.get(0).getPosition());
                m_notDatabaseModel_Category = m_notDatabaseModels.get(0)
                        .getCategory().toString();
                System.out.println("m_notDatabaseModel_Category"
                        + m_notDatabaseModel_Category);
                System.out.println("Name"
                        + m_notDatabaseModels.get(0).getName());

                yes = "true";
            }
        }
    }

    public void init() {
        vProgress.spin();

        showMenu();
        // setViewWithFont();

        getClothsFP();
        t.start();
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont() {
    // vTxtTitle.setTypeface(constant.fontface);
    // vTxtBack.setTypeface(constant.fontface);
    // vProgressTxt.setTypeface(constant.fontface);
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

    // ///tick function call section
    CountDownTimer t = new CountDownTimer(Long.MAX_VALUE, 1000) {
        public void onTick(long millisUntilFinished) {

            mTime++;
            if (mTime == 5) {
                vProgressTxt.setText("myEnergy. myStyle. \nmyDiModa");
            }
        }

        public void onFinish() {
            Log.d("test", "Timer last tick");
        }

    }.start();

    public void goFashionActivity() {

        System.out.println("Fashion" + Fashion8);
        if (!yes.equalsIgnoreCase("") && yes.equalsIgnoreCase("true")) {
            Intent intent1 = new Intent(DMAlgorithmActivity.this,
                    DMNotificationActivity.class);
            intent1.putExtra("favorite", "no");
            intent1.putExtra("category", m_notDatabaseModel_Category);
            intent1.putExtra("category_id", m_notDatabaseModels.get(0).getTarget());
            intent1.putExtra("name", m_notDatabaseModels.get(0).getName());
            startActivity(intent1);
            finish();
        } else if (isFromNotification) {
            Intent intent = new Intent(DMAlgorithmActivity.this, DMFashionActivity_7Hour.class);
            intent.putExtra("favorite", "no");
            intent.putExtra("showlayout", "showlayout"); // mayur to make sure we see the remember layout when needed
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(DMAlgorithmActivity.this, DMFashionActivity.class);
            intent.putExtra("favorite", "no");
            startActivity(intent);
            finish();
        }
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
        } else {
            query = ParseQuery.getQuery("DemoCloset");
        }

        if (constant.gCategory.equals("after5")
                || constant.gCategory.equals("formal"))
            query.whereNotEqualTo("Category", "casual");
        if (constant.gCategory.equals("casual")) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("shirt");
            list.add("trousers");
            query.whereContainedIn("Type", list);
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                if (e == null) {

                    if (!yes.equalsIgnoreCase("")) {

                        if (yes.equalsIgnoreCase("true")) {
                            makeSendData(makeJSONArray(clothList));

                            // makeSendData(makeJSONArray_DB(m_DatabaseModels));
                            sendClothsTS();
                        }
                    } else {
                        makeSendData(makeJSONArray(clothList));

                        // makeSendData(makeJSONArray_DB(m_DatabaseModels));
                        sendClothsTS();

                    }
                } else {
                    Toast.makeText(DMAlgorithmActivity.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
    // JSONObject to send server ----------
    public void makeSendData(JSONArray clothArr) {
        mSendData = new JSONObject();
        try {
            mSendData.put("version", "2");
            mSendData.put("category", constant.gCategory);
            mSendData.put("mode", constant.gMode);
            mSendData.put("closet", clothArr);
            mSendData.put("name", "genparams");
            mSendData.put("value", "1");
            System.out.println("Sending" + constant.gCategory + "" + constant.gMode + "" + clothArr);
            if (constant.gCategory.equalsIgnoreCase("")) {
                mSendData.put("category", "casual");
            }
            if (constant.gMode.equalsIgnoreCase("")) {

                mSendData.put("mode", "style me");

            } else {
                if (yes.equalsIgnoreCase("true")) {
                    mSendData.put("category", m_notDatabaseModels.get(0)
                            .getCategory());
                } else {
                    mSendData.put("category", constant.gCategory);
                    mSendData.put("mode", constant.gMode);
                }
            }

            if (constant.gMode.equals("help me"))
                mSendData.put("items", makeItemJSONArray(constant.gItemList));

            mSendData.put("blocked",
                    makeBlockedJSONArray(constant.gBlockedList));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("data---------", mSendData.toString());

		/*
         * m_DatabaseModel.setVersion(2);
		 * m_DatabaseModel.setCategory(constant.gCategory+constant.gMode);
		 * m_DatabaseModel.setTarget(""+clothArr);
		 * m_DatabaseModel.setName("genparams"); m_DatabaseModel.setValue("1");
		 * 
		 * mDbAdapter.add(m_DatabaseModel);
		 */

    }

    // / ------------------------------------------------ send json data to
    // server ------------------
    public void sendClothsTS() {
        mBaseUrl = "http://54.69.61.15/";//"http://54.191.209.169/";
        // commented mBaseUrl = "http://54.191.209.169/"; //
        // "http://54.69.61.15/";
        // mBaseUrl = "http://52.11.139.58/";
//		mBaseUrl = "http://AIProd.myDiModa.com/get_attire";
        MyAsyncTask task1 = new MyAsyncTask();
        task1.execute();
    }

    // / ----------------------------------------------- parse response
    // -------------------------
    public void parseResponse(JSONObject data) {
        hideProgress();
        constant.gFashion = new DMBlockedObject();
        if (data != null) {
            Log.e("response -----123", data.toString());


            try {
                JSONArray arr = data.getJSONArray("selection");
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        DMItemObject item = new DMItemObject(obj);


                        // If type we selected and type we get are same then check item.index is same or not
                        // If they are different , then assign our selected item.index to item object.
                        try {
                            if (AppUtils.getPref("type", this).equalsIgnoreCase(item.type)) {
                                if (!item.index.equalsIgnoreCase(AppUtils.getPref("index", this))) {
                                    System.out.println("Not Same");
                                    item.index = AppUtils.getPref("index", this);
                                    item.type = AppUtils.getPref("type", this);
                                }
                            }
                        } catch (Exception e) {

                        }
                        constant.gFashion.blockedList.add(item);
                        constant.gFashion.setBlockedList(constant.gFashion.blockedList);

                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "You can not get clothes",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        if (constant.gFashion.blockedList.size() > 0)

            goFashionActivity();
    }

    // / ----------------------------------------------- parse responsedb
    // -------------------------
    public void parseResponseDB(JSONObject data) {
        hideProgress();
        constant.gFashiondb = new DatabaseModel();
        if (data != null) {

            try {
                JSONArray arr = data.getJSONArray("selection");
                if (arr != null) {
                    /*
                     * DatabaseModel m_DatabaseModel = new DatabaseModel();
					 * m_DatabaseModel
					 * .setType(m_notDatabaseModels.get(0).getType());
					 */

					/*
					 * JSONObject obj = arr.getJSONObject(0); DatabaseModel
					 * m_DatabaseModel = new DatabaseModel();
					 * m_DatabaseModel.getTarget();
					 */
					/*
					 * DMItemObjectDatabase item = new DMItemObjectDatabase();
					 * item.index= m_notDatabaseModel.toString(); item.type =
					 * m_notDatabaseModel.toString();
					 * constant.gFashion.blockedList_db.add(item);
					 */

                    DMItemObjectDatabase item = new DMItemObjectDatabase();
                    item.index = "cmN4DOLWvl";
                    item.type = "suit";
					/*
					 * item.index= m_notDatabaseModel.toString(); item.type =
					 * m_notDatabaseModel.toString();
					 */
                    constant.gFashiondb.getTarget();
					/*
					 * for(int i=0;i<arr.length();i++) { JSONObject obj =
					 * arr.getJSONObject(i); DMItemObject item = new
					 * DMItemObject(obj); item.index ="cmN4DOLWvl";
					 * item.type="suit";
					 * constant.gFashion.blockedList.add(item); }
					 */
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "You can not get clothes",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        Log.e("response --12345 from db", data.toString());
        // if(constant.gFashion.blockedList.size()>0)
        goFashionActivity();
    }

    public class MyAsyncTask extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        // / server communicate using asyncTask

        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
        JSONObject mResponseData;

        @Override
        protected void onPreExecute() {

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

            if (!yes.equalsIgnoreCase("") && yes.equalsIgnoreCase("true")) {
                parseResponseDB(mResponseData);
            } else {
                // / when get response, call parser response function of the
                // class
                parseResponse(mResponseData);
            }

            super.onPostExecute(result);
        }
    }

    public void showProgress() {
        vProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        vProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        t.cancel();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    }

    ;

    private void processIntent(Intent intent) {
        // get your extras
        if (intent != null) {
            if (intent.hasExtra("notification_counter")) {
                intent.getExtras().getString("notification_counter");
                System.out.println("Algorithm"
                        + intent.getExtras().getString("notification_counter"));

            }
        }

    }

}

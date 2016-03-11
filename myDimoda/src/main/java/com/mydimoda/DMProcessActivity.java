package com.mydimoda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zcolorpattern;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DMProcessActivity extends Activity {

    ImageView vPhoto;
    RelativeLayout vProcess;
    String mColor = null;
    String mType;
    int mCloset = 0;

    int mTime = 0;
    boolean mIsPurchase = false, mIsGet = false, mIsFrmDialog;
    List<ParseObject> mClothList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        vPhoto = (ImageView) findViewById(R.id.photo_view);
        vProcess = (RelativeLayout) findViewById(R.id.process_layout);

        Intent intent = getIntent();
        mCloset = intent.getIntExtra("closet", 0);
        mType = intent.getStringExtra("type");
        mIsPurchase = intent.getBooleanExtra("purchase", false);
        mIsFrmDialog = intent.getBooleanExtra(constant.FRM_DIALG_KEY, false);
        System.out.println("DMProcessActivity Type--> " + mType);
        init();
    }

    public void init() {
        t.start();

        if (constant.gTakenBitmap != null) {
            vPhoto.setImageBitmap(constant.gTakenBitmap);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    /* show login layout */
                    showAnimation();
                    new MyAsyncTask().execute();
                }
            }, 1000);
        }
    }

    // ///tick function call section
    CountDownTimer t = new CountDownTimer(Long.MAX_VALUE, 1000) {
        public void onTick(long millisUntilFinished) {

            mTime++;
            if (mTime > 8) {
                if (mColor != null && !mIsGet) {
                    mIsGet = true;
                    if (mIsPurchase) {
                        getClothes();
                    } else {
                        goCaptureActivity();
                    }

                }
            }
        }

        public void onFinish() {
            Log.d("test", "Timer last tick");
        }

    }.start();


    public void goCaptureActivity() {
        Intent intent = new Intent(DMProcessActivity.this,
                DMCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", mType);
        intent.putExtra("isCapture", false);
        intent.putExtra(constant.FRM_DIALG_PROCESS_KEY, mIsFrmDialog);
        startActivity(intent);
        finish();
    }

    public void showAnimation() {
        vProcess.setVisibility(View.VISIBLE);
        vProcess.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.dialog_up_bottom));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* show login layout */
                showUpAnimation();
            }
        }, 4000);

    }

    public void showUpAnimation() {
        vProcess.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.dialog_bottom_up));

        new Handler().postDelayed(new Runnable() {
            public void run() {
				/* show login layout */
                vProcess.setVisibility(View.GONE);
            }
        }, 4000);
    }

    public void parse(Bitmap bitmap) {
        int[] buffer = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(buffer, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        try {
            zcolorpattern pattern = new zcolorpattern();
            pattern.create();
            pattern.recognize(buffer, bitmap.getWidth(), bitmap.getHeight(), 4);
            int color = pattern.getColor();
            int pat = pattern.getPattern();
            int cVal = pattern.getColorValue();

            int r = (cVal >> 16) & 0xFF;
            int g = (cVal >> 8) & 0xFF;
            int b = (cVal >> 0) & 0xFF;

            constant.gColorVal = toHex(r, g, b);
            mColor = constant.gColor[color];
            constant.gPatternVal = constant.gPattern[pat];

            constant.gMessage = "Color is " + constant.gColorVal
                    + " and Pattern is " + constant.gPatternVal;

            Log.e("", constant.gMessage);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // / ------------- hex color -----------------------
    public String toHex(int r, int g, int b) {
        return toBrowserHexValue(r) + toBrowserHexValue(g)
                + toBrowserHexValue(b);
    }

    @SuppressLint("DefaultLocale")
    private String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    public class MyAsyncTask extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        // / server communicate using asyncTask
        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
        String message = "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(
                String... params) {

            parse(constant.gCropBitmap);
            return UploadsList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

            super.onPostExecute(result);
        }
    }

    // / ----------------------------- get products from server
    // -------------------
    public void getClothes() {
        constant.showProgress(this, "Loading...");
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = null;
        if (constant.gIsCloset) {
            query = ParseQuery.getQuery("Clothes");
            query.whereEqualTo("User", user);
            query.orderByDescending("createdAt");
        } else {
            query = ParseQuery.getQuery("DemoCloset");
            query.orderByDescending("createdAt");
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                constant.hideProgress();
                if (e == null) {
                    makeClothList(clothList);
                } else {
                    Toast.makeText(DMProcessActivity.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void makeClothList(List<ParseObject> clothList) {
        ParseUser user = ParseUser.getCurrentUser();
        if (clothList != null) {
            mClothList = new ArrayList<ParseObject>();
            for (int i = 0; i < clothList.size(); i++) {
                if (constant.gIsCloset) {
                    ParseUser itemUser = clothList.get(i).getParseUser("User");
                    if (itemUser.getObjectId().equals(user.getObjectId())) {
                        mClothList.add(clothList.get(i));
                    }
                } else {
                    mClothList.add(clothList.get(i));
                }
            }

            compareProduct();
        }
    }

    // / ---------------------------- compare product with cloth list
    // --------------
    public void compareProduct() {
        boolean isSimilar = false;

        int mVal = Color.parseColor("#" + constant.gColorVal);
        int rVal = (mVal >> 16) & 0xFF;
        int gVal = (mVal >> 8) & 0xFF;
        int bVal = (mVal >> 0) & 0xFF;

        for (int i = 0; i < mClothList.size(); i++) {
            String type = mClothList.get(i).getString("Type");

            if (type.equals(mType)) {
                String pattern = mClothList.get(i).getString("Pattern");
                if (pattern.equals(constant.gPatternVal)) {
                    String color = mClothList.get(i).getString("Color");
                    int cVal = Color.parseColor("#" + color);
                    int r = (cVal >> 16) & 0xFF;
                    int g = (cVal >> 8) & 0xFF;
                    int b = (cVal >> 0) & 0xFF;

                    if (rVal > r - 20 && rVal < r + 20) {
                        if (bVal > b - 20 && bVal < b + 20) {
                            if (gVal > g - 20 && gVal < g + 20) {
                                isSimilar = true;
                                showSimilarDialog();
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (!isSimilar) {
            recommendProduct();
        }
    }

    // / ---------------------------- recommend ----------------------------
    public void recommendProduct() {

        String baseUrl = "http://54.69.61.15/recommend";
        // String baseUrl = "http://AIProd.myDiModa.com/recommend";
        JSONObject sendData = new JSONObject();

        JSONObject currentItem = new JSONObject();
        try {
            currentItem.put("color", constant.gColorVal);
            currentItem.put("pattern", constant.gPatternVal);
            currentItem.put("type", mType);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray clothArr = new JSONArray();
        //
        //
        //
        //
        // OPEN IN NEW VERSION
        //
        //
        //
        //
        // clothArr = dummyClosetArray();
        try {
            for (int i = 0; i < mClothList.size(); i++) {
                JSONObject item = new JSONObject();
                item.put("id", mClothList.get(i).getObjectId());
                item.put("type", mClothList.get(i).getString("Type"));
                item.put("color", mClothList.get(i).getString("Color"));
                item.put("pattern", mClothList.get(i).getString("Pattern"));

                clothArr.put(item);

                Log.e("item object", item.toString());
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            sendData.put("item", currentItem);
            sendData.put("closet", clothArr);
            sendData.put("version", "2");

            System.out.println("Send Data recommendProduct--> "
                    + sendData.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        new MyAsyncTask1(baseUrl, sendData).execute();
    }

    // / ------------------------- parse Response of recommend -----------------
    public void parseRecommend(JSONObject data) {
        Log.d("parse Response of recommend", data.toString());
        if (data != null) {
            try {
                String msg = data.getString("recommend");
                if (msg.equals("yes")) {
                    // goCaptureActivity();
                    showRecommendDialog1();
                } else {
                    showRecommendDialog();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            goPerfectActivity();
        }
    }

    public void showSimilarDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        FontsUtil.setExistenceLight(this, title);
        title.setText("There is already similiar cloth in your closet. Do you want to add this to your closet?");

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        FontsUtil.setExistenceLight(this, okBtn);

        okBtn.setText("OK");
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//				showRecommendDialog1();
                recommendProduct();
//				goCaptureActivity();
                dialog.dismiss();
//				finish();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        FontsUtil.setExistenceLight(this, cancelBtn);
        cancelBtn.setText("Cancel");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showRecommendDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        FontsUtil.setExistenceLight(this, title);
        // title.setText("This cloth are not compatible with your closet. Please do not purchase it.");
        title.setText("This item is not compatible with your wardrobe. We would not recommend a purchase.");

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        FontsUtil.setExistenceLight(this, okBtn);
        okBtn.setText("OK");
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
				/*
				 * Intent intent = new Intent(DMProcessActivity.this,
				 * DMAutoActivity.class);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * startActivity(intent); finish();
				 */
				/*
				 * 26-0-2015 Commented code Intent intent = new
				 * Intent(DMProcessActivity.this, DMAutoActivity.class);
				 * intent.putExtra("from", "exact"); intent.putExtra("closet",
				 * mType); AppUtils.putPref("type", String.valueOf(mType),
				 * DMProcessActivity.this); intent.putExtra("price", "0");
				 * startActivity(intent); dialog.dismiss();
				 */

                /**
                 * Reverted code
                 */
                finish();
                dialog.dismiss();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        FontsUtil.setExistenceLight(this, cancelBtn);
        cancelBtn.setText("Cancel");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Reverted code
                 */

                // goPerfectActivity();

                Intent intent = new Intent(DMProcessActivity.this,
                        DMAutoActivity.class);
                intent.putExtra("from", "exact");
                intent.putExtra("closet", mCloset);
                AppUtils.putPref("type", String.valueOf(mType),
                        DMProcessActivity.this);
                intent.putExtra("price",
                        AppUtils.getPref("price", DMProcessActivity.this));
                startActivity(intent);
                dialog.dismiss();
				/*
				 * 
				 * commented 26-06-2015 finish(); dialog.dismiss();
				 */
            }
        });
        dialog.show();
    }

    public void showRecommendDialog1() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        FontsUtil.setExistenceLight(this, title);
        // title.setText("This cloth are not compatible with your closet. Please do not purchase it.");
        title.setText("Nice look!  This will make your Style improve.  We recommend you purchase");

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        FontsUtil.setExistenceLight(this, okBtn);
        okBtn.setText("OK");
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
				/*
				 * Intent intent = new Intent(DMProcessActivity.this,
				 * DMAutoActivity.class);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * startActivity(intent); finish();
				 */
				/*
				 * 26-0-2015 Commented code Intent intent = new
				 * Intent(DMProcessActivity.this, DMAutoActivity.class);
				 * intent.putExtra("from", "exact"); intent.putExtra("closet",
				 * mType); AppUtils.putPref("type", String.valueOf(mType),
				 * DMProcessActivity.this); intent.putExtra("price", "0");
				 * startActivity(intent); dialog.dismiss();
				 */

                /**
                 * Reverted code
                 */
                goCaptureActivity();
                dialog.dismiss();
                finish();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        FontsUtil.setExistenceLight(this, cancelBtn);
        cancelBtn.setText("Cancel");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Reverted code
                 */

                // goPerfectActivity();

                Intent intent = new Intent(DMProcessActivity.this,
                        DMAutoActivity.class);
                intent.putExtra("from", "exact");
                intent.putExtra("closet", mCloset);
                AppUtils.putPref("type", String.valueOf(mType),
                        DMProcessActivity.this);

                intent.putExtra("price",
                        AppUtils.getPref("price", DMProcessActivity.this));
                startActivity(intent);
                dialog.dismiss();
				/*
				 * 
				 * commented 26-06-2015 finish(); dialog.dismiss();
				 */
            }
        });
        dialog.show();
    }

    // / Exact Activity
    public void goPerfectActivity() {
        Intent intent = new Intent(DMProcessActivity.this,
                DMExactActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public class MyAsyncTask1 extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        // / server communicate using asyncTask

        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
        JSONObject mResponseData;
        JSONObject mSendData;
        String mBaseUrl;

        public MyAsyncTask1(String url, JSONObject sendData) {
            mBaseUrl = url;
            mSendData = sendData;
        }

        @Override
        protected void onPreExecute() {
            constant.showProgress(DMProcessActivity.this, "Recommending...");
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(
                String... params) {

            // Creating JSON Parser instance
            JSONPostParser jParser = new JSONPostParser();
            // getting JSON string from URL
            mResponseData = jParser.getJSONFromUrl(mBaseUrl,
                    mSendData.toString());
            System.out.println("MyAsyncTask1--> sendData--> " + mSendData);
            return UploadsList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            constant.hideProgress();
            parseRecommend(mResponseData);
            super.onPostExecute(result);
        }
    }

    /**
     * @author Keyur Tailor:
     * <p/>
     * This function will create a dummyClosetArray with static values
     * for all Categories.
     * <p/>
     * This is used as solution to "error:invalid parameter" in Response
     * of "recommend".
     * <p/>
     * Basically it will generate dummy array for each category
     * initially after that original array will be filled as per size of
     * the "mClothList".
     */
    public JSONArray dummyClosetArray() {
        JSONArray clothArr = new JSONArray();
        String[] categoryArr = {"shirt", "jacket", "trousers", "tie", "suit"};
        for (int i = 0; i < categoryArr.length; i++) {
            try {
                JSONObject item = new JSONObject();
                item.put("id", randomString(10));
                item.put("type", categoryArr[i]);
                item.put("color", "000000");
                item.put("pattern", "empty");
                clothArr.put(item);

            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return clothArr;
    }

    /**
     * @param length
     * @return randomString
     * @author Keyur Tailor:
     * <p/>
     * A simple alphanumeric random function which will generate a
     * random string based on length.
     */
    public static String randomString(int length) {
        char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                .toCharArray();
        Random random = new SecureRandom();
        char[] randomString = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'b', '1',
                '0'};
        try {
            randomString = new char[length];

            for (int i = 0; i < randomString.length; i++) {
                // picks a random index out of character set > random character
                int randomCharIndex = random.nextInt(CHARSET_AZ_09.length);
                randomString[i] = CHARSET_AZ_09[randomCharIndex];
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String(randomString);

    }

    @Override
    public void onPause() {
        t.cancel();
        super.onPause();
    }
}

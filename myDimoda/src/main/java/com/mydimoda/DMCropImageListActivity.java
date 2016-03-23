package com.mydimoda;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zcolorpattern;
import com.mydimoda.adapter.DMImageRecycAdapter;
import com.mydimoda.adapter.DMOptionsListAdapter;
import com.mydimoda.model.CropListModel;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DMCropImageListActivity extends FragmentActivity {


    // / menu
    Button vBtnMenu;
    TextView vTxtBack;
    RelativeLayout vBackLayout;


    ProgressDialog vProgress;


    @Bind(R.id.act_crop_img_lst)
    RecyclerView mRecyclerView;
    ArrayList<CropListModel> mModelList;
    @Bind(R.id.doneImageVw)
    ImageView mDoneBtn;

    @Bind(R.id.title_text)
    TextView mTitleTxt;

    AlertDialog mCatDialog;
    AlertDialog mTypeDialog;
    DMImageRecycAdapter mMainAdapter;
    LinearLayoutManager mLayoutManager;
    String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image_list);
        ButterKnife.bind(this);
        // / layout
        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);
        FontsUtil.setExistenceLight(this, mTitleTxt);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

//        FontsUtil.setExistenceLight(this, mCatSpinner);
//        FontsUtil.setExistenceLight(this, mTypeSpinner);


// initilised
        mModelList = constant.getImageLst();
        vBackLayout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        prepareView();

        mDoneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCatAndTypeForALL()) {
                    new AnalyseTask().execute();
                }
            }
        });

    }

    public void prepareView() {
        mType = getIntent().getStringExtra("type");
        if(!TextUtils.isEmpty(mType)&&!mType.equalsIgnoreCase(constant.EMPTY_TYPE)&&
                !mType.equalsIgnoreCase("null")){
            for (CropListModel model :mModelList){
                model.setmType(mType);
            }
        }

        mMainAdapter = new DMImageRecycAdapter(mModelList, new DMImageRecycAdapter.CropListCallBacks() {
            @Override
            public void type(int position) {
                showTypeDialog(position);
            }

            @Override
            public void category(int position) {
                showCategoryDialog(position);
            }

            @Override
            public void delete(int position) {
                if(mModelList.size()>1){
                    mModelList.remove(position);
                    mMainAdapter.notifyDataSetChanged();
                }else{
                    constant.getImageLst().clear();
                    finish();
                }

            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMainAdapter);
    }


    // / ------------------------------------- save photo to parse.com database
    // -----------------------
    public void savePhotosToParse() {
        CropListModel mModel;
        constant.showProgress(this, "Saving data");

        for (int i = 0; i < mModelList.size(); i++) {
            mModel = mModelList.get(i);
            if (mModel.getmImage() != null) {
                byte[] byteArray = getByteArray(i);
                if (byteArray != null) {
                    String category = "";

                    category = mModel.getmCategory().toLowerCase();

                    // Make parse object
                    ParseUser user = ParseUser.getCurrentUser();
                    ParseFile file = new ParseFile("image.jpg", byteArray);

                    ParseObject object = new ParseObject("Clothes");
                    object.put("User", user);
                    object.put("Type", mModel.getmType().toLowerCase());
                    object.put("Color", mModel.getmColor());
                    object.put("Pattern", mModel.getmPattern());
                    object.put("Category", category);
                    object.put("ImageContent", file);


                    final int finalI = i;
                    object.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            // TODO Auto-generated method stub
                            if (e == null){

                                if(!isFinishing()){
                                    Toast.makeText(DMCropImageListActivity.this, "Saved",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            else {
                                Toast.makeText(DMCropImageListActivity.this,
                                        e.toString(), Toast.LENGTH_LONG).show();
                                Log.e(DMCropImageListActivity.this.getLocalClassName(), e.toString());

                            }


                            // CGChange - IsCloset
                            if (!constant.gIsCloset)
                                setIsCloset();

//                        constant.hideProgress();

                            getClothFP(finalI);
                            if (finalI == mModelList.size() - 1) {
                                if (getParent() == null) {
                                    setResult(RESULT_OK);
                                } else {
                                    getParent().setResult(RESULT_OK);
                                }
                                finish();
                            }


                            //
                        }
                    });
                }
            }
        }
    }

    // / ------------------------------------- set isCloset flag = true in user
    // table ------------------
    public void setIsCloset() {
        constant.gIsCloset = true;
        ParseUser user = ParseUser.getCurrentUser();
        user.put("isDemoCloset", true);
        user.saveInBackground();

        SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isCloset", constant.gIsCloset);
        editor.commit();
    }

    public byte[] getByteArray(int pos) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray = null;
        try {
            mModelList.get(pos).getmImage().compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byteArray = stream.toByteArray();
            stream.flush();
            stream.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return byteArray;
    }

    // / ------------------------------------------- show dialog

    @Override
    public void onResume() {
        super.onResume();
    }

    List<ParseObject> mClothList = new ArrayList<>();

    /**
     * mayur: to get the count of cloths already in closet so as to increase style me count
     */
    public void getClothFP(final int pos) {
        //   constant.showProgress(this, "Loading...");
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Clothes");
        query.whereEqualTo("Type", mModelList.get(pos).getmType().toLowerCase());
        query.whereEqualTo("User", user);
        query.orderByDescending("createdAt");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                if (e == null) {
                    mClothList = clothList;
                    stylePointProcessor(mClothList.size(), mModelList.get(pos).getmType().toLowerCase().trim());

                } else {
                    Toast.makeText(DMCropImageListActivity.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    int mMaxCount = 0;

    /**
     * for calculating and assigning style options
     *
     * @param items
     */
    public void stylePointProcessor(int items, final String mType) {
        if (items == 2 && !SharedPreferenceUtil.getBoolean(constant.PREF_MAX_COUNT_GVN + mType, false)) {
            final ParseUser user = ParseUser.getCurrentUser();

            mMaxCount = user.getInt(constant.USER_MAX_COUNT);
            user.put(constant.USER_MAX_COUNT, mMaxCount + 5);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        SharedPreferenceUtil.putValue(constant.PREF_MAX_COUNT_GVN + mType, true);
                        constant.maxCount = user.getInt(constant.USER_MAX_COUNT);
                        if (!(user.getBoolean(constant.RATED_APP) || user.getBoolean("Buy") ||
                                SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase("1"))) {
                            Toast.makeText(DMCropImageListActivity.this, "5 Style me options awarded",
                                    Toast.LENGTH_LONG).show();
                        }

                        constant.hideProgress();
                    } else {
                        Toast.makeText(DMCropImageListActivity.this, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (items > 2) {
            final ParseUser user = ParseUser.getCurrentUser();
            mMaxCount = user.getInt(constant.USER_MAX_COUNT);
            user.put(constant.USER_MAX_COUNT, mMaxCount + 1);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        constant.maxCount = user.getInt(constant.USER_MAX_COUNT);
                        if (!(user.getBoolean(constant.RATED_APP) || user.getBoolean("Buy") ||
                                SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase("1"))) {
                            Toast.makeText(DMCropImageListActivity.this, "1 Style me option awarded",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(DMCropImageListActivity.this, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        constant.hideProgress();
        finish();
    }

    final String[] mCatList = {"Formal", "Casual"};
    final String[] mTypeList = {"Shirt", "Trousers", "Suit", "Jacket", "Tie"};

    public void showCategoryDialog(final int pos) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppCompatAlertDialogStyle));
        View mmain = this.getLayoutInflater().inflate(R.layout.dialog_options, null);

        ListView mOptions = (ListView) mmain.findViewById(R.id.dialog_options_lstvw);
        final DMOptionsListAdapter mAdapter = new DMOptionsListAdapter(this, mCatList);

        mOptions.setAdapter(mAdapter);

        mOptions.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mModelList.get(pos).setmCategory(mAdapter.mList[position]);
                mMainAdapter.notifyDataSetChanged();

                if (mCatDialog != null && mCatDialog.isShowing()) {
                    mCatDialog.dismiss();
                }
                //    checkCatAndType(pos);
            }
        });
        mBuilder.setView(mmain);
        mCatDialog = mBuilder.create();
        mCatDialog.show();
    }

    public void showTypeDialog(final int pos) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        View mmain = this.getLayoutInflater().inflate(R.layout.dialog_options, null);

        ListView mOptions = (ListView) mmain.findViewById(R.id.dialog_options_lstvw);
        final DMOptionsListAdapter mAdapter = new DMOptionsListAdapter(this, mTypeList);

        mOptions.setAdapter(mAdapter);

        mOptions.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mModelList.get(pos).setmType(mAdapter.mList[position]);
                mMainAdapter.notifyDataSetChanged();
                if (mTypeDialog != null && mTypeDialog.isShowing()) {
                    mTypeDialog.dismiss();
                }
                checkCatAndType(pos);
            }
        });
        mBuilder.setView(mmain);
        mTypeDialog = mBuilder.create();
        mTypeDialog.show();
    }

    public boolean checkCatAndType(int pos) {
        for (int i = 0; i < mCatList.length; i++) {
            if (mCatList[i].equalsIgnoreCase(mModelList.get(pos).getmCategory().toString())) {
                for (int i2 = 0; i2 < mTypeList.length; i2++) {
                    if (mTypeList[i2].equalsIgnoreCase(mModelList.get(pos).getmType().toString())) {
                        mModelList.get(pos).setIsError(false);
                        mMainAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
            }
        }
        mModelList.get(pos).setIsError(true);
        mMainAdapter.notifyDataSetChanged();
        return false;
    }


    //this is actual method which extracts image rgb values
    public void parse(int position) {
        CropListModel mItem = mModelList.get(position);
        int[] buffer = new int[mItem.getmImage().getWidth() * mItem.getmImage().getHeight()];
        mItem.getmImage().getPixels(buffer, 0, mItem.getmImage().getWidth(), 0, 0, mItem.getmImage().getWidth(),
                mItem.getmImage().getHeight());
        try {
            zcolorpattern pattern = new zcolorpattern();
            pattern.create();
            pattern.recognize(buffer, mItem.getmImage().getWidth(), mItem.getmImage().getHeight(), 4);
            int color = pattern.getColor();
            int pat = pattern.getPattern();
            int cVal = pattern.getColorValue();

            int r = (cVal >> 16) & 0xFF;
            int g = (cVal >> 8) & 0xFF;
            int b = (cVal >> 0) & 0xFF;

            //constant.gColorVal = toHex(r, g, b);
            mModelList.get(position).setmColor(toHex(r, g, b));
            mModelList.get(position).setmPattern(constant.gPattern[pat]);
            // mColor = constant.gColor[color];
            //  constant.gPatternVal = constant.gPattern[pat];

            constant.gMessage = "Color is " + mModelList.get(position).getmColor()
                    + " and Pattern is " + mModelList.get(position).getmPattern();

            Log.e(this.getLocalClassName(), constant.gMessage);
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

    private class AnalyseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            vProgress = new ProgressDialog(DMCropImageListActivity.this);
            vProgress.setMessage(" Please wait...\n Analysing image(s)");
            vProgress.setCancelable(false);
            vProgress.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < mModelList.size(); i++) {
                parse(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (vProgress != null) {
                vProgress.dismiss();
            }

            savePhotosToParse();
        }

    }

    public boolean checkCatAndTypeForALL() {
        for (int i = 0; i < mModelList.size(); i++) {
            if (!checkCatAndType(i)) {
                mRecyclerView.smoothScrollToPosition(i);
                return false;
            }
        }
        return true;
    }
}

package com.mydimoda;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.interfaces.DialogItemClickListener;
import com.mydimoda.model.DatabaseModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class DMHomeActivity extends FragmentActivity {

    Button vBtnMenu;
    // / menu
    ListView vMenuList;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    DbAdapter mDbAdapter;
    Context mContext;
    DatabaseModel m_DatabaseModel;
    final public static String ONE_TIME = "onetime";
    String TAG = "";
    DialogInterface mDlgInterface;
    int RESULT_GALLERY = 2;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getLocalClassName();
        setContentView(R.layout.activity_home);
        mDbAdapter = new DbAdapter(DMHomeActivity.this);
        mDbAdapter.createDatabase();
        mDbAdapter.open();

        m_DatabaseModel = new DatabaseModel();
        System.out.println("Before" + AppUtils.getPref("pro", DMHomeActivity.this));
        mContext = DMHomeActivity.this;

        user = ParseUser.getCurrentUser();
        boolean bPurchased = user.getBoolean("ratedMyDimoda");
        System.out.println("HomePurchased" + bPurchased);


        if (bPurchased == true) {
            System.out.println("TRUE" + bPurchased);
        } else {
            if (AppUtils.getPref("pro", DMHomeActivity.this) != null) {
                if (AppUtils.getPref("pro", DMHomeActivity.this).equalsIgnoreCase("false")) {
                    AppRater.app_launched(DMHomeActivity.this);
                } else {
                    AppUtils.putPref("pro", "true", DMHomeActivity.this);
                }
            } else if (AppUtils.getPref("pro", DMHomeActivity.this) == null) {
                AppUtils.putPref("pro", "false", DMHomeActivity.this);
                AppRater.app_launched(DMHomeActivity.this);
            } else {
                AppUtils.putPref("pro", "false", DMHomeActivity.this);
                AppRater.app_launched(DMHomeActivity.this);
            }
        }
        System.out.println("After" + AppUtils.getPref("pro", DMHomeActivity.this));
        // / layout
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);

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

                if (position == 0) {
                    slideMenu();
                } else {
                    constant.selectMenuItem(DMHomeActivity.this, position,
                            false);
                }
            }
        });

        Log.e(this.getLocalClassName(), user.getInt(constant.USER_MAX_COUNT) + " count " + user.getInt("Count"));
        if (user.getInt(constant.USER_MAX_COUNT) >= 10) {
            constant.maxCount = user.getInt(constant.USER_MAX_COUNT);
        } else {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("License");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {

                        if (objects.size() > 0) {

                            ParseObject object = objects.get(0);
                            constant.maxCount = object.getInt("MaxValue");
                            constant.max_lic_count = object.getInt("MaxValue");
                            if (!SharedPreferenceUtil.getBoolean(constant.USER_MAX_COUNT_INITILISED, false)) {


                                if (user.getInt(constant.USER_MAX_COUNT) < constant.maxCount) {

                                    user.put(constant.USER_MAX_COUNT, constant.maxCount); // it needs to be initilised as 5 which is the minimum free style points
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            SharedPreferenceUtil.putValue(constant.USER_MAX_COUNT_INITILISED, true);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        Toast.makeText(DMHomeActivity.this, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if (user == null) {
            user = ParseUser.getCurrentUser();
        }
        configOldUsersMaxCount();
        shouldShowGalleryDialog(user);
        if (!AppUtils.getDefaults(this, constant.PREF_IS_GALRY_DIALOG_SHOWN, false)) {

            try {
                AppUtils.showGalleryDialog(this, new DialogItemClickListener() {

                    @Override
                    public void onImageClick(String imagePath) {
                        try {
                            mDlgInterface.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        goToCropActivity(imagePath);
                    }

                    @Override
                    public void onGalleryClick() {
                        try {
                            mDlgInterface.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        callGallery();
                    }

                    @Override
                    public void onCloseClick() {
                        try {
                            mDlgInterface.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDialogVisible(DialogInterface mDialogInterface) {
                        mDlgInterface = mDialogInterface;
                    }
                }, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        showMenu();
    }

    // / --------------------------------- init Data
    // ---------------------------------------
    public void initData() {
        constant.gCategory = "";
        constant.gMode = "";
        constant.gLikeNum = 0;
    }

    // / --------------------------------- show menu list
    // --------------------------------------
    public void showMenu() {
        System.out.println("Setting" + constant.gMenuList);
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

    public void showHomeFragment() {
        // / channel fragment
        FragmentTransaction fts = getFragmentManager().beginTransaction();
        DMHomeFragment fragment = new DMHomeFragment();
        fragment.mActivity = this;
        fts.replace(R.id.frame_layout, fragment);
        fts.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        initData();
        try {
            showHomeFragment();
        } catch (Exception e) {

        }
    }

    public void callGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = result.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            goToCropActivity(cursor.getString(columnIndex));
            cursor.close();
        }
    }

    public void goToCropActivity(String picPath) {
        constant.gTakenBitmap = BitmapFactory.decodeFile(picPath);
        Intent intent = new Intent(mContext, DMCaptureActivity.class);
        intent.putExtra("type", constant.EMPTY_TYPE);
        intent.putExtra("isCapture", false);
        intent.putExtra(constant.FRM_DIALG_KEY, true);
        startActivity(intent);
    }

    int totalCloths = 0;

    public void shouldShowGalleryDialog(ParseUser user) {
        SharedPreferences settings = mContext.getSharedPreferences(constant.PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.getBoolean("isCloset", false)) {
            ParseQuery<ParseObject> query = null;
            query = ParseQuery.getQuery("Clothes");
            query.whereEqualTo("User", user);
            query.orderByDescending("createdAt");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (list != null) {
                        totalCloths = list.size();
                        SharedPreferenceUtil.putValue(constant.PREF_CLOTH_COUNT, totalCloths);
                        SharedPreferenceUtil.save();
                        Log.e(this.getClass().getSimpleName(), "cloth list size: " + totalCloths);
                        if (totalCloths <= 50) {
                            //  AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, false, mContext);
                        } else {
                            AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, true, mContext);
                        }
                    }
                }
            });
        }
    }

    public void configOldUsersMaxCount() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_MAX_COUNT_CONFIGURED, false)) {

            SharedPreferences settings = mContext.getSharedPreferences(constant.PREFS_NAME, Context.MODE_PRIVATE);
            if (settings.getBoolean("isCloset", false)) {
                ParseQuery<ParseObject> query = null;
                query = ParseQuery.getQuery("Clothes");
                query.whereEqualTo("User", user);
                query.orderByDescending("createdAt");

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (list != null) {
                            int toAddCount = user.getInt(constant.USER_MAX_COUNT);
                            totalCloths = list.size();
                            SharedPreferenceUtil.putValue(constant.PREF_CLOTH_COUNT, totalCloths);
                            SharedPreferenceUtil.save();
                            Log.e("home", "cloth list size: " + totalCloths);
                            if (totalCloths >= 2 && user.getInt(constant.USER_MAX_COUNT) == constant.max_lic_count) { // for old users usermax count will by default set to max licence count

                                for (int i = 0; i < totalCloths; i++) {
                                    toAddCount++;
                                }
                                if (totalCloths >= 10) {
                                    toAddCount = toAddCount + 40;//we have to give 50 but..the loop above already gave 10 points
                                }
                                user.put(constant.USER_MAX_COUNT, toAddCount);
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        SharedPreferenceUtil.putValue(constant.PREF_MAX_COUNT_CONFIGURED, true);
                                    }
                                });

                            }else{
                                SharedPreferenceUtil.putValue(constant.PREF_MAX_COUNT_CONFIGURED, true);
                            }
                        }
                    }
                });
            }


        }

    }
}
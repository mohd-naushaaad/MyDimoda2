package com.mydimoda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMDialogGridAdapter;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;
import com.mydimoda.model.DialogImagesModel;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
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
    Dialog mGalleryDialog;
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

		/*if(AppUtils.getPref("key alarm", mContext) == null)
        {
			setAlarm_8hour();
			System.out.println("Appalarm preference set values");
		}
		else if(AppUtils.getPref("key alarm", mContext).equalsIgnoreCase("1"))
		{
			System.out.println("Appalarm preference already set in values");
		}
		else 
		{
			setAlarm_8hour();
			System.out.println("Appalarm preference set values");
		}*/


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

                            if (!SharedPreferenceUtil.getBoolean(constant.USER_MAX_COUNT_INITILISED, false)) {


                                if (user.getInt(constant.USER_MAX_COUNT) < constant.maxCount) {

                                    user.put(constant.USER_MAX_COUNT, constant.maxCount); // it needs to be initilised as 5 which is the minimum free style points
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            SharedPreferenceUtil.putValue(constant.USER_MAX_COUNT_INITILISED,true);
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
        shouldShowGalleryDialog(user);
        if (!AppUtils.getDefaults(this, constant.PREF_IS_GALRY_DIALOG_SHOWN, false) ) {
            showGalleryDialog();
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

    public ArrayList<DialogImagesModel> mGallerImageLst = new ArrayList();

    public void showGalleryDialog() {
        if (mGalleryDialog == null || !mGalleryDialog.isShowing()) {
            mGallerImageLst.clear();
            String[] projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATA};
            // content:// style URI for the "primary" external storage volume
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // Make the query.
            Cursor cur = getContentResolver().query(images,
                    projection, // Which columns to return
                    null,       // Which rows to return (all rows)
                    null,       // Selection arguments (none)
                    MediaStore.Images.Media.DATE_TAKEN        // Ordering
            );
            Log.i("ListingImages", " query count=" + cur.getCount());
            if (cur.moveToFirst()) {
                int bucketColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID);
                int PathColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);
                int DateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                long currenttime = System.currentTimeMillis();
                cur.moveToLast();
                int i = 0;
                int count = cur.getCount();
                DialogImagesModel mOdle;
                do {
                    try {
                        // Get the field values
                        if (Long.parseLong(cur.getString(DateColumn)) <= (currenttime - (24 * 60 * 60 * 1000) * i) || i == 0) {
                            mOdle = new DialogImagesModel();
                            mOdle.setOrigId(Long.valueOf(cur.getString(bucketColumn)));
                            mOdle.setImagePathl(cur.getString(PathColumn));
                            mGallerImageLst.add(mOdle);
                            i++;
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(Long.parseLong(cur.getString(DateColumn)));
                            Log.e(this.getLocalClassName(), c.getTime() + "");
                        }

                 /*   mUri2 = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e(TAG, mUri2 + "\n" + "");*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while ((count > constant.MAX_GAL_IMAGE_COUNT) ? cur.moveToPrevious() && (i < constant.MAX_GAL_IMAGE_COUNT) : cur.moveToPrevious());
            }


            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo));
            View mView = getLayoutInflater().inflate(R.layout.dialog_gallery, null);
            mBuilder.setView(mView);

            GridView mGalGridview = (GridView) mView.findViewById(R.id.dialog_gridview);
            TextView mGalTitlevw = (TextView) mView.findViewById(R.id.dialog_gal_title);
            TextView mGalTitleTextvw = (TextView) mView.findViewById(R.id.dialog_gal_title_txt);
            TextView mGalleryTvw = (TextView) mView.findViewById(R.id.dialog_gallery_tv);
            ImageView mCloseImgVw = (ImageView) mView.findViewById(R.id.dialog_gal_close);


            FontsUtil.setExistenceLight(this, mGalTitlevw);
            FontsUtil.setExistenceLight(this, mGalTitleTextvw);
            FontsUtil.setExistenceLight(this, mGalleryTvw);

            mCloseImgVw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mGalleryDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mGalleryTvw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callGallery();
                }
            });

            mGalGridview.setAdapter(new DMDialogGridAdapter(DMHomeActivity.this, mGallerImageLst));
            mGalGridview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mGalleryDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //   constant.gTakenBitmap = BitmapFactory.decodeFile();
                    goToCropActivity(mGallerImageLst.get(position).getImagePathl());
                }
            });

            mGalleryDialog = mBuilder.create();
            mGalleryDialog.show();
        }
        AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, true, DMHomeActivity.this);
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
                    if(list!=null){
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
}
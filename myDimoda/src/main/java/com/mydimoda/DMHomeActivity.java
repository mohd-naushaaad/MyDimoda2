package com.mydimoda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
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

        final ParseUser user = ParseUser.getCurrentUser();
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
     //   if (!AppUtils.getDefaults(this, constant.PREF_IS_GALRY_DIALOG_SHOWN, false)) {
            showGalleryDialog();
       //mayur test }
        if (user.getInt(constant.USER_MAX_COUNT) >= 10) {
            constant.maxCount = user.getInt(constant.USER_MAX_COUNT);
        } else {
            if (user.getInt(constant.USER_MAX_COUNT) < 5) {

                user.put(constant.USER_MAX_COUNT, 5); // it needs to be initilised as 5 which is the minimum free style points
                user.saveInBackground();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("License");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {

                        if (objects.size() > 0) {

                            ParseObject object = objects.get(0);
                            constant.maxCount = object.getInt("MaxValue");
                        }
                    } else {
                        Toast.makeText(DMHomeActivity.this, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
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

                cur.moveToLast();
                int i = 0;
                int count = cur.getCount();
                DialogImagesModel mOdle;
                do {
                    try {
                        // Get the field values
                        mOdle = new DialogImagesModel();
                        mOdle.setOrigId(Long.valueOf(cur.getString(bucketColumn)));
                        mOdle.setImagePathl(cur.getString(PathColumn));
                 /*   mUri2 = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e(TAG, mUri2 + "\n" + "");*/
                        mGallerImageLst.add(mOdle);
                        i++;
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
            FontsUtil.setExistenceLight(this, mGalTitlevw);
            FontsUtil.setExistenceLight(this, mGalTitleTextvw);
            mGalleryDialog = mBuilder.create();

            mGalGridview.setAdapter(new DMDialogGridAdapter(DMHomeActivity.this, mGallerImageLst));
            mGalGridview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mGalleryDialog.dismiss();
                    AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, true, DMHomeActivity.this);
                    constant.gTakenBitmap = BitmapFactory.decodeFile(mGallerImageLst.get(position).getImagePathl());
                    Intent intent = new Intent(mContext, DMCaptureActivity.class);
                    intent.putExtra("type", constant.EMPTY_TYPE);
                    intent.putExtra("isCapture", false);
                    intent.putExtra(constant.FRM_DIALG_KEY, true);
                    startActivity(intent);
                }
            });
            mGalleryDialog.show();
        }

    }
}

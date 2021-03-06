package com.mydimoda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.FragmentActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.camera.CameraActivity;
import com.mydimoda.camera.CropActivity;
import com.mydimoda.image.CustExifUtils;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DMCaptureActivity extends FragmentActivity implements OnClickListener,
        AnimationListener {

    private static final int ANIM_NONE = 0;
    private static final int ANIM_SLIDE_FORMAL = 1;

    private static final int NULL_ID = 99;
    private static final int FORMAL_ID = 100;
    private static final int CASUAL_ID = 101;
    private static final int CLOSE_ID = 102;

    private int anim_state = ANIM_NONE;
    private int sel_formal = NULL_ID;

    private boolean bForcusedMenu = false;

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    RelativeLayout vFormalLayout;
    LinearLayout vBtnLayout;
    ImageButton vBtnCapture;
    ImageView vCaptureImg, vFormal;
    TextView vTxtFormal;
    Button vBtnCancle, vBtnHangUp;

    ProgressDialog vProgress;
    Bitmap mBitmap;
    String mPattern, mType, mColorVal;

    FrameLayout mMaskLayout;
    FrameLayout mMaskLayout1;
    LinearLayout mMenuLayout;

    boolean mIsFormal = false, mIsCapture = false, mIsFrmDialog = false, mIsFrmDialogProcess = false;
    int RESULT_CAMERA = 1;
    int RESULT_GALLERY = 2;
    int RESULT_CROP = 3;

    String fileName;
    String mCurrentPhotoPath;
    Uri imageUri;


    AlertDialog mCatDialog;
    AlertDialog mTypeDialog;

    @BindView(R.id.act_capture_scrn_coach)
    ImageView mCoachMarkScreenIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);
        // / layout
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);
        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        vFormalLayout = (RelativeLayout) findViewById(R.id.formal_layout);
        vBtnLayout = (LinearLayout) findViewById(R.id.button_layout);
        vBtnCapture = (ImageButton) findViewById(R.id.capture_btn);
        vCaptureImg = (ImageView) findViewById(R.id.capture_image);
        vFormal = (ImageView) findViewById(R.id.formal_image);
        vTxtFormal = (TextView) findViewById(R.id.formal_text);
        FontsUtil.setExistenceLight(this, vTxtFormal);

        vBtnCancle = (Button) findViewById(R.id.cancel_btn);
        FontsUtil.setExistenceLight(this, vBtnCancle);

        vBtnHangUp = (Button) findViewById(R.id.hangup_btn);
        FontsUtil.setExistenceLight(this, vBtnHangUp);


        mMaskLayout = (FrameLayout) findViewById(R.id.maskLayer);
        mMaskLayout1 = (FrameLayout) findViewById(R.id.maskLayout1);
        mMenuLayout = (LinearLayout) findViewById(R.id.farmalLayer);

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
                constant.selectMenuItem(DMCaptureActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(constant.selectBackPress){
                    constant.selectBackPress = false;
                    onBackPressed();
                }else{
                goToCaptureOptionsAct();
                }
            }
        });

        vBtnCapture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mIsCapture)
                    callCamera();
            }
        });

        vBtnCancle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(constant.selectBackPress){
                    constant.selectBackPress = false;
                    onBackPressed();
                }else{
                    goToCaptureOptionsAct();
                }
            }
        });

        vBtnHangUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (bForcusedMenu)
                    return;

                showFormalMenu(true);

            }
        });

        vFormal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mIsFormal) {
                    mIsFormal = false;
                    vFormal.setBackgroundResource(R.drawable.remember_bg);
                } else {
                    mIsFormal = true;
                    vFormal.setBackgroundResource(R.drawable.remember_checked_bg);
                }
            }
        });

        showShowcaseView();
        initOnCreate();
    }

    void goToCaptureOptionsAct() {
        Intent cameraIntent = new Intent(DMCaptureActivity.this, DMCaptureOptionActivity.class);
        cameraIntent.putExtras(getIntent());
        startActivity(cameraIntent);
        finish();
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.formalBtn:
                mIsFormal = true;
                sel_formal = FORMAL_ID;
                showFormalMenu(false);
                break;
            case R.id.casualBtn:
                mIsFormal = false;
                sel_formal = CASUAL_ID;
                showFormalMenu(false);
                break;
            case R.id.cancelBtn:
                sel_formal = CLOSE_ID;
                showFormalMenu(false);
                break;

        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

        switch (anim_state) {
            case ANIM_SLIDE_FORMAL:
                mMenuLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        switch (anim_state) {
            case ANIM_SLIDE_FORMAL:
                if (!bForcusedMenu) {
                    mMenuLayout.setVisibility(View.INVISIBLE);
                    mMenuLayout.clearAnimation();

                    mMaskLayout1.setAlpha(1.0f);
                    mMaskLayout.setVisibility(View.GONE);

                    if (sel_formal != CLOSE_ID)
                        try {
                            savePhotoToParse();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Opps Something went wrong", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    sel_formal = NULL_ID;
                }
                break;
        }

        anim_state = ANIM_NONE;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    public void initOnCreate() {

        //   mTypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.background_floating_material_dark), PorterDuff.Mode.SRC_ATOP);
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        mIsCapture = intent.getBooleanExtra("isCapture", true);
        mIsFrmDialog = intent.getBooleanExtra(constant.FRM_DIALG_KEY, false);
        mIsFrmDialogProcess = intent.getBooleanExtra(constant.FRM_DIALG_PROCESS_KEY, false);
        if (mIsCapture) {
            hideLayout();
            callCamera();
        } else {
            if (constant.gTakenBitmap != null && !mIsFrmDialog) {
                mBitmap = constant.gTakenBitmap;
                vCaptureImg.setImageBitmap(mBitmap);
            } else {
                goCropActivity();
            }
        }
    }

    public void initOnResume() {
        showMenu();
        // setViewWithFont();
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont() {
    // vTxtBack.setTypeface(constant.fontface);
    // vBtnCancle.setTypeface(constant.fontface);
    // vBtnHangUp.setTypeface(constant.fontface);
    // vTxtFormal.setTypeface(constant.fontface);
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

    // / -------------------------------------- show, hide, formal layout,
    // button, layout ---------------
    public void showLayout() {
        vBtnLayout.setVisibility(View.VISIBLE);
    }

    public void hideLayout() {
        vBtnLayout.setVisibility(View.GONE);
    }

    // / --------------------------------------- call camera and gallery
    // --------------------------------
    public void callCamera() {
        Intent cameraIntent = new Intent(DMCaptureActivity.this,
                CameraActivity.class);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                imageUri = Uri.fromFile(photoFile);
                cameraIntent.putExtra("photoname", imageUri.getPath());
                startActivityForResult(cameraIntent, RESULT_CAMERA);
            }
        }
    }

    private void showFormalMenu(boolean bShow) {
        if (anim_state != ANIM_NONE)
            return;

        if (bShow) {
            mMaskLayout1.setAlpha(0.5f);
            mMaskLayout.setVisibility(View.VISIBLE);
            mMenuLayout.setVisibility(View.INVISIBLE);
            bForcusedMenu = true;
        } else {
            bForcusedMenu = false;
        }

        anim_state = ANIM_SLIDE_FORMAL;
        animateMenuVertical(mMenuLayout, bShow);
    }

    private void animateMenuVertical(LinearLayout menu, boolean bShow) {

        int selfHeight = menu.getMeasuredHeight();

        TranslateAnimation slideAnim = null;

        if (bShow)
            slideAnim = new TranslateAnimation(0, 0, selfHeight, 0);
        else
            slideAnim = new TranslateAnimation(0, 0, 0, selfHeight);

        slideAnim.setDuration(300);
        slideAnim.setFillAfter(true);
        slideAnim.setAnimationListener(this);
        menu.startAnimation(slideAnim);
    }

    private File createImageFile() throws IOException {

        String imageFileName = "PNG_temp_";
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();

        File image = File.createTempFile(imageFileName, /* prefix */
                ".png", /* suffix */
                storageDir /* directory */
        );
        fileName = image.getAbsolutePath();
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        return image;
    }

    public void callGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_GALLERY);
    }

    public Bitmap readFile(String fileName) {
        Bitmap bitmap;
        try {
            FileInputStream fis = this.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (Exception e) {
            bitmap = null;
        }
        return bitmap;
    }

    // / --------------------------------------- get bitmap
    // --------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        if (requestCode == RESULT_CAMERA && resultCode == RESULT_OK) {

            try {
                new CustExifUtils().getFixedImage(fileName, new CustExifUtils.CustExifCallBack() {
                    @Override
                    public void onRotationFixed(Bitmap bitmap) {
                        mBitmap = bitmap;
                        if (mBitmap != null) {
                            boolean isFront = false;

                            Bitmap bm = Bitmap.createScaledBitmap(mBitmap,
                                    mBitmap.getWidth() / 5, mBitmap.getHeight() / 5, false);
                            int wid = bm.getWidth();
                            int hei = bm.getHeight();


                            if (wid > 700)
                                isFront = false;
                            else
                                isFront = true;

                            if (wid > hei) {
                                Bitmap bmp = getRotateBitmap(bm, isFront);
                                mBitmap = bmp;
                                vCaptureImg.setImageBitmap(mBitmap);
                                //bmp.recycle();
                            }

                            wid = mBitmap.getWidth();
                            hei = mBitmap.getHeight();


                            if (wid > hei) {
                                Bitmap bmp = getRotateBitmap(bm, isFront);
                                mBitmap = bmp;
                                // bmp.recycle();
                            }
                            goCropActivity();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mBitmap = BitmapFactory.decodeFile(picturePath);

            goCropActivity();
        } else if (requestCode == RESULT_CROP && resultCode == RESULT_OK) {
            goProcessActivity();
        }
    }

    // / ---------------------------------------------- rotate bitmap
    // --------------------------
    public Bitmap getRotateBitmap(Bitmap bitmap, boolean isFront) {
        Bitmap rotateBitmap = null;
        Matrix mat = new Matrix();

        if (isFront) {
            mat.postRotate(90);
        } else {
            mat.postRotate(90);
        }

        try {
            rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), mat, false);
        } catch (Exception e) {
            Log.e("Photo Error", e.toString());
        }
        return rotateBitmap;
    }

    // / ------------------------------------- call function after 1 second
    // -----------------------
    public void goCropActivity() {
        if (!mIsFrmDialog) {
            constant.gTakenBitmap = mBitmap;
            if (mBitmap != null) {
                Intent intent = new Intent(this, CropActivity.class);
                intent.putExtra(constant.FRM_DIALG_KEY, false);
                startActivityForResult(intent, RESULT_CROP);
            }
        } else {
            Intent intent = new Intent(this, CropActivity.class);
            intent.putExtra(constant.FRM_DIALG_KEY, true);
            startActivity(intent);
            finish();
        }
    }

    public void goProcessActivity() {
        if (mBitmap != null) {
            constant.gTakenBitmap = mBitmap;
            Intent intent = new Intent(this, DMProcessActivity.class);
            intent.putExtra("purchase", false);
            intent.putExtra("type", mType);
            intent.putExtra(constant.FRM_DIALG_KEY, mIsFrmDialog);
            startActivity(intent);
            finish();
        }
    }

    // / ------------------------------------- save photo to parse.com database
    // -----------------------
    public void savePhotoToParse() {
        if (mBitmap != null) {
            byte[] byteArray = getByteArray();
            if (byteArray != null) {
                String category = "";
                if (mIsFormal)
                    category = "formal";
                else
                    category = "causal";

                // Make parse object
                ParseUser user = ParseUser.getCurrentUser();
                ParseFile file = new ParseFile("image", byteArray, "image/jpeg");

                ParseObject object = new ParseObject("Clothes");
                object.put("User", user);
                object.put("Type", mType);
                object.put("Color", mColorVal);
                object.put("Pattern", mPattern);
                object.put("Category", category);
                object.put("ImageContent", file);

                constant.showProgress(this, "Saving data");

                object.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        // TODO Auto-generated method stub
                        if (e == null)
                            Toast.makeText(DMCaptureActivity.this, "Saved",
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(DMCaptureActivity.this,
                                    AppUtils.asUpperCaseFirstChar(e.getMessage()), Toast.LENGTH_LONG).show();

                        if (!constant.gIsCloset)
                            setIsCloset();

//                        constant.hideProgress();

                        getClothFP();


                        //finish();
                    }
                });
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

    public byte[] getByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray = null;
        try {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byteArray = stream.toByteArray();
            stream.flush();
            stream.close();
         //   mBitmap.recycle();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return byteArray;
    }

    // / ------------------------------------------- show dialog
    // --------------------------------
    public void showMessage(String message) {
        showLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (constant.gMessage != null) {
            showMessage(constant.gMessage);

            mColorVal = constant.gColorVal;
            mPattern = constant.gPatternVal;

            constant.gMessage = null;
            constant.gColorVal = null;
            constant.gPatternVal = null;
        }

        initOnResume();
    }

    List<ParseObject> mClothList = new ArrayList<>();

    /**
     * mayur: to get the count of cloths already in closet to increase style me count accordingly
     */
    public void getClothFP() {
        //   constant.showProgress(this, "Loading...");
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Clothes");
        query.whereEqualTo("Type", mType);
        query.whereEqualTo("User", user);
        query.orderByDescending("createdAt");
        query.setLimit(constant.RESULT_SIZE);//mayur increased limit to 1000


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {

                if (e == null) {
                    mClothList = clothList;
                    stylePointProcessor(mClothList.size());

                } else {

                    Toast.makeText(DMCaptureActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
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
    public void stylePointProcessor(int items) {
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
                        constant.hideProgress();
                        if (!(user.getBoolean(constant.RATED_APP) || user.getBoolean("Buy") ||
                                SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase("1"))) {
                            Toast.makeText(DMCaptureActivity.this, "5 Style me options awarded",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(DMCaptureActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
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
                            Toast.makeText(DMCaptureActivity.this, getString(R.string.style_me_awarded_1, 1),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(DMCaptureActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        constant.hideProgress();
        finish();
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_CAPTURE_ACT_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_CAPTURE_ACT_SHOWN, true);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
          //   mBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

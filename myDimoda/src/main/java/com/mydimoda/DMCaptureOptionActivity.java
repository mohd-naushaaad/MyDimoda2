package com.mydimoda;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.camera.CropActivity;
import com.mydimoda.interfaces.DialogItemClickListener;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Keyur
 *         Created another screen to choose from camera or gallery.
 */
public class DMCaptureOptionActivity extends Activity {

    public static final int REQUEST_IMG_PICK1 = 21;
    public static final int REQUEST_IMG_CROP1 = 22;
    int RESULT_GALLERY = 2;
    int RESULT_CROP = 3;
    Context mContext;
    //  menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView typeText;
    TextView vTxtBack;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    TextView txtDescription;
    TextView txtCamera, txtPhotos;
    LinearLayout lltCamrea, lltPhotos;
    DialogInterface mDlgInterface;
    Bitmap mBitmap;
    String mType;
    boolean mFromMain;
    @Bind(R.id.act_cap_optn_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_option);
        ButterKnife.bind(this);

        mContext = this;
        // / layout
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);
        typeText = (TextView) findViewById(R.id.typeText);
        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);
        FontsUtil.setExistenceLight(this, typeText);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtCamera = (TextView) findViewById(R.id.txtCamera);
        txtPhotos = (TextView) findViewById(R.id.txtPhotos);
        FontsUtil.setExistenceLight(this, txtDescription);
        FontsUtil.setExistenceLight(this, txtCamera);
        FontsUtil.setExistenceLight(this, txtPhotos);

        lltCamrea = (LinearLayout) findViewById(R.id.lltCamera);
        lltPhotos = (LinearLayout) findViewById(R.id.lltPhotos);

        vBtnMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                slideMenu();
            }
        });

        vMenuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                vDrawerLayout.closeDrawer(vMenuLayout);
                if (position != 2)
                    constant.selectMenuItem(DMCaptureOptionActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        lltCamrea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goCaptureActivity(mType);
            }
        });

        lltPhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //callGallery();
                showGalleryDialog();
            }
        });
showShowcaseView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        showMenu();
        Intent in = getIntent();
        mFromMain = in.getBooleanExtra("FromMain", true);
        mType = in.getStringExtra("type");
    }

    // / --------------------------------- show menu list
    public void showMenu() {
        vMenuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
    }

    // / --------------------------------- slide menu section
    public void slideMenu() {
        if (vDrawerLayout.isDrawerOpen(vMenuLayout)) {
            vDrawerLayout.closeDrawer(vMenuLayout);
        } else {
            vDrawerLayout.openDrawer(vMenuLayout);
        }
    }

    public void goCaptureActivity(String type) {
        if (mFromMain == true) {
            Intent intent = new Intent(mContext, DMCaptureActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("isCapture", true);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, DMHelpActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
        finish();
    }

    public void callGallery() {
        /*Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_GALLERY);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Complete action using"), RESULT_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = result.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                mBitmap = BitmapFactory.decodeFile(picturePath);
                // goCropActivity();
                goToCropActivity(picturePath);
            } else {
                Toast.makeText(this, "Opps! Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == RESULT_CROP && resultCode == RESULT_OK) {
            if (AppUtils.getDialogImgSelectLst().size() > 0) {
                goToCropActivity(AppUtils.getDialogImgSelectLst().get(0));
            } else {
                finish();
            }
            //goProcessActivity();
        } else if (requestCode == RESULT_CROP && resultCode == 0) {
            showGalleryDialog();

        }

    }

    public void goCropActivity() {
        if (mBitmap != null) {
            constant.gTakenBitmap = mBitmap;
            Intent intent = new Intent(this, CropActivity.class);
            startActivityForResult(intent, RESULT_CROP);
        }
    }

    public void goProcessActivity() {
        if (mBitmap != null) {
            constant.gTakenBitmap = mBitmap;
            Intent intent = new Intent(this, DMProcessActivity.class);
            intent.putExtra("purchase", false);
            intent.putExtra("type", mType);
            startActivity(intent);
            finish();
        }
    }

    public void goToCropActivity(String picPath) {
        constant.gTakenBitmap = BitmapFactory.decodeFile(picPath);
        Intent intent = new Intent(mContext, CropActivity.class);
        intent.putExtra("type", mType);
        intent.putExtra("isCapture", false);
        intent.putExtra(constant.FRM_DIALG_KEY, true);
        startActivityForResult(intent, RESULT_CROP);


    }

    public void showGalleryDialog() {
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
            }, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_CAMERA_OPTION_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_CAMERA_OPTION_SHOWN, true);
                }
            });
        }
    }
}

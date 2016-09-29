package com.mydimoda;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
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
import com.mydimoda.async.MyAsyncTask;
import com.mydimoda.async.ServerResponse;
import com.mydimoda.camera.CropActivity;
import com.mydimoda.object.DMProductObject;
import com.mydimoda.object.DMReviewObject;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DMDetailActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    // / layout
    Button vBtnMyStyle, vBtnBuyStyle;
    TextView vTxtName, vTxtPrice;
    ImageView vImgStar1, vImgStar2, vImgStar3, vImgStar4, vImgStar5,
            vImgProduct;
    LinearLayout vLytStar;

    int mType = 0;
    DMProductObject mProduct;
    DMReviewObject mReview;
    Bitmap mBitmap;
    int RESULT_CROP = 3;

    @Bind(R.id.act_detail_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;

    @Bind(R.id.act_detail_hng_up_btn)
    Button mHangupBtn;

    private boolean isHangup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        FontsUtil.setExistenceLight(this, mHangupBtn);
        // / menu
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);
        vTxtTitle = (TextView) findViewById(R.id.title_view);
        FontsUtil.setExistenceLight(this, vTxtTitle);

        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        // / layout
        vBtnMyStyle = (Button) findViewById(R.id.my_style_btn);
        FontsUtil.setExistenceLight(this, vBtnMyStyle);

        vBtnBuyStyle = (Button) findViewById(R.id.buy_style_btn);
        FontsUtil.setExistenceLight(this, vBtnBuyStyle);

        vTxtName = (TextView) findViewById(R.id.product_title);
        FontsUtil.setExistenceLight(this, vTxtName);

        vTxtPrice = (TextView) findViewById(R.id.price_txt);
        FontsUtil.setExistenceLight(this, vTxtPrice);

        vImgStar1 = (ImageView) findViewById(R.id.star_img1);
        vImgStar2 = (ImageView) findViewById(R.id.star_img2);
        vImgStar3 = (ImageView) findViewById(R.id.star_img3);
        vImgStar4 = (ImageView) findViewById(R.id.star_img4);
        vImgStar5 = (ImageView) findViewById(R.id.star_img5);
        vImgProduct = (ImageView) findViewById(R.id.product_img);
        vLytStar = (LinearLayout) findViewById(R.id.star_layout);

        init();
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
                constant.selectMenuItem(DMDetailActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        vBtnMyStyle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                goCropActivity(false);
            }
        });

        vBtnBuyStyle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mProduct.DetailPageURL));
                startActivity(i);
            }
        });

        mHangupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHangup = true;
                goCropActivity(isHangup);
            }
        });

        showShowcaseView();
    }

    public void init() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        mProduct = (DMProductObject) intent.getSerializableExtra("product");

        showMenu();
//		setViewWithFont();
        showProductInfo();
    }

    // / --------------------------------- set font
    // -------------------------------------
//	public void setViewWithFont() {
//		vTxtTitle.setTypeface(constant.fontface);
//		vTxtBack.setTypeface(constant.fontface);
//		vTxtPrice.setTypeface(constant.fontface);
//		vTxtName.setTypeface(constant.fontface);
//		vBtnBuyStyle.setTypeface(constant.fontface);
//		vBtnMyStyle.setTypeface(constant.fontface);
//	}

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

    // / --------------------------------- show product data
    // ---------------------------
    public void showProductInfo() {
        vTxtName.setText(mProduct.Title);
        vTxtPrice.setText("Price : " + mProduct.Price);
        vLytStar.setVisibility(View.INVISIBLE);

        showProductImage();
        getProductInfoFS();
    }

    // / --------------------------------- show product image
    // ---------------------------
    public void showProductImage() {
        ParseApplication.getInstance().mImageLoader.displayImage(
                mProduct.LargeImage.url, vImgProduct,
                ParseApplication.getInstance().options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        mBitmap = loadedImage;
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view,
                                                 int current, int total) {
                    }
                });
        System.out.println("Pruduct Image in DetailAct --> " + mProduct.LargeImage.url);
    }

    // / --------------------------------- get Product Info from server
    // ---------------------
    public void getProductInfoFS() {
        String baseUrl = String.format("%s/api/getProductInfo?ItemId=%s",
                constant.gPrefUrl, mProduct.ASIN);

        constant.showProgress(this, "Loading Review...");
        new MyAsyncTask(new ServerResponse() {

            @Override
            public void getResponse(JSONObject data) {
                // TODO Auto-generated method stub
                constant.hideProgress();
                if (data != null) {
                    mReview = new DMReviewObject(data);
                } else {
                    Toast.makeText(DMDetailActivity.this, "Network Error",
                            Toast.LENGTH_LONG).show();
                }

                showReview();
            }
        }, baseUrl, true).execute();

    }

    // / ---------------------------------- show review of product
    // ---------------------------
    public void showReview() {
        if (mReview != null && mReview.mark != null && !mReview.mark.equals("")) {
            vLytStar.setVisibility(View.VISIBLE);
            String[] arr = mReview.mark.split("\\.");

            if (arr[0].equals("0")) {
                vImgStar1.setImageResource(getImgId(arr[1]));
                vImgStar2.setImageResource(R.drawable.img_star_40);
                vImgStar3.setImageResource(R.drawable.img_star_40);
                vImgStar4.setImageResource(R.drawable.img_star_40);
                vImgStar5.setImageResource(R.drawable.img_star_40);
            } else if (arr[0].equals("1")) {
                vImgStar1.setImageResource(R.drawable.img_star);
                vImgStar2.setImageResource(getImgId(arr[1]));
                vImgStar3.setImageResource(R.drawable.img_star_40);
                vImgStar4.setImageResource(R.drawable.img_star_40);
                vImgStar5.setImageResource(R.drawable.img_star_40);
            } else if (arr[0].equals("2")) {
                vImgStar1.setImageResource(R.drawable.img_star);
                vImgStar2.setImageResource(R.drawable.img_star);
                vImgStar3.setImageResource(getImgId(arr[1]));
                vImgStar4.setImageResource(R.drawable.img_star_40);
                vImgStar5.setImageResource(R.drawable.img_star_40);
            } else if (arr[0].equals("3")) {
                vImgStar1.setImageResource(R.drawable.img_star);
                vImgStar2.setImageResource(R.drawable.img_star);
                vImgStar3.setImageResource(R.drawable.img_star);
                vImgStar4.setImageResource(getImgId(arr[1]));
                vImgStar5.setImageResource(R.drawable.img_star_40);
            } else if (arr[0].equals("4")) {
                vImgStar1.setImageResource(R.drawable.img_star);
                vImgStar2.setImageResource(R.drawable.img_star);
                vImgStar3.setImageResource(R.drawable.img_star);
                vImgStar4.setImageResource(R.drawable.img_star);
                vImgStar5.setImageResource(getImgId(arr[1]));
            } else if (arr[0].equals("5")) {
                vImgStar1.setImageResource(R.drawable.img_star);
                vImgStar2.setImageResource(R.drawable.img_star);
                vImgStar3.setImageResource(R.drawable.img_star);
                vImgStar4.setImageResource(R.drawable.img_star);
                vImgStar5.setImageResource(R.drawable.img_star);
            }
        }
    }

    public int getImgId(String score) {
        if (score != null && !score.equals("")) {
            score = score.substring(0, 1);
            if (score.equals("1"))
                return R.drawable.img_star_41;
            else if (score.equals("2"))
                return R.drawable.img_star_42;
            else if (score.equals("3"))
                return R.drawable.img_star_43;
            else if (score.equals("4"))
                return R.drawable.img_star_44;
            else if (score.equals("5"))
                return R.drawable.img_star_45;
            else if (score.equals("6"))
                return R.drawable.img_star_46;
            else if (score.equals("7"))
                return R.drawable.img_star_47;
            else if (score.equals("8"))
                return R.drawable.img_star_48;
            else if (score.equals("9"))
                return R.drawable.img_star_49;
            else
                return R.drawable.img_star_40;
        }

        return R.drawable.img_star_40;
    }

    // / ----------------------------------- go Crop Activity
    // ---------------------------------------
    public void goCropActivity(boolean isHangup) {

        if (mBitmap != null) {
            constant.gTakenBitmap = mBitmap;
            Intent intent = new Intent(this, CropActivity.class);
            intent.putExtra(constant.FRM_DETAIL_FOR_HANGUP_KEY, isHangup);
            startActivityForResult(intent, RESULT_CROP);
        }
    }

    @SuppressLint("DefaultLocale")
    public void goProcessActivity(boolean isForHangup) {
        if (mBitmap != null) {
            constant.gTakenBitmap = mBitmap;

            String title = mProduct.Title.toLowerCase();
            String type = "";
            if (title.contains("shirt"))
                type = "shirt";
            else if (title.contains("trousers"))
                type = "trousers";
            else if (title.contains("jacket"))
                type = "jacket";
            else if (title.contains("tie"))
                type = "tie";
            else if (title.contains("suit"))
                type = "suit";
            else
                type = "shirt";

            Intent intent = new Intent(this, DMProcessActivity.class);
            intent.putExtra("purchase", true);
            intent.putExtra("type", type);
            intent.putExtra("closet", mType);
            intent.putExtra(constant.FRM_DETAIL_FOR_HANGUP_KEY, isForHangup);
            startActivity(intent);
        }
    }

    // / ---------------------------------- after crop, go to process activity
    // ------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            super.onBackPressed();
            return;
        }

        if (requestCode == RESULT_CROP && resultCode == RESULT_OK) {
            goProcessActivity(data.getBooleanExtra(constant.FRM_DETAIL_FOR_HANGUP_KEY, false));
        }
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_DETAIL_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_DETAIL_SHOWN, true);
                }
            });
        }
    }
}

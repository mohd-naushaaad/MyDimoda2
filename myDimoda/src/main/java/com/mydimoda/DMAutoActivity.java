package com.mydimoda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.adapter.DMProductAdapter;
import com.mydimoda.async.MyAsyncTask;
import com.mydimoda.async.ServerResponse;
import com.mydimoda.object.DMProductObject;
import com.mydimoda.widget.cropper.util.FontsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DMAutoActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    // / layout
    PullToRefreshGridView vGrdProduct;
    String mFrom, mMaxPrice;
    int mType, mPageIndex = 1, mTotalResult, mTotalPage, mItemPageOffset = 0;
    boolean mIsRefresh = false;

    List<DMProductObject> mProductList;
    DMProductAdapter mAdapter;
    Button m_filterbtn;

    TextView tv_title;

    // after new brands
    int mAWSOffset = 1;
    int mShopStyleOffset = 1;
    int mAsosOffset = 0;

    String mShopName = "productloop";
    String mSortType = "relevance";
    @BindView(R.id.act_auto_coach_mrk_iv)
    ImageView mCoachMarkScreenIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        ButterKnife.bind(this);

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
        m_filterbtn = (Button) findViewById(R.id.filterbtn);
        tv_title = (TextView) findViewById(R.id.tv_title);
        FontsUtil.setExistenceLight(this, tv_title);

        // / layout
        vGrdProduct = (PullToRefreshGridView) findViewById(R.id.product_grid);

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
                constant.selectMenuItem(DMAutoActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        m_filterbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent m_intent = new Intent(DMAutoActivity.this,
                        MainActivity.class);
                m_intent.putExtra("type", mType);
                System.out.println("Main" + mType);
                startActivity(m_intent);
            }
        });

        vGrdProduct.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(DMAutoActivity.this,
                        DMDetailActivity.class);
                intent.putExtra("type", mType);

                System.out.println("type ->" + mType);


                intent.putExtra("product", mProductList.get(position));
                startActivity(intent);
            }
        });

        vGrdProduct.setOnRefreshListener(new OnRefreshListener2<GridView>() {

            @SuppressWarnings("deprecation")
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                vGrdProduct.setLastUpdatedLabel("Last Updated: "
                        + constant.getCurrentHour());
                mIsRefresh = true;
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                vGrdProduct.setLastUpdatedLabel("Last Updated: "
                        + constant.getCurrentHour());
                mIsRefresh = true;

                if (mPageIndex <= mTotalPage) {
                    mPageIndex++;
                    // after brands addded
                    mAWSOffset = mPageIndex;
                    mShopStyleOffset += 10;
                    mAsosOffset += 10;

                    if (!TextUtils.isEmpty(AppUtils.brand)) {

                        AppUtils.yes = "false";
                        getClosetFS_Shared();
                    } else {
                        AppUtils.yes = "true";
                        getClosetFS();
                    }
                }else{
                    vGrdProduct.onRefreshComplete();
                }
            }

        });

    }

    public void init() {
        showMenu();
        // setViewWithFont();

        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        mMaxPrice = intent.getStringExtra("price");
        mType = intent.getIntExtra("closet", 0);
        mShopName = TextUtils.isEmpty(intent.getStringExtra(constant.SHOP_NAME)) ? mShopName : intent.getStringExtra(constant.SHOP_NAME);
        mSortType = TextUtils.isEmpty(intent.getStringExtra(constant.SORT_BY_KEY)) ? mSortType : intent.getStringExtra(constant.SORT_BY_KEY);
        System.out.println("from_Selected " + mFrom);
        System.out.println("closet_Selected " + mType);
        System.out.println("price_Selected " + mMaxPrice);
        System.out.println("Shop name " + mShopName);


        AppUtils.putPref("type", String.valueOf(mType), DMAutoActivity.this);
        AppUtils.putPref("price", String.valueOf(mMaxPrice),
                DMAutoActivity.this);
        if (mFrom.equals("exact")) {
            vTxtTitle.setText("Perfect Match");
            showShowcaseViewExact();
        } else {
            showShowcaseViewLucky();
            vTxtTitle.setText("I Feel Lucky");
            m_filterbtn.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(AppUtils.brand)) {
            getClosetFS_Shared();
        } else {
            getClosetFS();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

		/*
         * if(AppUtils.yes.equalsIgnoreCase("true")) {
		 *
		 * } else if((AppUtils.yes.equalsIgnoreCase("false"))) {
		 *
		 * if(AppUtils.brand == null) {
		 *
		 *
		 * } else if(AppUtils.brand != null) {
		 *
		 * if(mProductList != null) { mProductList.clear(); }
		 * getClosetFS_Shared(); } } else { }
		 */
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont() {
    // vTxtTitle.setTypeface(constant.fontface);
    // vTxtBack.setTypeface(constant.fontface);
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

    // / -------------------------------- show product list to GridView
    // -----------------------------------
    public void showProductGrid(int size) {
        if (mAdapter == null) {
            System.out.println("Size" + size);
            if (mProductList.size() <= 0) {
                tv_title.setVisibility(View.VISIBLE);
            }
            mAdapter = new DMProductAdapter(this, mProductList);
            vGrdProduct.setAdapter(mAdapter);

        } else {
            if (mProductList.size() <= 0) {
                tv_title.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    // / -------------------------------- get closet from server
    // ------------------------------
    @SuppressLint("DefaultLocale")
    public void getClosetFS() {
        String baseUrl = "";
        AppUtils.yes = "true";


        if (mFrom.equals("exact")) {
            if (mType == constant.NONE) {
                int type = mPageIndex % 5;

                if (mMaxPrice.equals("0")) {
                    baseUrl = String.format(
                            // "%s/api/getProductList?Category=%s&ItemPage=%d",
                            //      "%s/api/productloop?Category=%s&ItemPage=%d&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d",
                            "%s/api/%s?Category=%s&ItemPage=%d&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",

                            constant.gPrefUrl, mShopName, AppUtils.categoryArr[type], mPageIndex, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);
                    System.out.println("Called without brand");
                    // AppUtils.brand = categoryArr[type];
                    AppUtils.putPref("brand", AppUtils.categoryArr[type], DMAutoActivity.this);

                } else {
                    baseUrl = String
                            .format(
                                    //"%s/api/getProductList?Category=%s&MaximumPrice=%s&ItemPage=%d",
                                    "%s/api/%s?Category=%s&MaximumPrice=%s&ItemPage=%d&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                                    constant.gPrefUrl, mShopName, AppUtils.categoryArr[type],
                                    mMaxPrice, mPageIndex, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);
                    //  AppUtils.brand = categoryArr[type];
                    AppUtils.putPref("brand", AppUtils.categoryArr[type], DMAutoActivity.this);

                }

            } else {
                if (mMaxPrice.equals("0")) {
                    baseUrl = String.format(
                            //"%s/api/getProductList?Category=%s&ItemPage=%d",
                            "%s/api/%s?Category=%s&ItemPage=%d&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                            constant.gPrefUrl, mShopName, AppUtils.categoryArr[mType], mPageIndex, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);
                    //   AppUtils.brand = categoryArr[mType];
                    AppUtils.putPref("brand", AppUtils.categoryArr[mType], DMAutoActivity.this);
                } else {
                    baseUrl = String.format(
                            //"%s/api/getProductList?Category=%s&MaximumPrice=%s&ItemPage=%d",
                            "%s/api/%s?Category=%s&MaximumPrice=%s&ItemPage=%d&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s&ItemPageOffset=%d",
                            constant.gPrefUrl, mShopName, AppUtils.categoryArr[mType],
                            mMaxPrice, mPageIndex, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType, mItemPageOffset);
                    // AppUtils.brand = categoryArr[mType];
                    AppUtils.putPref("brand", AppUtils.categoryArr[mType], DMAutoActivity.this);
                }
            }
        } else {
            int n[] = {1, 2, 3, 4, 5};
            int type;
            Random random = new Random();
            if (TextUtils.isEmpty(AppUtils.lastCategoryCalled)) {
                type = n[random.nextInt(n.length)] % 5;
            } else {
                type = Arrays.asList(AppUtils.categoryArr).indexOf(AppUtils.lastCategoryCalled);

                if (type < 0) {
                    type = n[random.nextInt(n.length)] % 5;
                } else {
                    type = (Arrays.asList(AppUtils.categoryArr).indexOf(AppUtils.lastCategoryCalled) + 1) % 5;
                }

            }

            baseUrl = String.format(
                    //"%s/api/getProductList?Category=%s&ItemPage=%d",
                    "%s/api/productloop?Category=%s&ItemPage=%d&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                    constant.gPrefUrl, AppUtils.categoryArr[type], mPageIndex, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);
//             AppUtils.brand = categoryArr[type];
            AppUtils.putPref("brand", AppUtils.categoryArr[type], DMAutoActivity.this);
            AppUtils.lastCategoryCalled = AppUtils.categoryArr[type];

            Log.e("Autoactivity", "lucky random: " + AppUtils.categoryArr[type]);
        }

        if (!mIsRefresh) {
            constant.showProgress(this, "Loading...");
        }

        new MyAsyncTask(new ServerResponse() {

            @Override
            public void getResponse(JSONObject data) {
                // TODO Auto-generated method stub
                vGrdProduct.onRefreshComplete();
                constant.hideProgress();
                mIsRefresh = false;
                int itemCount = 0;
                JSONArray mArrayData = new JSONArray();
                JSONObject mObjectData = new JSONObject();
      //          Log.e("RESPONSE",data.toString());
                if (mProductList == null)
                    mProductList = new ArrayList<DMProductObject>();
                if (data != null) {
                    try {

                        mTotalResult = data.getInt("TotalResults");
                        mTotalPage = data.getInt("TotalPages");
                        mItemPageOffset =  data.optInt(constant.API_ITEM_OFFSET, 0); //mayur
                        if (data.get("Items") instanceof JSONArray) {
                            mArrayData = data.getJSONArray("Items");// mayur changed as new response gives array instead of object
                            //   itemCount = data.getInt("ItemCount") <= jObj.length() ? data.getInt("ItemCount") : jObj.length();
                        } else {
                            mObjectData = data.getJSONObject("Items");// mayur changed as new response object
                        }

                        itemCount = data.getInt("ItemCount");
                        //   if (jObj != null) {
                        for (int i = 0; i < itemCount; i++) {
                            JSONObject pObj;
                            if (mArrayData != null && !mArrayData.isNull(0)) {
                                pObj = mArrayData.getJSONObject(i);
                            } else {
                                pObj = mObjectData.getJSONObject(String.valueOf(i + 1));//getJSONObject(i);
                            }
                            DMProductObject item = new DMProductObject(pObj);

                            String title = item.Title.toLowerCase();

                            if (!title.contains("girl")
                                    && !title.contains("women")) {
                                mProductList.add(item);

                            }
                        }
                        //   }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    //uncomment when amazona and ASOS issue solved
                    Toast.makeText(DMAutoActivity.this, "Network Error",
                            Toast.LENGTH_LONG).show();
                }

                showProductGrid(itemCount);
            }
        }, baseUrl, true).execute();
    }

    // / -------------------------------- get closet from server
    // ------------------------------
    @SuppressLint("DefaultLocale")
    public void getClosetFS_Shared() {
        String baseUrl = "";


        if (mFrom.equals("exact")) {
            if (mType == constant.NONE) {

                int type = mPageIndex % 5;
                System.out.println("Main===>0" + AppUtils.categoryArr[type]);
                if (mMaxPrice.equals("0")) {
                    baseUrl = String
                            .format(
                                    //"%s/api/getProductList?Category=%s&ItemPage=%d&Brand=%s",
                                    "%s/api/%s?Category=%s&ItemPage=%d&Brand=%s&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                                    constant.gPrefUrl, mShopName, AppUtils.categoryArr[type],
                                    mPageIndex, AppUtils.brand, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);


                } else {
                    System.out.println("Main===>none" + AppUtils.categoryArr[type]);
                    baseUrl = String
                            .format(
                                    //"%s/api/getProductList?Category=%s&MaximumPrice=%s&ItemPage=%d&Brand=%s",
                                    "%s/api/%s?Category=%s&MaximumPrice=%s&ItemPage=%d&Brand=%s&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                                    constant.gPrefUrl, mShopName, AppUtils.categoryArr[type],
                                    mMaxPrice, mPageIndex, AppUtils.brand, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);
                }

            } else {
                if (mMaxPrice.equals("0")) {
                    System.out.println("Main===>type-1" + AppUtils.categoryArr[mType]);
                    baseUrl = String
                            .format(
                                    //"%s/api/getProductList?Category=%s&ItemPage=%d&Brand=%s",
                                    "%s/api/%s?Category=%s&ItemPage=%d&Brand=%s&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                                    constant.gPrefUrl, mShopName, AppUtils.categoryArr[mType],
                                    mPageIndex, AppUtils.brand, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);


                    System.out.println("Brand" + AppUtils.brand);
                    System.out.println("base" + baseUrl);


                } else {

                    AppUtils.getPref("closet", DMAutoActivity.this);
                    System.out.println("Main===>" + AppUtils.getPref("type", DMAutoActivity.this) + "" + AppUtils.getPref("closet", DMAutoActivity.this));
                    baseUrl = String
                            .format(
                                    //"%s/api/getProductList?Category=%s&MaximumPrice=%s&ItemPage=%d&Brand=%s",
                                    "%s/api/%s?Category=%s&MaximumPrice=%s&ItemPage=%d&Brand=%s&AWSOffset=%d&ShopStyleOffset=%d&AsosOffset=%d&Sort=%s",
                                    constant.gPrefUrl, mShopName, AppUtils.categoryArr[mType],
                                    mMaxPrice, mPageIndex, AppUtils.brand, mAWSOffset, mShopStyleOffset, mAsosOffset, mSortType);
                    System.out.println("Brand" + AppUtils.brand);
                    System.out.println("base" + baseUrl);
                }
            }
        } else {
            int n[] = {1, 2, 3, 4, 5};
            int type = -1;

            Random random = new Random();
            if (TextUtils.isEmpty(AppUtils.lastCategoryCalled)) {
                type = n[random.nextInt(n.length)] % 5;
            } else {
                type = Arrays.asList(AppUtils.categoryArr).indexOf(AppUtils.lastCategoryCalled);
                if (type < 0) {
                    type = n[random.nextInt(n.length)] % 5;
                } else {
                    type = Arrays.asList(AppUtils.categoryArr).indexOf(AppUtils.lastCategoryCalled) % 5;
                }
            }


            // baseUrl =
            // String.format("%s/api/getProductList?Category=%s&ItemPage=%d&Brand=%s",
            // constant.gPrefUrl,categoryArr[type],
            // n[random.nextInt(n.length)],AppUtils.brand);

            baseUrl = String.format(
                    //"%s/api/getProductList?Category=%s&ItemPage=%d",
                    "%s/api/productloop?Category=%s&ItemPage=%d",
                    constant.gPrefUrl, AppUtils.categoryArr[type],
                    n[random.nextInt(n.length)]);
            System.out.println("Type" + AppUtils.categoryArr[type]);
            System.out.println("baseUrlLucky" + baseUrl);
            AppUtils.lastCategoryCalled = AppUtils.categoryArr[type];
        }

        if (!mIsRefresh) {
            constant.showProgress(this, "Loading...");
        }

        new MyAsyncTask(new ServerResponse() {

            @Override
            public void getResponse(JSONObject data) {
                // TODO Auto-generated method stub
                vGrdProduct.onRefreshComplete();
                constant.hideProgress();
                mIsRefresh = false;
                int itemCount = 0;

                if (mProductList == null)
                    mProductList = new ArrayList<DMProductObject>();
                if (data != null) {
                    try {

                        // JSONArray jArrayMain = data.getJSONArray("alldata");
                        //for (int j = 0; j <= jArrayMain.length(); j++)
                        {

                            //JSONObject jObjData = jArrayMain.getJSONObject(j);

                            mTotalResult = data.getInt("TotalResults");
                            mTotalPage = data.getInt("TotalPages");

                            JSONArray jObj = data.getJSONArray("Items");// mayur changed as new responce gives array insted of object
                            itemCount = data.getInt("ItemCount") <= jObj.length() ? data.getInt("ItemCount") : jObj.length();

                            if (jObj != null) {
                                for (int i = 0; i < itemCount; i++) {
                                    JSONObject pObj = jObj.getJSONObject(i);
                                    DMProductObject item = new DMProductObject(pObj);

                                    String title = item.Title.toLowerCase();

                                    if (!title.contains("girl")
                                            && !title.contains("women")) {
                                        mProductList.add(item);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    //uncomment when amazona and ASOS issue solved
                    Toast.makeText(DMAutoActivity.this, "Network Error",
                            Toast.LENGTH_LONG).show();
                }
                showProductGrid(itemCount);
            }
        }, baseUrl, true).execute();
    }

    private void showShowcaseViewExact() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_AUTO_SHOWN, false)) {
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_AUTO_SHOWN, true);
                }
            });
        }
    }

    private void showShowcaseViewLucky() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_LUCKY_AUTO_SHOWN, false)) {
            mCoachMarkScreenIv.setImageResource(R.drawable.lucky_feeling_coach);
            mCoachMarkScreenIv.setVisibility(View.VISIBLE);
            mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCoachMarkScreenIv.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_LUCKY_AUTO_SHOWN, true);
                }
            });
        }
    }
}

package com.mydimoda;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if (user.getInt(constant.USER_MAX_COUNT) >= 5) {
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


}

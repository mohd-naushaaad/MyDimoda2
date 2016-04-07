package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMFavoriteListAdapter;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DMFavoriteActivity extends Activity {

    // / menu
    Button vBtnMenu;
    ListView vMenuList;
    TextView vTxtBack, vTxtTitle;
    DrawerLayout vDrawerLayout;
    LinearLayout vMenuLayout;
    RelativeLayout vBackLayout;

    ListView vFavoriteList;

    List<ParseObject> mFavoriteList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // / layout
        vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vMenuList = (ListView) findViewById(R.id.menu_list);
        vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        vBtnMenu = (Button) findViewById(R.id.menu_btn);
        vTxtTitle = (TextView) findViewById(R.id.title_view);
        FontsUtil.setExistenceLight(this, vTxtTitle);

        vTxtBack = (TextView) findViewById(R.id.back_txt);
        FontsUtil.setExistenceLight(this, vTxtBack);

        vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

        vFavoriteList = (ListView) findViewById(R.id.favorite_list);

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
                constant.selectMenuItem(DMFavoriteActivity.this, position, true);
            }
        });

        vBackLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        vFavoriteList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                makeFavoriteIdArray(position);
                Intent intent = new Intent(DMFavoriteActivity.this,
                        DMFashionActivity.class);
                intent.putExtra("favorite", "yes");
                intent.putExtra("favoritelist", "favoritelist");
                intent.putExtra("showlayout", "showlayout");
                intent.putExtra("date", constant.getCustomDate(mFavoriteList
                        .get(position).getString("DateTime")));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }

    public void init() {
        showMenu();
        // setViewWithFont();
        if (AppUtils.isConnectingToInternet(DMFavoriteActivity.this)) {
            getFavoriteList();
        } else {
            Toast.makeText(DMFavoriteActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
        }
    }

    // / --------------------------------- set font
    // -------------------------------------
    // public void setViewWithFont()
    // {
    // vTxtTitle.setTypeface(constant.fontface);
    // vTxtBack.setTypeface(constant.fontface);
    // }
    //
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

    // / -------------------------------------------- get favorite list from
    // parse --------------------
    public void getFavoriteList() {
        if (AppUtils.isConnectingToInternet(DMFavoriteActivity.this)) {
            constant.showProgress(this, "Loading...");
            ParseUser user = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
            query.orderByDescending("DateTime");
            query.whereEqualTo("User", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> clothList, ParseException e) {
                    constant.hideProgress();
                    if (e == null) {
                        showFavoriteList(clothList);
                    } else {
                        Toast.makeText(DMFavoriteActivity.this, AppUtils.asUpperCaseFirstChar(e.getMessage()),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(DMFavoriteActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
        }
    }

    // / --------------------------------------------------- show favorite list
    // -----------------------
    public void showFavoriteList(List<ParseObject> list) {
        if (list != null) {
            ParseUser user = ParseUser.getCurrentUser();
            mFavoriteList = new ArrayList<ParseObject>();
            for (int i = 0; i < list.size(); i++) {
                ParseUser itemUser = list.get(i).getParseUser("User");
                if (itemUser.getObjectId().equals(user.getObjectId())) {
                    mFavoriteList.add(list.get(i));
                }
            }
            vFavoriteList.setAdapter(new DMFavoriteListAdapter(this,
                    mFavoriteList));
        }
    }

    // / -------------------------------------------------- make id array that
    // is favorited by user -----------
    public void makeFavoriteIdArray(int pos) {
        if (mFavoriteList != null) {
            ParseObject object = mFavoriteList.get(pos);
            ParseObject shirtObj = (ParseObject) object.get("Shirt");
            ParseObject trousersObj = (ParseObject) object.get("Trousers");
            ParseObject jacketObj = (ParseObject) object.get("Jacket");
            ParseObject tieObj = (ParseObject) object.get("Tie");
            ParseObject suitObj = (ParseObject) object.get("Suit");

            constant.gFashionID = object.getObjectId();

            constant.gFashion = new DMBlockedObject();
            if (shirtObj != null) {
                DMItemObject item = new DMItemObject();
                item.index = shirtObj.getObjectId();
                item.type = "shirt";
                constant.gFashion.blockedList.add(item);
            }

            if (trousersObj != null) {
                DMItemObject item = new DMItemObject();
                item.index = trousersObj.getObjectId();
                item.type = "trousers";
                constant.gFashion.blockedList.add(item);
            }

            if (jacketObj != null) {
                DMItemObject item = new DMItemObject();
                item.index = jacketObj.getObjectId();
                item.type = "jacket";
                constant.gFashion.blockedList.add(item);
            }

            if (tieObj != null) {
                DMItemObject item = new DMItemObject();
                item.index = tieObj.getObjectId();
                item.type = "tie";
                constant.gFashion.blockedList.add(item);
            }

            if (suitObj != null) {
                DMItemObject item = new DMItemObject();
                item.index = suitObj.getObjectId();
                item.type = "suit";
                constant.gFashion.blockedList.add(item);
            }

        }
    }

}

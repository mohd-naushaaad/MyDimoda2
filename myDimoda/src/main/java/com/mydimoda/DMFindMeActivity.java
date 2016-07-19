package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
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

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DMFindMeActivity extends Activity {

	// / menu
	Button vBtnMenu;
	ListView vMenuList;
	TextView vTxtBack, vTxtTitle;
	DrawerLayout vDrawerLayout;
	LinearLayout vMenuLayout;
	RelativeLayout vBackLayout;
	RelativeLayout vExactLayout, vAutoLayout;
	TextView vTxtFindMe, vTxtAuto;
	String mCategory;
	@Bind(R.id.act_find_coach_mrk_iv)
	ImageView mCoachMarkScreenIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findme);
		ButterKnife.bind(this);

		final ParseUser user = ParseUser.getCurrentUser();
		boolean bPurchased = user.getBoolean("ratedMyDimoda");
		System.out.println("HomePurchased"+bPurchased);

		if(bPurchased == true)
		{
			System.out.println("TRUE"+bPurchased);
		}

		else
		{
		if(AppUtils.getPref("pro",DMFindMeActivity.this) != null)
		{
			if(AppUtils.getPref("pro",DMFindMeActivity.this).equalsIgnoreCase("false"))
			{
				AppRater.app_launched(DMFindMeActivity.this);
			}
			else
			{
				AppUtils.putPref("pro", "true", DMFindMeActivity.this);
			}
		}
		else if(AppUtils.getPref("pro",DMFindMeActivity.this) == null)
		{
			AppUtils.putPref("pro", "false", DMFindMeActivity.this);
			AppRater.app_launched(DMFindMeActivity.this);
		}
		else
		{
			AppUtils.putPref("pro", "false", DMFindMeActivity.this);
			AppRater.app_launched(DMFindMeActivity.this);
		}
		}
		
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

		vExactLayout = (RelativeLayout) findViewById(R.id.style_layout);
		vAutoLayout = (RelativeLayout) findViewById(R.id.help_layout);
		vTxtFindMe = (TextView) findViewById(R.id.style_txt);
		FontsUtil.setExistenceLight(this, vTxtFindMe);

		vTxtAuto = (TextView) findViewById(R.id.help_txt);
		FontsUtil.setExistenceLight(this, vTxtAuto);

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
				constant.selectMenuItem(DMFindMeActivity.this, position, true);
			}
		});

		vBackLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		vExactLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DMFindMeActivity.this,
						DMExactActivity.class);
				startActivity(intent);
			}
		});

		vAutoLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DMFindMeActivity.this,
						DMAutoActivity.class);
				intent.putExtra("from", "findme");
				intent.putExtra("closet", constant.NONE);
				intent.putExtra("price", 0);
				startActivity(intent);
			}
		});

		showShowcaseView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	public void init() {
		showMenu();
//		setViewWithFont();
	}

	// / --------------------------------- set font
	// -------------------------------------
//	public void setViewWithFont() {
//		vTxtTitle.setTypeface(constant.fontface);
//		vTxtBack.setTypeface(constant.fontface);
//		vTxtFindMe.setTypeface(constant.fontface);
//		vTxtAuto.setTypeface(constant.fontface);
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
	private void showShowcaseView() {
		if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_FIND_SHOWN, false)) {
			mCoachMarkScreenIv.setVisibility(View.VISIBLE);
			mCoachMarkScreenIv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mCoachMarkScreenIv.setVisibility(View.GONE);
					SharedPreferenceUtil.putValue(constant.PREF_IS_FIND_SHOWN, true);
				}
			});
		}
	}
}

package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMExactActivity extends Activity {

	// / menu
	Button vBtnMenu;
	ListView vMenuList;
	TextView vTxtBack, vTxtTitle;
	DrawerLayout vDrawerLayout;
	LinearLayout vMenuLayout;
	RelativeLayout vBackLayout;

	Button vBtnFind;
	EditText vEdtMax, vEdtDesign;
	TextView vTxtMax, vTxtShirt, vTxtTrouser, vTxtJacket, vTxtTie, vTxtDesign;
	ImageView vChkShirt, vChkTrouser, vChkJacket, vChkTie;
	RelativeLayout vLytShirt, vLytTrouser, vLytJacket, vLytTie;

	int mType = 0;
	boolean mFShirt, mFTrouser, mFJacket, mFTie;
	Intent intent = getIntent();
	String zone = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exact);

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

		vBtnFind = (Button) findViewById(R.id.btn_findme);
		FontsUtil.setExistenceLight(this, vBtnFind);

		vEdtMax = (EditText) findViewById(R.id.max_edit);
		FontsUtil.setExistenceLight(this, vEdtMax);

		vTxtMax = (TextView) findViewById(R.id.max_price_txt);
		FontsUtil.setExistenceLight(this, vTxtMax);

		vEdtDesign = (EditText) findViewById(R.id.design_edit);
		FontsUtil.setExistenceLight(this, vEdtDesign);

		vTxtDesign = (TextView) findViewById(R.id.design_price_txt);
		FontsUtil.setExistenceLight(this, vTxtDesign);

		vTxtShirt = (TextView) findViewById(R.id.shirt_txt);
		FontsUtil.setExistenceLight(this, vTxtShirt);

		vTxtTrouser = (TextView) findViewById(R.id.pants_txt);
		FontsUtil.setExistenceLight(this, vTxtTrouser);

		vTxtJacket = (TextView) findViewById(R.id.coat_txt);
		FontsUtil.setExistenceLight(this, vTxtJacket);

		vTxtTie = (TextView) findViewById(R.id.tie_txt);
		FontsUtil.setExistenceLight(this, vTxtTie);

		vChkShirt = (ImageView) findViewById(R.id.shirt_check);
		vChkTrouser = (ImageView) findViewById(R.id.pants_check);
		vChkJacket = (ImageView) findViewById(R.id.coat_check);
		vChkTie = (ImageView) findViewById(R.id.tie_check);
		vLytShirt = (RelativeLayout) findViewById(R.id.restore_layout);
		vLytTrouser = (RelativeLayout) findViewById(R.id.pants_layout);
		vLytJacket = (RelativeLayout) findViewById(R.id.coat_layout);
		vLytTie = (RelativeLayout) findViewById(R.id.tie_layout);

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
				constant.selectMenuItem(DMExactActivity.this, position, true);
			}
		});

		vBackLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		vBtnFind.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				findMeProduct();
			}
		});

		vLytShirt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectType(1);
				AppUtils.brand = "";
				vEdtDesign.setText("");
				vEdtDesign.setHint("ALL");
			}
		});

		vLytTrouser.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectType(2);
				AppUtils.brand = "";
				vEdtDesign.setText("");
				vEdtDesign.setHint("ALL");
			}
		});

		vLytJacket.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectType(3);
				AppUtils.brand = "";
				vEdtDesign.setText("");
				vEdtDesign.setHint("ALL");
			}
		});

		vLytTie.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectType(4);
				AppUtils.brand = "";
				vEdtDesign.setText("");
				vEdtDesign.setHint("ALL");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		init();

		/*
		 * Bundle extras = getIntent().getExtras(); if (extras != null) { zone =
		 * extras.getString("zone"); }
		 */
		vEdtDesign.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("TYPE" + mType);
				AppUtils.putPref("type", String.valueOf(mType),
						DMExactActivity.this);
				Intent m_intent = new Intent(DMExactActivity.this,
						MainActivity.class);
				m_intent.putExtra("type", mType);
				System.out.println("Main" + mType);
				startActivity(m_intent);

			}
		});

		if (AppUtils.brand != null) {
			vEdtDesign.setText(AppUtils.brand);
			AppUtils.yes = "false";
		} else {
			vEdtDesign.setText(AppUtils.brand);
			AppUtils.yes = "true";
			vEdtDesign.setHint("ALL");
		}

		/*
		 * if(AppUtils.getPref("zone", DMExactActivity.this) != null) {
		 * 
		 * vEdtDesign.setText(AppUtils.getPref("zone", DMExactActivity.this));
		 * AppUtils.yes ="false"; } else { AppUtils.yes ="true"; }
		 */

	}

	public void init() {
		showMenu();
		// setViewWithFont();
	}

	// / --------------------------------- set font
	// -------------------------------------
	// public void setViewWithFont()
	// {
	// vTxtTitle.setTypeface(constant.fontface);
	// vTxtBack.setTypeface(constant.fontface);
	// vTxtMax.setTypeface(constant.fontface);
	// vTxtDesign.setTypeface(constant.fontface);
	// vTxtShirt.setTypeface(constant.fontface);
	// vTxtTrouser.setTypeface(constant.fontface);
	// vTxtJacket.setTypeface(constant.fontface);
	// vTxtTie.setTypeface(constant.fontface);
	// vBtnFind.setTypeface(constant.fontface);
	//
	//
	//
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

	// / -------------------------------- when items is clicked, show checked
	// state --------
	public void selectType(int type) {
		if (type == 1) {
			// Shirts
			mFShirt = !mFShirt;
			mFTrouser = false;
			mFJacket = false;
			mFTie = false;
		} else if (type == 2) {
			// Trousers
			mFTrouser = !mFTrouser;
			mFShirt = false;
			mFJacket = false;
			mFTie = false;
		} else if (type == 3) {
			// Jacket
			mFJacket = !mFJacket;
			mFTrouser = false;
			mFShirt = false;
			mFTie = false;
		} else if (type == 4) {
			// Tie
			mFTie = !mFTie;
			mFJacket = false;
			mFTrouser = false;
			mFShirt = false;
		}

		vChkShirt.setImageResource(R.drawable.edit_yel_line_bg);
		vChkTrouser.setImageResource(R.drawable.edit_yel_line_bg);
		vChkJacket.setImageResource(R.drawable.edit_yel_line_bg);
		vChkTie.setImageResource(R.drawable.edit_yel_line_bg);

		if (mFShirt) {
			vChkShirt.setImageResource(R.drawable.img_check_sel);
			mType = constant.SHIRT;
			AppUtils.putPref("type", String.valueOf(mType),
					DMExactActivity.this);
			AppUtils.brand = "";
			vEdtDesign.setHint("ALL");
		} else if (mFTrouser) {
			vChkTrouser.setImageResource(R.drawable.img_check_sel);
			mType = constant.TROUSERS;
			AppUtils.brand = "";
			AppUtils.putPref("type", String.valueOf(mType),
					DMExactActivity.this);
			vEdtDesign.setHint("ALL");
		} else if (mFJacket) {
			vChkJacket.setImageResource(R.drawable.img_check_sel);
			mType = constant.JACKET;
			AppUtils.brand = "";
			AppUtils.putPref("type", String.valueOf(mType),
					DMExactActivity.this);
			vEdtDesign.setHint("ALL");
		} else if (mFTie) {
			vChkTie.setImageResource(R.drawable.img_check_sel);
			mType = constant.TIE;
			AppUtils.brand = "";
			AppUtils.putPref("type", String.valueOf(mType),
					DMExactActivity.this);
			vEdtDesign.setHint("ALL");
		} else {
			mType = constant.NONE;
			vEdtDesign.setHint("ALL");
		}
	}

	// / --------------------------- find products with price from server
	// ------------
	public void findMeProduct() {
		String maxPrice = vEdtMax.getText().toString();

		if (maxPrice.equals("")) {
			constant.alertbox("Warning!", "Max Price Empty.", this);
		} else {
			Intent intent = new Intent(this, DMAutoActivity.class);
			intent.putExtra("from", "exact");
			intent.putExtra("closet", mType);
			AppUtils.putPref("type", String.valueOf(mType),
					DMExactActivity.this);
			System.out.println("closet" + mType);
			intent.putExtra("price", maxPrice);
			startActivity(intent);
		}

	}

	@Override
	public void onBackPressed() {
		AppUtils.brand = "";
		vEdtDesign.setText("");
		super.onBackPressed();
	}

}

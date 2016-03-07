package com.mydimoda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
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

import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.social.google.GoogleIAP;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;

public class DMStyleActivity extends Activity {

	// / menu
	Button vBtnMenu;
	ListView vMenuList;
	TextView vTxtBack, vTxtTitle;
	DrawerLayout vDrawerLayout;
	LinearLayout vMenuLayout;
	RelativeLayout vBackLayout;

	RelativeLayout vStyleMeLayout, vHelpMeLayout;
	TextView vTxtStyleMe, vTxtHelpMe;
	String mCategory;
	boolean b_rate;
	private ProgressDialog progressbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_style);
		final ParseUser user = ParseUser.getCurrentUser();
		b_rate = user.getBoolean("ratedMyDimoda");
		System.out.println("HomePurchased"+b_rate);
	
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

		vStyleMeLayout = (RelativeLayout) findViewById(R.id.style_layout);
		vHelpMeLayout = (RelativeLayout) findViewById(R.id.help_layout);
		vTxtStyleMe = (TextView) findViewById(R.id.style_txt);
		FontsUtil.setExistenceLight(this, vTxtStyleMe);

		vTxtHelpMe = (TextView) findViewById(R.id.help_txt);
		FontsUtil.setExistenceLight(this, vTxtHelpMe);
		if (getIntent() != null) {

			try
			{
				if (getIntent().hasExtra("notification_look")) 
				{
					if(getIntent().getExtras().getString("notification_look") != null)
					{
						if(getIntent().getExtras().getString("notification_category") != null)
						{
							
							
							constant.gCategory = getIntent().getExtras().getString("notification_category");
							
							System.out.println("Cateofry_notification"+constant.gCategory);
							if(b_rate == true)
							{
								System.out.println("TRUE"+b_rate);
							}
							else
							{
								gotoAlgorithmActivity_8(constant.gCategory);
							}
							
							
						}
					}
				}
			}
			catch (Exception e)
			{

			}
		}
		
		init();

		if(b_rate == true)
		{
			System.out.println("TRUE"+b_rate);
		}

		else
		{
			if(AppUtils.getPref("pro",DMStyleActivity.this) != null)
			{
				if(AppUtils.getPref("pro",DMStyleActivity.this).equalsIgnoreCase("false"))
				{
					AppRater.app_launched(DMStyleActivity.this);
				}
				else
				{
					AppUtils.putPref("pro", "true", DMStyleActivity.this);
				}
			}
			else if(AppUtils.getPref("pro",DMStyleActivity.this) == null)
			{
				AppUtils.putPref("pro", "false", DMStyleActivity.this);
				AppRater.app_launched(DMStyleActivity.this);
			}
			else
			{
				AppUtils.putPref("pro", "false", DMStyleActivity.this);
				AppRater.app_launched(DMStyleActivity.this);
			}
		}
		vBtnMenu.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				slideMenu();
			}
		});

		vMenuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				constant.selectMenuItem(DMStyleActivity.this, position, true);
			}
		});

		vBackLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				finish();
			}
		});

		vStyleMeLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if(b_rate == true)
				{
					System.out.println("TRUE"+b_rate);
				}
				else
				{
					checkPermissions();
				}
			}
		});

		vHelpMeLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				constant.gMode = "help me";
				Intent intent = new Intent(DMStyleActivity.this,
						DMHangUpActivity.class);
				intent.putExtra("FromMain", false);
				startActivity(intent);
			}
		});

		GoogleIAP.initiate(this,
				getResources().getString(R.string.google_iap_base64));
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		GoogleIAP.destroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (!GoogleIAP.onActivityResult(requestCode, resultCode, data))
			super.onActivityResult(requestCode, resultCode, data);
	}

	public void init() {
		showMenu();
		// setViewWithFont();

		if (constant.gCategory == null)
			return;

		if (constant.gCategory.equals("formal"))
			vTxtTitle.setText("Formal");
		else if (constant.gCategory.equals("after5"))
			vTxtTitle.setText("Evening Out");
		else if (constant.gCategory.equals("casual"))
			vTxtTitle.setText("Casual");
	}

	// / --------------------------------- set font
	// -------------------------------------
	// public void setViewWithFont()
	// {
	// vTxtTitle.setTypeface(constant.fontface);
	// vTxtBack.setTypeface(constant.fontface);
	// vTxtStyleMe.setTypeface(constant.fontface);
	// vTxtHelpMe.setTypeface(constant.fontface);
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

	private void procRate() {

		SharedPreferences setting = getSharedPreferences("mydimoda_setting", 0);
		boolean never = setting.getBoolean("never", false);
		int count = setting.getInt("rate_count", 1);

		if (!never) {
			if (count == 3) {
				showRateAlert();
			} else {
				procPurchase();
			}
		} else {
			procPurchase();
		}
	}

	private void procPurchase() {

		final ParseUser user = ParseUser.getCurrentUser();
		boolean bPurchased = user.getBoolean("Buy");
        int maxCount = user.getInt(constant.USER_MAX_COUNT); // new max count according to new policy

		if (!bPurchased) {
			int count = user.getInt("Count");

			if (SharedPreferenceUtil.getString("inApp", "0").equalsIgnoreCase(
					"1")) {
				gotoAlgorithmActivity();
			} else if  (count >= (maxCount >= 5 ? maxCount : constant.maxCount)) {
				/*
				 * if(SharedPreferenceUtil.getString("inApp",
				 * "0").equalsIgnoreCase("1")) { gotoAlgorithmActivity(); } else
				 * {
				 */
				showPurchaseAlert();
				/* } */
			} else {
				gotoAlgorithmActivity();
			}
		} else {
			gotoAlgorithmActivity();
		}
	}

	private void checkPermissions() {

		final ParseUser user = ParseUser.getCurrentUser();
		boolean bRated = user.getBoolean("ratedmyDiModa");
		if (bRated) {
			procPurchase();
		} else {
			procRate();
		}


	}

	// private boolean OverOneDay() {
	//
	// long currenttime = System.currentTimeMillis();
	//
	// SharedPreferences setting = getSharedPreferences("mydimoda_setting", 0);
	// long reviewtime = setting.getLong("reviewtime", 0);
	// if(reviewtime == 0) {
	// Editor editor = getSharedPreferences("mydimoda_setting", 0).edit();
	// editor.putLong("reviewtime", currenttime);
	// editor.commit();
	//
	// return true;
	// }
	//
	// float delta = (float)(currenttime - reviewtime) / 1000.0f / 3600.0f;
	// if(delta > 24.0f) {
	// Editor editor = getSharedPreferences("mydimoda_setting", 0).edit();
	// editor.putLong("reviewtime", currenttime);
	// editor.commit();
	//
	// return true;
	// }
	//
	// return false;
	// }
	//
	private void showRateAlert() {

		new AlertDialog.Builder(this)
		.setTitle("Rate us!")
		.setMessage(
				"Would you like to rate us? Please rate us 5 stars!")
				.setCancelable(false)
				.setNegativeButton("Never",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						Editor editor = getSharedPreferences(
								"mydimoda_setting", 0).edit();
						editor.putBoolean("never", true);
						editor.commit();

						procPurchase();
					}
				})
				.setNeutralButton("Rate Now",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						ParseUser user = ParseUser.getCurrentUser();
						user.put("ratedmyDiModa", true);
						user.saveInBackground();
						rateApp();
					}
				})
				.setPositiveButton("Not Now",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						SharedPreferences setting = getSharedPreferences(
								"mydimoda_setting", 0);
						int count = setting.getInt("rate_count", 1);

						count++;
						if (count == 4)
							count = 1;
						Editor editor = getSharedPreferences(
								"mydimoda_setting", 0).edit();
						editor.putInt("rate_count", count);
						editor.commit();
						procPurchase();
					}
				}).show();
	}

	private void showPurchaseAlert() {

		new AlertDialog.Builder(this)
		.setTitle("Upgrade for myDiModa!")
		.setMessage(
				"To get more Style Me mode, please buy unlimited styling license at 1.99$")
				.setCancelable(false)
				.setNegativeButton("Buy",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						GoogleIAP.buyFeature(0);

						/*
						 * showProgressBar("");
						 * 
						 * ParseUser user = ParseUser.getCurrentUser();
						 * user.put("Buy", true);
						 * user.saveInBackground(new SaveCallback() {
						 * 
						 * @Override public void done(ParseException e1)
						 * {
						 * 
						 * hideProgressBar();
						 * 
						 * if(e1 == null) { gotoAlgorithmActivity(); }
						 * else { Toast.makeText(DMStyleActivity.this,
						 * e1.toString(), Toast.LENGTH_LONG).show(); } }
						 * });
						 */
					}
				})
				.setPositiveButton("Cancel",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
					}
				}).show();
	}

	private void rateApp() {

		Uri uri = Uri.parse("market://details?id=" + this.getPackageName());

		Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(myAppLinkToMarket);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "unable to find market app", Toast.LENGTH_LONG)
			.show();
		}
	}

	private void gotoAlgorithmActivity() {

		if (constant.gIsCloset) {
			ParseUser user = ParseUser.getCurrentUser();
			int count = user.getInt("Count");
			count++;
			user.put("Count", count);
			user.saveInBackground();
		}

		constant.gMode = "style me";
		Intent intent = new Intent(DMStyleActivity.this,DMAlgorithmActivity.class);
		startActivity(intent);
	}
	private void gotoAlgorithmActivity_8(String Category) {

		if (constant.gIsCloset) {
			ParseUser user = ParseUser.getCurrentUser();
			int count = user.getInt("Count");
			count++;
			user.put("Count", count);
			user.saveInBackground();
		}

		constant.gMode = "style me";
		Intent intent = new Intent(DMStyleActivity.this,DMAlgorithmActivity.class);
		intent.putExtra("category", Category);
		startActivity(intent);
	}

	@SuppressWarnings("unused")
	private void showProgressBar(String message) {

		if (progressbar != null)
			return;

		progressbar = ProgressDialog.show(this, null, message);
	}

	@SuppressWarnings("unused")
	private void hideProgressBar() {

		if (progressbar == null)
			return;

		progressbar.dismiss();
		progressbar = null;
	}
}

package com.mydimoda;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.parse.ParseUser;

public class DMMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(SharedPreferenceUtil.getBoolean("isNewInstall",false)==false) {
			SharedPreferenceUtil.putValue("isNewInstall", true);
			SharedPreferenceUtil.save();
			logout();
		}

		new Handler().postDelayed(new Runnable(){
			public void run() {
				/* show login layout */
				if(getUserData()){
					Intent intent = new Intent(DMMainActivity.this, DMIntroActivity.class);
					intent.putExtra("isStart", true);
					startActivity(intent);
				}else{
					Intent intent = new Intent(DMMainActivity.this, DMLoginActivity.class);
					startActivity(intent);
				}
				finish();
			}
		}, 2000);
	}
	
	public boolean getUserData()
	{
		SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME, 0);
		boolean isFirst  = settings.getBoolean("isFirst", false);//true remove the intro mayur
		
		return isFirst;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.l
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void logout() {
		SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME,
				0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString("username", "");
		editor.putString("email", "");
		editor.putString("password", "");
		editor.putString("userid", "");

		editor.commit();

		//Keyur
		//canceling notifications if any
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(1234);
		mNotificationManager.cancelAll();

		//mayur resetting checks
		AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, false, this);
		SharedPreferenceUtil.putValue(constant.USER_MAX_COUNT_INITILISED, false);
		SharedPreferenceUtil.putValue(constant.PREF_MAX_COUNT_CONFIGURED, false);
//Now call logout
		ParseUser.logOutInBackground();

	}

}

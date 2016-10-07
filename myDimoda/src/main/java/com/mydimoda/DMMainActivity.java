package com.mydimoda;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

public class DMMainActivity extends Activity {

	public static String[] perm_array = {
			Manifest.permission.CAMERA,
			Manifest.permission.WRITE_EXTERNAL_STORAGE

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadPermissions(perm_array);


	}

	void doAction(){
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
	public void loadPermissions(String[] perm_array) {
		System.out.println("Load permission : ");
		ArrayList<String> permArray = new ArrayList<>();
		for (String permission : perm_array) {
			if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				permArray.add(permission);
			}
		}
		perm_array = new String[permArray.size()];
		perm_array = permArray.toArray(perm_array);

		if (perm_array.length > 0) {
			ActivityCompat.requestPermissions(this, perm_array, 0);
		} else {
			doAction();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch (requestCode) {

			case 0:
				boolean isDenied = false;
				for (int i = 0; i < grantResults.length; i++) {
					System.out.println(grantResults[i]);
					if (grantResults[i] == -1) {
						isDenied = true;
					}
				}

				if (!isDenied) {
					//GlobalApp.getInstance().makeDir();
					doAction();
				} else {
					Toast.makeText(DMMainActivity.this, getResources().getString(R.string.perm_denied), Toast.LENGTH_LONG).show();
					finish();
				}
				break;
		}
	}


}

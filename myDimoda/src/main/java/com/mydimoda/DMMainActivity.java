package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class DMMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Handler().postDelayed(new Runnable(){
			public void run() {
				/* show login layout */
				
				if(getUserData())
				{
					Intent intent = new Intent(DMMainActivity.this, DMIntroActivity.class);
					intent.putExtra("isStart", true);
					startActivity(intent);
				}else
				{
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
		boolean isFirst  = settings.getBoolean("isFirst", true);
		
		return isFirst;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.l
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

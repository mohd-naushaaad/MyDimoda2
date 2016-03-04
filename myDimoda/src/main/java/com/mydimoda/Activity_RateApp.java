package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class Activity_RateApp extends Activity implements OnClickListener{
	private Button btn_rate;
	private TextView txt_later;
	private String APP_PNAME = "com.mydimoda";
	public static boolean rate_app_click = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_rate_app);
		btn_rate = (Button) findViewById(R.id.btn_rate);
		txt_later = (TextView) findViewById(R.id.btn_later);
		btn_rate.setOnClickListener(this);
		txt_later.setOnClickListener(this);
		/*if(AppUtils.getPref("pro",Activity_RateApp.this) == null)
		{
			AppUtils.putPref("pro", "false", Activity_RateApp.this);
		}

		else
		{
			finish();
		}*/

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.btn_rate:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));


			AppUtils.putPref("pro", "true", Activity_RateApp.this);
			ParseUser user = ParseUser.getCurrentUser();
			//			user.put("Buy", true);
			user.put("ratedmyDiModa", true);
			user.saveInBackground();
			rate_app_click = true;
			SharedPreferenceUtil.putValue("inApp", "1");
			SharedPreferenceUtil.save(); // to reset counter when user has rated
			finish();
			break;
		case R.id.btn_later:
			finish();
			break;
		default:
			break;
		}
	}

}

package com.mydimoda;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotificationClickActivity extends Activity 
{
	Context mContext;
	NotificationManager mNotificationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		
		mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		
		String action= (String)getIntent().getExtras().get("NOTIFICATION");
		int requestId = (Integer) getIntent().getExtras().get("requestId");
		
		if(action.equals("done"))
		{
			mNotificationManager.cancel(requestId);
			Intent intentTL = new Intent(mContext, DMFashionActivity_7Hour.class);
			intentTL.putExtra("favorite", "no");
			intentTL.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentTL);
			finish();
		}
		
	}
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		finish();
	}

}

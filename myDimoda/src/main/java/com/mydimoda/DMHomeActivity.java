package com.mydimoda;

import java.util.List;
import java.util.Random;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
		System.out.println("Before"+AppUtils.getPref("pro", DMHomeActivity.this));
		mContext =  DMHomeActivity.this;
		
		final ParseUser user = ParseUser.getCurrentUser();
		boolean bPurchased = user.getBoolean("ratedMyDimoda");
		System.out.println("HomePurchased"+bPurchased);

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



		if(bPurchased == true)
		{
			System.out.println("TRUE"+bPurchased);
		}

		else
		{
			if(AppUtils.getPref("pro",DMHomeActivity.this) != null)
			{
				if(AppUtils.getPref("pro",DMHomeActivity.this).equalsIgnoreCase("false"))
				{
					AppRater.app_launched(DMHomeActivity.this);
				}
				else
				{
					AppUtils.putPref("pro", "true", DMHomeActivity.this);
				}
			}
			else if(AppUtils.getPref("pro",DMHomeActivity.this) == null)
			{
				AppUtils.putPref("pro", "false", DMHomeActivity.this);
				AppRater.app_launched(DMHomeActivity.this);
			}
			else
			{
				AppUtils.putPref("pro", "false", DMHomeActivity.this);
				AppRater.app_launched(DMHomeActivity.this);
			}
		}
		System.out.println("After"+AppUtils.getPref("pro", DMHomeActivity.this));
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

		System.out.println("Setting"+constant.gMenuList);
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
		try
		{
			showHomeFragment();
		}
		catch(Exception e)
		{

		}
	}
	
	

	/**
	 * 8 Am  Logic
	 */

	public void setAlarm_8hour()
	{
		Random r = new Random();
		int id = r.nextInt();
		/*AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(this, AlarmReciever_8hour.class);
	    intent.putExtra("id", "" + id);
	    PendingIntent pintent = PendingIntent.getService(this, 123456789,
	            intent, 0);

	    // Set the alarm to start at 8:30 a.m.
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    calendar.set(Calendar.HOUR_OF_DAY, 8);
	    calendar.set(Calendar.MINUTE, 00);
	    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000 * 60 * 1 * 60 * 24, pintent);
		AppUtils.putPref("key alarm","1",mContext);*/
		/*   Intent intent = new Intent(this, AlarmReciever_8hour.class);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 14); // For 1 PM or 2 PM
		calendar.set(Calendar.MINUTE, 3);
		 PendingIntent pintent = PendingIntent.getService(this, 123456789,
		            intent, 0);
		AlarmManager am = (AlarmManager) DMHomeActivity.this.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		                                AlarmManager.INTERVAL_DAY, pintent);*/
		
		/*Calendar firingAt = Calendar.getInstance(); 
		firingAt.set(Calendar.HOUR_OF_DAY, 8); 
		firingAt.set(Calendar.MINUTE, 00);
		firingAt.set(Calendar.SECOND, 00);
		
		long millis = firingAt.getTimeInMillis(); 
		
		AlarmManager am = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(mContext,
				AlarmReciever_8hour.class);
		intent.putExtra(ONE_TIME, Boolean.TRUE);
		intent.putExtra("id", "" + id);
		PendingIntent pi = PendingIntent.getBroadcast(this, id,
				intent, PendingIntent.FLAG_ONE_SHOT);
		int thirtySecondsFromNow = (int) (System
				.currentTimeMillis() + 30 * 1000);
		// am.set(AlarmManager.RTC_WAKEUP,24*60*60*1000,pi);
		// am.set(AlarmManager.RTC_WAKEUP,thirtySecondsFromNow,pi);
		 am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				  SystemClock.elapsedRealtime() + 15*1000, pi);
//		am.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY , pi);
		Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    calendar.set(Calendar.HOUR_OF_DAY, 8);
	    calendar.set(Calendar.MINUTE, 00);
//		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pi);
	    am.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
		Toast.makeText(DMHomeActivity.this, "TIMER SET", Toast.LENGTH_LONG).show();
		AppUtils.putPref("key alarm","1",mContext);*/
		

		AlarmManager am = (AlarmManager) mContext .getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(mContext, AlarmReciever_7hour.class);
		intent.putExtra(ONE_TIME, Boolean.TRUE);
		intent.putExtra("id", "" + id);
		PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_ONE_SHOT);
		int thirtySecondsFromNow = (int) (System .currentTimeMillis() + 30 * 1000);
		// am.set(AlarmManager.RTC_WAKEUP,24*60*60*1000,pi);
		// am.set(AlarmManager.RTC_WAKEUP,thirtySeconds
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 15*1000, pi);
	}
	
	
	
}

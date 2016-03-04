package com.mydimoda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author Keyur
 * This class will send a broadcast whenever date or time is changed.
 *
 */
public class DateChangeReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		System.out.println("DATE CHANGED --------------------");
		//Keyur
		//Starting helper service.
    	//context.startService(new Intent(context, ServiceHelper.class));
		ServiceHelper.setNotificationAlarm(context);
		ServiceHelper.setDateChangeBroadcaster(context);
	}
}

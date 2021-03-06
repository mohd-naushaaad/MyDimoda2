package com.mydimoda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;

import java.util.ArrayList;

public class AlarmReciever extends BroadcastReceiver {
    NotificationManager nm;
    DbAdapter m_DbAdapter;
    ArrayList<DatabaseModel> m_ArrayList;
    ArrayList<String> m_notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPreferenceUtil.getBoolean(constant.PREF_IS_NOTI_ENABLE, true)) {

            m_DbAdapter = new DbAdapter(context);
            m_ArrayList = new ArrayList<DatabaseModel>();
            m_notification = new ArrayList<String>();

		/* nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	     CharSequence from = "Sample Notification";
	     CharSequence message = "Notification different milliseconds ...";
	     PendingIntent contentIntent = PendingIntent.getActivity(context, 0,  new Intent(), 0);
	     Notification notif = new Notification(R.drawable.ic_launcher, "Notification Test...", System.currentTimeMillis());
	     notif.setLatestEventInfo(context, from, message, contentIntent);
	     notif.flags= Notification.FLAG_AUTO_CANCEL;
	     nm.notify((int) System.currentTimeMillis(), notif);
		*/

            System.out.println("Id in receiver " + intent.getStringExtra("id"));
            m_notification.add(intent.getStringExtra("id"));
            String yes = "true";

            int NOTIFICATION_ID = 234;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }

            String noOfDownload = intent.getStringExtra("id");

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "my_channel_01")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Do you still like your look")
                    .setContentText("Please re-rate your look to improve recommendation");
            mBuilder.setAutoCancel(true);
            Intent resultIntent = new Intent(context, DMAlgorithmActivity.class);
            resultIntent.putExtra("notification_counter", noOfDownload);
            resultIntent.putExtra("notification_yes", yes);
            System.out.println("Clicked" + noOfDownload);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(DMAlgorithmActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        }

		/*if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) 
		{
			 Setting the alarm here 

			Intent alarmIntent = new Intent(context, AlarmReciever.class);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

			AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			int interval = 3600000;
			//			int interval = 60000;

			manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

			//			Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
		}*/
    }
}

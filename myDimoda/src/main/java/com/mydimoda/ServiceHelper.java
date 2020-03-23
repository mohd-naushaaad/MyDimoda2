package com.mydimoda;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import android.util.Log;

/**
 * This helper service is used to perform certain task even if app is killed or force stopped by user or system.
 * <p>
 * There is no guarantee that the service will be restarted as soon as the app is killed, it will be restarted for sure not immediately but definitely and android system will determine when to restart.
 * <p>
 * Generally this service should called once only (eg.GlobalInstanceHolder which extends Application)
 *
 * @author keyur.tailor
 */
public class ServiceHelper extends JobIntentService {
    private static final String TAG = "ServiceHelper";
    public static boolean isServiceRunning;
    public static final int JOB_ID = 1;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isServiceRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            super.onStartCommand(intent, flags, startId);
            if (!isServiceRunning) {
                Log.i(TAG, "Service onStartCommand");
                isServiceRunning = true;
                setNotificationAlarm(getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Log.i(TAG, "Service onTaskRemoved");
        setNotificationAlarm(getApplicationContext());
        setDateChangeBroadcaster(getApplicationContext());
        super.onTaskRemoved(rootIntent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    public static void setNotificationAlarm(Context mContext) {

        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(mContext, AlarmReciever_7hour.class);
        intent1.putExtra("onetime", Boolean.FALSE);
        intent1.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 1111, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        //After after n minutes
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1 , pi); // miliSec * Seconds * Minutes

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 7); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second
        firingCal.set(Calendar.MILLISECOND, 0); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        am.cancel(pi);//Canceling previous alarm if any.

        //KEEP OPEN IF TESTING...
        //am.setRepeating(AlarmManager.RTC_WAKEUP, 1000 * 60 * 60 , 1000 * 60 * 60 , pi);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() , 1000 * 60 * 60 , pi);

        //OPEN WHEN APP IS LIVE...
        if (intendedTime >= currentTime) // you can add buffer time too here to ignore some small differences in milliseconds
        {
            //set from today
            am.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pi);
        } else {
            //set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();
            am.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pi);
        }
    }

    public static void setDateChangeBroadcaster(final Context mContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(mContext, DateChangeReceiver.class);

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);

                    //System.out.println("SET DATE CHANGE on ----> "+calendar.getTime().toString());

                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 222000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy");
        isServiceRunning = false;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        System.out.println("onHandleWork intent = [" + intent + "]");
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ServiceHelper.class, JOB_ID, work);
    }
}

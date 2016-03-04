package com.mydimoda;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AlarmReciever_7hour extends WakefulBroadcastReceiver {
    Context mContext;
    String message = "New Look! We have created a new look for you from your wardrobe. Why don't you check it out and add to your favourites if you like?";
    JSONObject mSendData;
    public static ArrayList<String> mIdList = new ArrayList<String>();
    public ArrayList<String> imageList = new ArrayList<String>();
    public ArrayList<Bitmap> imageBmpList = new ArrayList<Bitmap>();

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        SharedPreferences settings = mContext.getSharedPreferences(constant.PREFS_NAME, Context.MODE_PRIVATE);
        constant.gIsCloset = settings.getBoolean("isCloset", false);
        constant.gUserId = settings.getString("userid", "");

        //Keyur
        // if user is not logged in then do not generate notification.
        if (!TextUtils.isEmpty(constant.gUserId)) {
            if (isNetworkAvailable(mContext))
                getClothsFP();
            else
                generateNotification(mContext, message, null);
        }
    }

    public class MyAsyncTask extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
        // / server communicate using asyncTask
        ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
        JSONObject mResponseData;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            // Creating JSON Parser instance
            JSONPostParser jParser = new JSONPostParser();
            // getting JSON string from URL
            String mBaseUrl = "http://54.69.61.15/";
            mResponseData = jParser.getJSONFromUrl(mBaseUrl, mSendData.toString());
            return UploadsList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            parseResponse(mResponseData);
            super.onPostExecute(result);
        }
    }

    // / --------------------------------------------------------- makeJSONArray from clothlist --------------
    public JSONArray makeJSONArray(List<ParseObject> clothList) {
        ParseUser user = ParseUser.getCurrentUser();
        JSONArray clothArr = new JSONArray();
        if (clothList != null) {
            for (int i = 0; i < clothList.size(); i++) {
                if (constant.gIsCloset) {
                    ParseUser itemUser = clothList.get(i).getParseUser("User");
                    if (itemUser.getObjectId().equals(user.getObjectId())) {
                        ParseObject parseObj = clothList.get(i);
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("type", parseObj.get("Type"));
                            jsonObj.put("id", parseObj.getObjectId());
                            jsonObj.put("color", parseObj.get("Color"));
                            jsonObj.put("pattern", parseObj.get("Pattern"));
                            clothArr.put(jsonObj);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    ParseObject parseObj = clothList.get(i);
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("type", parseObj.get("Type"));
                        jsonObj.put("id", parseObj.getObjectId());
                        jsonObj.put("color", parseObj.get("Color"));
                        jsonObj.put("pattern", parseObj.get("Pattern"));

                        clothArr.put(jsonObj);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return clothArr;
    }

    // / ------------------------------------------------------ make blocked or item json arr --------------
    public JSONArray makeItemJSONArray(List<DMItemObject> list) {
        JSONArray arr = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", list.get(i).index);
                    obj.put("type", list.get(i).type);
                    arr.put(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    public JSONArray makeBlockedJSONArray(List<DMBlockedObject> list) {
        JSONArray arr = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                arr.put(makeItemJSONArray(list.get(i).blockedList));
            }
        }
        return arr;
    }

    // --------------------------------------------------------- make JSONObject to send server ----------
    public void makeSendData(JSONArray clothArr) {
        mSendData = new JSONObject();
        try {
            mSendData.put("version", "2");
            mSendData.put("category", constant.gCategory);
            mSendData.put("mode", constant.gMode);
            mSendData.put("closet", clothArr);
            mSendData.put("name", "genparams");
            mSendData.put("value", "1");
            //System.out.println("Sending"+constant.gCategory+""+constant.gMode +""+clothArr);


            if (constant.gCategory.equalsIgnoreCase(""))
                mSendData.put("category", "casual");
            if (constant.gMode.equalsIgnoreCase(""))
                mSendData.put("mode", "style me");
            else {
                mSendData.put("category", constant.gCategory);
                mSendData.put("mode", constant.gMode);
            }

            mSendData.put("blocked", makeBlockedJSONArray(constant.gBlockedList));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("data---------", mSendData.toString());
    }

    // ----------------------------------------------- parse response // -------------------------
    public void parseResponse(JSONObject data) {
        constant.gFashion = new DMBlockedObject();
        if (data != null) {
            //Log.e("response -----123", data.toString());
            try {
                JSONArray arr = data.getJSONArray("selection");
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        DMItemObject item = new DMItemObject(obj);
                        // If type we selected and type we get are same then check item.index is same or not
                        // If they are different , then assign our selected item.index to item object.
                        try {
                            if (AppUtils.getPref("type", mContext).equalsIgnoreCase(item.type)) {
                                if (!item.index.equalsIgnoreCase(AppUtils.getPref("index", mContext))) {
                                    System.out.println("Not Same");
                                    item.index = AppUtils.getPref("index", mContext);
                                    item.type = AppUtils.getPref("type", mContext);
                                }
                            }
                        } catch (Exception e) {
                        }
                        constant.gFashion.blockedList.add(item);
                        constant.gFashion.setBlockedList(constant.gFashion.blockedList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mIdList.clear();
        if (constant.gFashion != null) {
            for (int i = 0; i < constant.gFashion.blockedList.size(); i++) {
                mIdList.add(constant.gFashion.blockedList.get(i).index);
            }
        }
        if (mIdList != null && mIdList.size() > 0)
            getClothsFP2(mIdList);
        else
            generateNotification(mContext, message, null);
    }


    // / -------------------------------------- get cloth from parse.com
    public void getClothsFP() {
        ParseUser user = ParseUser.getCurrentUser();

        int type = 1 + (int) (Math.random() * ((3 - 1) + 1));
        if (type == 1)
            constant.gCategory = "casual";
        else if (type == 2)
            constant.gCategory = "after5";
        else if (type == 3)
            constant.gCategory = "formal";
        else
            constant.gCategory = "casual";
        ParseQuery<ParseObject> query = null;
        if (constant.gIsCloset) {
            query = ParseQuery.getQuery("Clothes");
            query.whereEqualTo("User", user);
        } else
            query = ParseQuery.getQuery("DemoCloset");

        if (constant.gCategory.equals("after5") || constant.gCategory.equals("formal"))
            query.whereNotEqualTo("Category", "casual");
        if (constant.gCategory.equals("casual")) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("shirt");
            list.add("trousers");
            query.whereContainedIn("Type", list);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {
                if (e == null) {
                    makeSendData(makeJSONArray(clothList));
                    new MyAsyncTask().execute();
                    //makeClothList(clothList);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // / -------------------------------------- get cloth from parse.com
    public void getClothsFP2(List<String> list) {
        ParseQuery<ParseObject> query = null;
        if (constant.gIsCloset)
            query = ParseQuery.getQuery("Clothes");
        else
            query = ParseQuery.getQuery("DemoCloset");

        query.whereContainedIn("objectId", list);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clothList, ParseException e) {
                if (e == null)
                    makeClothList(clothList);
            }
        });
    }

    // --------------------------------------------------------- make photo list from parse object list --------------
    public void makeClothList(List<ParseObject> clothList) {
        imageList.clear();
        if (clothList != null) {
            String urlImage = "";
            if (clothList.size() > 0) {
                int size = clothList.size();
                if (size >= 4)
                    size = 4;
                for (int i = 0; i < size; i++) {
                    ParseFile urlObject = (ParseFile) clothList.get(i).get("ImageContent");
                    urlImage = urlObject.getUrl();
                    Log.e("URL image-> " + i, urlImage);
                    imageList.add(urlImage);
                }
            }
            //generateNotification(mContext, message, urlImage);
            new ImageAsync().execute();
        }
    }

    public class ImageAsync extends AsyncTask<Void, Void, Void> {
        Bitmap bmp = null;

        public ImageAsync() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                imageBmpList.clear();
                for (int i = 0; i < imageList.size(); i++) {
                    URL url = new URL(imageList.get(i));
                    Bitmap original = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    Bitmap bitmap = Bitmap.createScaledBitmap(original, 500, 500, true);
                    System.out.println("Original size--> " + i + " --> " + original.getByteCount());
                    System.out.println("Decoded size--> " + i + " --> " + bitmap.getByteCount());
                    imageBmpList.add(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            //generateNotification(mContext, message, bmp);
            generateCustomNotification();
            super.onPostExecute(result);
        }
    }

    public void generateCustomNotification() {
        try {
            message = "New Look! We have created a new look for you from your wardrobe.";
            String when = "";
            Bitmap bmp1 = null;
            Bitmap bmp2 = null;
            Bitmap bmp3 = null;
            Bitmap bmp4 = null;
            NotificationManager mNotificationManager;
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.drawable.ic_launcher, message, new Date().getTime());
            RemoteViews notificationView = new RemoteViews(mContext.getPackageName(), R.layout.notification_view);

            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            Date dt = Calendar.getInstance().getTime();
            try {
                when = sdfs.format(dt);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                bmp1 = imageBmpList.get(0);
                bmp2 = imageBmpList.get(1);
                bmp3 = imageBmpList.get(2);
                bmp4 = imageBmpList.get(3);
            } catch (Exception e) {
            }

            //notificationView.setImageViewResource(R.id.imgAppicon, R.drawable.ic_launcher);
            notificationView.setTextViewText(R.id.txtNoteDetail, message);
            notificationView.setTextViewText(R.id.txtNoteTime, when);
            //ParseApplication.getInstance().mImageLoader.displayImage(imageList.get(0), R.id.img1);
            //notificationView.setImageViewUri(R.id.img1, bmp1);

            if (imageBmpList.size() > 2) {
                notificationView.setViewVisibility(R.id.llt2images, View.GONE);
                notificationView.setViewVisibility(R.id.llt4images, View.VISIBLE);
                if (bmp1 != null)
                    notificationView.setImageViewBitmap(R.id.img1, bmp1);
                if (bmp2 != null)
                    notificationView.setImageViewBitmap(R.id.img2, bmp2);
                if (bmp3 != null)
                    notificationView.setImageViewBitmap(R.id.img3, bmp3);
                if (bmp4 != null)
                    notificationView.setImageViewBitmap(R.id.img4, bmp4);
            } else {
                notificationView.setViewVisibility(R.id.llt2images, View.VISIBLE);
                notificationView.setViewVisibility(R.id.llt4images, View.GONE);
                if (bmp1 != null)
                    notificationView.setImageViewBitmap(R.id.img1full, bmp1);
                if (bmp2 != null)
                    notificationView.setImageViewBitmap(R.id.img2full, bmp2);
                else
                    notificationView.setViewVisibility(R.id.img2full, View.GONE);
            }


            //Generated random number includes min and max number
            //int requestId = 1 + (int)(Math.random() * ((1000 - 1) + 1));
            int requestId = 1234;
            notification.contentView = notificationView;
            notification.bigContentView = notificationView;
            notification.defaults |= Notification.DEFAULT_ALL;
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;

            Intent intentTL = new Intent(mContext, NotificationClickActivity.class);
            intentTL.putExtra("NOTIFICATION", "done");
            intentTL.putExtra("requestId", requestId);
            intentTL.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestId, intentTL, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationView.setOnClickPendingIntent(R.id.lltNotificationMain, pendingIntent);
            mNotificationManager.cancelAll();
            mNotificationManager.notify(requestId, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateNotification(Context context, String m_message, Bitmap mBigImage) {
        if (SharedPreferenceUtil.getBoolean(constant.PREF_IS_NOTI_ENABLE, true)) {

            int icon = R.drawable.ic_launcher;
            Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            Bitmap image = mBigImage;
            URL url;
            long when = System.currentTimeMillis();
            String title = context.getString(R.string.app_name);

            //Intent intentTL = new Intent(context, DMMyLookActivity.class);
            //intentTL.putExtra("notification_look", "true");
            Intent intentTL = new Intent(context, DMFashionActivity_7Hour.class);
            intentTL.putExtra("favorite", "no");
            intentTL.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1234, intentTL, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setSmallIcon(icon)
                    .setLargeIcon(icon1)
                    .setContentText(m_message);


            if (mBigImage != null) // with picture.
            {
                NotificationCompat.BigPictureStyle bigPic = new NotificationCompat.BigPictureStyle();
                bigPic.bigPicture(image);
                bigPic.setSummaryText(m_message);
                bigPic.setBigContentTitle(title);
                mBuilder.setStyle(bigPic);
            } else // without picture
            {
                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(m_message);
                bigText.setBigContentTitle(title);
                //bigText.setSummaryText("DEF");
                mBuilder.setStyle(bigText);
            }


            mBuilder.setAutoCancel(true);
            mBuilder.setWhen(when);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            mBuilder.setContentIntent(pendingIntent);
            //mBuilder.addAction(0, "Open", pendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1234, mBuilder.build());
        }

    }

    public static boolean isNetworkAvailable(Context mContext) {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.e("Network Testing", "***Available***");
                        return true;
                    }

        }
        Log.e("Network Testing", "***Not Available***");
        return false;
        /*NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
		{
			Log.e("Network Testing", "***Available***");
			return true;
		}
		Log.e("Network Testing", "***Not Available***");
		return false;*/
    }

}

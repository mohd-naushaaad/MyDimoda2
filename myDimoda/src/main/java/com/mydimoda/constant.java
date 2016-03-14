package com.mydimoda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.mydimoda.model.DatabaseModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;

import org.apache.http.NameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class constant {

    // /// font face
    public static Typeface boldfontface = null;
    public static Typeface fontface = null;

    // / user data
    public static String gUserName = "";
    public static String gEmail = "";
    public static String gPassword = "";
    public static String gUserId = "";
    public static boolean gIsCloset = false;
    public static int maxCount = 5;

    // / server side, shared preference
    public static String PREFS_NAME = "MyPrefName";
    public static ArrayList<NameValuePair> nameValuePairs;
    public static ProgressDialog vProgress;
//	public static String 					gPrefUrl 	= "http://54.149.157.66/index.php";


    //	public static String 					gPrefUrl 	= "http://52.25.182.16/index.php";
    public static String gPrefUrl = "http://purchase.myDiModa.com/index.php";
    // / engine value
    public static String[] gColor = {"empty", "black", "blue", "brown", "dark blue",
            "dark green", "dark red", "gray", "green",
            "light blue", "orange", "pink", "purple", "red",
            "white", "yellow"};
    public static String[] gPattern = {"empty", "dot", "plaid", "stripe", "florial"};

    // / menu item
    public static String[] menuArr = {"HOME", "STYLE ME", "HANG UP", "ORGANIZE", "PURCHASE", "SETTINGS"};
    public static List<String> gMenuList = Arrays.asList(menuArr);

    // cloth order
    public static int SHIRT = 0;
    public static int JACKET = 1;
    public static int TROUSERS = 2;
    public static int TIE = 3;
    public static int SUIT = 4;
    public static int SHOES = 5;
    public static int NONE = 6;

    // / category when user click item of occasion list
    public static String gCategory = "";
    public static String gMode = "";
    public static int gLikeNum = 0;
    public static List<DMItemObject> gItemList = new ArrayList<DMItemObject>();
    public static List<DMBlockedObject> gBlockedList = new ArrayList<DMBlockedObject>();
    public static DMBlockedObject gFashion = null;
    public static DatabaseModel gFashiondb = null;

    // SAINT1021
    public static String gFashionID = "";

    public static Bitmap gTakenBitmap;
    public static Bitmap gCropBitmap = null;
    public static String gMessage = null;
    public static String gColorVal = null;
    public static String gPatternVal = null;

    public static boolean gIsStart = false;

    public constant() {

    }

    // / ------------------------------------------- get current date with
    // yyyy-MM-dd --------------------
    public static String getCurrentDate() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return fDate;
    }

    public static String getCurrentHour() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("MM/dd/yy hh:mm a").format(cDate);
        return fDate;
    }

    // / ------------------------------------------- get current date with "
    // Aug-2-2014 --------------------
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();

        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        String timeStr = getMonth(currentMonth) + " " + currentDay + ", "
                + currentYear;
        return timeStr;
    }

    // / ----------------------------------------------- get custom date , for
    // example Aug - 02- 2014 -------
    public static String getCustomDate(String time) {
        String[] arr = time.split("-");

        String timeStr = getMonth(Integer.parseInt(arr[1])) + " " + arr[2]
                + ", " + arr[0];
        return timeStr;
    }

    public static String getMonth(int currentMonth) {
        String month = "";
        if (currentMonth == 1) {
            month = "Jan";
        } else if (currentMonth == 2) {
            month = "Feb";
        } else if (currentMonth == 3) {
            month = "Mar";
        } else if (currentMonth == 4) {
            month = "Apr";
        } else if (currentMonth == 5) {
            month = "May";
        } else if (currentMonth == 6) {
            month = "Jun";
        } else if (currentMonth == 7) {
            month = "Jul";
        } else if (currentMonth == 8) {
            month = "Aug";
        } else if (currentMonth == 9) {
            month = "Sep";
        } else if (currentMonth == 10) {
            month = "Oct";
        } else if (currentMonth == 11) {
            month = "Nov";
        } else if (currentMonth == 12) {
            month = "Dec";
        }

        return month;
    }

    // / -------------------------------- alert dialog ----------------
    public static void alertbox(String title, String mymessage, Context context) {
        new AlertDialog.Builder(context).setMessage(mymessage).setTitle(title)
                .setCancelable(true)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }

    // / ------------------------------------ show progress , hide progress
    // -----------------------------
    public static void showProgress(Context context, String message) {

        try {
            vProgress = new ProgressDialog(context);
            vProgress.setMessage(message);
            vProgress.setCancelable(true);
            vProgress.show();
        } catch (Exception e) {

        }
    }

    public static void hideProgress() {
        if (vProgress != null) {
            vProgress.dismiss();
        }
    }

    // / ------------------------------------- menu item clicked
    // ------------------
    public static void selectMenuItem(Activity context, int pos,
                                      boolean isFinish) {

        if (pos == 0) // --- home
        {
            Intent intent = new Intent(context, DMHomeActivity.class);
            context.startActivity(intent);
            context.finish();

        } else if (pos == 1) // --- my look
        {

            Intent intent = new Intent(context, DMMyLookActivity.class);
            context.startActivity(intent);
            if (isFinish)
                context.finish();
        } else if (pos == 2) // --- hang up
        {

            constant.gCategory = "formal";
            Intent intent = new Intent(context, DMHangUpActivity.class);
            intent.putExtra("FromMain", true);
            context.startActivity(intent);
            if (isFinish)
                context.finish();
        } else if (pos == 3) // --- organize
        {

            constant.gCategory = "formal";
            Intent intent = new Intent(context, DMOrganizeActivity.class);
            intent.putExtra("findme", "show");
            context.startActivity(intent);
            if (isFinish)
                context.finish();

        } else if (pos == 4) // --- purchase
        {
            Intent intent = new Intent(context, DMFindMeActivity.class);
            context.startActivity(intent);
            if (isFinish)
                context.finish();

        } else if (pos == 5)// --- logout
        {
            Intent intent = new Intent(context, DMSettingActivity.class);
            context.startActivity(intent);
            if (isFinish)
                context.finish();
        }
    }

    public static final int MAX_GAL_IMAGE_COUNT = 12;
    public static final String EMPTY_TYPE = "Undefined";
    public static final String FRM_DIALG_KEY = "isFromDialog";
    public static final String FRM_DIALG_PROCESS_KEY = "isFromDialogProcess";


    //notification enable disable pref key
    public static final String PREF_IS_NOTI_ENABLE = "notificationenabledisable";
    public static final String PREF_MAX_COUNT = "maxearnedcounts";
    public static final String PREF_MAX_COUNT_GVN = "maxearnedcountsfor";
    /* public static final String PREF_MAX_COUNT_GVN_shirt = "maxearnedcountsforshirt";
     public static final String PREF_MAX_COUNT_trousers = "maxearnedcountsfortrousers";
     public static final String PREF_MAX_COUNT_GVN_jacket = "maxearnedcountsforjacket";
     public static final String PREF_MAX_COUNT_GVN_suit = "maxearnedcountsforsuits";*/
    public static final String USER_MAX_COUNT = "MaxUserCount";
    public static final String PREF_IS_GALRY_DIALOG_SHOWN = "isgalshown";


}

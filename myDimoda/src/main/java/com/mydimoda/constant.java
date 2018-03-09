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

import com.mydimoda.activities.PlanANewTripActivity;
import com.mydimoda.model.CropListModel;
import com.mydimoda.model.DatabaseModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;

import org.apache.http.NameValuePair;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private static ProgressDialog vProgress;
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
    public static String[] menuArr = {"HOME", "STYLE ME", "HANG UP", "ORGANIZE", "PURCHASE", "PLAN A NEW TRIP", "SETTINGS"};
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
    public static final String COUNT = "Count";

    public static final int RESULT_SIZE = 1000;

    public static final String API_ITEM_OFFSET = "ItemPageOffset";

    public static final int casual = 0, formal = 1, business = 2;
    //By Parth Ukani
    public static final String base_url = "http://54.69.61.15/";
    public static List<DMItemObject> helpSelection = new ArrayList<DMItemObject>();
    public static final String helpME = "help me";
    public static final String styleME = "style me";
    //Bundle Constant
    public static final String BUNDLE_LOOKLISTING = "bundle_looklisting";
    public static final String BUNDLE_LIST_OF_SELECTION = "bundle_list_of_selection";
    public static final String BUNDLE_ISFROMPLANNEWTRIP = "bundle_isfromplannewtrip";
    public static final String BUNDLE_CATEGORY = "bundle_category";
    public static final String BUNDLE_MODE = "bundle_mode";
    public static String BUNDLE_TRIP_NAME = "trip_name";
    public static final String BUNDLE_START_DATE = "bundle_start_date";
    public static final String BUNDLE_TRIP_LIST_LOOKS = "bundle_trip_looks";

    public static String TRIPNAME = "";
    public static Date STARTDATE = new Date();


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
            vProgress = null;
            vProgress = new ProgressDialog(new WeakReference<>(context).get());
            vProgress.setMessage(message);
            vProgress.setCancelable(false);
            vProgress.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHard(Context context, String message) {
        try {
            vProgress = null;
            vProgress = new ProgressDialog(new WeakReference<>(context).get());
            vProgress.setMessage(message);
            vProgress.setCancelable(false);
            vProgress.show();
        } catch (Exception e) {
            e.printStackTrace();
            vProgress = null;
            vProgress = new ProgressDialog(context);
            showProgressHard(context, message);
        }
    }

    public static void hideProgress() {
        if (vProgress != null) {
            try {
                vProgress.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
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

        } else if (pos == 5)// --- plan a new trip
        {
            Intent intent = new Intent(context, PlanANewTripActivity.class);
            context.startActivity(intent);
            if (isFinish)
                context.finish();
        } else if (pos == 6)// --- logout
        {
            Intent intent = new Intent(context, DMSettingActivity.class);
            context.startActivity(intent);
            if (isFinish)
                context.finish();
        }
    }

    public static int max_lic_count = 5;

    public static final int MAX_GAL_IMAGE_COUNT = 12;
    public static final String EMPTY_TYPE = "Undefined";
    public static final String FRM_DIALG_KEY = "isFromDialog";
    public static final String FRM_DIALG_PROCESS_KEY = "isFromDialogProcess";


    //notification enable disable pref key
    public static final String PREF_IS_NOTI_ENABLE = "notificationenabledisable";
    public static final String PREF_MAX_COUNT = "maxearnedcounts";
    public static final String PREF_MAX_COUNT_GVN = "maxearnedcountsfor";
    /* public static final String PREF_MAX_COUNT_GVN_shirt = "maxearnedcountsforshirt";*/
    public static final String PREF_MAX_COUNT_CONFIGURED = "ismaxcountconfigured";
    public static final String RATED_APP = "ratedmyDiModa";
    public static final String PREF_CLOTH_COUNT = "maxearnedcountsforsuits";
    public static final String USER_MAX_COUNT = "MaxUserCount";
    public static final String PREF_IS_GALRY_DIALOG_SHOWN = "isgalshown";
    public static final String USER_MAX_COUNT_INITILISED = "maxusercountinitilised";

    // Dipen Saved image temp to add more images
    private static ArrayList<CropListModel> mCroppedItemListModelTempArray;

    public static ArrayList<CropListModel> getTempImageLst() {
        if (mCroppedItemListModelTempArray == null) {
            mCroppedItemListModelTempArray = new ArrayList<>();
        }
        return mCroppedItemListModelTempArray;
    }

    //Mayur to save array of cropped images for otehr view
    private static ArrayList<CropListModel> mCroppedItemListModelArray;

    public static ArrayList<CropListModel> getImageLst() {
        if (mCroppedItemListModelArray == null) {
            mCroppedItemListModelArray = new ArrayList<>();
        }
        return mCroppedItemListModelArray;
    }

    //Mayur to save array of cropped images for otehr view
    private static ArrayList<Bitmap> clothsBitmapLst;

    public static ArrayList<Bitmap> getclothsBitmapLst() {
        if (clothsBitmapLst == null) {
            clothsBitmapLst = new ArrayList<>();
        }
        return clothsBitmapLst;
    }

    public static void clearClothBitmapList() {
        if (clothsBitmapLst != null) {
            for (Bitmap mBitmap : clothsBitmapLst) {
                mBitmap.recycle();
            }
            clothsBitmapLst.clear();
        }
    }

    public static String APP_LINK = "\nApp Store: http://apple.co/235CP4Y\n" +
            "Play Store: http://bit.ly/26tp8RZ";

    private static ArrayList<String> mStatuses;

    public static String getRandomStatus() {
        if (mStatuses == null || mStatuses.isEmpty()) {
            mStatuses = new ArrayList<>();
            mStatuses.add("Confidence provided by myDiModa.");
            mStatuses.add("Professionally styled by myDiModa.");
            mStatuses.add("Enhancing my look with myDiModa.");
        }
        try {
            return mStatuses.get(getRandom(0, 2));
        } catch (Exception e) {
            e.printStackTrace();
            return mStatuses.get(0);
        }
    }

    public static int getRandom(int from, int to) {
        if (from < to) { // incase some one tries to be funny
            return (new Random().nextInt(to - from + 1) + from);
        } else {
            return (new Random().nextInt(from - to + 1) + to);
        }
    }

    //ShowcaseView pref constants
    public static final String PREF_IS_HOME_SHOWN = "ishomeshown";
    public static final String PREF_IS_OCCASION_SHOWN = "isocassionshown";
    public static final String PREF_IS_STYLE_SHOWN = "isstyleshown";
    public static final String PREF_IS_FSN_SHOWN = "isfsnshown";
    public static final String PREF_IS_HANGUP_SHOWN = "ishangupshown";
    public static final String PREF_IS_HANGUP_HELP_SHOWN = "ishanguphelpshown";
    //Add by parth ukani
    public static final String PREF_IS_PLAN_NEW_TRIP_SHOWN = "prefisplannewtripshown";
    public static final String PREF_IS_REVIEW_TRIP_SHOWN = "prefisreviewtripshown";
    public static final String PREF_IS_SELECT_MERCHANDISE_ITEM_SHOWN = "prefismerchandiseshown";
    public static final String PREF_IS_LOOK_LISTING = "prefislooklisting";


    public static final String PREF_IS_FIND_SHOWN = "isfindshown";
    public static final String PREF_IS_AUTO_SHOWN = "isautoshown";
    public static final String PREF_IS_LUCKY_AUTO_SHOWN = "isluckyautoshown";

    public static final String PREF_IS_ORGANISE_SHOWN = "isorganiseshown";
    public static final String PREF_IS_SETTING_SHOWN = "issettingshown";
    public static final String PREF_IS_EXACT_SHOWN = "isexactshown";

    public static final String PREF_IS_DETAIL_SHOWN = "isdetilshown";

    public static final String PREF_IS_CAMERA_OPTION_SHOWN = "iscameraoptionhown";

    public static final String PREF_IS_CROP_SHOWN = "isfindcropshown";
    public static final String PREF_IS_CAPTURE_ACT_SHOWN = "iscaptureshown";
    public static final String PREF_IS_DELETE_ACT_SHOWN = "isdeleteisiscaptureshown";


    // find cloths bundle key
    public static final String SHOP_NAME = "shopname";
    public static final String SORT_BY_KEY = "sortby";


    // api key
    public static final String SORT = "Sort";

    public static final String SORT_HI_LO = "high-to-low";
    public static final String SORT_LO_HI = "low-to-high";
    public static final String SORT_RELEVANCE = "relevance"; // default


    //Shop names api functions
    public static final String All_OPTION = "productloop";
    public static final String AMAZON_SHOP = "getAWSdata";
    public static final String SHOPSTYLE_SHOP = "getShopStyleData";
    public static final String ASOS_SHOP = "getAsosData";


    // for hangup

    public static final String FRM_DETAIL_FOR_HANGUP_KEY = "isfromdetailforhangup";


    public static final String IMAGE_BYTEARRY_KEY = "imagebytearraykey";

    public static final String IMAGE_POS_KEY = "imageposkey";

    //mayur added for fixing cloth swap issue in help me
    public static List<DMItemObject> gItemListTemp = new ArrayList<DMItemObject>();


    public static final String INTENT_NOTI_KEY = "fromnoti";


}

package com.mydimoda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMDialogGridAdapter;
import com.mydimoda.interfaces.DialogItemClickListener;
import com.mydimoda.model.DialogImagesModel;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class AppUtils {

    public static String yes = "";
    public static String brand = "";

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public static void showAlertDialog(String message, Context m_Context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }

                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Shared Preferences
     *
     * @param key
     * @param value
     * @param context
     */

    public static void putPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    /**
     *  Boolean preferences
     */

    /**
     * Set Defaults
     *
     * @param key
     * @param value
     * @param context
     */
    public static void setDefaults(String key, Boolean value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getDefaults(Context context, String key, boolean defaultvalue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultvalue);
    }


    // Converts to celsius
    public static double convertFahrenheitToCelsius(double fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    // Converts to fahrenheit
    public static double convertCelsiusToFahrenheit(double celsius) {
        return ((celsius * 9) / 5) + 32;
    }


    /**
     * returns the string, the first char uppercase
     *
     * @param target
     * @return
     */
    public final static String asUpperCaseFirstChar(final String target) {

        if ((target == null) || (target.length() == 0)) {
            return target; // old dev: You could omit this check and simply live with an
            // exception if you like. //-new dev: hey thats not a way to talk to some one reading ur crappy code!!
        }
        return Character.toUpperCase(target.charAt(0))
                + (target.length() > 1 ? target.substring(1) : "");
    }

    /**
     * for calculating and assigning style options
     *
     * @param items
     */
    public void stylePointProcessor(int items, final String mType, final Context mContext) {
        if (items == 2 && !SharedPreferenceUtil.getBoolean(constant.PREF_MAX_COUNT_GVN + mType, false)) {
            final ParseUser user = ParseUser.getCurrentUser();

            final int mMaxCount = user.getInt(constant.USER_MAX_COUNT);
            user.put(constant.USER_MAX_COUNT, mMaxCount + 5);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        SharedPreferenceUtil.putValue(constant.PREF_MAX_COUNT_GVN + mType, true);
                        constant.maxCount = user.getInt(constant.USER_MAX_COUNT);
                        constant.hideProgress();
                    } else {
                        Toast.makeText(mContext, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (items > 2) {
            final ParseUser user = ParseUser.getCurrentUser();
            final int mMaxCount = user.getInt(constant.USER_MAX_COUNT);
            user.put(constant.USER_MAX_COUNT, mMaxCount + 1);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        constant.maxCount = user.getInt(constant.USER_MAX_COUNT);

                    } else {
                        Toast.makeText(mContext, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    static Dialog mGalleryDialog;

    /**
     * Displays a dialog with images from gallery
     * with time difference of 24hrs
     *
     * @param mContext
     */
    public static void showGalleryDialog(Context mContext, final DialogItemClickListener mCallback, boolean isFrmHome)
            throws ClassCastException {
        if (mGalleryDialog == null || !mGalleryDialog.isShowing()) {
            final ArrayList<DialogImagesModel> mGallerImageLst = new ArrayList();
            mGallerImageLst.clear();
            String[] projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATA};
            // content:// style URI for the "primary" external storage volume
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // Make the query.
            Cursor cur = mContext.getContentResolver().query(images,
                    projection, // Which columns to return
                    null,       // Which rows to return (all rows)
                    null,       // Selection arguments (none)
                    MediaStore.Images.Media.DATE_TAKEN        // Ordering
            );
            Log.i("ListingImages", " query count=" + cur.getCount());
            if (cur.moveToLast()) {
                int bucketColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID);
                int PathColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);
                int DateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                long currenttime = System.currentTimeMillis();
                int i = 0;
                int count = cur.getCount();
                DialogImagesModel mOdle;
                do {
                    try {
                        // Get the field values
                        if (Long.parseLong(cur.getString(DateColumn)) <= (currenttime - (24 * 60 * 60 * 1000) * i) || i == 0) {
                            mOdle = new DialogImagesModel();
                            mOdle.setOrigId(Long.valueOf(cur.getString(bucketColumn)));
                            mOdle.setImagePathl(cur.getString(PathColumn));
                            mGallerImageLst.add(mOdle);
                            i++;
                     /*       Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(Long.parseLong(cur.getString(DateColumn)));
                            Log.e(this.getLocalClassName(), c.getTime() + "");*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while ((count > constant.MAX_GAL_IMAGE_COUNT) ? cur.moveToPrevious() && (i < constant.MAX_GAL_IMAGE_COUNT) : cur.moveToPrevious());
            }


            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo));
            View mView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.dialog_gallery, null);
            mBuilder.setView(mView);

            GridView mGalGridview = (GridView) mView.findViewById(R.id.dialog_gridview);
            TextView mGalTitlevw = (TextView) mView.findViewById(R.id.dialog_gal_title);
            TextView mGalTitleTextvw = (TextView) mView.findViewById(R.id.dialog_gal_title_txt);
            TextView mGalleryTvw = (TextView) mView.findViewById(R.id.dialog_gallery_tv);
            ImageView mCloseImgVw = (ImageView) mView.findViewById(R.id.dialog_gal_close);

            if (!isFrmHome) {
                mGalTitlevw.setText("Select");
            }

            FontsUtil.setExistenceLight(mContext, mGalTitlevw);
            FontsUtil.setExistenceLight(mContext, mGalTitleTextvw);
            FontsUtil.setExistenceLight(mContext, mGalleryTvw);

            mCloseImgVw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onCloseClick();
                }
            });

            mGalleryTvw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCallback.onGalleryClick();
                }
            });

            mGalGridview.setAdapter(new DMDialogGridAdapter(mContext, mGallerImageLst));
            mGalGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mCallback.onImageClick(mGallerImageLst.get(position).getImagePathl());
                }
            });
            mGalleryDialog = mBuilder.create();
            mGalleryDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    mCallback.onDialogVisible(dialog);
                }
            });
            mGalleryDialog.show();
        }
        AppUtils.setDefaults(constant.PREF_IS_GALRY_DIALOG_SHOWN, true, mContext);
    }
    public static  boolean isOnline(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int generatRandomPositiveNegitiveValue(int max, int min) {
        int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
        return ii;
    }
    public static int generatRandomPositiveNegitiveValue() {
        int ii = -(10) + (int) (Math.random() * ((40 - (-10)) + 1));
        return ii;
    }
}

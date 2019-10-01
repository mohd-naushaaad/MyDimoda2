package com.mydimoda.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Path and db name definition
     */
    private static String DB_PATH = "/data/data/com.mydimoda/databases/";
    private static String DB_NAME = "myDiModa.sqlite";

    private static String TAG = "TAG";
    /***
     * Context and SqliteDatabase definition
     */
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);


        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }

    /**
     * Database Creation
     *
     * @throws IOException
     */
    public void createDataBase() throws IOException {


        boolean mDataBaseExist = checkDataBase();
        Log.d(TAG, "create DB in Helper. Data exists?" + mDataBaseExist);
        if (!mDataBaseExist) {
            Log.d(TAG, "get Writable in DatabaseHelper");
            this.getWritableDatabase();
            try {
                Log.d(TAG, "copy Database");
                copyDataBase();
            } catch (IOException mIOException) {
                Log.d(TAG, "copy not succeed");
                throw new Error("ErrorCopyingDataBase");

            }
        }
    }

    /**
     * Check database Exist or not
     *
     * @return
     */
    public boolean checkDataBase() {

        SQLiteDatabase mCheckDataBase = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            mCheckDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException mSQLiteException) {
            Log.e(TAG, "DatabaseNotFound " + mSQLiteException.toString());
        }

        if (mCheckDataBase != null) {
            mCheckDataBase.close();
        }
        return mCheckDataBase != null;

    }


    /**
     * Copy Database from assets code
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {

        Log.d(TAG, "copy");
        InputStream mInput = mContext.getResources().getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;
        Log.d(TAG, "Output:" + outFileName);
        File createOutFile = new File(outFileName);
        if (!createOutFile.exists()) {
            createOutFile.mkdir();
        }
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();

    }


    /**
     * open the database after copying from assets
     *
     * @return
     * @throws SQLException
     */

    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }


    @Override

    public synchronized void close() {

        if (mDataBase != null)
            mDataBase.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
		/*try 
		{
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
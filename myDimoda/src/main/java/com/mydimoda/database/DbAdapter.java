package com.mydimoda.database;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mydimoda.model.DatabaseModel;

public class DbAdapter {

	private final Context mContext;
	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;
	private static String TAG = "TAG";
	private static final String TABLE_CATEGORY  = "Categories";
	private static final String KEY_ID = "id";
	private static final String KEY_VERSION = "version";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_FEEDBACK = "feedback";
	private static final String KEY_TARGET = "target";
	private static final String KEY_NAME ="name";
	private static final String KEY_VALUE ="value";
	private static final String KEY_COLOR ="color";
	private static final String KEY_PATTERN ="pattern";
	private static final String KEY_TYPE ="type";
	private static final String KEY_DATETIME = "datetime";

	ArrayList<DatabaseModel> selectedTimezoneList= new ArrayList<DatabaseModel>();

	public DbAdapter(Context context) 
	{
		this.mContext = context;
		mDbHelper = new DatabaseHelper(mContext);
		Log.d(TAG,"done");
	}

	public DbAdapter createDatabase() throws SQLException 
	{
		try 
		{
			Log.d(TAG,"create database");
			mDbHelper.createDataBase();
		} 
		catch (IOException mIOException) 
		{
			throw new Error("UnableToCreateDatabase"+ mIOException.toString());
		}
		return this;
	}

	public DbAdapter open() throws SQLException 
	{
		try 
		{
			Log.d(TAG,"Open");
			mDbHelper.openDataBase();
			mDbHelper.close();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mDb = mDbHelper.getWritableDatabase();
		} 
		catch (SQLException mSQLException) 
		{
			Log.e(TAG, mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() 
	{
		mDbHelper.close();
	}



	/**
	 * Add Data
	 * @param m_DatabaseModel
	 * @return
	 */

	public long add(DatabaseModel m_DatabaseModel)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_VERSION, m_DatabaseModel.getVersion());
		initialValues.put(KEY_CATEGORY, m_DatabaseModel.getCategory());
		initialValues.put(KEY_FEEDBACK, m_DatabaseModel.getFeedback());
		initialValues.put(KEY_TARGET, m_DatabaseModel.getTarget());
		initialValues.put(KEY_NAME, m_DatabaseModel.getName());
		initialValues.put(KEY_VALUE, m_DatabaseModel.getValue());
		initialValues.put(KEY_COLOR, m_DatabaseModel.getColor());
		initialValues.put(KEY_PATTERN, m_DatabaseModel.getPattern());
		initialValues.put(KEY_TYPE, m_DatabaseModel.getType());
		initialValues.put(KEY_DATETIME, m_DatabaseModel.getDatetime());
	
		return mDb.insert(TABLE_CATEGORY, null, initialValues);
	}

	public boolean updateCity(DatabaseModel m_DatabaseModel, String _id)
	{
		Log.e("", "_id "+_id);
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_VERSION, m_DatabaseModel.getVersion());
		initialValues.put(KEY_CATEGORY, m_DatabaseModel.getCategory());
		initialValues.put(KEY_FEEDBACK, m_DatabaseModel.getFeedback());
		initialValues.put(KEY_TARGET, m_DatabaseModel.getTarget());
		initialValues.put(KEY_NAME, m_DatabaseModel.getName());
		initialValues.put(KEY_VALUE, m_DatabaseModel.getValue());
		initialValues.put(KEY_COLOR, m_DatabaseModel.getColor());
		initialValues.put(KEY_PATTERN, m_DatabaseModel.getPattern());
		initialValues.put(KEY_TYPE, m_DatabaseModel.getType());
		initialValues.put(KEY_DATETIME, m_DatabaseModel.getDatetime());

		return mDb.update(TABLE_CATEGORY, initialValues,"id="+_id,null) > 0;
	}

	public ArrayList<DatabaseModel> getAllCity()
	{
		ArrayList<DatabaseModel> model=new ArrayList<DatabaseModel>();
		Cursor mCursor = mDb.query(TABLE_CATEGORY, null, null, null, null, null,KEY_ID+" DESC");

		mCursor.moveToNext();
		for(int i=0;i<mCursor.getCount();i++)
		{
			DatabaseModel unit_model= new DatabaseModel();

			unit_model.setId(mCursor.getInt(Integer.valueOf(mCursor.getColumnIndex(KEY_ID))));
			unit_model.setVersion(mCursor.getInt(Integer.valueOf(mCursor.getColumnIndex(KEY_VERSION))));
			unit_model.setCategory(mCursor.getString(mCursor.getColumnIndex(KEY_CATEGORY)));
			unit_model.setFeedback(mCursor.getString(mCursor.getColumnIndex(KEY_FEEDBACK)));
			unit_model.setTarget(mCursor.getString(mCursor.getColumnIndex(KEY_TARGET)));
			unit_model.setName(mCursor.getString(mCursor.getColumnIndex(KEY_NAME)));
			unit_model.setValue(mCursor.getString(mCursor.getColumnIndex(KEY_VALUE)));
			unit_model.setColor(mCursor.getString(mCursor.getColumnIndex(KEY_COLOR)));
			unit_model.setPattern(mCursor.getString(mCursor.getColumnIndex(KEY_PATTERN)));
			unit_model.setType(mCursor.getString(mCursor.getColumnIndex(KEY_TYPE)));
			unit_model.setDatetime(mCursor.getString(mCursor.getColumnIndex(KEY_DATETIME)));
			
			model.add(unit_model);
			mCursor.moveToNext();
		}

	/*	ArrayList<DatabaseModel> newlist = new ArrayList<DatabaseModel>();

		for (int i = 0; i < model.size(); i++) 
		{
			if(model.get(i).getId() ==  1)
			{
				newlist.add(model.get(i));
				break;
			}
		}

		for (int i = 0; i < model.size(); i++) 
		{
			if(model.get(i).getId() !=  1)
			{
				newlist.add(model.get(i));
			}
		}*/
		return model;

	}


	public ArrayList<DatabaseModel> getAllCityOneSelected(String id)
	{
		ArrayList<DatabaseModel> model=new ArrayList<DatabaseModel>();

		String Query = "Select * from " + TABLE_CATEGORY + " where " + KEY_VALUE + "='"
				+ id + "'";

		System.out.println("id"+Query);
		Cursor mCursor = mDb.rawQuery(Query, null);

		mCursor.moveToFirst();
		for(int i=0;i<mCursor.getCount();i++)
		{
			DatabaseModel unit_model= new DatabaseModel();

			unit_model.setName(mCursor.getString(mCursor.getColumnIndex(KEY_NAME)));
			unit_model.setTarget(mCursor.getString(mCursor.getColumnIndex(KEY_TARGET)));
			unit_model.setCategory(mCursor.getString(mCursor.getColumnIndex(KEY_CATEGORY)));
			
			model.add(unit_model);

			System.out.println(unit_model.toString());
			mCursor.moveToNext();
		}

		return model;

	}
	/*public void updateCheckBox(CityDatabaseModel model)
	{
		String updateQry;
		if(model.getFlag()==1)
			updateQry="update " + TABLE_TIMEZONE + " set " + KEY_FLAG + " = 0 where " + KEY_ID + " = " + model.getId();  
		else
			updateQry="update " + TABLE_TIMEZONE + " set " + KEY_FLAG + " = 1 where " + KEY_ID + " = " + model.getId();
		mDb.execSQL(updateQry);
	}*/

/*
	public void updateCity(CityDatabaseModel model)
	{
		String updateQry,otherUpdate;
		updateQry="update " + TABLE_TIMEZONE + " set " + KEY_FLAG + " = 1 where " + KEY_ID + " = " + model.getId();
		otherUpdate="update " + TABLE_TIMEZONE + " set " + KEY_FLAG + " = 0 where " + KEY_ID + " != " + model.getId();
		mDb.execSQL(updateQry);
		mDb.execSQL(otherUpdate);
	}
*/
	/*public void checkCitySelected()
	{
		String Query = "Select * from " + TABLE_TIMEZONE + " where " + KEY_FLAG + "=1";
		Cursor c=mDb.rawQuery(Query, null);
		if(c.getCount()<=0)
		{
			String updateQry="update " + TABLE_TIMEZONE + " set " + KEY_FLAG + " = 1 where " + KEY_ID + " = 1";
			mDb.execSQL(updateQry);
		}
	}*/
/*
	*//**
	 * Duplicate Data Check
	 * @param fieldValue
	 * @return
	 *//*
	public boolean RecordAlreadyExist(String fieldValue) 
	{
		Cursor cursor = mDb.query(TABLE_TIMEZONE, new String[] {KEY_NAME}, KEY_NAME + " LIKE '%" + fieldValue + "%'",
				null, null, null, null, null);

		cursor.moveToFirst();

		if(cursor.getCount()<=0)
		{
			CityDatabaseModel model=new CityDatabaseModel();
			model.getName();
			model.getId();
			cursor.moveToNext();
			System.out.println("model duplicate ---->>>"+model.getName());
			return false;
		}

		return true;
	}
*/


	/**
	 * 	Delete Data
	 * @return
	 */
	public boolean delete(int id) 
	{
		return mDb.delete(TABLE_CATEGORY, KEY_ID + "='" + id +"'", null) > -1;
	}
}

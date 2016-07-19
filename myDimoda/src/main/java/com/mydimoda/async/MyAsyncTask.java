package com.mydimoda.async;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class MyAsyncTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
	
	/// server communicate using asyncTask
	
	ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
	JSONObject 		mResponseData 	= null;
	String     		mBaseURL 	 	= "";
	boolean         mIsGet			= false;
	ServerResponse  mCallback     	= null;
	
	public MyAsyncTask(ServerResponse callback, String url,boolean flag)
	{
		mCallback = callback;
		mBaseURL = url;
		mIsGet   = flag;
		Log.d(getClass().getSimpleName(),url);
	}
	
	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(
			String... params) {

		if(mIsGet)
		{
			// Creating JSON Parser instance
			JSONGetParser jParser = new JSONGetParser();
			// getting JSON string from URL
			mResponseData = jParser.getJSONFromUrl(mBaseURL);
		}else
		{
			// Creating JSON Parser instance
			JSONAPostParser jParser = new JSONAPostParser();
			// getting JSON string from URL
			mResponseData = jParser.getJSONFromUrl(mBaseURL);  
		}
		
		
 		return UploadsList;
	}

	@Override
	protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
  
		/// when get response, call parser response  function of the class
		if(mCallback != null)
			mCallback.getResponse(mResponseData);
		super.onPostExecute(result);
	}
}

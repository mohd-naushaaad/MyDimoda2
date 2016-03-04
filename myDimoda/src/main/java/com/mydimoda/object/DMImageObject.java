package com.mydimoda.object;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class DMImageObject implements Serializable{

	public String url;
	public int    width;
	public int    height;
	
	public DMImageObject()
	{
		
	}
	
	public DMImageObject(JSONObject data)
	{
		if(data != null)
		{
			try {
				url = data.getString("url");
				width = data.getInt("width");
				height = data.getInt("height");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}



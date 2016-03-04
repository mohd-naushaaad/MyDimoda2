package com.mydimoda.object;

import org.json.JSONException;
import org.json.JSONObject;

public class DMItemObject {

	public String type;
	public String index;
	
	public DMItemObject()
	{
		
	}
	
	public DMItemObject(JSONObject data)
	{
		if(data != null)
		{
			try {
				index = data.getString("id");
				type  = data.getString("type");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}

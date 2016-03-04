package com.mydimoda.object;

import org.json.JSONException;
import org.json.JSONObject;

public class DMReviewObject {

	public String  mark;
	public String  count;
	public String  IFrameURL;
	public boolean HasReviews;
	
	public DMReviewObject()
	{
		
	}
	
	public DMReviewObject(JSONObject data)
	{
		if(data != null)
		{
			try {
				
				JSONObject jObj = data.getJSONObject("CustomerReviews");
				IFrameURL = jObj.getString("IFrameURL");
				HasReviews = jObj.getBoolean("HasReviews");
				
				if(HasReviews)
				{
					mark  = data.getString("mark");
					count = data.getString("count");
				}else
				{
					mark = "0.0";
					count = "0";
				}
				
				if(!mark.contains("."))
				{
					mark = mark + ".0";
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

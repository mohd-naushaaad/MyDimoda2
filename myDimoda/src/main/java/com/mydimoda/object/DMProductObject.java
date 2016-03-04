package com.mydimoda.object;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class DMProductObject implements Serializable{

	public String 			ASIN;
	public String 			ParentASIN;
	public String 			DetailPageURL;
	public String 			Title;
	public String 			Feature;
	public String 			Price;
	public DMImageObject 	SmallImage;
	public DMImageObject 	MediumImage;
	public DMImageObject 	LargeImage;
	
	public DMProductObject()
	{
		
	}
	
	public DMProductObject(JSONObject data)
	{
		if(data != null)
		{
			try {
				ASIN 			= data.getString("ASIN");
				ParentASIN 		= data.getString("ParentASIN");
				DetailPageURL 	= data.getString("DetailPageURL");
				Title 			= data.getString("Title");
				Feature 		= data.getString("Feature");
				Price 			= data.getString("Price");
				
				JSONObject sObj = data.getJSONObject("SmallImage");
				SmallImage = new DMImageObject(sObj);
				
				JSONObject mObj = data.getJSONObject("MediumImage");
				MediumImage = new DMImageObject(mObj);
				
				JSONObject lObj = data.getJSONObject("LargeImage");
				LargeImage = new DMImageObject(lObj);
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


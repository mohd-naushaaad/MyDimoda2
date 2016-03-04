package com.mydimoda.social.google;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mydimoda.DMAlgorithmActivity;
import com.mydimoda.constant;
import com.mydimoda.social.google.util.IabHelper;
import com.mydimoda.social.google.util.IabResult;
import com.mydimoda.social.google.util.Inventory;
import com.mydimoda.social.google.util.Purchase;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class GoogleIAP {
	
	private static final String TAG = "GoogleIAP";
	
	public final static String SKU_PAID = "com.mydimoda.paid";
	
	public static Activity s_activity;
	
	public static IabHelper mHelper;
	
	private static boolean isSuccessfulSetupIAP;
	
	private static final int RC_REQUEST = 10001;
	
//ccho	private static native void provideContent(int featureId);
//ccho	public static native void provideFailed();
	
	public static void initiate(Activity activity, String base64EncodedPublicKey) {
		
		s_activity = activity;
		
		mHelper = new IabHelper(s_activity, base64EncodedPublicKey);
    	mHelper.enableDebugLogging(false);
    	
    	Log.d(TAG, "Starting setup.");
    	
    	try {
    		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
	            public void onIabSetupFinished(IabResult result) {
	                if (!result.isSuccess()) {
	                	isSuccessfulSetupIAP = false;
	                	complain("Problem setting up in-app billing: " + result);
	                    return;
	                }
	                
	                if (mHelper == null) 
	                	return;
	                
	                isSuccessfulSetupIAP = true;
	                Log.d(TAG, "Setup successful. Querying inventory.");
	                mHelper.queryInventoryAsync(mGotInventoryListener);
	            }
	        });
    	} catch (Exception e) {
    		Log.e("xxx", "GoogleIAP.initiate : " + e.getMessage());
    		mHelper = null;
    	}
	}
	
	public static void destroy() {
		
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}
	
	public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (mHelper != null)
			return mHelper.handleActivityResult(requestCode, resultCode, data);
		return false;
	}
	
	public static void buyFeature(int featureId) {
		
		if(featureId == 0)
		{
			Message msg = new Message();
			Bundle b = new Bundle();
			
			String strSku = SKU_PAID;
			
			b.putString("IAP_TYPE", strSku);
			msg.setData(b);
			handlerIAP.sendMessage(msg);
		}
	}
	
	public static void complain(String message) {
		Log.e(TAG, "*** GoogleIAP Error: " + message);
	}
	
	static Handler handlerIAP = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			if(!isSuccessfulSetupIAP) {
//ccho				provideFailed();
				
				AlertDialog.Builder bld = new AlertDialog.Builder(s_activity);
				bld.setMessage("You can't buy this item on your device.");
				bld.setNegativeButton("Confirm", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {						
					}
				});
				
				bld.create().show();
				
				super.handleMessage(msg);
				
				return;
			}
			
			complain("buy Item: "+ msg.getData().getString("IAP_TYPE"));
			
			try {
				mHelper.launchPurchaseFlow(s_activity, msg.getData().getString("IAP_TYPE"), RC_REQUEST, 
		                				   mPurchaseFinishedListener, "");
			} catch (NullPointerException e) {
	        	Log.e("Error", "Null pointer");
	        	isSuccessfulSetupIAP = false;
	        	mHelper = null;			
			}
			catch (Exception e) {
	        	complain("init exception: " + e.getMessage());	            
	        }			
			
			super.handleMessage(msg);
		}		
	};
	
	private static IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        	
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
        	if (mHelper == null) {
//ccho        		provideFailed(); 
        		return;
        	}
        	
            if (result.isFailure()) {	
           		complain("!! Error purchasing: " + result);
//ccho           		provideFailed();
                return;
            }
   		 
            if (purchase.getSku().equals(SKU_PAID)) {
            	sendParse();
  //ccho              provideContent(0);
            }       
        }
    };
    
    private static IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        
    	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        	Log.d(TAG, "Query inventory finished.");
        	if (mHelper == null)	
        	{
 //ccho       		provideFailed(); 
        		return;
        	}
        	
            if (result.isFailure()) 
            {
        		complain("!! Failed to query inventory: " + result);
  //ccho          	provideFailed();
                return;
            }
            complain("Query inventory was successful.");
            
            if (inventory.hasPurchase(SKU_PAID))
            	sendParse();
//ccho            	provideContent(0);  
        }
    };
    
    private static void sendParse() {
    	
 //ccho   	showProgressBar("");
		
		ParseUser user = ParseUser.getCurrentUser();
		user.put("Buy", true);
		user.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e1) {
				
//ccho				hideProgressBar();
				
				if(e1 == null) {
					gotoAlgorithmActivity();
				}
				else {
				}
			}						
		});
    }
    
    private static void gotoAlgorithmActivity() {
		
		ParseUser user = ParseUser.getCurrentUser();
		int count = user.getInt("Count");
		count++;
		user.put("Count", count);
		user.saveInBackground();
		
		constant.gMode = "style me";
		Intent intent = new Intent(s_activity, DMAlgorithmActivity.class);
		s_activity.startActivity(intent);
	}
}
package com.mydimoda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMFashionGridAdapter;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.DatabaseModel;
import com.mydimoda.model.OrderClothModel;
import com.mydimoda.object.DMBlockedObject;
import com.mydimoda.object.DMItemObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DMNotificationActivity extends Activity {

	// / menu
	Button vBtnMenu;
	ListView vMenuList;
	TextView vTxtBack, vTxtTitle;
	DrawerLayout vDrawerLayout;
	LinearLayout vMenuLayout;
	RelativeLayout vBackLayout;

	DMFashionGridAdapter m_DmFashionGridAdapter;

	GridView vFashionGrid;
	TextView vRememberDate, vTxtRemember;
	Button vBtnLike, vBtnDismiss, vBtnHome;
	ImageButton vBtnRemember;
	RelativeLayout vRemeberLayout, vHomeBtnLayout;
	LinearLayout vBtnLayout;
	ProgressDialog vProgress;

	boolean mIsRemember = false;
	List<ParseObject> mClothList = null;
	List<String> mIdList = null;

	String mBaseUrl;
	JSONObject mSendData;

	boolean mIsDislike = false;
	DatabaseModel m_DatabaseModel;
	DbAdapter mDbAdapter;

	ArrayList<String> mylist = new ArrayList<String>();
	AlarmReciever mAlarmReciever = new AlarmReciever();
	final public static String ONE_TIME = "onetime";
	Intent intent = getIntent();

	String category, category2, name;
	DatabaseModel m_Model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notfashion);
		mDbAdapter = new DbAdapter(DMNotificationActivity.this);
		mDbAdapter.createDatabase();
		mDbAdapter.open();
		m_Model = new DatabaseModel();
		Bundle extras = getIntent().getExtras();
		m_DatabaseModel = new DatabaseModel();
		if (extras != null) {
			category = extras.getString("category");
			category2 = extras.getString("category_id");
			System.out.println("category" + category2);
			// category[{"id":"3dt5Sak0sV","type":"trousers"},{"id":"rgmAWghdlr","type":"shirt"}]
			json(category2);

			name = extras.getString("name");
			System.out.println("Name" + name);
			if (name.equalsIgnoreCase("Clothes")) {
				constant.gIsCloset = false;

			}
			if (name.equalsIgnoreCase("Clothes")) {

				constant.gIsCloset = true;
			}
		}

		// / layout
		vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		vMenuList = (ListView) findViewById(R.id.menu_list);
		vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
		vBtnMenu = (Button) findViewById(R.id.menu_btn);
		vTxtTitle = (TextView) findViewById(R.id.title_view);
		vTxtBack = (TextView) findViewById(R.id.back_txt);
		vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

		vFashionGrid = (GridView) findViewById(R.id.fashion_gridview);
		vBtnLike = (Button) findViewById(R.id.like_btn);
		vBtnDismiss = (Button) findViewById(R.id.dissmiss_btn);

		vBtnHome = (Button) findViewById(R.id.btn_home);
		vBtnRemember = (ImageButton) findViewById(R.id.remember_btn);
		vRememberDate = (TextView) findViewById(R.id.remember_date);
		vTxtRemember = (TextView) findViewById(R.id.remember_view);
		vRemeberLayout = (RelativeLayout) findViewById(R.id.remember_layout);
		vHomeBtnLayout = (RelativeLayout) findViewById(R.id.homebtn_layout);
		vBtnLayout = (LinearLayout) findViewById(R.id.button_layout);

		vBtnMenu.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				slideMenu();
			}
		});

		vMenuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				constant.selectMenuItem(DMNotificationActivity.this, position,
						true);
			}
		});

		vBackLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent m_intent = new Intent(DMNotificationActivity.this,
						DMHomeActivity.class);
				startActivity(m_intent);

			}
		});

		vBtnHome.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DMNotificationActivity.this,
						DMHomeActivity.class);
				startActivity(intent);
				finish();
			}
		});

		vBtnLike.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRemeberLayout("");

			}
		});

		vBtnDismiss.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dislikeCloth();
			}
		});

		vBtnRemember.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mIsRemember) {
					// constant.alertbox("Warning",
					// "Do you want to remove this look from Favorites?",
					// DMFashionActivity.this);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							DMNotificationActivity.this);
					alertDialogBuilder.setTitle("Warning");
					alertDialogBuilder
							.setMessage(
									"Do you want to remove this look from Favorites?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											ParseObject.createWithoutData(
													"Favorite",
													constant.gFashionID)
													.deleteEventually();
											// finish();
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				} else {
					mIsRemember = true;
					vBtnRemember.setBackgroundResource(R.drawable.remember_checked_bg);
					FavoriteCloths();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	public void init() {
		showMenu();
		setViewWithFont();

		Intent in = getIntent();
		String favorite = in.getStringExtra("favorite");
		if (favorite.equals("yes")) {
			// favorite
			showRemeberLayout(in.getStringExtra("date"));
			mIsRemember = true;
			vBtnRemember.setBackgroundResource(R.drawable.remember_checked_bg);

		} else {
			// general
			showBtnLayout();
			vRemeberLayout.setVisibility(View.GONE);
		}

		// init id list to get cloths
		initIdList();
	}

	// / --------------------------------- set font
	// -------------------------------------
	public void setViewWithFont() {
		vTxtTitle.setTypeface(constant.fontface);
		vTxtBack.setTypeface(constant.fontface);
		vRememberDate.setTypeface(constant.fontface);
		vTxtRemember.setTypeface(constant.fontface);
		vBtnHome.setTypeface(constant.fontface);
	}

	// / --------------------------------- show menu list
	// --------------------------------------
	public void showMenu() {
		vMenuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
	}

	// / --------------------------------- slide menu section
	// ------------------------------
	public void slideMenu() {
		if (vDrawerLayout.isDrawerOpen(vMenuLayout)) {
			vDrawerLayout.closeDrawer(vMenuLayout);
		} else
			vDrawerLayout.openDrawer(vMenuLayout);
	}

	// / -------------------------------------- go algorithmActivity
	// ------------
	public void goAlgorithmActivity() {
		Intent intent = new Intent(DMNotificationActivity.this,
				DMAlgorithmActivity.class);
		startActivity(intent);
		finish();
	}

	// / ------------------------------------- show button layout, hide button
	// layout ---------
	public void showBtnLayout() {
		vHomeBtnLayout.setVisibility(View.GONE);
		vBtnLayout.setVisibility(View.VISIBLE);
	}

	public void hideBtnLayout() {
		vHomeBtnLayout.setVisibility(View.VISIBLE);
		vBtnLayout.setVisibility(View.GONE);
	}

	// / ------------------------------------- show cloth
	// ---------------------------------------
	public void showFashionList(List<OrderClothModel> list) {
		vFashionGrid.setAdapter(new DMFashionGridAdapter(
				DMNotificationActivity.this, list));
	}

	// / ------------------------------------- show remember layout
	// ------------------------
	public void showRemeberLayout(String time) {
		hideBtnLayout();
		vRemeberLayout.setVisibility(View.VISIBLE);

		if (time.equals(""))
			vRememberDate.setText(constant.getCurrentTime());
		else
			vRememberDate.setText(time);

		likeCloth();
	}

	// / -------------------------------------- initialize id list
	// ---------------------------
	public void initIdList() {/*
							 * if(constant.gFashion != null) {
							 * 
							 * 
							 * mIdList = new ArrayList<String>(); for(int
							 * i=0;i<constant.gFashion.blockedList.size();i++) {
							 * mIdList
							 * .add(constant.gFashion.blockedList.get(i).index);
							 * 
							 * System.out.println("Size"+constant.gFashion.
							 * blockedList.size()); } }
							 * 
							 * System.out.println("Size"+constant.gFashion);
							 * if(mIdList != null && mIdList.size()>0)
							 * getClothsFP(mIdList);
							 */
		getClothsFP(mylist);

	}

	// / -------------------------------------- get cloth from parse.com
	// ----------------------
	public void getClothsFP(List<String> list) {
		showProgress("Loading...");

		ParseQuery<ParseObject> query = null;
		if (constant.gIsCloset) {
			query = ParseQuery.getQuery("Clothes");
			query.setLimit(constant.RESULT_SIZE);//mayur increased limit to 1000

		} else {
			query = ParseQuery.getQuery("DemoCloset");
		}

		query.whereContainedIn("objectId", list);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> clothList, ParseException e) {

				hideProgress();
				if (e == null) {
					makeClothList(clothList);
				} else {
					Toast.makeText(DMNotificationActivity.this, e.toString(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// / --------------------------------------------------------- make photo
	// list from parse object list --------------
	public void makeClothList(List<ParseObject> clothList) {
		if (clothList != null) {
			mClothList = clothList;
			List<OrderClothModel> photoList = new ArrayList<OrderClothModel>();
			for (int i = 0; i < mClothList.size(); i++) {
				OrderClothModel model = new OrderClothModel();
				ParseFile urlObject = (ParseFile) mClothList.get(i).get(
						"ImageContent");
				String url = urlObject.getUrl();
				Log.e("URL", mClothList.get(i).get("ImageContent").toString());

				model.setImageUrl(url);

				if (mClothList.get(i).getString("Type")
						.equalsIgnoreCase("shirt")) {
					model.setPosition(constant.SHIRT);

				} else if (mClothList.get(i).getString("Type")
						.equalsIgnoreCase("jacket")) {
					model.setPosition(constant.JACKET);

				} else if (mClothList.get(i).getString("Type")
						.equalsIgnoreCase("trousers")) {
					model.setPosition(constant.TROUSERS);

				} else if (mClothList.get(i).getString("Type")
						.equalsIgnoreCase("tie")) {
					model.setPosition(constant.TIE);
				} else if (mClothList.get(i).getString("Type")
						.equalsIgnoreCase("suit")) {
					model.setPosition(constant.SUIT);
				}

				photoList.add(model);
			}

			// sort user list by action count
			Collections.sort(photoList, new Comparator<OrderClothModel>() {
				@Override
				public int compare(OrderClothModel s1, OrderClothModel s2) {
					return s1.getPosition() - s2.getPosition();
				}
			});

			showFashionList(photoList);
		}
	}

	// / --------------------------------------------------------- save favorite
	// with date to parse.com database -----------
	public void FavoriteCloths() {
		ParseUser user = ParseUser.getCurrentUser();
		ParseObject favorite = new ParseObject("Favorite");
		favorite.put("User", user);
		favorite.put("DateTime", constant.getCurrentDate());

		for (int i = 0; i < mClothList.size(); i++) {
			ParseObject obj = mClothList.get(i);

			String type = obj.getString("Type");
			if (type.equals("shirt"))
				favorite.put("Shirt", obj);
			else if (type.equals("tie"))
				favorite.put("Tie", obj);
			else if (type.equals("jacket"))
				favorite.put("Jacket", obj);
			else if (type.equals("trousers"))
				favorite.put("Trousers", obj);
			else if (type.equals("suit"))
				favorite.put("Suit", obj);
		}

		constant.showProgress(this, "Favoriting");

		favorite.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null)
					Toast.makeText(DMNotificationActivity.this, "Favorited",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(DMNotificationActivity.this, e.toString(),
							Toast.LENGTH_LONG).show();

				constant.hideProgress();
			}
		});
	}

	// / ------------------------------------------- like cloths
	// ------------------
	public void likeCloth() {
		// mBaseUrl="http://54.69.61.15:/newa";//"http://54.191.209.169:/newa";
		// mBaseUrl="http://52.11.139.58:/upd24";//"http://54.69.61.15:/newa";
		mBaseUrl = "http://AIProd.myDiModa.com/resp_attire_24";// "http://54.69.61.15:/newa";
		makeSendData("like");

		MyAsyncTask task1 = new MyAsyncTask();
		task1.execute();

	}

	public void dislikeCloth() {
		mIsDislike = true;
		constant.gBlockedList.add(constant.gFashion);
		// mBaseUrl="http://54.69.61.15:/newa";//"http://54.191.209.169:/newa";
		// mBaseUrl="http://52.11.139.58:/upd24";//"http://54.69.61.15:/newa";
		mBaseUrl = "http://AIProd.myDiModa.com/resp_attire_24";
		makeSendData("dislike");
		MyAsyncTask task1 = new MyAsyncTask();
		task1.execute();
	}

	// / ----------------------------------------------- make send data with
	// json format ------------
	public void makeSendData(String feedback) {
		mSendData = new JSONObject();
		try {
			mSendData.put("version", "2");
			mSendData.put("category", category);
			mSendData.put("feedback", feedback);
			mSendData.put("target", makeTargetJSONArray());
			mSendData.put("name", "genparams");
			mSendData.put("value", "1");

			Log.e("data-----upd24liked---", mSendData.toString());

			/*
			 * if(feedback.equalsIgnoreCase("dislike")) {
			 * m_DatabaseModel.setFeedback(feedback); } else {
			 * m_DatabaseModel.setFeedback(feedback);
			 * m_DatabaseModel.setVersion(2);
			 * m_DatabaseModel.setCategory(constant.gCategory);
			 * m_DatabaseModel.setTarget(""+makeTargetJSONArrayid_type());
			 * m_DatabaseModel.setName("genparams");
			 * 
			 * Random r = new Random(); int id = r.nextInt();
			 * m_DatabaseModel.setValue(""+id);
			 * m_DatabaseModel.setFeedback(feedback);
			 * 
			 * mDbAdapter.add(m_DatabaseModel);
			 * 
			 * 
			 * AlarmManager
			 * am=(AlarmManager)DMNotificationActivity.this.getSystemService
			 * (Context.ALARM_SERVICE); Intent intent = new
			 * Intent(DMNotificationActivity.this, AlarmReciever.class);
			 * intent.putExtra(ONE_TIME, Boolean.TRUE); intent.putExtra("id",
			 * ""+id); // PendingIntent pi =
			 * PendingIntent.getBroadcast(DMFashionActivity.this, 0, intent, 0);
			 * 
			 * PendingIntent pi = PendingIntent.getBroadcast(this, id , intent,
			 * PendingIntent.FLAG_ONE_SHOT);
			 * 
			 * am.set(AlarmManager.RTC_WAKEUP,24*60*60*1000,pi); }
			 */

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// / ---------------------------------------------- make target json array
	// -----------
	public JSONArray makeTargetJSONArray() {
		JSONArray arr = new JSONArray();
		if (mClothList != null) {
			for (int i = 0; i < mClothList.size(); i++) {
				ParseObject parseObj = mClothList.get(i);
				JSONObject obj = new JSONObject();
				try {
					obj.put("color", parseObj.getString("Color"));

					obj.put("pattern", parseObj.getString("Pattern"));
					obj.put("type", parseObj.getString("Type"));
					obj.put("id", parseObj.getObjectId());

					m_DatabaseModel.setType(parseObj.getString("Type")
							.toString());
					m_DatabaseModel.setColor(mClothList.get(i).getString(
							"Color"));
					m_DatabaseModel.setPattern(mClothList.get(i).getString(
							"Pattern"));

					arr.put(obj);
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

			System.out
					.println("TYPE:::" + m_DatabaseModel.getType().toString());
		}
		return arr;
	}

	public JSONArray makeTargetJSONArrayid_type() {
		JSONArray arr = new JSONArray();
		if (mClothList != null) {
			for (int i = 0; i < mClothList.size(); i++) {
				ParseObject parseObj = mClothList.get(i);
				JSONObject obj = new JSONObject();
				try {

					obj.put("id", parseObj.getObjectId());
					obj.put("type", parseObj.getString("Type"));

					arr.put(obj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return arr;
	}

	// / -------------------------------------------- When like, dislike , parse
	// response --------
	public void parseResponse(JSONObject data) {

		if (data != null) {
			Log.e("data", data.toString());

			try {
				String status = data.getString("status");

				if (status.equals("ok")) {
					if (mIsDislike) {
						Toast.makeText(this, "Disliked", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(this, "Liked", Toast.LENGTH_LONG).show();
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// Toast.makeText(this, "Unknown server error.",
				// Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			// Toast.makeText(this, "You can not like",
			// Toast.LENGTH_LONG).show();
		}

		if (mIsDislike) {
			mIsDislike = false;
			goAlgorithmActivity();
		} else {
			initItemList();
			if (constant.gMode.equals("help me")) {
				constant.gLikeNum++;
				if (constant.gCategory.equals("casual")) {
					constant.gLikeNum = 0;
				} else if (constant.gCategory.equals("after5")) {
					if (constant.gLikeNum == 2) {
						constant.gLikeNum = 0;
					} else {
						constant.gItemList = getItemList();
						goAlgorithmActivity();
					}
				} else if (constant.gCategory.equals("formal")) {
					if (constant.gLikeNum == 3) {
						constant.gLikeNum = 0;
					} else {
						constant.gItemList = getItemList();
						goAlgorithmActivity();
					}
				}
			} else {
				constant.gLikeNum = 0;
			}
		}
	}

	// / --------------------------------------- When mode is help me, make item
	// list -------------
	public List<DMItemObject> getItemList() {
		List<DMItemObject> list = new ArrayList<DMItemObject>();
		if (mClothList != null) {
			for (int i = 0; i < mClothList.size(); i++) {
				DMItemObject item = new DMItemObject();
				item.index = mClothList.get(i).getObjectId();
				item.type = mClothList.get(i).getString("Type");

				list.add(item);
			}
		}
		return list;
	}

	public void initItemList() {
		constant.gItemList = new ArrayList<DMItemObject>();
		constant.gBlockedList = new ArrayList<DMBlockedObject>();
	}

	public class MyAsyncTask extends
			AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		// / server communicate using asyncTask

		ArrayList<HashMap<String, String>> UploadsList = new ArrayList<HashMap<String, String>>();
		JSONObject mResponseData;

		@Override
		protected void onPreExecute() {
			constant.showProgress(DMNotificationActivity.this, "Loading...");
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {

			// Creating JSON Parser instance
			JSONPostParser jParser = new JSONPostParser();
			// getting JSON string from URL
			mResponseData = jParser.getJSONFromUrl(mBaseUrl,
					mSendData.toString());

			return UploadsList;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			// / when get response, call parser response function of the class
			constant.hideProgress();
			parseResponse(mResponseData);

			super.onPostExecute(result);
		}
	}

	public void showProgress(String message) {
		vProgress = new ProgressDialog(this);
		vProgress.setMessage(message);
		vProgress.setCancelable(true);
		vProgress.show();
	}

	public void hideProgress() {
		vProgress.dismiss();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent m_intent = new Intent(DMNotificationActivity.this,
				DMHomeActivity.class);
		startActivity(m_intent);

	}

	public void json(String category) {
		String OutputData = "";
		try {
			JSONArray json = new JSONArray(category);

			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonChildNode = json.getJSONObject(i);
				String song_id = jsonChildNode.optString("id").toString();
				mylist.add(song_id);

				String song_name = jsonChildNode.optString("type").toString();

				OutputData += "Node : \n\n     " + song_id + " | " + song_name
						+ " \n\n ";
			}
			System.out.println("" + OutputData);
			System.out.println("List array" + mylist);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * System.out.println("category__Ubsude"+category); try { jsonResponse =
		 * new JSONObject(category);
		 * 
		 * 
		 * int lengthJsonArr = jsonMainNode.length();
		 * 
		 * for(int i=0; i < lengthJsonArr; i++) { JSONObject jsonChildNode =
		 * jsonMainNode.getJSONObject(i); int song_id=
		 * Integer.parseInt(jsonChildNode.optString("id").toString()); String
		 * song_name= jsonChildNode.optString("type").toString(); OutputData +=
		 * "Node : \n\n      "+ song_id +" | "+ song_name +" \n\n ";
		 * 
		 * System.out.println(""+OutputData);
		 * 
		 * }
		 * 
		 * } catch(Exception e) {
		 * 
		 * }
		 */

	}

}

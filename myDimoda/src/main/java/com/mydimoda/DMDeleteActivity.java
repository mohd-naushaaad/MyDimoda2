package com.mydimoda;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.adapter.DMDeleteGridAdapter;
import com.mydimoda.adapter.DMListItemCallback;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DMDeleteActivity extends Activity implements DMListItemCallback {

	// / menu
	Button vBtnMenu;
	ListView vMenuList;
	TextView vTxtBack, vTxtTitle;
	DrawerLayout vDrawerLayout;
	LinearLayout vMenuLayout;
	RelativeLayout vBackLayout;

	GridView vClothGrid;

	String mType;
	DMDeleteGridAdapter mAdapter;
	List<ParseObject> mClothList = null;
	List<String> mUrlList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// / layout
		vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		vMenuList = (ListView) findViewById(R.id.menu_list);
		vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
		vBtnMenu = (Button) findViewById(R.id.menu_btn);
		vTxtTitle = (TextView) findViewById(R.id.title_view);

		FontsUtil.setExistenceLight(this, vTxtTitle);

		vTxtBack = (TextView) findViewById(R.id.back_txt);
		FontsUtil.setExistenceLight(this, vTxtBack);

		vBackLayout = (RelativeLayout) findViewById(R.id.back_layout);

		vClothGrid = (GridView) findViewById(R.id.cloths_grid);

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
				constant.selectMenuItem(DMDeleteActivity.this, position, true);
			}
		});

		vBackLayout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		vClothGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

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
		// setViewWithFont();

		Intent in = getIntent();
		mType = in.getStringExtra("type");
		showTitle();
		getClothFP();
	}

	// / --------------------------------- set font
	// -------------------------------------
	// public void setViewWithFont()
	// {
	// vTxtTitle.setTypeface(constant.fontface);
	// vTxtBack.setTypeface(constant.fontface);
	// }

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

	public void showTitle() {
		if (mType.equals("shirt")) {
			vTxtTitle.setText("Shirt");
		} else if (mType.equals("trousers")) {
			vTxtTitle.setText("Trousers");
		} else if (mType.equals("jacket")) {
			vTxtTitle.setText("Jacket");
		} else if (mType.equals("tie")) {
			vTxtTitle.setText("Tie");
		} else if (mType.equals("suit")) {
			vTxtTitle.setText("Suit");
		}
	}

	// / -------------------------------------- check object in favorite list
	// ------------
	public void checkFavoriteFP(final ParseObject object, final int pos) {
		constant.showProgress(this, "Waiting");
		String type = object.getString("Type");
		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");

		query.whereEqualTo("User", user);
		if (type.equals("shirt"))
			query.whereEqualTo("Shirt", object);
		else if (type.equals("tie"))
			query.whereEqualTo("Tie", object);
		else if (type.equals("jacket"))
			query.whereEqualTo("Jacket", object);
		else if (type.equals("trousers"))
			query.whereEqualTo("Trousers", object);
		else if (type.equals("suit"))
			query.whereEqualTo("Suit", object);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> clothList, ParseException e) {

				constant.hideProgress();
				if (e == null) {
					checkContains(clothList, object, pos);
				} else {
					Toast.makeText(DMDeleteActivity.this, e.toString(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void checkContains(List<ParseObject> list, ParseObject object,
			int pos) {
		if (list != null && list.size() > 0) {
			confimeDelete(
					"This cloth is favorited item. Are you sure, you want to delete it?",
					list, object, pos);
		} else {
			confimeDelete("Are you sure, you want to delete it?", list, object, pos);
		}
	}

	// / ------------------------------------- delete cloth from parse
	// ----------------------
	public void delectClothFP(ParseObject object, final int pos) {
		// / CGChange - IsCloset = true
		if (!constant.gIsCloset) {
			setIsCloset();
			mClothList.remove(pos);
			mUrlList.remove(pos);
			mAdapter.notifyDataSetChanged();
			finish();

		} else {
			constant.showProgress(this, "Deleting");
			object.deleteInBackground(new DeleteCallback() {

				@Override
				public void done(ParseException e) {
					// TODO Auto-generated method stub
					constant.hideProgress();
					mClothList.remove(pos);
					mUrlList.remove(pos);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	// / ------------------------------------- set isCloset flag = true in user
	// table ------------------
	public void setIsCloset() {
		constant.gIsCloset = true;
		ParseUser user = ParseUser.getCurrentUser();
		user.put("isDemoCloset", true);
		user.saveInBackground();

		SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME,
				0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("isCloset", constant.gIsCloset);
		editor.commit();
	}

	// / ------------------------------------- get cloth from parse
	// -------------------------------------
	public void getClothFP() {
		constant.showProgress(this, "Loading...");
		ParseUser user = ParseUser.getCurrentUser();

		ParseQuery<ParseObject> query = null;
		if (constant.gIsCloset) {
			query = ParseQuery.getQuery("Clothes");
			query.whereEqualTo("Type", mType);
			query.whereEqualTo("User", user);
			query.orderByDescending("createdAt");
		} else {
			query = ParseQuery.getQuery("DemoCloset");
			query.whereEqualTo("Type", mType);
			query.orderByDescending("createdAt");
		}

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> clothList, ParseException e) {

				constant.hideProgress();
				if (e == null) {
					makeClothList(clothList);
				} else {
					Toast.makeText(DMDeleteActivity.this, e.toString(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// / --------------------------------------------------------- make photo
	// list from parse object list --------------
	public void makeClothList(List<ParseObject> clothList) {
		ParseUser user = ParseUser.getCurrentUser();
		if (clothList != null) {
			mClothList = new ArrayList<ParseObject>();
			mUrlList = new ArrayList<String>();
			for (int i = 0; i < clothList.size(); i++) {
				if (constant.gIsCloset) {
					ParseUser itemUser = clothList.get(i).getParseUser("User");
					if (itemUser.getObjectId().equals(user.getObjectId())) {
						mClothList.add(clothList.get(i));
						ParseFile urlObject = (ParseFile) clothList.get(i).get(
								"ImageContent");
						String url = urlObject.getUrl();
						mUrlList.add(url);
					}
				} else {
					mClothList.add(clothList.get(i));
					ParseFile urlObject = (ParseFile) clothList.get(i).get(
							"ImageContent");
					String url = urlObject.getUrl();
					mUrlList.add(url);
				}
			}
			showClothList();
		}
	}

	// / ------------------------------------- show cloth
	// ---------------------------------------
	public void showClothList() {
		mAdapter = new DMDeleteGridAdapter(this, mUrlList, this);
		vClothGrid.setAdapter(mAdapter);
	}

	// / ------------------------------------ check category
	// ------------------------------
	public void checkCategory(ParseObject object, int pos) {
		String category = object.getString("Category");
		if (category.equals("formal")) {
			constant.alertbox("Warning!",
					"The category of this cloth is formal", this);
		} else {
			alertbox("Do you want to change category to formal?", object, pos);
		}
	}

	// / ------------------------------------ change cloth category to formal
	// ------------------
	public void changeCategory(ParseObject object, final int pos) {
		constant.showProgress(this, "Waiting");
		String clothId = object.getObjectId();

		ParseQuery<ParseObject> query = null;
		if (constant.gIsCloset) {
			query = ParseQuery.getQuery("Clothes");
		} else {
			query = ParseQuery.getQuery("DemoCloset");
		}

		query.getInBackground(clothId, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				// TODO Auto-generated method stub
				constant.hideProgress();
				if (e == null) {
					object.put("Category", "formal");
					object.saveInBackground();
					mClothList.get(pos).put("Category", "formal");
				}
			}
		});
	}

	public void confimeDelete(String message,
			final List<ParseObject> favoriteList, final ParseObject object,
			final int pos) {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
		dialog.setContentView(R.layout.dialog_delete);

		dialog.setCanceledOnTouchOutside(true);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		FontsUtil.setExistenceLight(this, title);
		title.setText(message);

		Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
		FontsUtil.setExistenceLight(this, okBtn);
		okBtn.setText("Yes");
		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				for (int i = 0; i < favoriteList.size(); i++)
					favoriteList.get(i).deleteEventually();
				delectClothFP(object, pos);
				dialog.dismiss();
			}
		});

		Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
		FontsUtil.setExistenceLight(this, cancelBtn);
		cancelBtn.setText("No");
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void alertbox(String message, final ParseObject object, final int pos) {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
		dialog.setContentView(R.layout.dialog_delete);
		dialog.setCanceledOnTouchOutside(true);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		FontsUtil.setExistenceLight(this, title);
		title.setText(message);

		Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
		FontsUtil.setExistenceLight(this, okBtn);
		okBtn.setText("Yes");
		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				changeCategory(object, pos);
				dialog.dismiss();
			}
		});

		Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
		FontsUtil.setExistenceLight(this, cancelBtn);
		cancelBtn.setText("No");

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public void selectButton(int pos, int type) {
		// TODO Auto-generated method stub
		if (type == 1)// delete
		{
			checkFavoriteFP(mClothList.get(pos), pos);
		} else {
			checkCategory(mClothList.get(pos), pos);
		}
	}
}

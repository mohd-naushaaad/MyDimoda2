package com.mydimoda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mydimoda.database.DbAdapter;
import com.mydimoda.model.CityDatabaseModel;
import com.mydimoda.model.CityModel;
import com.mydimoda.widget.cropper.util.FontsUtil;

// Activity that display customized list view and search box
public class MainActivity extends Activity {

	// array list to store section positions
	ArrayList<Integer> mListSectionPos;

	// array list to store listView data
	ArrayList<String> mListItems;

	// custom list view with pinned header
	PinnedHeaderListView mListView;

	// custom adapter
	PinnedHeaderAdapter mAdaptor;

	// search box
	EditText mSearchView;

	// loading view
	ProgressBar mLoadingView;

	// empty view
	TextView mEmptyView, btnClose, txt_view;
	DbAdapter mDbAdapter;

	CityDatabaseModel m_CityDatabaseModel;
	boolean exist;

	ArrayList<CityModel> cityList = new ArrayList<CityModel>();
	ArrayList<CityModel> tempList = new ArrayList<CityModel>();

	String category;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// UI elements
		setupViews();

		if (AppUtils.getPref("type", MainActivity.this) == null) {
			AppUtils.putPref("type", "0", MainActivity.this);
			String[] listItems = getResources().getStringArray(
					R.array.shirts_names);
			String[] zoneItems = getResources().getStringArray(
					R.array.shirts_names_zone);

			for (int i = 0; i < listItems.length; i++) {
				CityModel model = new CityModel(listItems[i].toUpperCase()
						.toString(), zoneItems[i]);
				cityList.add(model);
				tempList.add(model);
			}

			Collections.sort(cityList);
			Collections.sort(tempList);

		} else if (AppUtils.getPref("type", MainActivity.this)
				.equalsIgnoreCase("0")) {

			// String[] listItems = TimeZone.getAvailableIDs();
			String[] listItems = getResources().getStringArray(
					R.array.shirts_names);
			String[] zoneItems = getResources().getStringArray(
					R.array.shirts_names_zone);
			// String[] zoneItems
			// =getResources().getStringArray(R.array.cities_tz);

			for (int i = 0; i < listItems.length; i++) {
				CityModel model = new CityModel(listItems[i].toUpperCase()
						.toString(), zoneItems[i]);
				cityList.add(model);
				tempList.add(model);
			}
			Collections.sort(cityList);
			Collections.sort(tempList);
		}else if (AppUtils.getPref("type", MainActivity.this).equalsIgnoreCase(
				"1")) {

			// String[] listItems = TimeZone.getAvailableIDs();
			String[] listItems = getResources().getStringArray(
					R.array.jacket_names);
			String[] zoneItems = getResources().getStringArray(
					R.array.jacket_names_zone);
			// String[] zoneItems
			// =getResources().getStringArray(R.array.cities_tz);

			for (int i = 0; i < listItems.length; i++) {
				CityModel model = new CityModel(listItems[i].toUpperCase()
						.toString(), zoneItems[i]);
				cityList.add(model);
				tempList.add(model);
			}
			Collections.sort(cityList);
			Collections.sort(tempList);
		} else if (AppUtils.getPref("type", MainActivity.this)
				.equalsIgnoreCase("2")) {

			// String[] listItems = TimeZone.getAvailableIDs();
			String[] listItems = getResources().getStringArray(
					R.array.trouser_names);
			String[] zoneItems = getResources().getStringArray(
					R.array.trouser_names_zone);
			// String[] zoneItems
			// =getResources().getStringArray(R.array.cities_tz);

			for (int i = 0; i < listItems.length; i++) {
				CityModel model = new CityModel(listItems[i].toUpperCase()
						.toString(), zoneItems[i]);
				cityList.add(model);
				tempList.add(model);
			}
			Collections.sort(cityList);
			Collections.sort(tempList);

		} else if (AppUtils.getPref("type", MainActivity.this)
				.equalsIgnoreCase("3")) {

			// String[] listItems = TimeZone.getAvailableIDs();
			String[] listItems = getResources().getStringArray(
					R.array.tie_names);
			String[] zoneItems = getResources().getStringArray(
					R.array.tie_names_zone);
			// String[] zoneItems
			// =getResources().getStringArray(R.array.cities_tz);

			for (int i = 0; i < listItems.length; i++) {
				CityModel model = new CityModel(listItems[i].toUpperCase()
						.toString(), zoneItems[i]);
				cityList.add(model);
				tempList.add(model);
			}
			Collections.sort(cityList);
			Collections.sort(tempList);
		}

		TimeZone timezone = null;
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, MMM d, yyyy h:mm a");
		Date now = new Date();

		// Array to ArrayList
		// mItems = new ArrayList<String>(Arrays.asList(listItems));
		mListSectionPos = new ArrayList<Integer>();
		mListItems = new ArrayList<String>();

		// for handling configuration change
		if (savedInstanceState != null) {
			mListItems = savedInstanceState.getStringArrayList("mListItems");
			mListSectionPos = savedInstanceState
					.getIntegerArrayList("mListSectionPos");

			if (mListItems != null && mListItems.size() > 0
					&& mListSectionPos != null && mListSectionPos.size() > 0) {
				setListAdaptor();
			}

			String constraint = savedInstanceState.getString("constraint");
			if (constraint != null && constraint.length() > 0) {
				mSearchView.setText(constraint);

				Typeface typeFace = Typeface.createFromAsset(getAssets(),
						"fonts/Roboto-Light.ttf");
				mSearchView.setTypeface(typeFace);

				setIndexBarViewVisibility(constraint);
			}
		} else {

			new Poplulate().execute(tempList);
		}
	}

	private void setupViews() {
		setContentView(R.layout.main_act);

		overridePendingTransition(R.anim.slide_down, R.anim.slide_down_out);

		txt_view = (TextView) findViewById(R.id.txt_view);
		FontsUtil.setExistenceLight(this, txt_view);
		mSearchView = (EditText) findViewById(R.id.search_view);
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		btnClose = (TextView) findViewById(R.id.btnClose);
		FontsUtil.setExistenceLight(this, btnClose);

		mDbAdapter = new DbAdapter(MainActivity.this);
		mDbAdapter.open();
		m_CityDatabaseModel = new CityDatabaseModel();

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String poiName = ((TextView) view).getText().toString();
				// CityModel model=tempList.get(position-1);
				System.out.println("poiName  " + poiName);

				String zone = "";
				for (int i = 0; i < cityList.size(); i++) {
					if (cityList.get(i).getName().trim()
							.equalsIgnoreCase(poiName))
						zone = cityList.get(i).getZone();

				}

				System.out.println("zone" + zone);

				// AppUtils.putPref("zone", zone,MainActivity.this);
				AppUtils.yes = "false";
				AppUtils.brand = zone;
				/*
				 * Intent m_intent = new
				 * Intent(MainActivity.this,DMExactActivity.class);
				 * m_intent.putExtra("zone",zone); startActivity(m_intent);
				 */

				finish();
				overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
			}
		});
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
			}
		});

		mEmptyView = (TextView) findViewById(R.id.empty_view);
		FontsUtil.setExistenceLight(this, mEmptyView);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		mSearchView.addTextChangedListener(filterTextWatcher);
		super.onPostCreate(savedInstanceState);
	}

	private void setListAdaptor() {
		// create instance of PinnedHeaderAdapter and set adapter to list view
		mAdaptor = new PinnedHeaderAdapter(this, mListItems, mListSectionPos);

		mListView.setAdapter(mAdaptor);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// set header view
		View pinnedHeaderView = inflater.inflate(R.layout.section_row_view,
				mListView, false);
		mListView.setPinnedHeaderView(pinnedHeaderView);

		// set index bar view
		IndexBarView indexBarView = (IndexBarView) inflater.inflate(
				R.layout.index_bar_view, mListView, false);
		indexBarView.setData(mListView, mListItems, mListSectionPos);
		mListView.setIndexBarView(indexBarView);

		// set preview text view
		View previewTextView = inflater.inflate(R.layout.preview_view,
				mListView, false);
		mListView.setPreviewView(previewTextView);

		// for configure pinned header view on scroll change
		mListView.setOnScrollListener(mAdaptor);
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			String searchText = s.toString();
			Log.e("searchtext", searchText);
			tempList.clear();
			for (int i = 0; i < cityList.size(); i++) {
				if (cityList.get(i).getName().toLowerCase().trim()
						.startsWith(searchText.toLowerCase().trim()))
					tempList.add(cityList.get(i));
			}
			Collections.sort(tempList);
			new Poplulate().execute(tempList);

			/*
			 * if (mAdaptor != null && str != null)
			 * mAdaptor.getFilter().filter(str);
			 */
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Log.e("TextWatcherTest", "afterTextChanged:\t" + s.toString());
		}
	};

	private void setIndexBarViewVisibility(String constraint) {
		// hide index bar for search results
		if (constraint != null && constraint.length() > 0) {
			mListView.setIndexBarVisibility(false);
		} else {
			mListView.setIndexBarVisibility(true);
		}
	}

	// sort array and extract sections in background Thread here we use
	// AsyncTask
	private class Poplulate extends AsyncTask<ArrayList<CityModel>, Void, Void> {

		private void showLoading(View contentView, View loadingView,
				View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		}

		private void showContent(View contentView, View loadingView,
				View emptyView) {
			contentView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
		}

		private void showEmptyText(View contentView, View loadingView,
				View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPreExecute() {
			// show loading indicator
			showLoading(mListView, mLoadingView, mEmptyView);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<CityModel>... params) {
			mListItems.clear();
			mListSectionPos.clear();
			ArrayList<String> items = new ArrayList<String>();
			ArrayList<CityModel> city = params[0];
			for (int i = 0; i < city.size(); i++)
				items.add(city.get(i).getName());

			if (city.size() > 0) {

				// NOT forget to sort array
				// Collections.sort(items, new SortIgnoreCase());

				String prev_section = "";
				for (String current_item : items) {
					String current_section = current_item.substring(0, 1)
							.toUpperCase(Locale.getDefault());

					if (!prev_section.equals(current_section)) {
						mListItems.add(current_section);
						mListItems.add(current_item);
						// array list of section positions
						mListSectionPos
								.add(mListItems.indexOf(current_section));
						prev_section = current_section;
					} else {
						mListItems.add(current_item);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!isCancelled()) {
				if (mListItems.size() <= 0) {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				} else {
					setListAdaptor();
					showContent(mListView, mLoadingView, mEmptyView);
				}
			}
			super.onPostExecute(result);
		}
	}

	public class SortIgnoreCase implements Comparator<String> {
		public int compare(String s1, String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mListItems != null && mListItems.size() > 0) {
			outState.putStringArrayList("mListItems", mListItems);
		}
		if (mListSectionPos != null && mListSectionPos.size() > 0) {
			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
		}
		String searchText = mSearchView.getText().toString();

		System.out.println("Search text" + mSearchView.getText().toString());
		if (searchText != null && searchText.length() > 0) {
			outState.putString("constraint", searchText);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
	}
}

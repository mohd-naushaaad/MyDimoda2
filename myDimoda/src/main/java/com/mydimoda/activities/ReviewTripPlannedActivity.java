package com.mydimoda.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mydimoda.AppUtils;
import com.mydimoda.R;
import com.mydimoda.SharedPreferenceUtil;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.adapter.ReviewTripAdp;
import com.mydimoda.constant;
import com.mydimoda.model.ModelLookListing;
import com.mydimoda.model.ReviewTripData;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/9/2018.
 */

public class ReviewTripPlannedActivity extends AppCompatActivity implements ReviewTripAdp.TripClickListner {
    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.back_txt)
    TextView backTxt;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.back_layout)
    RelativeLayout backLayout;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.rv_trip_planned)
    RecyclerView rvTripPlanned;
    @BindView(R.id.rl_coach_review_trip)
    RelativeLayout rlCoachReviewTrip;
    @BindView(R.id.menu_btn)
    Button menuBtn;
    @BindView(R.id.menu_list)
    ListView menuList;
    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ReviewTripAdp reviewTripAdp;
    private List<ReviewTripData> tripDataList;
    private ProgressDialog dialog;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener clickListener;
    private int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_trip_listing);
        ButterKnife.bind(this);
        init();
        sideMenuClickListner();
    }

    private void sideMenuClickListner() {
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                constant.selectMenuItem(ReviewTripPlannedActivity.this, position,
                        true);
            }
        });
    }

    public void showMenu() {
        System.out.println("Setting" + constant.gMenuList);
        menuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
    }

    private void init() {
        dialog = new ProgressDialog(this);
        showMenu();
        showShowcaseView();
        clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (AppUtils.isInternetConnected(ReviewTripPlannedActivity.this)) {
                            deleteTrip(pos);
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to delete this trip")
                .setTitle(getString(R.string.app_name))
                .setPositiveButton("Yes", clickListener)
                .setNegativeButton("No", clickListener);

        applyCustomFont();
        setUpAdp();
        if (AppUtils.isConnectingToInternet(this)) {
            getDataFromParse();
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress() {
        dialog.setMessage("Please wait...");
        dialog.show();
    }

    private void hideProgress() {
        dialog.dismiss();
    }

    private void showShowcaseView() {
        if (!SharedPreferenceUtil.getBoolean(constant.PREF_IS_REVIEW_TRIP_SHOWN, false)) {
            rlCoachReviewTrip.setVisibility(View.VISIBLE);
            rlCoachReviewTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlCoachReviewTrip.setVisibility(View.GONE);
                    SharedPreferenceUtil.putValue(constant.PREF_IS_REVIEW_TRIP_SHOWN, true);
                }
            });
        }

    }

    private void getDataFromParse() {
        showProgress();
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TripData");
        query.whereEqualTo("User", user);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                hideProgress();
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i);
                        ReviewTripData tripData = new ReviewTripData(getListFronJsonArray(parseObject.getJSONArray("Looks"))
                                , parseObject.getString("Title")
                                , parseObject.getDate("Start_date")
                                , parseObject.getObjectId());
                        tripDataList.add(tripData);
                    }
                    reviewTripAdp.notifyDataSetChanged();
                    if (tripDataList.size() == 0) {
                        Toast.makeText(ReviewTripPlannedActivity.this, "No trips planned", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
        });
    }

    private List<ModelLookListing> getListFronJsonArray(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ModelLookListing>>() {
        }.getType();
        List<ModelLookListing> myModelList = gson.fromJson(String.valueOf(jsonArray), listType);
        return myModelList;
    }

    private void setUpAdp() {
        tripDataList = new ArrayList<>();
        reviewTripAdp = new ReviewTripAdp(this, tripDataList, this);
        rvTripPlanned.setLayoutManager(new LinearLayoutManager(this));
        rvTripPlanned.setAdapter(reviewTripAdp);
    }

    private void applyCustomFont() {
        FontsUtil.setExistenceLight(this, titleView);
        FontsUtil.setExistenceLight(this, backTxt);
    }

    @OnClick({R.id.back_layout, R.id.menu_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.menu_btn:
                slideMenu();
                break;
        }
    }

    public void slideMenu() {
        if (drawerLayout.isDrawerOpen(menuLayout)) {
            drawerLayout.closeDrawer(menuLayout);
        } else
            drawerLayout.openDrawer(menuLayout);
    }

    @Override
    public void onClickofTrip(int pos) {
        Intent intent = new Intent(this, LookListingActiivty.class);
        Bundle bundle = new Bundle();
        constant.start_date = tripDataList.get(pos).getStartDate();
        constant.trip_name = tripDataList.get(pos).getTripTitle();
        bundle.putParcelable(constant.BUNDLE_TRIP_LIST_LOOKS, Parcels.wrap(tripDataList.get(pos).getTotalLookList()));
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onClickOfDeleteIcon(int pos) {
        this.pos = pos;
        builder.show();

    }


    private void deleteTrip(final int pos) {
        showProgress();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TripData");
        query.whereEqualTo("objectId", tripDataList.get(pos).getRowId());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                hideProgress();
                if (e == null) {
                    try {
                        object.delete();
                        object.saveInBackground();
                        tripDataList.remove(pos);
                        reviewTripAdp.notifyItemRemoved(pos);
                        if (tripDataList.size() == 0) {
                            Toast.makeText(ReviewTripPlannedActivity.this, "No trips planned", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {

                }
            }
        });
    }
}

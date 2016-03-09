package com.mydimoda.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.R;
import com.mydimoda.constant;
import com.mydimoda.widget.cropper.CropImageView;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;

public class CropActivity extends Activity implements OnClickListener {

	public static final String LOGTAG = "CropActivity";

	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	private static final int GUIDELINES_OFF = 0;
	private CropImageView _cropView;

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	Bitmap croppedImage;

	TextView doneText, typeText, back_txt;

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
		bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
		mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_crop);

		TextView textView2 = (TextView) findViewById(R.id.textView2);
		FontsUtil.setExistenceLight(this, textView2);

		doneText = (TextView) findViewById(R.id.doneText);
		FontsUtil.setExistenceLight(this, doneText);

		typeText = (TextView) findViewById(R.id.typeText);
		FontsUtil.setExistenceLight(this, typeText);

		back_txt = (TextView) findViewById(R.id.back_txt);
		FontsUtil.setExistenceLight(this, back_txt);

		_cropView = (CropImageView) findViewById(R.id.cropView);
		_cropView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
				DEFAULT_ASPECT_RATIO_VALUES);
		_cropView.setGuidelines(GUIDELINES_OFF);

		if (constant.gTakenBitmap != null)
			_cropView.setImageBitmap(constant.gTakenBitmap);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOGTAG, "onActivityResult requestCode " + requestCode);
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onClick(View v) {

		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.button_click);
		v.startAnimation(anim);

		switch (v.getId()) {
		case R.id.back_layout: {
			finish();
			break;
		}
		case R.id.doneText:
			checkPermission();
			break;
		default:
			break;
		}
	}

	private void procDone() {
		constant.gCropBitmap = _cropView.getCroppedImage();
		if (getParent() == null)
			setResult(RESULT_OK);
		else
			getParent().setResult(RESULT_OK);
		finish();
	}

	private void checkPermission() {

		final ParseUser user = ParseUser.getCurrentUser();
		boolean bRated = user.getBoolean("ratedmyDiModa");
		// int count = user.getInt("Count");
		if (!bRated) {
			SharedPreferences setting = getSharedPreferences(
					"mydimoda_setting", 0);
			boolean never = setting.getBoolean("never", false);
			int count = setting.getInt("rate_count", 1);

			if (!never) {
				if (count == 3) {
					showRateAlert();
				} else {
					procDone();
				}
			} else {
				procDone();
			}
		} else {
			procDone();
		}
	}

	// private boolean OverOneDay() {
	//
	// long currenttime = System.currentTimeMillis();
	//
	// SharedPreferences setting = getSharedPreferences("mydimoda_setting", 0);
	// long reviewtime = setting.getLong("reviewtime", 0);
	// if(reviewtime == 0) {
	// Editor editor = getSharedPreferences("mydimoda_setting", 0).edit();
	// editor.putLong("reviewtime", currenttime);
	// editor.commit();
	//
	// return true;
	// }
	//
	// float delta = (float)(currenttime - reviewtime) / 1000.0f / 3600.0f;
	// if(delta > 24.0f) {
	// Editor editor = getSharedPreferences("mydimoda_setting", 0).edit();
	// editor.putLong("reviewtime", currenttime);
	// editor.commit();
	//
	// return true;
	// }
	//
	// return false;
	// }

	private void showRateAlert() {

		new AlertDialog.Builder(this)
				.setTitle("Rate us!")
				.setMessage(
						"Would you like to rate us? Please rate us 5 stars!")
				.setCancelable(false)
				.setNegativeButton("Never",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Editor editor = getSharedPreferences(
										"mydimoda_setting", 0).edit();
								editor.putBoolean("never", true);
								editor.commit();

								procDone();
							}
						})
				.setNeutralButton("Rate Now",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								ParseUser user = ParseUser.getCurrentUser();
								user.put("ratedmyDiModa", true);
								user.saveInBackground();
								rateApp();
							}
						})
				.setPositiveButton("Not Now",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								SharedPreferences setting = getSharedPreferences(
										"mydimoda_setting", 0);
								int count = setting.getInt("rate_count", 1);

								count++;
								if (count == 4)
									count = 1;
								Editor editor = getSharedPreferences(
										"mydimoda_setting", 0).edit();
								editor.putInt("rate_count", count);
								editor.commit();
								procDone();
							}
						}).show();
	}

	private void rateApp() {

		Uri uri = Uri.parse("market://details?id=" + this.getPackageName());

		Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(myAppLinkToMarket);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "unable to find market app", Toast.LENGTH_LONG)
					.show();
		}
	}
}
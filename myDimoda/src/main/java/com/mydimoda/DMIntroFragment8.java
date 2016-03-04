package com.mydimoda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMIntroFragment8 extends Fragment {

	ImageView vImgIntro;
	Button vBtnStart;
	TextView vTxtTitle;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_intro2, container, false);

		vImgIntro = (ImageView) view.findViewById(R.id.imageview1);

		vBtnStart = (Button) view.findViewById(R.id.start_btn);
		FontsUtil.setExistenceLight(getActivity(), vBtnStart);

		vTxtTitle = (TextView) view.findViewById(R.id.title_txt);
		// FontsUtil.setExistenceLight(getActivity(), vTxtTitle);
		FontsUtil.setLight(getActivity(), vTxtTitle);
		init();

		vBtnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData();

				if (constant.gIsStart) {
					Intent intent = new Intent(getActivity(),
							DMLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
				} else {
					getActivity().finish();
				}

			}
		});
		return view;
	}

	public void init() {
		// vTxtTitle.setTypeface(constant.fontface);
		// vBtnStart.setTypeface(constant.fontface);
		vImgIntro.setImageResource(R.drawable.mydimoda3);
		vTxtTitle
				.setText("myDiModa will also determine if the new item will improve your overall wardrobe styling options");
	}

	public void saveData() {
		SharedPreferences settings = getActivity().getSharedPreferences(
				constant.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("isFirst", false);
		editor.commit();
	}

	// / ------------------------------ make instance of JMAllEventFragment
	// ------------
	public static DMIntroFragment8 newInstance() {
		DMIntroFragment8 fragment = new DMIntroFragment8();
		return fragment;
	}

}

package com.mydimoda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMIntroFragment7 extends Fragment {

	ImageView vImgIntro;
	TextView vTxtTitle;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_intro1, container, false);

		vImgIntro = (ImageView) view.findViewById(R.id.imageview1);
		vTxtTitle = (TextView) view.findViewById(R.id.title_txt);
		// FontsUtil.setExistenceLight(getActivity(), vTxtTitle);
		FontsUtil.setLight(getActivity(), vTxtTitle);

		init();
		return view;
	}

	public void init() {
		vImgIntro.setImageResource(R.drawable.mydimoda4);
		// vTxtTitle.setTypeface(constant.fontface);
		vTxtTitle
				.setText("myDiModa will check if you have an item too close similar already in your wardrobe, no more buying duplicates in error");
	}

	// / ------------------------------ make instance of JMAllEventFragment
	// ------------
	public static DMIntroFragment7 newInstance() {
		DMIntroFragment7 fragment = new DMIntroFragment7();
		return fragment;
	}

}

package com.mydimoda;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMIntroFragment3_5 extends Fragment {

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
		vImgIntro.setImageResource(R.drawable.test3_5);
//		vTxtTitle.setTypeface(constant.fontface);
		vTxtTitle.setText(R.string.fragment_3_5);
	}

	// / ------------------------------ make instance of JMAllEventFragment
	// ------------
	public static DMIntroFragment3_5 newInstance() {
		DMIntroFragment3_5 fragment = new DMIntroFragment3_5();
		return fragment;
	}

}

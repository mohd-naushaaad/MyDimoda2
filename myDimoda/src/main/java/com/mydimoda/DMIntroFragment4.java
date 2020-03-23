package com.mydimoda;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMIntroFragment4 extends Fragment {

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
		vImgIntro.setImageResource(R.drawable.intro_occasion);
//		vTxtTitle.setTypeface(constant.fontface);
		vTxtTitle.setText(R.string.fragment_4);
	}

	// / ------------------------------ make instance of JMAllEventFragment
	// ------------
	public static DMIntroFragment4 newInstance() {
		DMIntroFragment4 fragment = new DMIntroFragment4();
		return fragment;
	}

}

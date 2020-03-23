package com.mydimoda;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydimoda.widget.cropper.util.FontsUtil;

public class DMIntroFragment6 extends Fragment {

	ImageView vImgIntro;
	TextView  vTxtTitle;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_intro1, container, false);
		
		vImgIntro = (ImageView)view.findViewById(R.id.imageview1);
		vTxtTitle = (TextView)view.findViewById(R.id.title_txt);
//		FontsUtil.setExistenceLight(getActivity(), vTxtTitle);
		FontsUtil.setLight(getActivity(), vTxtTitle);
		init();
		return view;
	}
	
	public void init()
	{
		vImgIntro.setImageResource(R.drawable.intro_detail);
//		vTxtTitle.setTypeface(constant.fontface);
		vTxtTitle.setText("After you pick an item in Purchase, select myStyle to get styling recommendations from myDiModa");
	}
	
/// ------------------------------ make instance of JMAllEventFragment ------------	
	public static DMIntroFragment6 newInstance()
	{
		DMIntroFragment6 fragment = new DMIntroFragment6();
		return fragment;
	}
		
}

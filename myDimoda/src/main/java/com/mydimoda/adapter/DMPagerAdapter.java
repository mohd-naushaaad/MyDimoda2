package com.mydimoda.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mydimoda.DMIntroFragment1;
import com.mydimoda.DMIntroFragment2;
import com.mydimoda.DMIntroFragment3;
import com.mydimoda.DMIntroFragment3_5;
import com.mydimoda.DMIntroFragment4;
import com.mydimoda.DMIntroFragment5;
import com.mydimoda.DMIntroFragment6;
import com.mydimoda.DMIntroFragment7;
import com.mydimoda.DMIntroFragment8;

public class DMPagerAdapter extends FragmentPagerAdapter{

	public DMPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}
	@Override
	public android.support.v4.app.Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		if(pos == 0)
		{
			return DMIntroFragment1.newInstance();
		}else if(pos == 1)
		{
			return DMIntroFragment2.newInstance();
		}else if(pos == 2)
		{
			return DMIntroFragment3_5.newInstance();
		}else if(pos == 3)
		{
			return DMIntroFragment3.newInstance();
		}else if(pos == 4)
		{
			return DMIntroFragment4.newInstance();
		}else if(pos == 5)
		{
			return DMIntroFragment5.newInstance();
		}else if(pos == 6)
		{
			return DMIntroFragment6.newInstance();
		}else if(pos == 7)
		{
			return DMIntroFragment7.newInstance();
		}else
		{
			return DMIntroFragment8.newInstance();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}

}

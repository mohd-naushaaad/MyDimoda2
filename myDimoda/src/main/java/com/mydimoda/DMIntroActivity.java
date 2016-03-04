package com.mydimoda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.mydimoda.adapter.DMPagerAdapter;

public class DMIntroActivity extends FragmentActivity {

	ViewPager 			vPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		vPager = (ViewPager)findViewById(R.id.home_pager);
		
		Intent intent = getIntent();
		constant.gIsStart = intent.getBooleanExtra("isStart", false);
		
		setFragments();
	}
	
	public void setFragments()
	{
		vPager.setAdapter(new DMPagerAdapter(getSupportFragmentManager()));
	}
}

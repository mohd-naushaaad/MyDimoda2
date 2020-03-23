package com.mydimoda;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

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

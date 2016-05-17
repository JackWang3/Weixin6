package com.imooc.imooc_weixin6_0;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;

public class MainActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener {
	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private String[] mTitles = new String[]{
			"First Fragment!", "Second Fragment!",
			"Third Fragment!", "Fourth Fragment!"};
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new 
			ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		
		initView();
		initDatas();
		mViewPager.setAdapter(mAdapter);
		initEvent();
	}
	
	/**
	 * ��ʼ�������¼�
	 */
	private void initEvent() {
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDatas() {
		for (String title : mTitles) {
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, title);
			tabFragment.setArguments(bundle);
			mTabs.add(tabFragment);
		}
		
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return mTabs.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				return mTabs.get(position);
			}
		};
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		ChangeColorIconWithText one = (ChangeColorIconWithText) 
				findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) 
				findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) 
				findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) 
				findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);
		
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		
		one.setAlpha(1.0f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setOverflowButtonAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����menu��ʾicon
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
			if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
				if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
					try {
						Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
						m.setAccessible(true);
						m.invoke(menu, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onClick(View v) {
		resetOtherTabs();
		switch (v.getId()) {
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);	
			break;
		}
	}
	
	/**
	 * ����������TabIndicator����ɫ
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//		Log.e("TAG", "position = " + position + ", positionOffset = " +positionOffset +
//				"positionOffsetPixels = " +positionOffsetPixels);
		if (positionOffset > 0) {
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	@Override
	public void onPageSelected(int position) {
		
	}
}

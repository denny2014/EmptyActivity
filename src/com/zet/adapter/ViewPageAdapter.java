package com.zet.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPageAdapter extends PagerAdapter {
	List<View> viewLists;

	public ViewPageAdapter(List<View> lists) {
		// TODO Auto-generated constructor stub
		this.viewLists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewLists.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(viewLists.get(position), 0);
		return viewLists.get(position);
	}
}

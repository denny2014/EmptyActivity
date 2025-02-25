package com.zet.adapter;

import java.util.List;

import android.R.id;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.utils.LogUtils;
import com.zet.R;
import com.zet.parser.DETAIL;

public class EmailDetailAdapter extends BaseAdapter {
	private List<DETAIL> lists;
	private Context context;
	private LayoutInflater mLayoutInflater;

	public EmailDetailAdapter(Context mContext, List<DETAIL> lists) {
		this.lists = lists;
		this.context = mContext;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void changeData(List<DETAIL> lists) {
		this.lists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return lists.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.email_detail_item,
					null);
		}
		DETAIL mDetail = lists.get(position);
		TextView mTextView1 = (TextView) convertView
				.findViewById(R.id.exp_email_detail_1);
//		TextView mTextView2 = (TextView) convertView
//				.findViewById(R.id.exp_email_detail_2);
		TextView mTextView3 = (TextView) convertView
				.findViewById(R.id.exp_email_detail_3);
//		TextView mTextView4 = (TextView) convertView
//				.findViewById(R.id.exp_email_detail_4);
		String mName=TextUtils.isEmpty(mDetail.getELEMENT_NAME())?"":mDetail.getELEMENT_NAME();
		String mContent=TextUtils.isEmpty(mDetail.getPROC_CONTENT())?"":mDetail.getPROC_CONTENT();
		if(!TextUtils.isEmpty(mName)&&!TextUtils.isEmpty(mContent)){
			mTextView1.setText(mName+" : ");
			mTextView3.setText(mContent);
		}
//		String mDate=TextUtils.isEmpty(mDetail.getSET_YEAR())?"":mDetail.getSET_YEAR();
//		if(!TextUtils.isEmpty(mDate)){
//			mTextView2.setText("日期 ：" + mDate);
//		}
		return convertView;
	}

}

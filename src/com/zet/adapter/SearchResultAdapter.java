package com.zet.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.utils.InjectUtils;
import com.zet.R;
import com.zet.model.DocInfo;
import com.zet.model.NewsInBrief;
import com.zet.model.ReciveOneMailDB;

/**
 * Created by macchen on 15/4/10.
 */
public class SearchResultAdapter extends BaseAdapter {

	private List<Object> list;

	SimpleDateFormat fomat = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat fomat2 = new SimpleDateFormat("yyyy-MM-dd");
	public SearchResultAdapter(List<? extends Object> list) {
		this.list = (List<Object>) list;
	}

	public List<Object> getList() {
		return list;
	}

	public void addList(List<? extends Object> lists) {
		list.addAll(lists);
	}

	public void setList(List<? extends Object> list) {
		this.list = (List<Object>) list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object object = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			if (object instanceof ReciveOneMailDB) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_item_search_result, null);

			} else {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, null);
			}
			holder = new ViewHolder();
			InjectUtils.inject(holder, convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (object instanceof DocInfo) {
			DocInfo info = (DocInfo) object;
			holder.tvName.setText(info.getTitle());
			if (info.getPublish_date().equals("")) {
				holder.tvYear.setText(info.getDocYear() + "年度");
			}else{
				try {
					holder.tvYear.setText(fomat2.format(fomat.parse(info.getPublish_date())));
				} catch (ParseException e) {
					e.printStackTrace();
					holder.tvYear.setText(info.getPublish_date());
				}
			}
			holder.tvName.setTextColor(info.isLook() ? Color.GRAY : Color.BLACK);
		} else if (object instanceof ReciveOneMailDB) {
			ReciveOneMailDB mReciveOneMail = (ReciveOneMailDB) object;
			try {
				holder.tvName.setText(TextUtils.isEmpty(mReciveOneMail.getSubject()) ? "无标题文档" : mReciveOneMail.getSubject());
				holder.tvYear.setText(mReciveOneMail.getSentDate() + "年度");
				holder.tvName.setTextColor(mReciveOneMail.getHasRead().equals("true") ? Color.GRAY : Color.BLACK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (object instanceof NewsInBrief) {
			NewsInBrief mNewsInBrief = (NewsInBrief) object;
			holder.tvName.setText(mNewsInBrief.getTITLE());
			holder.tvYear.setText(mNewsInBrief.getSET_YEAR());
			holder.tvName.setTextColor(mNewsInBrief.isLook() ? Color.GRAY : Color.BLACK);
		}
		return convertView;
	}

	public class ViewHolder {
		@ViewInject(R.id.tvResultName)
		public TextView tvName;
		@ViewInject(R.id.tvResultYear)
		TextView tvYear;
	}
}

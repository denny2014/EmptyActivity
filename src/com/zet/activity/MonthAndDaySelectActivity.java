package com.zet.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.SerializeUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.adapter.DepartAdapter;
import com.zet.adapter.YearAdapter;
import com.zet.asyncHandler.DepartHandler;
import com.zet.asyncHandler.YearHandler;
import com.zet.db.SQLiteUtils;
import com.zet.model.DepartInfo;
import com.zet.model.UserInfo;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.net.INetProxy;

/**
 * 选择列表 Created by macchen on 15/4/3.
 */
@ContentView(R.layout.activity_select_department)
public class MonthAndDaySelectActivity extends BaseActivity implements AdapterView.OnItemClickListener {
	@ViewInject(R.id.listview)
	ListView lvSelectable;

	/**
	 * 选择类型(0表示月、1表示日)
	 */
	private int selectItem = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.iv_collect).setVisibility(View.INVISIBLE);
		findViewById(R.id.iv_help).setVisibility(View.INVISIBLE);

		List<String> list = new ArrayList<String>();
		selectItem = getIntent().getIntExtra("select_type", 0);
		if (selectItem == 0) {
			list.add("所有月份");
			for (int i = 0; i < 12; i++) {
				if (i < 9) {
					list.add("0" + (i + 1) + "");
				} else {
					list.add((i + 1) + "");
				}
			}
		} else {
			list.add("所有日期");
			for (int i = 0; i < 31; i++) {
				if (i < 9) {
					list.add("0" + (i + 1) + "");
				} else {
					list.add((i + 1) + "");
				}
			}
		}
		lvSelectable.setAdapter(new YearAdapter(list));
		lvSelectable.setOnItemClickListener(this);

	}

	@OnClick({ R.id.tv_back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		// DepartInfo info = (DepartInfo) lvSelectable.getAdapter().getItem(
		// position);
		// intent.putExtra("depart_code", info.getCode());
		// intent.putExtra("depart_name", info.getName());
		intent.putExtra("value", lvSelectable.getAdapter().getItem(position).toString());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean success(String type, Object objects) {
		Object[] data = (Object[]) objects;
		if (type.equals(Constants.DEPARTMENT_ACTION)) {
			List<DepartInfo> infos = (List<DepartInfo>) data[1];
			DepartInfo info = new DepartInfo();
			info.setCode("");
			info.setName(getString(R.string.activity_search_all_depart));
			infos.add(0, info);
			lvSelectable.setAdapter(new DepartAdapter(infos));
		} else if (type.equals(Constants.GET_DOC_YEARS_ACTION)) {
			List<String> infos = (List<String>) data[1];
			infos.add(0, getString(R.string.activity_search_all_year));
			lvSelectable.setAdapter(new YearAdapter(infos));
		}
		return super.success(type, objects);
	}
}
package com.zet.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.DialogUtils;
import com.bdkj.bdlibrary.utils.IMap;
import com.bdkj.bdlibrary.utils.JSONUtils;
import com.zet.Constants;
import com.zet.R;
import com.zet.model.NewsInBrief;
import com.zet.pull.PullableListView;

public class ParseJsonUtil {
	public static ParseJsonUtil utils;

	public static ParseJsonUtil getInstance() {
		if (utils == null) {
			utils = new ParseJsonUtil();
		}
		return utils;
	}

	/** 请求请求是否成功 */
	public boolean parseIsSuccess(String str) {
		try {
			JSONObject object = new JSONObject(str);
			return "1".equals(object.getString(Constants.RESULT_KEY));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 获取失败信息 */
	public String getErrorMessage(String str) {
		try {
			JSONObject object = new JSONObject(str);
			return object.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获取简讯列表信息 */
	public List<NewsInBrief> parseNewsInBriefMessage(String str) {
		List<NewsInBrief> list = new ArrayList<NewsInBrief>();
		IMap map = null;
		try {
			JSONObject object = new JSONObject(str);
			map = JSONUtils.fromJson(object);
			if (map.containsKey("data") && map.get("data") instanceof List) {
				list = new ArrayList<NewsInBrief>();
				ArrayList<IMap> datas = (ArrayList<IMap>) map.get("data");
				for (IMap single : datas) {
					NewsInBrief mNewsInBrief = new NewsInBrief();
					mNewsInBrief.build(single);
					list.add(mNewsInBrief);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void createErrorDialog(final Context context, String msg) {
		DialogUtils.showAlertNoTitle(context, msg,
				context.getString(R.string.dialog_sure),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
	}

	public void showFailDialog(final Context context) {
		DialogUtils.showAlertNoTitle(context,
				context.getString(R.string.dialog_loading_fail),
				context.getString(R.string.dialog_sure),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
	}
}

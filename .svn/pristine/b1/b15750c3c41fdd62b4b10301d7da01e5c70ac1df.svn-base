package com.zet.asyncHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bdkj.bdlibrary.utils.IMap;
import com.bdkj.bdlibrary.utils.JSONUtils;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.Constants;
import com.zet.model.NewsInBrief;
import com.zet.net.BaseHandler;

public class NewsInBriefHandler extends BaseHandler{

	@Override
	public void onSuccess(int arg0, Header[] arg1, JSONObject jsonObject) {
		// TODO Auto-generated method stub
		super.onSuccess(arg0, arg1, jsonObject);
	        boolean success = false;
	        int resultCode = -1;
	        List<NewsInBrief> list = null;
	        String msg = "";
	        IMap map = null;
	        try {
	            map = JSONUtils.fromJson(jsonObject);
	            resultCode = map.getInt(Constants.RESULT_KEY);
	            LogUtils.e("resultCode : "+resultCode);
	            success = (resultCode == Constants.SUCCESS_CODE);
	            if (!success) {
	                msg = map.getString("msg");
	            } else {
	                if (map.containsKey("data") && map.get("data") instanceof List) {
	                    list = new ArrayList<NewsInBrief>();
	                    ArrayList<IMap> datas = (ArrayList<IMap>) map.get("data");
	                    for (IMap single : datas) {
	                    	NewsInBrief mNewsInBrief = new NewsInBrief();
	                    	LogUtils.e("single : "+single.toString());
	                    	mNewsInBrief.build(single);
	                        list.add(mNewsInBrief);
	                    }
	                    LogUtils.e("list.tostring :"+ list.toString());
	                }
	            }
	        } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        if (success && list != null) {
	            success(resultCode, list);
	        } else {
	            dataErrorMsg(resultCode, msg);
	        }
	    }
}

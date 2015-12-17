package com.zet.asyncHandler;

import com.bdkj.bdlibrary.utils.IMap;
import com.bdkj.bdlibrary.utils.JSONUtils;
import com.zet.Constants;
import com.zet.model.DocInfo;
import com.zet.net.BaseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取搜索列表的通信处理和解析 (non-Javadoc)
 */
public class DocHandler extends BaseHandler {

    @Override
    public void onSuccess(int arg0, Header[] arg1, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.onSuccess(arg0, arg1, jsonObject);
        boolean success = false;
        int resultCode = -1;
        List<DocInfo> list = null;
        String msg = "";
        IMap map = null;
        try {
            map = JSONUtils.fromJson(jsonObject);
            resultCode = map.getInt(Constants.RESULT_KEY);
            success = (resultCode == Constants.SUCCESS_CODE);
            if (!success) {
                msg = map.getString("msg");
            } else {
                if (map.containsKey("data") && map.get("data") instanceof List) {
                    list = new ArrayList<DocInfo>();
                    ArrayList<IMap> datas = (ArrayList<IMap>) map.get("data");
                    for (IMap single : datas) {
                        DocInfo info = new DocInfo();
                        info.build(single);
                        list.add(info);
                    }
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

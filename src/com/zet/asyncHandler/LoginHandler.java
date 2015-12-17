package com.zet.asyncHandler;

import com.bdkj.bdlibrary.utils.IMap;
import com.bdkj.bdlibrary.utils.JSONUtils;
import com.zet.Constants;
import com.zet.model.UserInfo;
import com.zet.net.BaseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录的通信处理和解析 (non-Javadoc)
 */
public class LoginHandler extends BaseHandler {

    @Override
    public void onSuccess(int arg0, Header[] arg1, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.onSuccess(arg0, arg1, jsonObject);
        boolean success = false;
        int resultCode = -1;
        UserInfo userInfo = null;
        String msg = "";
        IMap map = null;
        try {
            map = JSONUtils.fromJson(jsonObject);
            resultCode = map.getInt(Constants.RESULT_KEY);
            success = (resultCode == Constants.SUCCESS_CODE);
            if (!success) {
                msg = map.getString("msg");
            } else {
                if (map.containsKey("data") && map.get("data") instanceof IMap) {
                    IMap data = (IMap) map.get("data");
                    userInfo = new UserInfo();
                    userInfo.build(data);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (success && userInfo != null) {
            success(resultCode, userInfo);
        } else {
            dataErrorMsg(resultCode, msg);
        }
    }
}

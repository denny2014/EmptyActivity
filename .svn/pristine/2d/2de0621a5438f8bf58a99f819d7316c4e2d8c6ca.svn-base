package com.zet.asyncHandler;

import com.bdkj.bdlibrary.model.VersionInfo;
import com.bdkj.bdlibrary.utils.IMap;
import com.bdkj.bdlibrary.utils.JSONUtils;
import com.zet.Constants;
import com.zet.net.BaseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 检查版本的通信处理和解析 (non-Javadoc)
 */
public class CheckVersionHandler extends BaseHandler {

    @Override
    public void onSuccess(int arg0, Header[] arg1, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.onSuccess(arg0, arg1, jsonObject);
        boolean success = false;
        int resultCode = -1;
        VersionInfo versionInfo = null;
        String msg = "";
        String wpsUrl = "";
        String wpsver = "";
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
                    versionInfo = new VersionInfo();
                    versionInfo.setNewVersion(data.getString("ver"));
                    versionInfo.setUpdate(2);
                    versionInfo.setUrl(data.getString("url"));
                    wpsUrl = data.getString("wpsurl");
                    wpsver = data.getString("wpsver");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (success && versionInfo != null) {
            success(resultCode, versionInfo,wpsUrl,wpsver);
        } else {
            dataErrorMsg(resultCode, msg);
        }
    }
}

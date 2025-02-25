package com.zet.asyncHandler;

import com.bdkj.bdlibrary.utils.IMap;
import com.bdkj.bdlibrary.utils.JSONUtils;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.Constants;
import com.zet.model.DetailInfo;
import com.zet.model.DocInfo;
import com.zet.model.FileInfo;
import com.zet.model.TiYaoInfo;
import com.zet.net.BaseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 获取详情的通信处理和解析 (non-Javadoc)
 */
public class DetailHandler extends BaseHandler {

    @Override
    public void onSuccess(int arg0, Header[] arg1, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.onSuccess(arg0, arg1, jsonObject);
        boolean success = false;
        int resultCode = -1;
        DetailInfo info = null;
        LogUtils.e("zjy"+jsonObject.toString());
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
                    info = new DetailInfo();
                    IMap data = (IMap) map.get("data");
                    info.setTitle(data.getString("title"));
                    if (data.containsKey("tiyao") && data.get("tiyao") instanceof List) {
                        List<TiYaoInfo> infos = new ArrayList<TiYaoInfo>();
                        List<IMap> tiyaoData = (List<IMap>) data.get("tiyao");
                        for (IMap single : tiyaoData) {
                            TiYaoInfo tiYaoInfo = new TiYaoInfo();
                            tiYaoInfo.build(single);
                            infos.add(tiYaoInfo);
                        }
                        info.setTiyao(infos);
                    }
                    if (data.containsKey("tiyao1")) {
                        Object object = data.get("tiyao1");
                        if (object instanceof IMap) {
                            IMap tiyaoData = (IMap) object;
                            Iterator<String> iterator = tiyaoData.keySet().iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                if ("1".equals(key)) {
                                    continue;
                                } else if (tiyaoData.get(key) instanceof List) {
                                    List<TiYaoInfo> infos = new ArrayList<TiYaoInfo>();
                                    List<IMap> singleData = (List<IMap>) tiyaoData.get(key);
                                    for (IMap single : singleData) {
                                        TiYaoInfo tiYaoInfo = new TiYaoInfo();
                                        tiYaoInfo.build(single);
                                        infos.add(tiYaoInfo);
                                    }
//                                    info.setTiyao1(infos);
                                    if(info.getTiyao()==null)
                                    {
                                        info.setTiyao(infos);
                                    }
                                    else {
                                        info.getTiyao().addAll(infos);
                                    }
                                }

                            }
                        } /*else if (object instanceof List) {
                            List<TiYaoInfo> infos = new ArrayList<TiYaoInfo>();
                            List<IMap> tiyaoData = (List<IMap>) object;
                            for (IMap single : tiyaoData) {
                                TiYaoInfo tiYaoInfo = new TiYaoInfo();
                                tiYaoInfo.build(single);
                                infos.add(tiYaoInfo);
                            }
                            info.setTiyao1(infos);
                        }*/
                    }

                    if (data.containsKey("files")) {
                        Object object = data.get("files");
                        if (object instanceof IMap) {

                        } else if (object instanceof List) {
                            List<FileInfo> infos = new ArrayList<FileInfo>();
                            List<IMap> fileData = (List<IMap>) object;
                            for (IMap single : fileData) {
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.build(single);
                                infos.add(fileInfo);
                            }
                            info.setFujian(infos);
                        }
                    }

                    if (data.containsKey("relate")) {
                        Object object = data.get("relate");
                        if (object instanceof IMap) {

                        } else if (object instanceof List) {
                            List<DocInfo> infos = new ArrayList<DocInfo>();
                            List<IMap> fileData = (List<IMap>) object;
                            for (IMap single : fileData) {
                                DocInfo docInfo = new DocInfo();
                                docInfo.build(single);
                                infos.add(docInfo);
                            }
                            info.setRelative(infos);
                        }
                    }

                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (success && info != null) {
            success(resultCode, info);
        } else {
            dataErrorMsg(resultCode, msg);
        }
    }
}

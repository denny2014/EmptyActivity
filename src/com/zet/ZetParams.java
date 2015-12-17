package com.zet;

import com.lidroid.xutils.utils.LogUtils;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 通信过程的参数定义
 * Created by macchen on 15/4/7.
 */
public class ZetParams {

    /**
     * IMEI登录参数
     *
     * @param imei
     * @return
     */
    public static RequestParams getImeiLogin(String imei) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.LOGIN_IMEI_ACTION);
        params.put("imei", imei);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

    /**
     * 获取公司名称
     *
     * @param imei
     * @return
     */
    public static RequestParams getCompanyName(String imei) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.GETCOMMPANY_ACTION);
        params.put("imei", imei);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

    /**
     * 用户名密码登录参数
     *
     * @param username
     * @param pass
     * @param imei
     * @return
     */
    public static RequestParams getLogin(String username, String pass, String imei) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.LOGIN_ACTION);
        params.put("loginname", username);
        params.put("password", pass);
        params.put("imei", imei);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

    /**
     * 修改密码参数
     *
     * @param username
     * @param oldPass
     * @param newPass
     * @return
     */
    public static RequestParams getChangePass(String username, String oldPass, String newPass) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.CHANGE_PSW_ACTION);
        params.put("loginname", username);
        params.put("oldpasswd", oldPass);
        params.put("passwd", newPass);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

    /**
     * 获取所有部门
     *
     * @param admdivcode
     * @param divcode
     * @param userId
     * @return
     */
    public static RequestParams getDepartment(String admdivcode, String divcode, int userId) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.DEPARTMENT_ACTION);
        params.put("admdivcode", admdivcode);
        params.put("divcode", divcode);
        params.put("userid", userId);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

    /**
     * 获取搜索结果
     *
     * @param keyWord
     * @param admdivcode
     * @param divcode
     * @param depcode
     * @param fileNO
     * @param sdocyear
     * @param userId
     * @param doctype
     * @param row
     * @param start
     * @param onlineString
     * @return
     */
    public static RequestParams getSearchList(String keyWord, String admdivcode, String divcode, String depcode, String fileNO, String sdocyear, int userId, String doctype, String row, String start, String onlineString,String docType, String month, String day) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.SEARCH_ACTION);
        try {
            params.put("keyword", URLEncoder.encode(keyWord, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            params.put("keyword", keyWord);
        }
        params.put("admdivcode", admdivcode);
        params.put("divcode", divcode);
        params.put("depcode", depcode);
        params.put("fileno", fileNO);
        if(docType.equals("11")){
        	params.put("year", sdocyear);
        	params.put("month", month);
        	params.put("day", day);
        }else {
        	params.put("docyear", sdocyear);
		}
        params.put("userid", userId);
        params.put("doctype", doctype);
        params.put("rows", row);
        params.put("start", start);
        params.put("online", onlineString);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        LogUtils.e(params.toString());
        return params;
    }
    

    /**
     * 获取详情
     *
     * @param fileId
     * @param fileNo
     * @param setyear
     * @param userId
     * @return
     */
    public static RequestParams getDetail(String fileId, String fileNo, String setyear, int userId) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.DETAIL_ACTION);
        params.put("fileid", fileId);
        params.put("fileno", fileNo);
        params.put("setyear", setyear);
        params.put("userid", userId);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

    /**
     * 检查版本
     *
     * @param curVer
     * @param imei
     * @return
     */
    public static RequestParams checkVersion(String curVer, String imei) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.CHECK_VERSION_ACTION);
        params.put("ver", curVer);
        params.put("imei", imei);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }


    /**
     * 获取年度
     *
     * @param admdivcode
     * @param divcode
     * @param userId
     * @return
     */
    public static RequestParams getYear(String admdivcode, String divcode, int userId,String docType) {
        RequestParams params = new RequestParams();
        params.put("action", Constants.GET_DOC_YEARS_ACTION);
        params.put("admdivcode", admdivcode);
        params.put("divcode", divcode);
        params.put("userid", userId);
        params.put("doctype", docType);
        params.put("debug", Constants.PARAMS_DEBUG_VALUE);
        return params;
    }

}

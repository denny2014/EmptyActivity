package com.zet.model;

import com.bdkj.bdlibrary.utils.IMap;

import java.io.Serializable;

/**
 * Created by macchen on 15/4/7.
 */
public class UserInfo implements Serializable {
    private int userId;
    private String userCode;
    private String admdivCode;
    private String divCode;
    private int isEnabled;
    private String username;
    private String nickName;
    private String admdivName;
    private String divName;
    private String USERMAIL;
    private String USERMAILPASS;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAdmdivCode() {
        return admdivCode;
    }

    public void setAdmdivCode(String admdivCode) {
        this.admdivCode = admdivCode;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdmdivName() {
        return admdivName;
    }

    public void setAdmdivName(String admdivName) {
        this.admdivName = admdivName;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDivCode() {
        return divCode;
    }

    public void setDivCode(String divCode) {
        this.divCode = divCode;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUSERMAIL() {
		return USERMAIL;
	}

	public String getUSERMAILPASS() {
		return USERMAILPASS;
	}

	public void build(IMap iMap) {
        userId = iMap.getInt("USER_ID");
        userCode = iMap.getString("USER_CODE");
        admdivCode = iMap.getString("ADMDIV_CODE");
        divCode = iMap.getString("DIV_CODE");
        isEnabled = iMap.getInt("IS_ENABLED");
        username = iMap.getString("USER_NAME");
        nickName = iMap.getString("NICKNAME");
        admdivName = iMap.getString("ADMDIV_NAME");
        divName = iMap.getString("DIV_NAME");
        USERMAIL= iMap.getString("USERMAIL");
        USERMAILPASS= iMap.getString("USERMAILPASS");
    }
}

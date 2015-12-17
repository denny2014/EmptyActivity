package com.zet.model;

import com.bdkj.bdlibrary.utils.IMap;

import java.io.Serializable;

/**
 * Created by zhangjy on 15/8/4.
 */
public class NewsInBrief implements Serializable {
	private int DEP_CODE;
	private String DEP_NAME;
	private String SET_YEAR;
	private int RELATE_ID;
	private int SERIALNUM;
	private String FILE_NO;
	private String TITLE;
	private String FILE_ID;
	/**
	 * 是否看过
	 */
	private boolean isLook;

	public String getFILE_ID() {
		return FILE_ID;
	}

	public int getDEP_CODE() {
		return DEP_CODE;
	}

	public String getDEP_NAME() {
		return DEP_NAME;
	}

	public String getSET_YEAR() {
		return SET_YEAR;
	}

//	public int getRELATE_ID() {
//		return RELATE_ID;
//	}

//	public int getSERIALNUM() {
//		return SERIALNUM;
//	}

	public String getFILE_NO() {
		return FILE_NO;
	}

	public String getTITLE() {
		return TITLE;
	}

	public boolean isLook() {
		return isLook;
	}

	public void setIsLook(boolean isLook) {
		this.isLook = isLook;
	}

	public void build(IMap iMap) {
		this.DEP_CODE = iMap.getInt("DEP_CODE");
		this.DEP_NAME = iMap.getString("DEP_NAME");
		this.SET_YEAR = iMap.getString("SET_YEAR");
//		this.RELATE_ID = iMap.getInt("RELATE_ID");
//		this.SERIALNUM = iMap.getInt("SERIALNUM");
		this.FILE_NO = iMap.getString("FILE_NO");
		this.TITLE = iMap.getString("TITLE");
//		this.isLook = iMap.getBoolean("isLook");
		this.FILE_ID= iMap.getString("FILE_ID");
	}

	@Override
	public String toString() {
		return "NewsInBrief [DEP_CODE=" + DEP_CODE + ", DEP_NAME=" + DEP_NAME
				+ ", SET_YEAR=" + SET_YEAR + ", RELATE_ID=" + RELATE_ID
				+ ", SERIALNUM=" + SERIALNUM + ", FILE_NO=" + FILE_NO
				+ ", TITLE=" + TITLE + ", isLook=" + isLook + "]";
	}
}

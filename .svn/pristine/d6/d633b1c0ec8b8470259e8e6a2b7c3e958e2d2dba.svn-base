package com.zet.parser;

import java.util.List;
import java.util.Vector;

public class RSSFeed {
	private String ADMDIV_CODE;
	private String SET_YEAR;
	private String DOC_YEAR;
	private String DIV_CODE;
	private String PRJ_CODE;
	private String FILE_ID;
	private String TITLE;
	private String REPORT_DAT;
	private String DEP_CODE;
	private String DEP_NAME;
	private String LEADER;
	private String REPORT_USER;
	private String FILE_NO;
	private String LEADER_NAME;
	private String FILE_TYPE;
//	private String REPORT_TYPE;
	private List<DETAIL> mDetails;
	private List<ATTACH> mAttachs;
	
	private int mDetailsItemCount;
	private int mAttachsItemCount;
	public RSSFeed() {
		mDetails = new Vector<DETAIL>(0);
		mAttachs = new Vector<ATTACH>(0);
		
//		itemlist = new Vector<RSSItem>(0);
	}
	
	public int addDETAILItem(DETAIL mDETAIL) {
		mDetails.add(mDETAIL);
		mDetailsItemCount++;
		return mDetailsItemCount;
	}
	public int addATTACHItem(ATTACH mATTACH) {
		mAttachs.add(mATTACH);
		mAttachsItemCount++;
		return mAttachsItemCount;
	}
	
	public int getmDetailsItemCount() {
		return mDetailsItemCount;
	}

	public int getmAttachsItemCount() {
		return mAttachsItemCount;
	}

	public DETAIL getDETAILItem(int location) {
		return mDetails.get(location);
	}
	
	public ATTACH getATTACHItem(int location) {
		return mAttachs.get(location);
	}

	public List<DETAIL> getAllDETAILItems() {
		return mDetails;
	}
	
	public List<ATTACH> getAllATTACHItems() {
		return mAttachs;
	}
	public String getSET_YEAR() {
		return SET_YEAR;
	}

	public void setADMDIV_CODE(String aDMDIV_CODE) {
		ADMDIV_CODE = aDMDIV_CODE;
	}

	public void setDOC_YEAR(String dOC_YEAR) {
		DOC_YEAR = dOC_YEAR;
	}

	public void setDIV_CODE(String dIV_CODE) {
		DIV_CODE = dIV_CODE;
	}

	public void setFILE_ID(String fILE_ID) {
		FILE_ID = fILE_ID;
	}

	public void setDEP_CODE(String dEP_CODE) {
		DEP_CODE = dEP_CODE;
	}

	public void setDEP_NAME(String dEP_NAME) {
		DEP_NAME = dEP_NAME;
	}

	public void setLEADER(String lEADER) {
		LEADER = lEADER;
	}

	public void setFILE_NO(String fILE_NO) {
		FILE_NO = fILE_NO;
	}

	public void setLEADER_NAME(String lEADER_NAME) {
		LEADER_NAME = lEADER_NAME;
	}

	public void setSET_YEAR(String sET_YEAR) {
		SET_YEAR = sET_YEAR;
	}

	public String getPRJ_CODE() {
		return PRJ_CODE;
	}

	public void setPRJ_CODE(String pRJ_CODE) {
		PRJ_CODE = pRJ_CODE;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public String getREPORT_DAT() {
		return REPORT_DAT;
	}

	public void setREPORT_DAT(String rEPORT_DAT) {
		REPORT_DAT = rEPORT_DAT;
	}

	public String getREPORT_USER() {
		return REPORT_USER;
	}

	public void setREPORT_USER(String rEPORT_USER) {
		REPORT_USER = rEPORT_USER;
	}

	public String getFILE_TYPE() {
		return FILE_TYPE;
	}

	public void setFILE_TYPE(String fILE_TYPE) {
		FILE_TYPE = fILE_TYPE;
	}

	public String getADMDIV_CODE() {
		return ADMDIV_CODE;
	}

	public String getDOC_YEAR() {
		return DOC_YEAR;
	}

	public String getDIV_CODE() {
		return DIV_CODE;
	}

	public String getFILE_ID() {
		return FILE_ID;
	}

	public String getDEP_CODE() {
		return DEP_CODE;
	}

	public String getDEP_NAME() {
		return DEP_NAME;
	}

	public String getLEADER() {
		return LEADER;
	}

	public String getFILE_NO() {
		return FILE_NO;
	}

	public String getLEADER_NAME() {
		return LEADER_NAME;
	}

	@Override
	public String toString() {
		return "RSSFeed [ADMDIV_CODE=" + ADMDIV_CODE + ", SET_YEAR=" + SET_YEAR
				+ ", DOC_YEAR=" + DOC_YEAR + ", DIV_CODE=" + DIV_CODE
				+ ", PRJ_CODE=" + PRJ_CODE + ", FILE_ID=" + FILE_ID
				+ ", TITLE=" + TITLE + ", REPORT_DAT=" + REPORT_DAT
				+ ", DEP_CODE=" + DEP_CODE + ", DEP_NAME=" + DEP_NAME
				+ ", LEADER=" + LEADER + ", REPORT_USER=" + REPORT_USER
				+ ", FILE_NO=" + FILE_NO + ", LEADER_NAME=" + LEADER_NAME
				+ ", REPORT_TYPE=" + FILE_TYPE + ", mDetails=" + mDetails
				+ ", mAttachs=" + mAttachs + ", mDetailsItemCount="
				+ mDetailsItemCount + ", mAttachsItemCount="
				+ mAttachsItemCount + "]";
	}
}
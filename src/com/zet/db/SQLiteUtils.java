package com.zet.db;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zet.R;
import com.zet.adapter.DepartAdapter;
import com.zet.email.ReciveOneMail;
import com.zet.model.DepartInfo;
import com.zet.model.DocInfo;
import com.zet.model.ReciveOneMailDB;
import com.zet.parser.ATTACH;
import com.zet.parser.DETAIL;
import com.zet.parser.RSSFeed;

/**
 * 数据库工具类 Created by macchen on 15/4/10.
 */
public class SQLiteUtils {

	private static class SQLiteUtilsInstance {
		public static SQLiteUtils utils = new SQLiteUtils();
	}

	private static SQLiteDatabase database = null;

	private SQLiteUtils() {
	}

	/**
	 * 获取数据库单例
	 * 
	 * @param context
	 * @return
	 */
	public static SQLiteUtils getInstance(Context context) {
		if (database == null) {
			BaseSQLHelper helper = new BaseSQLHelper(context);
			database = helper.getWritableDatabase();
		}
		return SQLiteUtilsInstance.utils;
	}
	public boolean emailIsExistDB(String fileId, String emailAddress,Boolean Isemaillist, RSSFeed mRSSFeed) {
		boolean isCollect = false;
		String select;
		if(Isemaillist){
			select = "select * from  emaillist where emailAddress = '"
					+ emailAddress + "' and userid = '" + fileId + "'";
		}else{
//			select = "select * from  emailworks  where emailAddress = '"
//					+ emailAddress + "'  and userId = '" + fileId  + "'";
			// 同时检查uid与fileId，如果两个有一个相同则认为相同
			String rssFileId = fileId;
			if (mRSSFeed != null) {
				rssFileId = mRSSFeed.getFILE_ID();
			}
			select = "select * from  emailworks  where emailAddress = '"
					+ emailAddress + "'  and " + 
					"(userId = '" + fileId  + "' OR fileId = '" + rssFileId  + "')";
		}
		Cursor cursor = database.rawQuery(select, null);
		if (cursor != null && cursor.getCount() > 0) {
			isCollect = true;
		}
		cursor.close();
//		LogUtils.e(fileId+isCollect);
		return isCollect;
	}
	/**
	 * 添加邮件到数据库
	 * 
	 * @param info
	 * @return
	 */
	public long addEmailToDB(ReciveOneMail mReciveOneMail,
			boolean isContainAttach, String userid, String emailAddress,
			String appfileName) {
		if(emailIsExistDB(userid, emailAddress, true, null)){
			database.delete("emaillist", "userid=?", new String[]{userid});
		}
		ContentValues values = new ContentValues();
		try {
			values.put("userid", userid);
			values.put("subject", mReciveOneMail.getSubject());
			values.put("sentDate", mReciveOneMail.getSentDate());
			values.put("replysign", "" + mReciveOneMail.getReplySign());
			values.put("hasRead", "" + mReciveOneMail.isNew());
			values.put("containAttachment", "" + isContainAttach);
			values.put("form", mReciveOneMail.getFrom());
			values.put("topeople", mReciveOneMail.getMailAddress("to"));
			values.put("ccpeople", mReciveOneMail.getMailAddress("cc"));
			values.put("messageid", mReciveOneMail.getMessageId());
			values.put("bodycontent", mReciveOneMail.getBodyText());
			values.put("emailAddress", emailAddress);
			values.put("appfileName", appfileName);
			values.put("emailVisual", "" + false);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long index1 = database.insert("emaillist", null, values);
		return index1;
	}

	/**
	 * 添加本地邮件数据到数据库
	 * 
	 * @param info
	 * @return
	 */
	public long addLocalEmailToDB(String userid, String emailAddress) {
		if(emailIsExistDB(userid, emailAddress, true, null)){
			database.delete("emaillist", "userid=?", new String[]{userid});
		}
		ContentValues values = new ContentValues();
		try {
			values.put("userid", userid);
			values.put("emailAddress", emailAddress);
			values.put("emailVisual", "" + false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long index1 = database.insert("emaillist", null, values);
		return index1;
	}
//	public String getEmailIDate(String uid, String emailAddress) {
//		String docYear = null ;
//		String title;
//		String select = "select * from  emailworks  where emailAddress = '"
//				+ emailAddress + "'  and userid = '" + uid + "'";
//		Cursor cursor = database.rawQuery(select, null);
//		if (cursor != null && cursor.getCount() > 0) {
//			while (cursor.moveToNext()) {
//				docYear = cursor.getString(cursor.getColumnIndex("docYear"));
//				title = cursor.getString(cursor.getColumnIndex("title"));
//			}
//		}
//		cursor.close();
//		return docYear;
//	}
	/**
	 * 添加EmailWorks到数据库
	 * 
	 * @param info
	 * @return
	 */
	public long addEmailWorksToDB(RSSFeed mRSSFeed, String userid,
			String emailAddress) {
		if(emailIsExistDB(userid, emailAddress, false, mRSSFeed)){
			return -1;
		}
		ContentValues values = new ContentValues();
		try {
			values.put("admdivCode", mRSSFeed.getADMDIV_CODE());
			values.put("setyear", mRSSFeed.getSET_YEAR());
			values.put("docYear", mRSSFeed.getDOC_YEAR());
			values.put("divCode", mRSSFeed.getDIV_CODE());
			values.put("prjCode", mRSSFeed.getPRJ_CODE());
			values.put("fileId", mRSSFeed.getFILE_ID());
			values.put("title", mRSSFeed.getTITLE());
			values.put("peportDate", mRSSFeed.getREPORT_DAT());
			values.put("depCode", mRSSFeed.getDEP_CODE());
			values.put("depName", mRSSFeed.getDEP_NAME());
			values.put("leader", mRSSFeed.getLEADER());
			values.put("reportUser", mRSSFeed.getREPORT_USER());
			values.put("fileNo", mRSSFeed.getFILE_NO());
			values.put("leaderName", mRSSFeed.getLEADER_NAME());
			values.put("reportType", mRSSFeed.getFILE_TYPE());
			values.put("detailsNum", mRSSFeed.getmDetailsItemCount());
			values.put("attachNum", mRSSFeed.getmAttachsItemCount());
			values.put("emailAddress", emailAddress);
			values.put("userId", userid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long index = database.insert("emailworks", null, values);
		return index;
	}

	/**
	 * 添加emaildetails到数据库
	 * 
	 * @param info
	 * @return
	 */
	public void addEmailDetailsToDB(List<DETAIL> mDetails, String userid,
			String emailAddress, String detailId) {
		ContentValues values = new ContentValues();
		try {
			for (DETAIL mDetail : mDetails) {
				values.put("detailId", detailId);
				values.put("setyear", mDetail.getSET_YEAR());
				values.put("fileId", mDetail.getFILE_ID());
				values.put("elementCode", mDetail.getELEMENT_CODE());
				values.put("elementName", mDetail.getELEMENT_NAME());
				values.put("procContent", mDetail.getPROC_CONTENT());
				values.put("emailAddress", emailAddress);
				values.put("userId", userid);
				database.insert("emaildetails", null, values);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressLint("NewApi")
	public List<DETAIL> getEmailDetailFromDb(String emailAddress, String emailUid) {
		List<DETAIL> list = new ArrayList<DETAIL>();
		String select = "select * from  emaildetails  where emailAddress = '"
				+ emailAddress + "'  and userId = '" + emailUid + "'";
		Cursor cursor = null;
		cursor = database.rawQuery(select, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				DETAIL info = new DETAIL();
				info.setSET_YEAR(cursor.getString(cursor
						.getColumnIndex("setyear")));
				info.setFILE_ID(cursor.getString(cursor
						.getColumnIndex("fileId")));
				info.setELEMENT_NAME(cursor.getString(cursor
						.getColumnIndex("elementName")));
				info.setPROC_CONTENT(cursor.getString(cursor
						.getColumnIndex("procContent")));
				list.add(info);
			}
		}
		cursor.close();
		return list;
	}
	/**
	 * 添加emailattachs到数据库
	 * 
	 * @param info
	 * @return
	 */
	public void addEmailAttachsToDB(List<ATTACH> mAttachs, String userid,
			String emailAddress) {
		ContentValues values = new ContentValues();

		try {
			for (ATTACH attach : mAttachs) {
				values.put("setyear", attach.getSET_YEAR());
				values.put("fileId", attach.getFILE_ID());
				values.put("fileType", attach.getFILE_TYPE());
				values.put("attachId", attach.getATTACH_ID());
				values.put("fileBin", attach.getFILE_BIN());
				values.put("fileName", attach.getFILE_NAME());
				values.put("emailAddress", emailAddress);
				values.put("userId", userid);
				database.insert("emailattachs", null, values);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public List<ATTACH> getEmailAttachsFromDb(String emailAddress, String emailUid) {
		List<ATTACH> list = new ArrayList<ATTACH>();
		String select = "select * from  emailattachs  where emailAddress = '"
				+ emailAddress + "'  and userId = '" + emailUid + "'";
		Cursor cursor = null;
		cursor = database.rawQuery(select, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ATTACH info = new ATTACH();
				info.setSET_YEAR(cursor.getString(cursor
						.getColumnIndex("setyear")));
				info.setFILE_ID(cursor.getString(cursor
						.getColumnIndex("fileId")));
				info.setFILE_TYPE(cursor.getString(cursor
						.getColumnIndex("fileType")));
				info.setATTACH_ID(cursor.getString(cursor
						.getColumnIndex("attachId")));
				info.setFILE_NAME(cursor.getString(cursor
						.getColumnIndex("fileName")));
				info.setFILE_BIN(cursor.getString(cursor
						.getColumnIndex("fileBin")));
				list.add(info);
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 更新数据库
	 * 
	 * @param fileId
	 * @return
	 */

	public int emailUpdate(String fileId, String keyValue) {
		ContentValues values = new ContentValues();
		values.put(keyValue, "" + true);// key为字段名，value为值
		return database.update("emaillist", values, "userid = ?",
				new String[] { fileId });
	}

	/**
	 * 查询本地是否存在
	 * 
	 * @param fileId
	 * @return
	 */
	public boolean emailIsExist(String fileId, String emailAddress) {
		boolean isCollect = false;
		String select = "select * from  emaillist,emailworks  where emaillist.emailAddress = '"
				+ emailAddress + "'  and emailworks.emailAddress = '"
				+ emailAddress + "'  and emaillist.userid = '" 
				+ fileId + "' and emailworks.userid = '" 
				+ fileId + "'";
		Cursor cursor = database.rawQuery(select, null);
		if (cursor != null && cursor.getCount() > 0) {
			isCollect = true;
		}
		cursor.close();
//		LogUtils.e(fileId+isCollect);
		return isCollect;
	}

	/**
	 * 获取所有的邮件
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public List<ReciveOneMailDB> getEmailFromDb(int pageBagin, int pageEnd,
			String emailName, String keyWords, String emailAddress,String workType,boolean isYearAndDep) {
		List<ReciveOneMailDB> list = new ArrayList<ReciveOneMailDB>();
		Cursor cursor = null;//
		if (TextUtils.isEmpty(keyWords)) {
			String selectStr = "";
			if(workType.contains("1")){
				selectStr="select emaillist.*,emailworks.docYear,emaildetails.procContent from emaillist ,emailworks,emaildetails where emaillist.userid = emailworks.userid and emaillist.userid = emaildetails.userid and emaildetails.elementCode = '0005' and emaillist.emailAddress = '"
						+emailAddress+"' and emaildetails.emailAddress = '"
						+emailAddress+"' and emailworks.emailAddress= '"
						+emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType like '%" 
						+workType +"%' order by  emaillist.sentDate  desc";
			}else{
				selectStr="select emaillist.*,emailworks.docYear,emailworks.title from emaillist , emailworks where emaillist.userid = emailworks.userid and emaillist.emailAddress = '"
			            +emailAddress+"' and emailworks.emailAddress= '"
						+emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType like '%" 
			            +workType +"%' order by  emaillist.sentDate  desc";
			}
			cursor = database.rawQuery(selectStr, null);
		} else {
			String selectStrkeyWords = null;
			//工作汇报、工作报告、依法执行的检索条件为标题、年度、业务部门
			//预算执行、部门预算的检索条件为标题、年度、业务部门、主管部门
			if(isYearAndDep){
				if(keyWords.contains("所有部门年度")){
					keyWords="";//or emailworks.fileNo like '%"+ keyWordsSecond + "%'
				}
				String depNameStr = keyWords;
				String docYearStr = keyWords;
				
				if (keyWords.contains("@&%")) {
					String[] words = keyWords.split("@&%");
					if (words.length <= 1) {
						depNameStr = words[0];
						docYearStr = "";
					} else {
						depNameStr = words[0];
						docYearStr = words[1];
					}
					
					if (depNameStr.contains("所有")) {
						depNameStr = "";
					}
					if (docYearStr.contains("所有")) {
						docYearStr = "";
					}
				}
				if(workType.contains("1")){
					selectStrkeyWords="select emaillist.*,emailworks.docYear,emaildetails.procContent from emaillist , emailworks,emaildetails where emaillist.userid = emailworks.userid and emaillist.userid = emaildetails.userid and emaildetails.elementCode = '0005' and emaillist.emailAddress = '"
				            +emailAddress+"' and emaildetails.emailAddress = '"
							+emailAddress+"' and emailworks.emailAddress= '"
				            +emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType like '%" 
							+workType +"%' and (emailworks.depName like '%"
							+ depNameStr + "%' and emailworks.docYear like '%"
							+ docYearStr + "%') order by  emaillist.sentDate  desc";
				}else if(workType.contains("4")||workType.contains("5")){
					selectStrkeyWords="select emaillist.*,emailworks.docYear,emailworks.title from emaillist , emailworks where emaillist.userid = emailworks.userid and emaillist.emailAddress = '"
				            +emailAddress+"' and emailworks.emailAddress= '"
							+emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType = '" 
				            +workType +"' and (emailworks.depName like '%"
							+ depNameStr + "%' and emailworks.docYear like '%"
							+ docYearStr + "%') order by  emaillist.sentDate  desc";
				}else if(workType.contains("2")||workType.contains("3")){
					selectStrkeyWords="select emaillist.*,emailworks.docYear,emailworks.title from emaillist , emailworks where emaillist.userid = emailworks.userid and emaillist.emailAddress = '"
				            +emailAddress+"' and emailworks.emailAddress= '"
							+emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType = '" 
				            +workType +"' and (emailworks.depName like '%"
							+ depNameStr + "%'  and emailworks.docYear like '%"
							+ docYearStr + "%' ) order by  emaillist.sentDate  desc";
				}
			}else{
				String keyWordsFirst="";
				String keyWordsSecond="";
				if(keyWords.contains("@&%")){
					keyWordsFirst = keyWords.split("@&%")[0];
					keyWordsSecond= keyWords.split("@&%")[1];
				}else {
					keyWordsFirst=keyWords;
					keyWordsSecond=keyWords;
				}
			if(workType.contains("1")){
				selectStrkeyWords="select emaillist.*,emailworks.docYear,emaildetails.procContent from emaillist , emailworks,emaildetails where emaillist.userid = emailworks.userid and emaillist.userid = emaildetails.userid and emaildetails.elementCode = '0005' and emaillist.emailAddress = '"
			            +emailAddress+"' and emaildetails.emailAddress = '"
						+emailAddress+"' and emailworks.emailAddress= '"
			            +emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType like '%" 
						+workType +"%' and (emailworks.depName like '%"
						+ keyWordsFirst + "%'  or emaildetails.procContent like '%"
						+ keyWordsFirst + "%' or emailworks.fileNo like '%"+ 
						keyWordsSecond + "%' or emailworks.docYear like '%"
						+ keyWordsFirst + "%') order by  emaillist.sentDate  desc";
			}else if(workType.contains("4")||workType.contains("5")){
				selectStrkeyWords="select emaillist.*,emailworks.docYear,emailworks.title from emaillist , emailworks where emaillist.userid = emailworks.userid and emaillist.emailAddress = '"
			            +emailAddress+"' and emailworks.emailAddress= '"
						+emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType = '" 
			            +workType +"' and (emailworks.depName like '%"
						+ keyWordsFirst + "%'   or emailworks.title like '%"
						+ keyWordsFirst + "%' or emailworks.fileNo like '%"+ 
						keyWordsSecond + "%'  or emailworks.docYear like '%"
						+ keyWordsFirst + "%') order by  emaillist.sentDate  desc";
			}else if(workType.contains("2")||workType.contains("3")){
				selectStrkeyWords="select emaillist.*,emailworks.docYear,emailworks.title from emaillist , emailworks where emaillist.userid = emailworks.userid and emaillist.emailAddress = '"
			            +emailAddress+"' and emailworks.emailAddress= '"
						+emailAddress+"'  and emaillist.emailVisual = 'false' and emailworks.reportType = '" 
			            +workType +"' and (emailworks.depName like '%"
						+ keyWordsFirst + "%'  or emailworks.title like '%"
						+ keyWordsFirst + "%' or emailworks.fileNo like '%"+ 
						keyWordsSecond + "%'  or emailworks.docYear like '%"
						+ keyWordsFirst + "%' or emailworks.leaderName like '%"
						+ keyWordsFirst + "%') order by  emaillist.sentDate  desc";
			}
			}
			cursor = database.rawQuery(selectStrkeyWords, null);
		}
		
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ReciveOneMailDB info = new ReciveOneMailDB();
				info.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
//				String sentDate = getEmailIDate(cursor.getString(cursor.getColumnIndex("userid")), emailAddress);
				if(workType.contains("1")){
					info.setSubject(cursor.getString(cursor
							.getColumnIndex("procContent")));
				}else {
					info.setSubject(cursor.getString(cursor
							.getColumnIndex("title")));
				}
				info.setSentDate(cursor.getString(cursor
						.getColumnIndex("docYear")));
				info.setReplysign(cursor.getString(cursor
						.getColumnIndex("replysign")));
				info.setHasRead(cursor.getString(cursor
						.getColumnIndex("hasRead")));
				info.setContainAttachment(cursor.getString(cursor
						.getColumnIndex("containAttachment")));
				info.setForm(cursor.getString(cursor.getColumnIndex("form")));
				info.setTopeople(cursor.getString(cursor
						.getColumnIndex("topeople")));
				info.setCcpeople(cursor.getString(cursor
						.getColumnIndex("ccpeople")));
				info.setMessageid(cursor.getString(cursor
						.getColumnIndex("messageid")));
				info.setBodycontent(cursor.getString(cursor
						.getColumnIndex("bodycontent")));
				info.setAppfileName(cursor.getString(cursor
						.getColumnIndex("appfileName")));
				list.add(info);
			}
		}
		cursor.close();
		return list;
	}

	
	/**
	 * 获取所有的邮件
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public List<String> getEmailDepOrYearFromDb(boolean isDep,String workType) {
		List<String> list = new ArrayList<String>();
		Cursor cursor = null;//
			String selectStr = "";
			if(isDep){
				selectStr="select emailworks.depName from emailworks where reportType like '%" 
						+workType +"%'";
			}else {
				selectStr="select emailworks.docYear from emailworks where reportType like '%" 
						+workType +"%' order by  docYear  desc";
			}
			cursor = database.rawQuery(selectStr, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				if(isDep){
					String depName = cursor.getString(cursor
							.getColumnIndex("depName"));
					if(!list.contains(depName)){
						list.add(depName);
					}
				}else {
					String docYear = cursor.getString(cursor
							.getColumnIndex("docYear"));
					if(!list.contains(docYear)){
						list.add(docYear);
					}
				}
				
			}
		}
		cursor.close();
		return list;
	}
	/**
	 * 添加收藏
	 * 
	 * @param info
	 * @return
	 */
	public long addCollect(DocInfo info) {
		ContentValues values = new ContentValues();
		values.put("title", info.getTitle());
		values.put("docYear", info.getDocYear());
		values.put("fileID", info.getFileId());
		values.put("fileName", info.getFileName());
		values.put("fileheader", info.getFileHead());
		values.put("filesize", info.getFileSize());
		values.put("fileTitle", info.getFileTitle());
		values.put("fileNo", info.getFileNo());
		values.put("setYear", info.getSetYear());
		return database.insert("collection", null, values);

	}

	/**
	 * 删除收藏
	 * 
	 * @param id
	 * @return
	 */
	public int delCollect(int id) {
		return database.delete("collection", "_id=?", new String[] { id + "" });
	}

	/**
	 * 删除收藏
	 * 
	 * @param fileId
	 * @return
	 */
	public int delCollect(String fileId) {
		return database.delete("collection", "fileID=?",
				new String[] { fileId });
	}

	/**
	 * 是否收藏
	 * 
	 * @param fileId
	 * @return
	 */
	public boolean isCollect(String fileId) {
		boolean isCollect = false;
		Cursor cursor = database.query("collection", null, "fileID=?",
				new String[] { fileId }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			isCollect = true;
		}
		cursor.close();
		return isCollect;
	}

	/**
	 * 获取所有的收藏
	 * 
	 * @return
	 */
	public List<DocInfo> getCollect() {
		List<DocInfo> list = new ArrayList<DocInfo>();
		Cursor cursor = database.query("collection", null, null, null, null,
				null, "_id DESC");
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				DocInfo info = new DocInfo();
				info.setDocYear(cursor.getString(cursor
						.getColumnIndex("docYear")));
				info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				info.setFileId(cursor.getString(cursor.getColumnIndex("fileID")));
				info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				info.setFileName(cursor.getString(cursor
						.getColumnIndex("fileName")));
				info.setFileHead(cursor.getString(cursor
						.getColumnIndex("fileheader")));
				info.setFileSize(cursor.getInt(cursor
						.getColumnIndex("filesize")));
				info.setFileTitle(cursor.getString(cursor
						.getColumnIndex("fileTitle")));
				info.setFileNo(cursor.getString(cursor.getColumnIndex("fileNo")));
				info.setSetYear(cursor.getString(cursor
						.getColumnIndex("setYear")));
				list.add(info);
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 添加查看记录
	 * 
	 * @param info
	 * @return
	 */
	/*
	 * public long addRecord(DocInfo info) { ContentValues values = new
	 * ContentValues(); values.put("title", info.getTitle());
	 * values.put("docYear", info.getDocYear()); values.put("fileID",
	 * info.getFileId()); values.put("fileName", info.getFileName());
	 * values.put("fileheader", info.getFileHead()); values.put("filesize",
	 * info.getFileSize()); values.put("fileTitle", info.getFileTitle());
	 * values.put("fileNo", info.getFileName()); values.put("setYear",
	 * info.getSetYear()); values.put("add_time", System.currentTimeMillis());
	 * return database.insert("record", null, values); }
	 */

	/**
	 * 删除查看记录
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * public int delRecord(int id) { return database.delete("record", "_id=?",
	 * new String[]{id + ""}); }
	 */

	/**
	 * 删除查看记录
	 * 
	 * @param fileId
	 * @return
	 */
	/*
	 * public int delRecord(String fileId) { return database.delete("record",
	 * "fileID=?", new String[]{fileId}); }
	 */

	/**
	 * 是否查看过
	 * 
	 * @param fileId
	 * @return
	 */
	/*
	 * public boolean isRecord(String fileId) { boolean isCollect = false;
	 * Cursor cursor = database.query("record", null, "fileID=?", new
	 * String[]{fileId}, null, null, null); if (cursor != null &&
	 * cursor.getCount() > 0) { isCollect = true; } cursor.close(); return
	 * isCollect; }
	 */

	/**
	 * 获取所有的记录
	 * 
	 * @return
	 */
	/*
	 * public List<RecordInfo> getAllRecord() { List<RecordInfo> list = new
	 * ArrayList<RecordInfo>(); Cursor cursor = database.query("collection",
	 * null, null, null, null, null, null); if (cursor != null &&
	 * cursor.getCount() > 0) { while (cursor.moveToNext()) { RecordInfo info =
	 * new RecordInfo();
	 * info.setDocYear(cursor.getString(cursor.getColumnIndex("docYear")));
	 * info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
	 * info.setFileId(cursor.getString(cursor.getColumnIndex("fileID")));
	 * info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
	 * info.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
	 * info.setFileHead(cursor.getString(cursor.getColumnIndex("fileheader")));
	 * info.setFileSize(cursor.getInt(cursor.getColumnIndex("filesize")));
	 * info.setFileTitle(cursor.getString(cursor.getColumnIndex("fileTitle")));
	 * info.setFileNo(cursor.getString(cursor.getColumnIndex("fileNo")));
	 * info.setSetYear(cursor.getString(cursor.getColumnIndex("setYear")));
	 * info.setAddTime(cursor.getLong(cursor.getColumnIndex("add_time")));
	 * list.add(info); } } cursor.close(); return list; }
	 */

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		if (database != null) {
			database.close();
			database = null;
		}
	}

}

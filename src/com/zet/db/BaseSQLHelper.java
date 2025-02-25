package com.zet.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by macchen on 15/4/10.
 */
public class BaseSQLHelper extends SQLiteOpenHelper {
	// 默认数据库名称,版本
	private static final String defaultBDName = "database.db";
	private static final int defaultVersion = 4;

	public BaseSQLHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	public BaseSQLHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public BaseSQLHelper(Context context) {
		super(context, defaultBDName, null, defaultVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS collection(_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,docYear TEXT,fileID TEXT,fileName TEXT,fileheader TEXT,filesize INTEGER,fileTitle TEXT,fileNo TEXT,setYear TEXT,userId TEXT)");
			db.execSQL("CREATE TABLE IF NOT EXISTS record(_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,docYear TEXT,fileID TEXT,fileName TEXT,fileheader TEXT,filesize INTEGER,fileTitle TEXT,fileNo TEXT,setYear TEXT,userId TEXT,add_time TEXT)");
			db.execSQL("CREATE TABLE IF NOT EXISTS emaillist(_id INTEGER PRIMARY KEY AUTOINCREMENT,subject TEXT,sentDate TEXT,replysign TEXT,hasRead TEXT,containAttachment TEXT, form TEXT, topeople TEXT,ccpeople TEXT,messageid TEXT,bodycontent TEXT,userid TEXT , emailAddress TEXT , appfileName TEXT , emailVisual TEXT)");
			db.execSQL("CREATE TABLE IF NOT EXISTS emailworks(_id INTEGER PRIMARY KEY AUTOINCREMENT,admdivCode TEXT,setyear TEXT,docYear TEXT,divCode TEXT,prjCode TEXT, fileId TEXT, title TEXT,peportDate TEXT,depCode TEXT,depName TEXT,	leader TEXT , reportUser TEXT , fileNo TEXT , leaderName TEXT ,reportType TEXT ,detailsNum TEXT ,attachNum TEXT ,emailAddress TEXT ,userId TEXT)");
			db.execSQL("CREATE TABLE IF NOT EXISTS emaildetails(_id INTEGER PRIMARY KEY AUTOINCREMENT,detailId TEXT,setyear TEXT,fileId TEXT,elementCode TEXT,elementName TEXT,procContent TEXT,emailAddress TEXT ,userId TEXT)");
			db.execSQL("CREATE TABLE IF NOT EXISTS emailattachs(_id INTEGER PRIMARY KEY AUTOINCREMENT,setyear TEXT,fileId TEXT,fileType TEXT,attachId TEXT,fileBin TEXT,fileName TEXT,emailAddress TEXT ,userId TEXT)");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			db.execSQL("DROP TABLE IF EXISTS collection");
			db.execSQL("DROP TABLE IF EXISTS record");
			db.execSQL("DROP TABLE IF EXISTS emaillist");
		}
		onCreate(db);
	}
}

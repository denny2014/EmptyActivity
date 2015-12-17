package com.zet.model;

import com.bdkj.bdlibrary.utils.IMap;

import java.io.Serializable;

/**
 * Created by macchen on 15/4/10.
 */
public class DocInfo implements Serializable {
    private int id;
    private String docYear;
    private String fileHead;
    private String fileId;
    private String fileName;
    private String fileNo;
    private int fileSize;
    private String fileTitle;
    private String setYear;
    private String title;
    private String publish_date="";

    /**
     * 是否看过
     */
    private boolean isLook;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocYear() {
        return docYear;
    }

    public void setDocYear(String docYear) {
        this.docYear = docYear;
    }

    public String getFileHead() {
        return fileHead;
    }

    public void setFileHead(String fileHead) {
        this.fileHead = fileHead;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getSetYear() {
        return setYear;
    }

    public void setSetYear(String setYear) {
        this.setYear = setYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLook() {
        return isLook;
    }

    public void setIsLook(boolean isLook) {
        this.isLook = isLook;
    }

    public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public void build(IMap iMap) {
        this.docYear = iMap.getString("DOC_YEAR");
        this.fileHead = iMap.getString("FILE_HEAD");
        this.fileId = iMap.getString("FILE_ID");
        this.fileName = iMap.getString("FILE_NAME");
        this.fileNo = iMap.getString("FILE_NO");
        this.fileSize = iMap.getInt("FILE_SIZE");
        this.fileTitle = iMap.getString("FILE_TITLE");
        this.setYear = iMap.getString("SET_YEAR");
        this.title = iMap.getString("TITLE");
        this.publish_date = iMap.getString("PUBLISH_DATE");
        this.isLook = false;
    }
}

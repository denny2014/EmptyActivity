package com.zet.model;

import com.bdkj.bdlibrary.utils.IMap;

/**
 * 文件信息
 * Created by macchen on 15/4/11.
 */
public class FileInfo {
    private String filename;
    private String title;
    private String fileHead;
    private String fileId;
    private long filesize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public void build(IMap iMap) {
        filename = iMap.getString("FILE_NAME");
        title = iMap.getString("TITLE");
        fileHead = iMap.getString("FILE_HEAD");
        fileId = iMap.getString("FILE_ID");
        filesize = iMap.getInt("FILE_SIZE");
    }
}

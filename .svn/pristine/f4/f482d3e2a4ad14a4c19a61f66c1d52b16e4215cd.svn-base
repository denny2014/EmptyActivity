package com.zet.model;

import com.bdkj.bdlibrary.utils.IMap;

/**
 * 提要信息
 * Created by macchen on 15/4/11.
 */
public class TiYaoInfo {

    private String code;
    private String name;
    private String content;
    private boolean isWarp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isWarp() {
        return isWarp;
    }

    public void setIsWarp(boolean isWarp) {
        this.isWarp = isWarp;
    }

    public void build(IMap iMap) {
        code = iMap.getString("ELEMENT_CODE");
        name = iMap.getString("ELEMENT_NAME");
        content = iMap.getString("PROC_CONTENT");
        content = content.replaceAll("<br>", "\n");
    }
}

package com.zet.model;

import com.bdkj.bdlibrary.utils.IMap;

/**
 * Created by macchen on 15/4/10.
 */
public class DepartInfo {

    private String code;
    private String name;

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

    public void build(IMap iMap) {
        code = iMap.getString("DEP_CODE");
        name = iMap.getString("DEP_NAME");
    }
}

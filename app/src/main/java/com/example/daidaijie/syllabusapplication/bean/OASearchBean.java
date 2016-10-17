package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/10/17.
 */

public class OASearchBean {

    private int subcompanyId;

    private String keyword;

    public OASearchBean() {
        keyword = "";
        subcompanyId = 0;
    }

    public int getSubcompanyId() {
        return subcompanyId;
    }

    public void setSubcompanyId(int subcompanyId) {
        this.subcompanyId = subcompanyId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

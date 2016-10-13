package com.example.daidaijie.syllabusapplication.bean;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/8/7.
 */
public class Banner extends RealmObject {
    private int id;
    private String link;
    private String description;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

package com.example.daidaijie.syllabusapplication.bean;

import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/9/17.
 */
public class CollectionBean  {

    /**
     * collection_id : 867658
     * season : 1
     * start_year : 2016
     */

    @PrimaryKey
    private String collection_id;

    private int season;
    private int start_year;


    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getStart_year() {
        return start_year;
    }

    public void setStart_year(int start_year) {
        this.start_year = start_year;
    }
}
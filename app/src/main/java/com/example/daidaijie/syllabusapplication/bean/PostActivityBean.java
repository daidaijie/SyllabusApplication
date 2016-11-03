package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/8/15.
 */
public class PostActivityBean {

    public String source;

    //url
    public String content;

    public int uid;

    public String token;

    public int post_type;

    public long activity_start_time;

    public long activity_end_time;

    public String activity_location;

    public String description;

    public String photo_list_json;

    public PostActivityBean() {
        post_type = 2;
        activity_location = "未指定";
    }
}

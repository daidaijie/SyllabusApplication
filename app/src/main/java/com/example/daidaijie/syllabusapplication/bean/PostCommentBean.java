package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/8/18.
 */
public class PostCommentBean {

    private String comment;
    private String token;
    private int post_id;
    private int uid;

    public PostCommentBean(int post_id, int uid, String comment, String token) {
        this.post_id = post_id;
        this.uid = uid;
        this.comment = comment;
        this.token = token;
    }
}

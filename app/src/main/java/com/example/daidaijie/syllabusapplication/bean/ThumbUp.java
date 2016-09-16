package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/9/16.
 * 用于发送点赞请求
 */
public class ThumbUp {
    private int post_id;
    private int uid;
    private String token;

    public ThumbUp() {
    }

    public ThumbUp(int post_id, int uid, String token) {
        this.post_id = post_id;
        this.uid = uid;
        this.token = token;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

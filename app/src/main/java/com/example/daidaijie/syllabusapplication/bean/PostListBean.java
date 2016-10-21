package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class PostListBean implements Serializable {

    private String content;

    public boolean isMyLove = false;

    /**
     * account : 15jhzhao3
     * nickname : HoraceChiu
     * image : http://file.bmob.cn/M03/EF/4D/oYYBAFebXOKAFp0WAACB0FUPsuY523.jpg
     * id : 2184
     */

    private PostUserBean user;
    private String description;
    private String source;
    private String photo_list_json;
    private int post_type;
    private int id;
    private String post_time;
    /**
     * uid : 1
     * id : 329
     */

    private List<CommentsBean> comments;
    /**
     * uid : 2
     * id : 1158
     */

    private List<ThumbUpsBean> thumb_ups;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostUserBean getUser() {
        return user;
    }

    public void setUser(PostUserBean user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPhoto_list_json() {
        return photo_list_json;
    }

    public void setPhoto_list_json(String photo_list_json) {
        this.photo_list_json = photo_list_json;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public List<ThumbUpsBean> getThumb_ups() {
        return thumb_ups;
    }

    public void setThumb_ups(List<ThumbUpsBean> thumb_ups) {
        this.thumb_ups = thumb_ups;
    }

}

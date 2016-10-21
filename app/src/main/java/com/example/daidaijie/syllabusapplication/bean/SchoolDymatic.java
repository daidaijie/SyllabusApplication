package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

/**
 * Created by daidaijie on 2016/9/14.
 */
public class SchoolDymatic {


    /**
     * activity_end_time : null
     * activity_location : 未指定
     * activity_start_time : 2016-09-13 17:55:15
     * post_type : 2
     * description : App里程碑！想不到开学第一天有5万的启动次数😊！昨天的最高历史记录也没破2万～[跳跳]
     * thumb_ups : [{"uid":2,"id":1469},{"uid":116,"id":1475},{"uid":2721,"id":1481}]
     * id : 900
     * photo_list_json : {
     * "photo_list" : [
     * {
     * "size_big" : "http:\/\/bmob-cdn-5361.b0.upaiyun.com\/2016\/09\/13\/00dd57e8f4d94541809d820558644a54.jpg",
     * "size_small" : "http:\/\/bmob-cdn-5361.b0.upaiyun.com\/2016\/09\/13\/00dd57e8f4d94541809d820558644a54.jpg"
     * }
     * ]
     * }
     * post_time : 2016-09-13 17:55:15
     * content :
     * source : 汕大课程表团队
     * user : {"image":"http://bmob-cdn-5361.b0.upaiyun.com/2016/09/13/1cee50d2285e4f41802335d179080d85.jpg","nickname":"铲晒菲律宾的香蕉🐔🌶","id":2,"account":"14jhwang"}
     * comments : [{"uid":1137,"id":716}]
     */

    private String activity_end_time;
    private String activity_location;
    private String activity_start_time;
    private int post_type;
    private String description;
    private int id;
    private String photo_list_json;
    private String post_time;
    private String content;
    private String source;

    public boolean isMyLove = false;


    /**
     * image : http://bmob-cdn-5361.b0.upaiyun.com/2016/09/13/1cee50d2285e4f41802335d179080d85.jpg
     * nickname : 铲晒菲律宾的香蕉🐔🌶
     * id : 2
     * account : 14jhwang
     */

    private PostUserBean user;
    /**
     * uid : 2
     * id : 1469
     */

    private List<ThumbUpsBean> thumb_ups;
    /**
     * uid : 1137
     * id : 716
     */

    private List<CommentsBean> comments;

    public String getActivity_end_time() {
        return activity_end_time;
    }

    public void setActivity_end_time(String activity_end_time) {
        this.activity_end_time = activity_end_time;
    }

    public String getActivity_location() {
        return activity_location;
    }

    public void setActivity_location(String activity_location) {
        this.activity_location = activity_location;
    }

    public String getActivity_start_time() {
        return activity_start_time;
    }

    public void setActivity_start_time(String activity_start_time) {
        this.activity_start_time = activity_start_time;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto_list_json() {
        return photo_list_json;
    }

    public void setPhoto_list_json(String photo_list_json) {
        this.photo_list_json = photo_list_json;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public PostUserBean getUser() {
        return user;
    }

    public void setUser(PostUserBean user) {
        this.user = user;
    }

    public List<ThumbUpsBean> getThumb_ups() {
        return thumb_ups;
    }

    public void setThumb_ups(List<ThumbUpsBean> thumb_ups) {
        this.thumb_ups = thumb_ups;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

}

package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserBody {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("uid")
    @Expose
    public Integer uid;
//    @SerializedName("birthday")
//    @Expose
//    public Integer birthday;
    @SerializedName("nickname")
    @Expose
    public String nickname;
    @SerializedName("gender")
    @Expose
    public Integer gender;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("image")
    @Expose
    public String image;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateUserBody() {
    }

    /**
     *
     * @param uid
     * @param id
     * @param token
     * @param nickname
     * @param gender
     * @param profile
     */
    public UpdateUserBody(Integer id, Integer uid, String nickname, Integer gender, String profile, String token, String image) {
        this.id = id;
        this.uid = uid;
//        this.birthday = birthday;
        this.nickname = nickname;
        this.gender = gender;
        this.profile = profile;
        this.token = token;
        this.image = image;
    }

}
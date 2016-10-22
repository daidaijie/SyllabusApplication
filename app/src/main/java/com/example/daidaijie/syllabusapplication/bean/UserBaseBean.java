package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/8/16.
 */
public class UserBaseBean extends RealmObject {

    /**
     * level : 2
     * account : 13yjli3
     * image : http://file.bmob.cn/M02/AE/32/oYYBAFdnwZiAe4EBAABaXRzBuAk291.jpg
     * birthday : null
     * profile : 哈哈哈哈哈
     * gender : 0
     * id : 9
     * nickname : 余强
     */

    @PrimaryKey
    private String account;

    private int level;
    private String image;
    private String birthday;
    private String profile;
    private int gender;
    private int id;
    private String nickname;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImage() {
        return image;
    }

    public String getPhoto(){
        if (image == null || image.isEmpty()) {
            return "res://" + App.getContext().getPackageName()
                    + "/" + R.drawable.ic_syllabus_icon;
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

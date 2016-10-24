package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/9/14.
 */
public class PostUserBean implements Serializable {
    private String account;
    private String nickname;
    private String image;
    private int id;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        if (nickname == null || nickname.isEmpty()) {
            return account;
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        if (image == null || image.isEmpty()) {
            return "res://" + App.getContext().getPackageName()
                    + "/" + R.drawable.ic_syllabus_icon;
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


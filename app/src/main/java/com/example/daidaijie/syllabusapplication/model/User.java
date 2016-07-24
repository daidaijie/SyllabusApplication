package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class User {

    private static User sUser;

    private UserInfo mUserInfo;

    private User() {
    }

    public static User getInstance() {

        if (sUser == null) {
            sUser = new User();
        }
        return sUser;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public static void saveUser() {

    }
}

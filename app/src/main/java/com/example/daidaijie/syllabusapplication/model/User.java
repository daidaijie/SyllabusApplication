package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

import java.util.Map;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class User {

    //example 13yjli3
    private String mAccount;

    private static User sUser;

    private UserInfo mUserInfo;

    public Map<String, Syllabus> mSyllabusMap;

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

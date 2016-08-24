package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class User {

    //example 13yjli3
    public String mAccount;

    //example *********
    public String mPassword;

    private static User sUser = new User();

    private UserInfo mUserInfo;

    private UserBaseBean mUserBaseBean;

//    public Map<String, Syllabus> mSyllabusMap;

    //现在只测试一个学期的情况
    public Syllabus mSyllabus;

    private User() {
    }

    public static User getInstance() {
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

    public UserBaseBean getUserBaseBean() {
        return mUserBaseBean;
    }

    public void setUserBaseBean(UserBaseBean userBaseBean) {
        mUserBaseBean = userBaseBean;
    }
}

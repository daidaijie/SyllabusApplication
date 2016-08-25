package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class User {

    private static final String FILE_NAME = "User";
    private static final String EXTRA_USERNAME
            = "com.example.daidaijie.syllabusapplication.model.User.username";
    private static final String EXTRA_PASSWORD
            = "com.example.daidaijie.syllabusapplication.model.User.password";


    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    //example 13yjli3
    private String mAccount;

    //example *********
    private String mPassword;

    private static User sUser = new User();

    private UserInfo mUserInfo;

    private UserBaseBean mUserBaseBean;

//    public Map<String, Syllabus> mSyllabusMap;

    //现在只测试一个学期的情况
    public Syllabus mSyllabus;

    private User() {
        mSharedPreferences = App.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mAccount = mSharedPreferences.getString(EXTRA_USERNAME, "");
        mPassword = mSharedPreferences.getString(EXTRA_PASSWORD, "");

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

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mEditor.putString(EXTRA_USERNAME, account);
        mEditor.commit();
        mAccount = account;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mEditor.putString(EXTRA_PASSWORD, password);
        mPassword = password;
        mEditor.commit();
    }
}

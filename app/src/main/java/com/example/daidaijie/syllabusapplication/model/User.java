package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;

import java.util.Map;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class User {

    private static final String FILE_NAME = "User";

    public static final String EXTRA_CURRENT_ACCOUNT
            = "com.example.daidaijie.syllabusapplication.model.User.currentAccount";

    private static final String EXTRA_USERNAME
            = "com.example.daidaijie.syllabusapplication.model.User.username";
    private static final String EXTRA_PASSWORD
            = "com.example.daidaijie.syllabusapplication.model.User.password";

    private static final String EXTRA_USER_INFO
            = "com.example.daidaijie.syllabusapplication.model.User.mUserInfo";

    private static final String EXTRA_USER_BASE_BEAN
            = "com.example.daidaijie.syllabusapplication.model.User.mUserBaseBean";

    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    private String mCurrentAccount;

    //example 13yjli3
    private String mAccount;

    //example *********
    private String mPassword;

    //某个学期
    private String nowSemester;

    private static User sUser = new User();

    private UserInfo mUserInfo;

    private UserBaseBean mUserBaseBean;

    public Map<Semester, Syllabus> mSyllabusMap;


    //现在只测试一个学期的情况
    public Syllabus mSyllabus;

    private User() {
        mSharedPreferences = App.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mCurrentAccount = mSharedPreferences.getString(EXTRA_CURRENT_ACCOUNT,"");

        mAccount = mSharedPreferences.getString(EXTRA_USERNAME, "");
        mPassword = mSharedPreferences.getString(EXTRA_PASSWORD, "");

        String userInfoJsonString = mSharedPreferences.getString(EXTRA_USER_INFO, "");
        if (userInfoJsonString.isEmpty()) {
            mUserInfo = null;
        } else {
            mUserInfo = GsonUtil.getDefault().fromJson(userInfoJsonString, UserInfo.class);
        }

        String userBaseBeanJsonString = mSharedPreferences.getString(EXTRA_USER_BASE_BEAN, "");
        if (userBaseBeanJsonString.isEmpty()) {
            mUserBaseBean = null;
        } else {
            mUserBaseBean = GsonUtil.getDefault().fromJson(userBaseBeanJsonString, UserBaseBean.class);
        }

    }

    public static User getInstance() {
        return sUser;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        Log.e("LoginPresenter", "setUserInfo: " + userInfo.getUser_id());
        Log.e("LoginPresenter", "setUserInfo: " + (userInfo.getToken() == null));
        mEditor.putString(EXTRA_USER_INFO, GsonUtil.getDefault().toJson(userInfo));
        mEditor.commit();
        mUserInfo = userInfo;
    }

    public static void saveUser() {
    }

    public UserBaseBean getUserBaseBean() {
        return mUserBaseBean;
    }

    public void setUserBaseBean(UserBaseBean userBaseBean) {
        mEditor.putString(EXTRA_USER_BASE_BEAN, GsonUtil.getDefault().toJson(userBaseBean));
        mEditor.commit();
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

    public String getCurrentAccount() {
        return mCurrentAccount;
    }

    public void setCurrentAccount(String currentAccount) {
        mCurrentAccount = currentAccount;
    }
}

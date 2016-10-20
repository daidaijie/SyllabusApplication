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
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class User {

    private static final String FILE_NAME = "UserManager";

    public static final String TAG = "UserManager";

    public static final String EXTRA_CURRENT_ACCOUNT
            = "com.example.daidaijie.syllabusapplication.model.UserManager.currentAccount";

    private static final String EXTRA_USERNAME
            = "com.example.daidaijie.syllabusapplication.model.UserManager.username";
    private static final String EXTRA_PASSWORD
            = "com.example.daidaijie.syllabusapplication.model.UserManager.password";

    private static final String EXTRA_USER_INFO
            = "com.example.daidaijie.syllabusapplication.model.UserManager.mUserInfo";

    private static final String EXTRA_USER_BASE_BEAN
            = "com.example.daidaijie.syllabusapplication.model.UserManager.mUserBaseBean";

    private static final String EXTRA_SYLLABUSMAP
            = "com.example.daidaijie.syllabusapplication.model.UserManager.mSyllabusMap";

    private static final String EXTRA_CURRENT_SEMESTER
            = "com.example.daidaijie.syllabusapplication.model.UserManager.mCurrentSemester";

    private static final String EXTRA_WALL_PAPAER
            = "com.example.daidaijie.syllabusapplication.model.UserManager.mWallPaperFileName";


    SharedPreferences mUserSharedPreferences;
    SharedPreferences.Editor mUserEditor;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    private String mCurrentAccount;

    private String mWallPaperFileName;

    //example 13yjli3
    private String mAccount;

    //example *********
    private String mPassword;

    private static User sUser = new User();

    private UserInfo mUserInfo;

    private UserBaseBean mUserBaseBean;

    private Map<Semester, Syllabus> mSyllabusMap;

    private Semester mCurrentSemester;


    private User() {
        //从Account文件里面获取当前用户名
        mUserSharedPreferences = App.getContext().getSharedPreferences("Account", Context.MODE_PRIVATE);
        mUserEditor = mUserSharedPreferences.edit();
        mCurrentAccount = mUserSharedPreferences.getString(EXTRA_CURRENT_ACCOUNT, "");

        mWallPaperFileName = mUserSharedPreferences.getString(EXTRA_WALL_PAPAER, "");

        String currentSemesterString = mUserSharedPreferences.getString(EXTRA_CURRENT_SEMESTER, "");
        if (currentSemesterString.trim().isEmpty()) {
            mCurrentSemester = null;
        } else {
            mCurrentSemester = GsonUtil.getDefault().fromJson(currentSemesterString, Semester.class);
        }


        if (mCurrentAccount.trim().isEmpty()) {
        } else {
            setCurrentAccountStore();
        }
    }

    public static User getInstance() {
        return sUser;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mEditor.putString(EXTRA_USER_INFO, GsonUtil.getDefault().toJson(userInfo));
        mEditor.commit();
        mUserInfo = userInfo;
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
        if (mAccount == null) {
            return "";
        }
        return mAccount;
    }

    public void setAccount(String account) {
        mEditor.putString(EXTRA_USERNAME, account);
        mEditor.commit();
        mAccount = account;
    }

    public String getPassword() {
        if (mPassword == null) {
            return "";
        }
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
        mUserEditor.putString(EXTRA_CURRENT_ACCOUNT, currentAccount);
        mUserEditor.commit();
        setCurrentAccountStore();
    }

    private void setCurrentAccountStore() {
        //再打开当前用户对应文件夹的信息
        mSharedPreferences = App.getContext().getSharedPreferences(mCurrentAccount + FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

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

        String syllabusMapJsonString = mSharedPreferences.getString(EXTRA_SYLLABUSMAP, "");
        if (syllabusMapJsonString.trim().isEmpty()) {
            mSyllabusMap = new HashMap<>();
        } else {
            mSyllabusMap = GsonUtil.getDefault().fromJson(syllabusMapJsonString,
                    new TypeToken<Map<Semester, Syllabus>>() {
                    }.getType());
        }
        RealmConfiguration configuration = new RealmConfiguration.Builder(App.getContext())
                .name(mAccount + ".realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(configuration);

    }


    public Syllabus getSyllabus(Semester semester) {
        return mSyllabusMap.get(semester);
    }

    public void setSyllabus(Semester semester, Syllabus syllabus) {
        mSyllabusMap.put(semester, syllabus);
        mEditor.putString(EXTRA_SYLLABUSMAP, GsonUtil.getDefault().toJson(mSyllabusMap));
        mEditor.commit();
    }

    public void saveSyllabus() {
        mEditor.putString(EXTRA_SYLLABUSMAP, GsonUtil.getDefault().toJson(mSyllabusMap));
        mEditor.commit();
    }

    public Semester getCurrentSemester() {
        return mCurrentSemester;
    }

    public void setCurrentSemester(Semester currentSemester) {
        mCurrentSemester = currentSemester;
        if (mSyllabusMap != null) {
            for (Semester semester : mSyllabusMap.keySet()) {
                if (semester.equals(mCurrentSemester)) {
                    mCurrentSemester = semester;
                }
            }

        }
        mUserEditor.putString(EXTRA_CURRENT_SEMESTER, GsonUtil.getDefault().toJson(mCurrentSemester));
        mUserEditor.commit();
    }

    public String getWallPaperFileName() {
        return mWallPaperFileName;
    }

    public void setWallPaperFileName(String wallPaperFileName) {
        mUserEditor.putString(EXTRA_WALL_PAPAER, wallPaperFileName);
        mUserEditor.commit();
        mWallPaperFileName = wallPaperFileName;
    }
}

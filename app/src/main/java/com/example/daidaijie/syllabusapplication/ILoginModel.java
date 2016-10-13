package com.example.daidaijie.syllabusapplication;

import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface ILoginModel {

    void setUserLogin(UserLogin userLogin);

    UserLogin getUserLogin();

    Observable<UserLogin> getUserLoginFromMemory();

    Observable<UserLogin> getUserLoginFromDisk();

    Observable<UserLogin> getUserLoginFromCache();

    void saveUserLoginToDisk();

    Semester getCurrentSemester();
}

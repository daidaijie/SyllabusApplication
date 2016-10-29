package com.example.daidaijie.syllabusapplication.user;

import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/12.
 */

public interface IUserModel {

    Observable<UserBaseBean> getUserBaseBeanFromMemory();

    Observable<UserBaseBean> getUserBaseBeanFromDisk();

    Observable<UserBaseBean> getUserBaseBeanFromNet();

    Observable<UserBaseBean> getUserBaseBeanFromCache();

    UserBaseBean getUserBaseBeanNormal();

    Observable<UserInfo> getUserInfoFromMemory();

    Observable<UserInfo> getUserInfoFromDisk();

    Observable<UserInfo> getUserInfoFromNet();

    Observable<UserInfo> getUserInfo();

    UserInfo getUserInfoNormal();

    void updateUserInfo(UserInfo userInfo);

    void updateUserBaseBean(UserBaseBean userBaseBean);
}

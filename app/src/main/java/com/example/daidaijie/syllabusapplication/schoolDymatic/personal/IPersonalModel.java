package com.example.daidaijie.syllabusapplication.schoolDymatic.personal;

import android.support.annotation.Nullable;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/22.
 */

public interface IPersonalModel {

    interface OnPostPhotoCallBack {

        void onSuccess(String photoJson);

        void onFail(String msg);
    }

    Observable<Void> updateUserInfo(String nickName, String profile, @Nullable String img);

    void postPhotoToBmob(@Nullable String headImage, OnPostPhotoCallBack onPostPhotoCallBack);
}

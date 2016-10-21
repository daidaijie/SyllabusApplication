package com.example.daidaijie.syllabusapplication.base;

/**
 * Created by daidaijie on 2016/10/21.
 */

public interface IBaseModel {

    interface OnGetSuccessCallBack<T> {
        void onGetSuccess(T t);
    }

    interface OnGetFailCallBack {
        void onGetFail();
    }
}

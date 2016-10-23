package com.example.daidaijie.syllabusapplication.other.update;

import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/23.
 */

public interface IUpdateModel {

    Observable<UpdateInfoBean> getUpdateInfo();
}

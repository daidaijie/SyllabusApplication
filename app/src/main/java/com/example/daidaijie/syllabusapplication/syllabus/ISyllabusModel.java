package com.example.daidaijie.syllabusapplication.syllabus;

import android.support.annotation.Nullable;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/19.
 */

public interface ISyllabusModel extends IBaseModel {

    Observable<Syllabus> getSyllabusFromMemory();

    Observable<Syllabus> getSyllabusFromDisk();

    Observable<Syllabus> getSyllabusFromNet();

    Observable<Syllabus> getSyllabusFromCache();

    void getSyllabusNormal(OnGetSuccessCallBack<Syllabus> onGetSuccessCallBack,
                           @Nullable OnGetFailCallBack onGetFailCallBack);

}

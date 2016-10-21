package com.example.daidaijie.syllabusapplication.schoolDynamatic.dymatic;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/21.
 */

public interface ISchoolDymaticModel {

    Observable<List<SchoolDymatic>> getSchoolDynamicListFromNet();

    Observable<List<SchoolDymatic>> loadSchoolDynamicListFromNet();

    void getDymaticByPosition(int position, IBaseModel.OnGetSuccessCallBack<SchoolDymatic> onGetSuccessCallBack);

    Observable<ThumbUpReturn> like(int position);

    Observable<Void> unlike(int position);
}

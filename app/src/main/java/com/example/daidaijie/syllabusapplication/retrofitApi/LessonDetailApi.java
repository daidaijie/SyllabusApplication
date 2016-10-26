package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.LessonDetailInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daidaijie on 2016/7/30.
 */
public interface LessonDetailApi {

    @GET("credit/api/v2.1/member")
    Observable<HttpResult<LessonDetailInfo>> getLessonDetail(@Query("class_id") long lessonID);
}

package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.ExamInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/3.
 */
public interface ExamInfoService {

    @FormUrlEncoded
    @POST("credit/api/v2/exam")
    Observable<ExamInfo> getExamInfo(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("years") String years,
                                     @Field("semester") String semester);
}

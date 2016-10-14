package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.ExamInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/3.
 */
public interface ExamInfoApi {

    @FormUrlEncoded
    @POST("credit/api/v2.1/exam")
    Observable<HttpResult<ExamInfo>> getExamInfo(@Field("username") String username,
                                                 @Field("password") String password,
                                                 @Field("years") String years,
                                                 @Field("semester") String semester);
}

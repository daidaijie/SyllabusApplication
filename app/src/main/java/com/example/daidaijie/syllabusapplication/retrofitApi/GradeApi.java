package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.GradeInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/19.
 */
public interface GradeApi {

    @FormUrlEncoded
    @POST("credit/api/v2.1/grade")
    Observable<HttpResult<GradeInfo>> getGrade(@Field("username") String username,
                                               @Field("password") String password);
}

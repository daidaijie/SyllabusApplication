package com.example.daidaijie.syllabusapplication;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/7/18.
 */
public interface UserInfoService {
    @FormUrlEncoded
    @POST("/credit/api/v2/syllabus")
    Observable<UserInfo> getUserInfo(
            @Field("username") String username,
            @Field("password") String password,
            @Field("submit") String submit,
            @Field("years") String years,
            @Field("semester") String semester
    );
}

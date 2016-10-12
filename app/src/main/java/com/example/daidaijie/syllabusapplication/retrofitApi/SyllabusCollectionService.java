package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.CollectionId;
import com.example.daidaijie.syllabusapplication.bean.CollectionInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/17.
 */
public interface SyllabusCollectionService {

    /**
     * 获取收集的课表列表
     *
     * @param username
     * @param token
     * @return
     */
    @GET("interaction/api/v2.1/collector")
    Observable<HttpResult<CollectionInfo>> getCollectionInfo(@Header("username") String username,
                                                             @Header("token") String token);


    @FormUrlEncoded
    @POST("interaction/api/v2.1/collector")
    Observable<HttpResult<CollectionId>> addCollection(@Field("username") String username,
                                                       @Field("token") String token,
                                                       @Field("start_year") int start_year,
                                                       @Field("season") int season);

    @FormUrlEncoded
    @POST("interaction/api/v2.1/syllabus_collection")
    Observable<HttpResult<CollectionId>> sendSyllabus(@Field("username") String username,
                                                      @Field("token") String token,
                                                      @Field("start_year") int start_year,
                                                      @Field("season") int season,
                                                      @Field("syllabus") int syllabus);
}

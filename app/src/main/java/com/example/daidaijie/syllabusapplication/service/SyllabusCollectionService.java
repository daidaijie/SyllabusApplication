package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.CollectionInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/17.
 */
public interface SyllabusCollectionService {

    @GET("interaction/api/v2.1/collector")
    Observable<HttpResult<CollectionInfo>> getCollectionInfo(@Header("username") String username,
                                                             @Header("token") String token);
}

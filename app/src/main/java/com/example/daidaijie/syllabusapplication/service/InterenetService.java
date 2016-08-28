package com.example.daidaijie.syllabusapplication.service;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/28.
 */
public interface InterenetService {

    @POST("userflux")
    Observable<String> getInternetInfo();

}

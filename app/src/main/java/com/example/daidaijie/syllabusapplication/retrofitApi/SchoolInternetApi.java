package com.example.daidaijie.syllabusapplication.retrofitApi;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/28.
 */
public interface SchoolInternetApi {

    @POST("userflux")
    Observable<String> getInternetInfo();

}

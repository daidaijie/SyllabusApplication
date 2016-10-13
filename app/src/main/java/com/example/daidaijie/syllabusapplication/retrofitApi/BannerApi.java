package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.BannerInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/7.
 */
public interface BannerApi {

    @GET("/interaction/api/v2.1/banner")
    Observable<HttpResult<BannerInfo>> getBanner();
}

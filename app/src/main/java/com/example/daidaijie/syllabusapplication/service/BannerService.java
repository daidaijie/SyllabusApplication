package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.BannerInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/7.
 */
public interface BannerService {

    @GET("/interaction/api/v2/banner")
    Observable<BannerInfo> getBanner();
}

package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.ThumbUp;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by smallfly on 16-3-28.
 * 用于点赞的Api
 */
public interface ThumbUpService {

    @POST("/interaction/api/v2/like")
    Observable<HttpResult<ThumbUpReturn>> like_this(@Body ThumbUp thumbUp);
}

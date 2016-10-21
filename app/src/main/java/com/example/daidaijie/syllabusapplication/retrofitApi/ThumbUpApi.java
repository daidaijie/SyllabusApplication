package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.ThumbUp;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by smallfly on 16-3-28.
 * 用于点赞的Api
 */
public interface ThumbUpApi {

    @POST("/interaction/api/v2.1/like")
    Observable<HttpResult<ThumbUpReturn>> like(@Body ThumbUp thumbUp);

    @DELETE("/interaction/api/v2.1/like")
    Observable<HttpResult<Void>> unlike(@Header("id") int like_id, @Header("uid") int uid, @Header("token") String token);
}

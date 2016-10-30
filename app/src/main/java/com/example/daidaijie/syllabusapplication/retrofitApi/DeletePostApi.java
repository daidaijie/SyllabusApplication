package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;

import retrofit2.http.DELETE;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by smallfly on 16-3-28.
 */
public interface DeletePostApi {
    @DELETE("/interaction/api/v2.1/post")
    Observable<HttpResult<Void>> deletePost(@Header("id") int post_id, @Header("uid") int uid, @Header("token") String token);
}

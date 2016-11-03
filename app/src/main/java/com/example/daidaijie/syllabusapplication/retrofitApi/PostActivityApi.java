package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PostActivityBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by smallfly on 16-3-27.
 */
public interface PostActivityApi {

    @POST("/interaction/api/v2.1/activity")
    Observable<HttpResult<Void>> post(@Body PostActivityBean postActivityBean);

}

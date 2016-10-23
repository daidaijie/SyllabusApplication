package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by daidaijie on 2016/10/23.
 */

public interface UpdateApi {

    @GET("interaction/api/v2.1/version")
    Observable<HttpResult<UpdateInfoBean>> getUpdateInfo();
}

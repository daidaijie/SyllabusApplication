package com.example.daidaijie.syllabusapplication.retrofitApi;


import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.UpdateUserBody;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by smallfly on 16-3-27.
 * 用于更新用户数据
 */
public interface UpdateUserApi {
    @Headers("Content-type: application/json")
    @PUT("/interaction/api/v2.1/user")
    Observable<HttpResult<Void>> update(@Body UpdateUserBody updateUserBody);
}

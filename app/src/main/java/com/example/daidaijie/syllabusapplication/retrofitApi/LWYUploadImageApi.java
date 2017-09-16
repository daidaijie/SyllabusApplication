package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.QiNiuImageInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by smallfly on 16-3-27.
 * 用于上传文件的restful请求
 */
public interface LWYUploadImageApi {

    @POST("uploadImage")
    Observable<QiNiuImageInfo> upload(@Body RequestBody requestBody);
}

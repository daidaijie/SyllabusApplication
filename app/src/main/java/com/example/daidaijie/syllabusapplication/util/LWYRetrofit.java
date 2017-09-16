package com.example.daidaijie.syllabusapplication.util;

import com.example.daidaijie.syllabusapplication.bean.QiNiuImageInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.LWYUploadImageApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2017/9/16.
 */

public class LWYRetrofit {

    private Retrofit retrofit;

    private LWYUploadImageApi lwyUploadImageApi;

    private static final MediaType mediaType = MediaType.parse("image/*");

    private LWYRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://119.29.21.168:8080/syllabus/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        lwyUploadImageApi = retrofit.create(LWYUploadImageApi.class);
    }

    public static Retrofit getRetrofit() {
        return LWYRetrofitHolder.RETROFIT.retrofit;
    }

    public static LWYRetrofit getInstance() {
        return LWYRetrofitHolder.RETROFIT;
    }

    private static class LWYRetrofitHolder {
        private final static LWYRetrofit RETROFIT = new LWYRetrofit();
    }

    public rx.Observable<QiNiuImageInfo> uploadFile(File file) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                .build();

        return lwyUploadImageApi.upload(body)
                .subscribeOn(Schedulers.io());
    }
}

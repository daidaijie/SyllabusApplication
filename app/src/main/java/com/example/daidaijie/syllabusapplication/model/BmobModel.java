package com.example.daidaijie.syllabusapplication.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by daidaijie on 2016/9/24.
 */
public class BmobModel {

    public Retrofit mRetrofit;

    private static BmobModel ourInstance = new BmobModel();

    public static BmobModel getInstance() {
        return ourInstance;
    }

    private BmobModel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.bmob.cn/1/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

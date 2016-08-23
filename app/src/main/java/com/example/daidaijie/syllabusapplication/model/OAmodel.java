package com.example.daidaijie.syllabusapplication.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class OAmodel {

    public Retrofit mRetrofit;

    private static OAmodel ourInstance = new OAmodel();

    public static OAmodel getInstance() {
        return ourInstance;
    }

    private OAmodel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://wechat.stu.edu.cn/webservice_oa/oa_stu_/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


}

package com.example.daidaijie.syllabusapplication.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by daidaijie on 2016/9/2.
 */
public class InternetModel {

    public Retrofit mRetrofit;


    private static InternetModel ourInstance = new InternetModel();

    public static InternetModel getInstance() {
        return ourInstance;
    }

    private InternetModel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://1.1.1.2/ac_portal/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}

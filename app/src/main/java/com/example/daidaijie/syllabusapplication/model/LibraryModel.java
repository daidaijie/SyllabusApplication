package com.example.daidaijie.syllabusapplication.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by daidaijie on 2016/9/5.
 */
public class LibraryModel {

    public Retrofit mRetrofit;


    private static LibraryModel ourInstance = new LibraryModel();

    public static LibraryModel getInstance() {
        return ourInstance;
    }

    private LibraryModel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://opac.lib.stu.edu.cn:83/opac/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}

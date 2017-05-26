package com.example.daidaijie.syllabusapplication;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.BmobRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.LibraryRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.OARetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class RetrofitModule {

    @SchoolRetrofit
    @Provides
    @Singleton
    public Retrofit provideSchoolRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://class.stuapps.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @BmobRetrofit
    @Provides
    @Singleton
    public Retrofit provideBmobRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.bmob.cn/1/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @OARetrofit
    @Provides
    @Singleton
    public Retrofit provideOARetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://wechat.stu.edu.cn/webservice_oa/oa_stu_/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @LibraryRetrofit
    @Provides
    @Singleton
    public Retrofit provideLibraryRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://opac.lib.stu.edu.cn:83/opac/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }


}

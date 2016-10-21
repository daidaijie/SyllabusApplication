package com.example.daidaijie.syllabusapplication;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.di.qualifier.gson.DefaultGson;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.BmobRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.LibraryRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.OARetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Singleton
@Component(modules = {ApplicationModule.class, RetrofitModule.class, UtilModule.class})
public interface AppComponent {

    Context getApplicationContext();

    ILoginModel getLoginModel();

    @BmobRetrofit
    Retrofit getBombRetrofit();

    @OARetrofit
    Retrofit getOARetrofit();

    @LibraryRetrofit
    Retrofit getLibRetrofit();

    @SchoolRetrofit
    Retrofit getSchoolRetrofit();

    @DefaultRealm
    Realm getDefaultRealm();

    @DefaultGson
    Gson getDefaultGson();

    IConfigModel getConfigModel();
}

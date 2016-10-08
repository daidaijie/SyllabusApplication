package com.example.daidaijie.syllabusapplication;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.qualifier.gson.DefaultGson;
import com.example.daidaijie.syllabusapplication.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.qualifier.retrofitQualifier.BmobRetrofit;
import com.example.daidaijie.syllabusapplication.qualifier.retrofitQualifier.OARetrofit;
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

    @BmobRetrofit
    Retrofit getBombRetrofit();

    @OARetrofit
    Retrofit getOARetrofit();

    @DefaultRealm
    Realm getDefaultRealm();

    @DefaultGson
    Gson getDefaultGson();
}

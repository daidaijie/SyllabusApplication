package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.qualifier.PerModule;
import com.example.daidaijie.syllabusapplication.qualifier.gson.DefaultGson;
import com.example.daidaijie.syllabusapplication.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.qualifier.retrofitQualifier.BmobRetrofit;
import com.example.daidaijie.syllabusapplication.retrofitApi.TakeOutInfoApi;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/9.
 */

@Module
public class TakeOutModelModule {

    @Provides
    @PerModule
    ITakeOutModel provideTakeOutModel(@DefaultRealm Realm realm,
                                      @BmobRetrofit Retrofit retrofit,
                                      @DefaultGson Gson gson) {
        return new TakeOutModel(realm, retrofit.create(TakeOutInfoApi.class), gson);
    }
}

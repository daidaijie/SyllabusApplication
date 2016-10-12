package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.BmobRetrofit;
import com.example.daidaijie.syllabusapplication.retrofitApi.TakeOutInfoApi;

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
                                      @BmobRetrofit Retrofit retrofit) {
        return new TakeOutModel(realm, retrofit.create(TakeOutInfoApi.class));
    }
}

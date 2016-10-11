package com.example.daidaijie.syllabusapplication;

import com.example.daidaijie.syllabusapplication.qualifier.gson.DefaultGson;
import com.example.daidaijie.syllabusapplication.qualifier.realm.DefaultRealm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class UtilModule {

    @Provides
    @Singleton
    @DefaultRealm
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    @DefaultGson
    Gson provideGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
    }
}

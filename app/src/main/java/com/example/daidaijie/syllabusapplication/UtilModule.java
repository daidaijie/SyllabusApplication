package com.example.daidaijie.syllabusapplication;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.qualifier.gson.DefaultGson;
import com.example.daidaijie.syllabusapplication.qualifier.realm.DefaultRealm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class UtilModule {

    @Provides
    @Singleton
    @DefaultRealm
    Realm provideRealm(Context context) {
        RealmConfiguration configuration = new RealmConfiguration
                .Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
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

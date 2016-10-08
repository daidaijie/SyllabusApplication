package com.example.daidaijie.syllabusapplication;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.qualifier.realm.DefaultRealm;

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
public class RealmModule {

    @Provides
    @Singleton
    @DefaultRealm
    Realm provideRealm(Context context) {
        RealmConfiguration configuration = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(configuration);
        return Realm.getDefaultInstance();
    }
}

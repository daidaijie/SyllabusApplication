package com.example.daidaijie.syllabusapplication.user;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.string.Username;
import com.example.daidaijie.syllabusapplication.di.scope.PerUser;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/12.
 */

@Module
public class UserModule {

    private String username;

    public UserModule(String username) {
        this.username = username;
    }

    @Provides
    @PerUser
    @Username
    String provideUsername() {
        return username;
    }

    @Provides
    @UserRealm
    @PerUser
    Realm provideUserRealm(Context context, @Username String username) {
        RealmConfiguration configuration = new RealmConfiguration
                .Builder(context)
                .name(username + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(configuration);
    }

    @Provides
    @PerUser
    IUserModel provideUserModel(@Username String username,
                                @UserRealm Realm realm,
                                @SchoolRetrofit Retrofit retrofit) {
        return new UserModel(username, realm, retrofit);
    }
}

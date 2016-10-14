package com.example.daidaijie.syllabusapplication.user;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
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


    @Provides
    @UserRealm
    @PerUser
    Realm provideUserRealm(Context context, ILoginModel loginModel) {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(context)
                .schemaVersion(App.userVersion)
                .name(loginModel.getUserLogin().getUsername() + ".realm");
        if (App.isDebug) {
            builder.deleteRealmIfMigrationNeeded();
        }

        return Realm.getInstance(builder.build());
    }

    @Provides
    @PerUser
    @LoginUser
    IUserModel provideUserModel(ILoginModel loginModel,
                                @UserRealm Realm realm,
                                @SchoolRetrofit Retrofit retrofit) {
        return new UserModel(loginModel, realm, retrofit);
    }
}

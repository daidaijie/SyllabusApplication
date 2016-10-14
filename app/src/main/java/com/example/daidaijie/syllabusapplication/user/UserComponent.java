package com.example.daidaijie.syllabusapplication.user;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerUser;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/12.
 */

@PerUser
@Component(dependencies = AppComponent.class, modules = UserModule.class)
public abstract class UserComponent {

    private static UserComponent INSTANCE;

    public static UserComponent buildInstance(AppComponent appComponent) {
        INSTANCE = DaggerUserComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule())
                .build();
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }

    public static UserComponent getINSTANCE() {
        return INSTANCE;
    }

    @SchoolRetrofit
    public abstract Retrofit agetSchoolRetrofit();

    @DefaultRealm
    public abstract Realm getDefaultRealm();

    @UserRealm
    public abstract Realm getUserRealm();

    public abstract ILoginModel getLoginModel();

    @LoginUser
    public abstract IUserModel getLoginUserModel();

}

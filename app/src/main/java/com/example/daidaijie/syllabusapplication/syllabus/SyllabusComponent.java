package com.example.daidaijie.syllabusapplication.syllabus;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.IConfigModel;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/19.
 */
@PerModule
@Component(dependencies = UserComponent.class, modules = SyllabusModelModule.class)
public abstract class SyllabusComponent {

    private static SyllabusComponent INSTANCE;

    public static SyllabusComponent newInstance(AppComponent appComponent) {
        if (INSTANCE == null) {
            INSTANCE = DaggerSyllabusComponent.builder()
                    .userComponent(UserComponent.buildInstance(appComponent))
                    .syllabusModelModule(new SyllabusModelModule())
                    .build();
        }
        return INSTANCE;
    }


    public static SyllabusComponent getINSTANCE() {
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }

    public abstract ISyllabusModel getSyllabusModel();

    public abstract ILoginModel getLoginModel();

    public abstract IConfigModel getConfigModel();

    @UserRealm
    public abstract Realm getUserRealm();

    @SchoolRetrofit
    public abstract Retrofit getSchoolRetrofit();
}

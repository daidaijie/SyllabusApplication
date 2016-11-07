package com.example.daidaijie.syllabusapplication.schoolDymatic.circle;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerModule
@Component(dependencies = UserComponent.class, modules = StuCircleModelModule.class)
public abstract class StuCircleModelComponent {

    private static StuCircleModelComponent INSTANCE;

    public static StuCircleModelComponent getInstance(AppComponent appComponent) {
        if (INSTANCE == null) {
            INSTANCE = DaggerStuCircleModelComponent.builder()
                    .userComponent(UserComponent.buildInstance(appComponent))
                    .stuCircleModelModule(new StuCircleModelModule())
                    .build();
        }
        return INSTANCE;
    }

    public static void destory() {
        INSTANCE = null;
    }

    public abstract ISchoolCircleModel getSchoolCircleModel();

    @SchoolRetrofit
    public abstract Retrofit getSchoolRetrofit();

    @LoginUser
    public abstract IUserModel getUserModel();
}

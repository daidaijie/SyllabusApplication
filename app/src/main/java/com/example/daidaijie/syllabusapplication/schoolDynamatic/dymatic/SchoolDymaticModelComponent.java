package com.example.daidaijie.syllabusapplication.schoolDynamatic.dymatic;

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
@Component(dependencies = UserComponent.class, modules = SchoolDymaticModelModule.class)
public abstract class SchoolDymaticModelComponent {

    private static SchoolDymaticModelComponent INSTANCE;

    public static SchoolDymaticModelComponent getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = DaggerSchoolDymaticModelComponent.builder()
                    .userComponent(UserComponent.getINSTANCE())
                    .schoolDymaticModelModule(new SchoolDymaticModelModule())
                    .build();
        }
        return INSTANCE;
    }


    public static void destroy() {
        INSTANCE = null;
    }

    public abstract ISchoolDymaticModel getSchoolDymaticModel();

    @SchoolRetrofit
    public abstract Retrofit getSchoolRetrofit();

    @LoginUser
    public abstract IUserModel getUserModel();
}

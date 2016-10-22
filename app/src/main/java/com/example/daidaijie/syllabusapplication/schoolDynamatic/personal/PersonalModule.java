package com.example.daidaijie.syllabusapplication.schoolDynamatic.personal;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.UpdateUserApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/22.
 */

@Module
public class PersonalModule {

    PersonalContract.view mView;

    public PersonalModule(PersonalContract.view view) {
        mView = view;
    }

    @PerActivity
    @Provides
    PersonalContract.view provideView() {
        return mView;
    }

    @PerActivity
    @Provides
    IPersonalModel providePersonModel(@SchoolRetrofit Retrofit retrofit) {
        return new PersonalModel(retrofit.create(UpdateUserApi.class));
    }
}

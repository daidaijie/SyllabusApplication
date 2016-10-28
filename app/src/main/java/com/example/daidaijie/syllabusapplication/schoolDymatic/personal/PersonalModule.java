package com.example.daidaijie.syllabusapplication.schoolDymatic.personal;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.PushPostApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.UpdateUserApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

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
    IPersonalModel providePersonModel(@SchoolRetrofit Retrofit retrofit,
                                      @LoginUser IUserModel userModel) {
        return new PersonalModel(retrofit.create(UpdateUserApi.class),
                retrofit.create(PushPostApi.class), userModel);
    }
}

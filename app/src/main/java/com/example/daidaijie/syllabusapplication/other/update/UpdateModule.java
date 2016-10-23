package com.example.daidaijie.syllabusapplication.other.update;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.UpdateApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/23.
 */

@Module
public class UpdateModule {

    UpdateContract.view mView;

    public UpdateModule(UpdateContract.view view) {
        mView = view;
    }

    @Provides
    @PerActivity
    UpdateContract.view provideView() {
        return mView;
    }

    @Provides
    @PerActivity
    IUpdateModel provideUpdateModel(@SchoolRetrofit Retrofit retrofit) {
        return new UpdateModel(retrofit.create(UpdateApi.class));
    }
}

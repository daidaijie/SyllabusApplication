package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.retrofitApi.DeletePostApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.SchoolDymaticApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.ThumbUpApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/21.
 */

@Module
public class SchoolDymaticModelModule {

    @PerModule
    @Provides
    ISchoolDymaticModel provideSchoolDymaticModel(@LoginUser IUserModel userModel,
                                                  @SchoolRetrofit Retrofit retrofit) {
        return new SchoolDymaticModel(retrofit.create(SchoolDymaticApi.class), userModel,
                retrofit.create(ThumbUpApi.class), retrofit.create(DeletePostApi.class));
    }
}

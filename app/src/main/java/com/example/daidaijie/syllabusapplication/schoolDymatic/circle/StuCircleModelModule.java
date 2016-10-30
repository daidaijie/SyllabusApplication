package com.example.daidaijie.syllabusapplication.schoolDymatic.circle;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.retrofitApi.CirclesApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.DeletePostApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.ThumbUpApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/21.
 */

@Module
public class StuCircleModelModule {


    @PerModule
    @Provides
    ISchoolCircleModel provideISchoolCircleModel(@LoginUser IUserModel userModel,
                                                 @SchoolRetrofit Retrofit retrofit) {
        return new SchoolCircleModel(retrofit.create(CirclesApi.class), userModel,
                retrofit.create(ThumbUpApi.class), retrofit.create(DeletePostApi.class));
    }
}

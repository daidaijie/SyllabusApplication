package com.example.daidaijie.syllabusapplication.exam;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.retrofitApi.ExamInfoApi;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/14.
 */

@Module
public class ExamModelModule {

    @Provides
    @PerModule
    IExamModel provideExamModel(@SchoolRetrofit Retrofit retrofit,
                                @UserRealm Realm realm,
                                ILoginModel loginModel) {
        return new ExamModel(retrofit.create(ExamInfoApi.class), realm, loginModel);
    }

}

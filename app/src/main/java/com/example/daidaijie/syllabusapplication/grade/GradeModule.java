package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.GradeApi;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class GradeModule {

    private final GradeContract.view mView;


    public GradeModule(GradeContract.view view) {
        mView = view;
    }

    @Provides
    @PerActivity
    GradeContract.view provideView() {
        return mView;
    }


    @Provides
    @PerActivity
    IGradeModel provideGradeModel(ILoginModel loginModel,
                                  @UserRealm Realm realm,
                                  @SchoolRetrofit Retrofit retrofit) {
        return new GradeModel(retrofit.create(GradeApi.class), loginModel, realm);
    }

}

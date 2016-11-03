package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.PostActivityApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/31.
 */

@Module
public class PostDymaticModule {

    PostDymaticContract.view mView;

    public PostDymaticModule(PostDymaticContract.view view) {
        mView = view;
    }

    @PerActivity
    @Provides
    PostDymaticContract.view provideView() {
        return mView;
    }

    @PerActivity
    @Provides
    IPostDymaticModel getIPostDymaticModel(@SchoolRetrofit Retrofit retrofit,
                                           @LoginUser IUserModel userModel) {
        return new PostDymaticModel(retrofit.create(PostActivityApi.class), userModel);
    }

}

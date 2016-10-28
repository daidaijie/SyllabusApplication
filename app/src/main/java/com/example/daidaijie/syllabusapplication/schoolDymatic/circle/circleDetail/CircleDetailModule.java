package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.circleDetail;

import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.CircleCommentsApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/21.
 */

@Module
public class CircleDetailModule {


    private int mPosition;

    private CircleDetailContract.view mView;

    public CircleDetailModule(int position, CircleDetailContract.view view) {
        mPosition = position;
        mView = view;
    }

    @PerActivity
    @Provides
    int providePosition() {
        return mPosition;
    }

    @PerActivity
    @Provides
    CircleDetailContract.view provideView() {
        return mView;
    }

    @PerActivity
    @Provides
    ICommentModel provideCommentModel(@SchoolRetrofit Retrofit retrofit,
                                      @LoginUser IUserModel iUserModel) {
        return new CommentModel(retrofit.create(CircleCommentsApi.class),iUserModel);
    }
}

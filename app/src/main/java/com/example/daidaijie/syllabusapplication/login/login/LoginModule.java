package com.example.daidaijie.syllabusapplication.login.login;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.LoginModel;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.UnLoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.user.UserModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class LoginModule {

    private final LoginContract.view mView;


    public LoginModule(LoginContract.view view) {
        mView = view;
    }

    @Provides
    @PerActivity
    LoginContract.view provideView() {
        return mView;
    }

    @Provides
    @PerActivity
    @UnLoginUser
    IUserModel provideUnLoginUserModel(ILoginModel loginModel,
                                       @SchoolRetrofit Retrofit retrofit) {
        return new UserModel(loginModel, retrofit);
    }


}

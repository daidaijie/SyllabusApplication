package com.example.daidaijie.syllabusapplication.login.splash;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/11/9.
 */


@Module
public class SplashModule {

    private final SplashContract.view mView;


    public SplashModule(SplashContract.view view) {
        mView = view;
    }

    @Provides
    @PerActivity
    SplashContract.view provideView() {
        return mView;
    }

}
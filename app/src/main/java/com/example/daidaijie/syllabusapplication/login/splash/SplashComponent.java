package com.example.daidaijie.syllabusapplication.login.splash;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = SplashModule.class)
public interface SplashComponent {

    void inject(SplashActivity splashActivity);
}

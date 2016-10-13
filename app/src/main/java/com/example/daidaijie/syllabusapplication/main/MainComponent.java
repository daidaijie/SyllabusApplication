package com.example.daidaijie.syllabusapplication.main;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = UserComponent.class, modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}

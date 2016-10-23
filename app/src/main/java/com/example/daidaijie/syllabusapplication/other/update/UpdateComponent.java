package com.example.daidaijie.syllabusapplication.other.update;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/23.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = UpdateModule.class)
public interface UpdateComponent {

    void inject(UpdateActivity updateActivity);
}

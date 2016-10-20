package com.example.daidaijie.syllabusapplication.syllabus.main.activity;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/19.
 */

@PerActivity
@Component(dependencies = UserComponent.class, modules = SyllabusMainModule.class)
public interface SyllabusMainComponent {

    void inject(SyllabusActivity syllabusActivity);
}

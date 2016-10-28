package com.example.daidaijie.syllabusapplication.schoolDymatic.personal;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/22.
 */

@PerActivity
@Component(dependencies = UserComponent.class, modules = PersonalModule.class)
public interface PersonalComponent {

    void inject(PersonalActivity personalActivity);

}

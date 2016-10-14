package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.main.MainModule;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = UserComponent.class, modules = GradeModule.class)
public interface GradeComponent {

    void inject(GradeActivity gradeActivity);
}

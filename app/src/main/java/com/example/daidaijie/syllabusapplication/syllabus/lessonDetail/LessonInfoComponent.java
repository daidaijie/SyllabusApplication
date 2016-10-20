package com.example.daidaijie.syllabusapplication.syllabus.lessonDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/20.
 */
@PerActivity
@Component(dependencies = SyllabusComponent.class, modules = LessonInfoModule.class)
public interface LessonInfoComponent {

    void inject(LessonInfoActivity lessonInfoActivity);
}

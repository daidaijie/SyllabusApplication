package com.example.daidaijie.syllabusapplication.exam.detail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.exam.ExamModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/14.
 */

@PerActivity
@Component(dependencies = ExamModelComponent.class, modules = ExamDetailModule.class)
public interface ExamDetailComponent {

    void inject(ExamDetailActivity examDetailActivity);
}

package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/22.
 */

@PerActivity
@Component(dependencies = SyllabusComponent.class, modules = ClassmateModule.class)
public interface ClassmateComponent {

    void inject(ClassmateListActivity classmateListActivity);
}

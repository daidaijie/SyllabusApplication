package com.example.daidaijie.syllabusapplication.syllabus.main.fragment;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/19.
 */

@PerFragment
@Component(dependencies = SyllabusComponent.class, modules = SyllabusFragmentModule.class)
public interface SyllabusFragmentComponent {

    void inject(SyllabusFragment syllabusFragment);
}

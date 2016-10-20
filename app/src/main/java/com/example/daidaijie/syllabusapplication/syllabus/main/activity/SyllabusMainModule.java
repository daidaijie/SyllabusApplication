package com.example.daidaijie.syllabusapplication.syllabus.main.activity;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/19.
 */

@Module
public class SyllabusMainModule {

    private SyllabusContract.view mView;

    public SyllabusMainModule(SyllabusContract.view view) {
        mView = view;
    }

    @PerActivity
    @Provides
    SyllabusContract.view provideView() {
        return mView;
    }

}

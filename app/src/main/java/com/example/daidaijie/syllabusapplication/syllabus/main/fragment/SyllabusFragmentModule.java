package com.example.daidaijie.syllabusapplication.syllabus.main.fragment;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/19.
 */

@Module
public class SyllabusFragmentModule {

    private SyllabusFragmentContract.view mView;

    private int mWeek;

    public SyllabusFragmentModule(SyllabusFragmentContract.view view, int week) {
        mView = view;
        mWeek = week;
    }

    @PerFragment
    @Provides
    SyllabusFragmentContract.view provideView() {
        return mView;
    }

    @PerFragment
    @Provides
    int provideWeek() {
        return mWeek;
    }

}

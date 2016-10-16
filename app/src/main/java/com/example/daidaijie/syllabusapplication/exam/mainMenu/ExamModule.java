package com.example.daidaijie.syllabusapplication.exam.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.exam.IExamModel;
import com.example.daidaijie.syllabusapplication.exam.detail.ExamDetailContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/14.
 */

@Module
public class ExamModule {

    ExamContract.view mView;

    public ExamModule(ExamContract.view view) {
        mView = view;
    }

    @PerActivity
    @Provides
    ExamContract.view provideView() {
        return mView;
    }
}

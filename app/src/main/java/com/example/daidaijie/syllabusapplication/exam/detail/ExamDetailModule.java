package com.example.daidaijie.syllabusapplication.exam.detail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.exam.IExamModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/14.
 */

@Module
public class ExamDetailModule {

    ExamDetailContract.view mView;

    int mPosition;

    public ExamDetailModule(ExamDetailContract.view view, int position) {
        mView = view;
        mPosition = position;
    }

    @PerActivity
    @Provides
    ExamDetailContract.view provideView() {
        return mView;
    }

    @PerActivity
    @Provides
    int providePosition() {
        return mPosition;
    }

    @PerActivity
    @Provides
    IExamItemModel getExamDeatailModel(IExamModel examModel, int position) {
        return new ExamItemModel(examModel.getExamInList(position));
    }

}

package com.example.daidaijie.syllabusapplication.exam.detail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/10/14.
 */

public class ExamDetailPresenter implements ExamDetailContract.presenter {

    IExamItemModel mExamItemModel;

    ExamDetailContract.view mView;

    @Inject
    @PerActivity
    public ExamDetailPresenter(IExamItemModel examItemModel, ExamDetailContract.view view) {
        mExamItemModel = examItemModel;
        mView = view;
    }

    @Override
    public void start() {
        mView.showData(mExamItemModel.getExam());
    }

    @Override
    public void setUpstate() {
        mView.showState(mExamItemModel.getExamState());
    }
}

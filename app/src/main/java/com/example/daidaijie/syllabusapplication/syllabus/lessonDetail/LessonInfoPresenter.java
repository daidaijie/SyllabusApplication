package com.example.daidaijie.syllabusapplication.syllabus.lessonDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.syllabus.ISyllabusModel;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/10/20.
 */

public class LessonInfoPresenter implements LessonInfoContract.presenter {

    long mLessonId;

    LessonInfoContract.view mView;

    ISyllabusModel mISyllabusModel;

    @PerActivity
    @Inject
    public LessonInfoPresenter(long lessonId, LessonInfoContract.view view, ISyllabusModel syllabusModel) {
        mLessonId = lessonId;
        mView = view;
        mISyllabusModel = syllabusModel;
    }

    @Override
    public void start() {
        mISyllabusModel.getSyllabusNormal(new IBaseModel.OnGetSuccessCallBack<Syllabus>() {
            @Override
            public void onGetSuccess(Syllabus syllabus) {
                mView.showData(syllabus.getLessonByID(mLessonId));
            }
        }, null);
    }
}

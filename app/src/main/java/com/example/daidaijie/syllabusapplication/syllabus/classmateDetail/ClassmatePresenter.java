package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/26.
 */

public class ClassmatePresenter implements ClassmateContract.presenter {

    IClassmateModel mIClassmateModel;
    ClassmateContract.view mView;

    @Inject
    public ClassmatePresenter(IClassmateModel IClassmateModel, ClassmateContract.view view) {
        mIClassmateModel = IClassmateModel;
        mView = view;
    }

    @Override
    public void loadData() {
        mView.showLoading(true);
        mIClassmateModel.getStudentsFromNet()
                .subscribe(new Subscriber<List<StudentInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.printStack(e);
                        mView.showLoading(false);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("更新失败");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                    }

                    @Override
                    public void onNext(List<StudentInfo> studentInfos) {
                        mView.showData(studentInfos);
                    }
                });
    }

    @Override
    public void search(String keyword) {
        mIClassmateModel.searchStudentsList(keyword, new IBaseModel.OnGetSuccessCallBack<List<StudentInfo>>() {
            @Override
            public void onGetSuccess(List<StudentInfo> studentInfos) {
                mView.showData(studentInfos);
            }
        }, new IBaseModel.OnGetFailCallBack() {
            @Override
            public void onGetFail() {
                mView.showData(new ArrayList<StudentInfo>());
            }
        });
    }

    @Override
    public void start() {
        mIClassmateModel.getLessonNormal(new IBaseModel.OnGetSuccessCallBack<Lesson>() {
            @Override
            public void onGetSuccess(Lesson lesson) {
                mView.setTitleBg(lesson.getBgColor());
            }
        });

        mIClassmateModel.getStuDentInfoNormal(new IBaseModel.OnGetSuccessCallBack<List<StudentInfo>>() {
            @Override
            public void onGetSuccess(List<StudentInfo> studentInfos) {
                mView.showData(studentInfos);
            }

        });
    }
}

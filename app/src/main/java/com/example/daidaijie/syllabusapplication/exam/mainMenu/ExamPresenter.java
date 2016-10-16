package com.example.daidaijie.syllabusapplication.exam.mainMenu;

import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.exam.IExamModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/14.
 */

public class ExamPresenter implements ExamContract.presenter {

    IExamModel mExamModel;
    ExamContract.view mView;

    boolean isLoaded;

    @Inject
    @PerActivity
    public ExamPresenter(IExamModel examModel, ExamContract.view view) {
        mExamModel = examModel;
        mView = view;
        isLoaded = false;
    }

    @Override
    public void loadData() {
        mView.showFresh(true);
        mExamModel.getExamFromNet()
                .subscribe(new Subscriber<List<Exam>>() {
                    @Override
                    public void onCompleted() {
                        mView.showFresh(false);
                        isLoaded = true;
                        mView.setIsLoaded(isLoaded);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showFresh(false);
                        isLoaded = true;
                        mView.setIsLoaded(isLoaded);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("获取失败");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                    }

                    @Override
                    public void onNext(List<Exam> exams) {
                        if (exams == null || exams.size() == 0) {
                            mView.showInfoMessage("本学期暂无考试");
                            return;
                        }
                        mView.showData(exams);
                        mView.showSuccessMessage("更新成功");
                    }
                });

    }

    @Override
    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    @Override
    public void start() {
        mExamModel.getExamFromCache()
                .subscribe(new Subscriber<List<Exam>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Exam> exams) {
                        mView.showData(exams);
                    }
                });
        if (!isLoaded) {
            loadData();
        }
    }
}

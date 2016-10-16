package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class GradePresenter implements GradeContract.presenter {

    GradeContract.view mView;

    IGradeModel mIGradeModel;

    @Inject
    @PerActivity
    public GradePresenter(GradeContract.view view, IGradeModel IGradeModel) {
        mView = view;
        mIGradeModel = IGradeModel;
    }

    @Override
    public void start() {
        mIGradeModel.getGradeStoreListFromCache()
                .subscribe(new Subscriber<List<SemesterGrade>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<SemesterGrade> semesterGrades) {
                        if (semesterGrades != null) {
                            mView.setData(semesterGrades);
                        }
                    }
                });
        loadData();
    }

    @Override
    public void loadData() {
        try {
            mView.showFresh(true);
            mIGradeModel.getGradeStoreListFromNet()
                    .subscribe(new Subscriber<List<SemesterGrade>>() {
                        @Override
                        public void onCompleted() {
                            mView.showFresh(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showFresh(false);
                            if (e.getMessage() == null) {
                                mView.showFailMessage("获取失败");
                            } else {
                                mView.showFailMessage(e.getMessage().toUpperCase());
                            }
                        }

                        @Override
                        public void onNext(List<SemesterGrade> semesterGrades) {
                            if (semesterGrades != null) {
                                mView.setData(semesterGrades);
                                mView.showSuccessMessage("更新成功");
                            } else {
                                mView.showInfoMessage("暂无成绩");
                            }
                        }
                    });
        } catch (Exception e) {
            LoggerUtil.e("crash", e.getMessage());
        }
    }
}

package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.bean.GradeStore;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

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
                .subscribe(new Subscriber<GradeStore>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GradeStore gradeStore) {
                        if (gradeStore != null) {
                            mView.setData(gradeStore.getSemesterGrades());
                        }
                        mView.setHeader(gradeStore);
                    }
                });
        loadData();
    }

    @Override
    public void loadData() {
        mView.showFresh(true);
        mIGradeModel.getGradeStoreListFromNet()
                .subscribe(new Subscriber<GradeStore>() {
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
                    public void onNext(GradeStore gradeStore) {
                        if (gradeStore != null) {
                            if (gradeStore.getSemesterGrades().size() == 0) {
                                mView.showInfoMessage("暂无成绩");
                            } else {
                                mView.setData(gradeStore.getSemesterGrades());
                                mView.showSuccessMessage("更新成功");
                            }
                        }else {
                            mView.showInfoMessage("暂无成绩");
                        }
                        mView.setHeader(gradeStore);
                    }
                });

    }
}

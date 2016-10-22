package com.example.daidaijie.syllabusapplication.schoolDynamatic.dymatic.mainMenu;

import com.example.daidaijie.syllabusapplication.adapter.SchoolDymaticAdapter;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.schoolDynamatic.dymatic.ISchoolDymaticModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class SchoolDymaticPresenter implements SchoolDymaticContract.presenter {

    SchoolDymaticContract.view mView;

    ISchoolDymaticModel mISchoolDymaticModel;

    @Inject
    @PerFragment
    public SchoolDymaticPresenter(SchoolDymaticContract.view view, ISchoolDymaticModel ISchoolDymaticModel) {
        mView = view;
        mISchoolDymaticModel = ISchoolDymaticModel;
    }

    @Override
    public void refresh() {
        mView.showRefresh(true);
        mISchoolDymaticModel.loadSchoolDynamicListFromNet()
                .subscribe(new Subscriber<List<SchoolDymatic>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.printStack(e);
                        mView.showRefresh(false);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("获取失败");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                    }

                    @Override
                    public void onNext(List<SchoolDymatic> schoolDymatics) {
                        mView.showData(schoolDymatics);
                    }
                });
    }

    @Override
    public void loadData() {
        mISchoolDymaticModel.getSchoolDynamicListFromNet()
                .subscribe(new Subscriber<List<SchoolDymatic>>() {
                    @Override
                    public void onCompleted() {
                        mView.loadMoreFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage() == null) {
                            mView.showFailMessage("获取失败");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                        mView.loadMoreFinish();
                    }

                    @Override
                    public void onNext(List<SchoolDymatic> schoolDymatics) {
                        mView.showData(schoolDymatics);
                    }
                });

    }

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void onLike(int position, boolean isLike, final SchoolDymaticAdapter.OnLikeStateChangeListener onLikeStateChangeListener) {
        if (isLike) {
            mISchoolDymaticModel.like(position)
                    .subscribe(new Subscriber<ThumbUpReturn>() {
                        @Override
                        public void onCompleted() {
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() == null) {
                                mView.showFailMessage("点赞失败");
                            } else {
                                mView.showFailMessage(e.getMessage().toUpperCase());
                            }
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onNext(ThumbUpReturn thumbUpReturn) {
                            onLikeStateChangeListener.onLike(true);
                        }
                    });
        } else {
            mISchoolDymaticModel.unlike(position)
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() == null) {
                                mView.showFailMessage("取消点赞失败");
                            } else {
                                mView.showFailMessage(e.getMessage().toUpperCase());
                            }
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            onLikeStateChangeListener.onLike(false);
                        }
                    });
        }

    }
}

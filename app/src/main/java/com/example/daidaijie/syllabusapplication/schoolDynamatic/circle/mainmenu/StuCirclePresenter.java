package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.mainmenu;

import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.ISchoolCircleModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class StuCirclePresenter implements StuCircleContract.presenter {

    ISchoolCircleModel mISchoolCircleModel;

    StuCircleContract.view mView;

    @Inject
    @PerFragment
    public StuCirclePresenter(ISchoolCircleModel ISchoolCircleModel, StuCircleContract.view view) {
        mISchoolCircleModel = ISchoolCircleModel;
        mView = view;
    }

    @Override
    public void refresh() {
        mView.showRefresh(true);
        mISchoolCircleModel.loadCircleListFromNet()
                .subscribe(new Subscriber<List<PostListBean>>() {
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
                    public void onNext(List<PostListBean> postListBeen) {
                        mView.showData(postListBeen);
                    }
                });
    }

    @Override
    public void loadData() {
        mISchoolCircleModel.getCircleListFromNet()
                .subscribe(new Subscriber<List<PostListBean>>() {
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
                    public void onNext(List<PostListBean> postListBeen) {
                        mView.showData(postListBeen);
                    }
                });
    }

    @Override
    public void onLike(int position, boolean isLike, final CirclesAdapter.OnLikeStateChangeListener onLikeStateChangeListener) {
        if (isLike) {
            mISchoolCircleModel.like(position)
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
            mISchoolCircleModel.unlike(position)
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

    @Override
    public void start() {
        refresh();
    }
}

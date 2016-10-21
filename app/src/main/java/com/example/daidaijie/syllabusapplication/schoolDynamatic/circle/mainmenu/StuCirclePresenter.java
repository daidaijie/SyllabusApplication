package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.mainmenu;

import com.example.daidaijie.syllabusapplication.bean.PostListBean;
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
    public void start() {
        refresh();
    }
}

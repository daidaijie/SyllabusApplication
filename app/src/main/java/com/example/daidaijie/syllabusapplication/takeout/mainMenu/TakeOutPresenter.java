package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by daidaijie on 2016/10/8.
 */

public class TakeOutPresenter implements TakeOutContract.presenter {

    private TakeOutContract.view mView;

    private ITakeOutModel mTakeOutModel;

    @Inject
    @PerActivity
    public TakeOutPresenter(TakeOutContract.view view, ITakeOutModel takeOutModel) {
        mView = view;
        mTakeOutModel = takeOutModel;
    }

    @Override
    public void start() {
        /**
         * 三级缓存获取数据
         * rxjava写法
         */
        mView.showRefresh(true);
        mTakeOutModel.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TakeOutInfoBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.e("mainTakeOut", e.getMessage());
                        mView.showFailMessage("获取失败!");
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onNext(List<TakeOutInfoBean> takeOutInfoBeen) {
                        mView.showData(takeOutInfoBeen);
                    }
                });
    }


    @Override
    public void loadDataFromNet() {
        mView.showRefresh(true);
        mTakeOutModel.getDataFromNet()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TakeOutInfoBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showFailMessage("获取失败!");
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onNext(List<TakeOutInfoBean> takeOutInfoBeen) {
                        mView.showData(takeOutInfoBeen);
                    }
                });
    }
}

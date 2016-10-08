package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/10/8.
 */

public class TakeOutPresenter implements TakeOutContract.TakeOutPresenter {

    private TakeOutContract.TakeOutView mTakeOutView;

    private ITakeOutModel mTakeOutModel;

    @Inject
    @PerActivity
    public TakeOutPresenter(TakeOutContract.TakeOutView takeOutView, ITakeOutModel takeOutModel) {
        mTakeOutView = takeOutView;
        mTakeOutModel = takeOutModel;
    }

    @Override
    public void start() {
        mTakeOutModel.loadDataFromDist(new ITakeOutModel.OnLoadListener() {
            @Override
            public void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen) {
                mTakeOutView.showData(takeOutInfoBeen);
                if (takeOutInfoBeen.size() == 0) {
                    loadData();
                }
            }

            @Override
            public void onLoadFail(String msg) {
                mTakeOutView.showFailMessage(msg);
            }
        });
    }

    @Override
    public void loadData() {
        mTakeOutView.showRefresh(true);
        mTakeOutModel.loadDataFromNet(new ITakeOutModel.OnLoadListener() {
            @Override
            public void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen) {
                mTakeOutView.showData(takeOutInfoBeen);
                mTakeOutView.showRefresh(false);
            }

            @Override
            public void onLoadFail(String msg) {
                mTakeOutView.showFailMessage("获取失败");
                mTakeOutView.showRefresh(false);
            }
        });
    }
}

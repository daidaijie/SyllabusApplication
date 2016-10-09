package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;

import java.util.List;

import javax.inject.Inject;

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
        mView.showFailMessage(mTakeOutModel+"");
        mTakeOutModel.loadDataFromDist(new ITakeOutModel.OnLoadListener() {
            @Override
            public void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen) {
                mView.showData(takeOutInfoBeen);
                if (takeOutInfoBeen.size() == 0) {
                    loadData();
                }
                mView.showData(takeOutInfoBeen);
            }

            @Override
            public void onLoadFail(String msg) {
                mView.showFailMessage(msg);
            }
        });
    }

    @Override
    public void loadData() {
        mView.showRefresh(true);
        mTakeOutModel.loadDataFromNet(new ITakeOutModel.OnLoadListener() {
            @Override
            public void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen) {
                mView.showData(takeOutInfoBeen);
                mView.showRefresh(false);
            }

            @Override
            public void onLoadFail(String msg) {
                mView.showFailMessage("获取失败");
                mView.showRefresh(false);
            }
        });
    }
}

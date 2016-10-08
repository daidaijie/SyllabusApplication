package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutContract;

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
        mTakeOutView.showFailMessage("测试测试");
    }

    @Override
    public void loadData() {
        if (mTakeOutModel == null) {
            mTakeOutView.showFailMessage("model is null");
            return;
        }
        mTakeOutModel.loadDataFromNet(new ITakeOutModel.OnLoadListener() {
            @Override
            public void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen) {
                mTakeOutView.showFailMessage(takeOutInfoBeen.size() + "");
            }

            @Override
            public void onLoadFail() {
                mTakeOutView.showFailMessage("获取失败");
            }
        });
    }
}

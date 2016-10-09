package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/10/8.
 */

public class TakeOutDetailPresenter implements TakeOutDetailContract.presenter {

    private String objectID;

    private TakeOutDetailContract.view mView;

    private ITakeOutModel mTakeOutModel;

    @Inject
    @PerActivity
    public TakeOutDetailPresenter(TakeOutDetailContract.view view, ITakeOutModel takeOutModel, String objectID) {
        mView = view;
        mTakeOutModel = takeOutModel;
        this.objectID = objectID;
    }

    @Override
    public void loadData() {
        mView.showRefresh(true);
        mTakeOutModel.loadItemFromNet(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBeen) {
                mView.showRefresh(false);
                mView.setUpTakeOutInfo(takeOutInfoBeen);
            }

            @Override
            public void onLoadFail(String msg) {
                mView.showRefresh(false);
                mView.showFailMessage("获取失败");
            }
        });
    }

    @Override
    public void start() {
        mView.showFailMessage(mTakeOutModel + "");
        mTakeOutModel.loadItemFromDist(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBeen) {
//                mView.showFailMessage(takeOutInfoBeen.getName());
            }

            @Override
            public void onLoadFail(String msg) {

            }
        });
    }
}

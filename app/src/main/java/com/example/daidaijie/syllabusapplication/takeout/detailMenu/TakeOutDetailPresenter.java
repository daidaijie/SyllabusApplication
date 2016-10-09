package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
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

    private TakeOutInfoBean mTakeOutInfoBean;

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
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                mTakeOutInfoBean = takeOutInfoBean;
                mView.showRefresh(false);
                mView.setUpTakeOutInfo(takeOutInfoBean);
                mView.setMenuList(takeOutInfoBean);
            }

            @Override
            public void onLoadFail(String msg) {
                mView.showRefresh(false);
                mView.showFailMessage("获取失败");
            }
        });
    }

    @Override
    public void addDish(int position) {
        mTakeOutInfoBean.getTakeOutBuyBean().addDishes(mTakeOutInfoBean.getDishes().get(position));
        mView.showPrice(mTakeOutInfoBean.getTakeOutBuyBean());
    }

    @Override
    public void reduceDish(int position) {
        mTakeOutInfoBean.getTakeOutBuyBean().removeDishes(mTakeOutInfoBean.getDishes().get(position));
        mView.showPrice(mTakeOutInfoBean.getTakeOutBuyBean());
    }

    @Override
    public void showPopWindows() {
        mView.showPopWindows(mTakeOutInfoBean);
    }


    @Override
    public void start() {
        mTakeOutModel.loadItemFromDist(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                mTakeOutInfoBean = takeOutInfoBean;
                mView.setUpTakeOutInfo(takeOutInfoBean);
                mView.setMenuList(takeOutInfoBean);
            }

            @Override
            public void onLoadFail(String msg) {
                loadData();
            }
        });
    }
}

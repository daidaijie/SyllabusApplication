package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import javax.inject.Inject;

import rx.Subscriber;

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
        mTakeOutModel.getItemFromNet(objectID)
                .subscribe(new Subscriber<TakeOutInfoBean>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefresh(false);
                        mView.showFailMessage("获取失败");
                    }

                    @Override
                    public void onNext(TakeOutInfoBean takeOutInfoBean) {
                        takeOutInfoBean.loadTakeOutSubMenus();
                        mView.setUpTakeOutInfo(takeOutInfoBean);
                        mView.setMenuList(takeOutInfoBean);
                    }
                });
    }

    @Override
    public void addDish(final int position) {
        TakeOutInfoBean takeOutInfoBean = mTakeOutModel.getTakeOutInfoBeanById(objectID);
        takeOutInfoBean.getTakeOutBuyBean().addDishes(takeOutInfoBean.getDishes().get(position));
        mView.showPrice(takeOutInfoBean.getTakeOutBuyBean());
    }

    @Override
    public void reduceDish(final int position) {
        TakeOutInfoBean takeOutInfoBean = mTakeOutModel.getTakeOutInfoBeanById(objectID);
        takeOutInfoBean.getTakeOutBuyBean().removeDishes(takeOutInfoBean.getDishes().get(position));
        mView.showPrice(takeOutInfoBean.getTakeOutBuyBean());
    }

    @Override
    public void showPopWindows() {
        TakeOutInfoBean takeOutInfoBean = mTakeOutModel.getTakeOutInfoBeanById(objectID);
        if (takeOutInfoBean.getTakeOutBuyBean().getBuyMap().size() != 0) {
            mView.showPopWindows(takeOutInfoBean);
        }
    }

    @Override
    public void toSearch() {
        TakeOutInfoBean takeOutInfoBean = mTakeOutModel.getTakeOutInfoBeanById(objectID);
        if (takeOutInfoBean.getDishes() != null && takeOutInfoBean.getDishes().size() != 0) {
            mView.toSearch(objectID);
        }
    }

    @Override
    public void showPrice() {
        TakeOutInfoBean takeOutInfoBean = mTakeOutModel.getTakeOutInfoBeanById(objectID);
        mView.showPrice(takeOutInfoBean.getTakeOutBuyBean());
    }


    @Override
    public void start() {
        mView.showRefresh(true);
        mTakeOutModel.getItem(objectID)
                .subscribe(new Subscriber<TakeOutInfoBean>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.e("detailTakeOut", e.getMessage());
                        mView.showRefresh(false);
                        mView.showFailMessage("获取失败");
                    }

                    @Override
                    public void onNext(TakeOutInfoBean takeOutInfoBean) {
                        mView.setUpTakeOutInfo(takeOutInfoBean);
                        mView.setMenuList(takeOutInfoBean);
                    }
                });
    }
}

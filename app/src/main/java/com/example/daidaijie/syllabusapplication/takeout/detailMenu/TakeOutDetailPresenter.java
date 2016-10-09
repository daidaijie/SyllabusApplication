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
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
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
    public void addDish(final int position) {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                takeOutInfoBean.getTakeOutBuyBean().addDishes(takeOutInfoBean.getDishes().get(position));
                mView.showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }

            @Override
            public void onLoadFail(String msg) {
            }
        });

    }

    @Override
    public void reduceDish(final int position) {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                takeOutInfoBean.getTakeOutBuyBean().removeDishes(takeOutInfoBean.getDishes().get(position));
                mView.showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }

            @Override
            public void onLoadFail(String msg) {
            }
        });
    }

    @Override
    public void showPopWindows() {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                if (takeOutInfoBean.getTakeOutBuyBean().getBuyMap().size() != 0) {
                    mView.showPopWindows(takeOutInfoBean);
                }
            }

            @Override
            public void onLoadFail(String msg) {
            }
        });
    }

    @Override
    public void toSearch() {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                if (takeOutInfoBean.getDishes() != null && takeOutInfoBean.getDishes().size() != 0) {
                    mView.toSearch(objectID);
                }
            }

            @Override
            public void onLoadFail(String msg) {
            }
        });

    }

    @Override
    public void showPrice() {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                mView.showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }

            @Override
            public void onLoadFail(String msg) {

            }
        });
    }


    @Override
    public void start() {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                mView.setUpTakeOutInfo(takeOutInfoBean);
                mView.setMenuList(takeOutInfoBean);
            }

            @Override
            public void onLoadFail(String msg) {
                mTakeOutModel.loadItemFromDist(objectID, new ITakeOutModel.OnLoadItemListener() {
                    @Override
                    public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                        mView.setUpTakeOutInfo(takeOutInfoBean);
                        mView.setMenuList(takeOutInfoBean);
                    }

                    @Override
                    public void onLoadFail(String msg) {
                        mTakeOutModel.loadItemFromDist(objectID, new ITakeOutModel.OnLoadItemListener() {
                            @Override
                            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                                mView.setUpTakeOutInfo(takeOutInfoBean);
                                mView.setMenuList(takeOutInfoBean);
                            }

                            @Override
                            public void onLoadFail(String msg) {
                                loadData();
                            }
                        });
                    }
                });
            }
        });
    }
}

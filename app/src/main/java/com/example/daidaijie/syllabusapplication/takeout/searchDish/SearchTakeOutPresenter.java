package com.example.daidaijie.syllabusapplication.takeout.searchDish;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/9.
 */

public class SearchTakeOutPresenter implements SearchTakeOutContract.presenter {

    private String objectID;

    private SearchTakeOutContract.view mView;

    private ITakeOutModel mTakeOutModel;

    @Inject
    @PerActivity
    public SearchTakeOutPresenter(SearchTakeOutContract.view view, ITakeOutModel takeOutModel, String objectID) {
        mView = view;
        mTakeOutModel = takeOutModel;
        this.objectID = objectID;
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
    public void start() {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(TakeOutInfoBean takeOutInfoBean) {
                mView.setUpTakeOutInfo(takeOutInfoBean);
            }

            @Override
            public void onLoadFail(String msg) {

            }
        });
    }

    @Override
    public void search(final String keyWord) {
        mTakeOutModel.loadItemFromMemory(objectID, new ITakeOutModel.OnLoadItemListener() {
            @Override
            public void onLoadSuccess(final TakeOutInfoBean takeOutInfoBean) {
                final List<Dishes> searchDishes = new ArrayList<>();
                if (keyWord.trim().isEmpty()) {
                    mView.showSearchResult(takeOutInfoBean.getTakeOutBuyBean(),
                            searchDishes, "");
                    return;
                }
                Observable.from(takeOutInfoBean.getDishes())
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.computation())
                        .filter(new Func1<Dishes, Boolean>() {
                            @Override
                            public Boolean call(Dishes dishes) {
                                return dishes.getName().contains(keyWord);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Dishes>() {

                            @Override
                            public void onCompleted() {
                                mView.showSearchResult(takeOutInfoBean.getTakeOutBuyBean(),
                                        searchDishes, keyWord);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(Dishes dishes) {
                                searchDishes.add(dishes);
                            }
                        });
            }

            @Override
            public void onLoadFail(String msg) {

            }
        });
    }
}

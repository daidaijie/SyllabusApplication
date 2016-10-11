package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.BmobResult;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.service.TakeOutInfoApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/8.
 */

public class TakeOutModel implements ITakeOutModel {

    private Realm mRealm;
    private TakeOutInfoApi mTakeOutInfoApi;
    private Gson mGson;

    private Map<String, TakeOutInfoBean> mInfoBeanMap;

    private List<TakeOutInfoBean> mTakeOutInfoList;


    public TakeOutModel(Realm realm, TakeOutInfoApi takeOutInfoApi, Gson gson) {
        mRealm = realm;
        mTakeOutInfoApi = takeOutInfoApi;
        mGson = gson;
        mTakeOutInfoList = new ArrayList<>();
        mInfoBeanMap = new LinkedHashMap<>();
    }

    @Override
    public Observable<List<TakeOutInfoBean>> getDataFromMemory() {
        return Observable.create(new Observable.OnSubscribe<List<TakeOutInfoBean>>() {
            @Override
            public void call(Subscriber<? super List<TakeOutInfoBean>> subscriber) {
                subscriber.onNext(mTakeOutInfoList);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<TakeOutInfoBean>> getDataFromDist() {
        return Observable.create(new Observable.OnSubscribe<List<TakeOutInfoBean>>() {
            @Override
            public void call(Subscriber<? super List<TakeOutInfoBean>> subscriber) {
                RealmResults<TakeOutInfoBean> takeOutInfoBeanResults =
                        mRealm.where(TakeOutInfoBean.class).findAll();
                mTakeOutInfoList = takeOutInfoBeanResults.subList(0,
                        takeOutInfoBeanResults.size());
                loadToMemory(mTakeOutInfoList);
                subscriber.onNext(mTakeOutInfoList);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<TakeOutInfoBean>> getDataFromNet() {
        return mTakeOutInfoApi.getTakeOutInfo("name,long_number,short_number,condition")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BmobResult<TakeOutInfoBean>, Observable<List<TakeOutInfoBean>>>() {
                    @Override
                    public Observable<List<TakeOutInfoBean>> call(BmobResult<TakeOutInfoBean> takeOutInfoBeanBmobResult) {
                        if (takeOutInfoBeanBmobResult.getResults().size() != 0) {
                            mTakeOutInfoList = takeOutInfoBeanBmobResult.getResults();
                            loadToMemory(mTakeOutInfoList);
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    for (TakeOutInfoBean takeOutInfoBean : mTakeOutInfoList) {
                                        TakeOutInfoBean hasBean = getTakeOutInfoBeanById(takeOutInfoBean.getObjectId());
                                        if (hasBean != null) {
                                            takeOutInfoBean.setMenu(hasBean.getMenu());
                                        }
                                        realm.copyToRealmOrUpdate(takeOutInfoBean);
                                    }
                                }
                            });
                        }
                        return Observable.just(takeOutInfoBeanBmobResult.getResults());
                    }
                });
    }

    @Override
    public Observable<List<TakeOutInfoBean>> getData() {
        return Observable.concat(getDataFromMemory(), getDataFromDist(), getDataFromNet())
                .takeFirst(new Func1<List<TakeOutInfoBean>, Boolean>() {
                    @Override
                    public Boolean call(List<TakeOutInfoBean> takeOutInfoBeen) {
                        return (takeOutInfoBeen != null && takeOutInfoBeen.size() != 0);
                    }
                });
    }

    @Override
    public void loadItemFromNet(final String objectID, final OnLoadItemListener onLoadItemListener) {
        mTakeOutInfoApi.getTakeOutDetailInfo(objectID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TakeOutInfoBean>() {

                    TakeOutInfoBean mTakeOutInfoBean;

                    @Override
                    public void onCompleted() {
                        TakeOutInfoBean hasBean = getTakeOutInfoBeanById(objectID);
                        if (hasBean != null) {
                            mTakeOutInfoBean.setTakeOutBuyBean(hasBean.getTakeOutBuyBean());
                        }

                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(mTakeOutInfoBean);
                            }
                        });
                        mTakeOutInfoBean.loadTakeOutSubMenus();
                        loadToMemory(mTakeOutInfoBean);
                        onLoadItemListener.onLoadSuccess(mTakeOutInfoBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadItemListener.onLoadFail(e.getMessage());
                    }

                    @Override
                    public void onNext(TakeOutInfoBean takeOutInfoBean) {
                        mTakeOutInfoBean = takeOutInfoBean;
                        mTakeOutInfoBean.loadTakeOutSubMenus();
                    }
                });

    }

    @Override
    public void loadItemFromDist(String objectID, OnLoadItemListener onLoadItemListener) {
        try {
            TakeOutInfoBean bean = mRealm.where(TakeOutInfoBean.class)
                    .equalTo("objectId", objectID)
                    .findFirst();
            if (bean.loadTakeOutSubMenus()) {
                loadToMemory(bean);
                onLoadItemListener.onLoadSuccess(bean);
            } else {
                loadToMemory(bean);
                onLoadItemListener.onLoadFail("NULL_MENU");
            }
        } catch (Throwable e) {
            onLoadItemListener.onLoadFail(e.getMessage());
        }
    }

    @Override
    public void loadItemFromMemory(String objectID, OnLoadItemListener onLoadItemListener) {
        if (mInfoBeanMap == null) {
            onLoadItemListener.onLoadFail("NULL");
        }
        TakeOutInfoBean takeOutInfoBean = mInfoBeanMap.get(objectID);
        if (takeOutInfoBean == null) {
            onLoadItemListener.onLoadFail("NULL_ITEM");
        } else {
            if (takeOutInfoBean.loadTakeOutSubMenus()) {
                onLoadItemListener.onLoadSuccess(takeOutInfoBean);
            } else {
                onLoadItemListener.onLoadFail("NULL_MENU");
            }
        }

    }

    private TakeOutInfoBean getTakeOutInfoBeanById(String objectID) {
        TakeOutInfoBean bean = mInfoBeanMap.get(objectID);
        if (bean != null) {
            return bean;
        }
        bean = mRealm.where(TakeOutInfoBean.class)
                .equalTo("objectId", objectID)
                .findFirst();
        return bean;
    }

    private void loadToMemory(List<TakeOutInfoBean> takeOutInfoBeen) {
        for (TakeOutInfoBean takeOutInfoBean : takeOutInfoBeen) {
            loadToMemory(takeOutInfoBean);
        }
    }

    private void loadToMemory(TakeOutInfoBean takeOutInfoBean) {
        mInfoBeanMap.put(takeOutInfoBean.getObjectId(), takeOutInfoBean);
    }
}

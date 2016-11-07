package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.BmobResult;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.retrofitApi.TakeOutInfoApi;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.orhanobut.logger.Logger;

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

    private Map<String, TakeOutInfoBean> mInfoBeanMap;

    private List<TakeOutInfoBean> mTakeOutInfoList;


    public TakeOutModel(Realm realm, TakeOutInfoApi takeOutInfoApi) {
        mRealm = realm;
        mTakeOutInfoApi = takeOutInfoApi;
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
                for (TakeOutInfoBean takeOutInfoBean : takeOutInfoBeanResults) {
                    mTakeOutInfoList.add(mRealm.copyFromRealm(takeOutInfoBean));
                }
                for (TakeOutInfoBean takeOutInfoBean : mTakeOutInfoList) {
                    loadCache(takeOutInfoBean);
                }
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
                            for (TakeOutInfoBean takeOutInfoBean : mTakeOutInfoList) {
                                loadCache(takeOutInfoBean);
                            }
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
    public Observable<TakeOutInfoBean> getItemFromMemory(final String objectID) {
        return Observable.create(new Observable.OnSubscribe<TakeOutInfoBean>() {
            @Override
            public void call(Subscriber<? super TakeOutInfoBean> subscriber) {
                subscriber.onNext(mInfoBeanMap.get(objectID));
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TakeOutInfoBean> getItemFromDist(final String objectID) {
        return Observable.create(new Observable.OnSubscribe<TakeOutInfoBean>() {
            @Override
            public void call(Subscriber<? super TakeOutInfoBean> subscriber) {
                TakeOutInfoBean takeOutInfoBean = mRealm.where(TakeOutInfoBean.class)
                        .equalTo("objectId", objectID)
                        .findFirst();
                loadCache(takeOutInfoBean);
                if (takeOutInfoBean.getMenu() != null && !takeOutInfoBean.getMenu().isEmpty()) {
                    addToList(takeOutInfoBean);
                    mInfoBeanMap.put(takeOutInfoBean.getObjectId(), takeOutInfoBean);
                }
                subscriber.onNext(takeOutInfoBean);
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TakeOutInfoBean> getItemFromNet(final String objectID) {
        return mTakeOutInfoApi.getTakeOutDetailInfo(objectID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<TakeOutInfoBean, Observable<TakeOutInfoBean>>() {
                    @Override
                    public Observable<TakeOutInfoBean> call(final TakeOutInfoBean takeOutInfoBean) {
                        loadCache(takeOutInfoBean);
                        addToList(takeOutInfoBean);
                        mInfoBeanMap.put(takeOutInfoBean.getObjectId(), takeOutInfoBean);
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(takeOutInfoBean);
                            }
                        });
                        return Observable.just(takeOutInfoBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TakeOutInfoBean> getItem(String objectID) {
        return Observable.concat(getItemFromMemory(objectID), getItemFromDist(objectID), getItemFromNet(objectID))
                .takeFirst(new Func1<TakeOutInfoBean, Boolean>() {
                    @Override
                    public Boolean call(TakeOutInfoBean takeOutInfoBeen) {
                        if (takeOutInfoBeen == null) {
                            return false;
                        }
                        if (takeOutInfoBeen.getMenu() != null && !takeOutInfoBeen.getMenu().isEmpty()) {
                            takeOutInfoBeen.loadTakeOutSubMenus();
                            return true;
                        }
                        return false;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public TakeOutInfoBean getTakeOutInfoBeanById(String objectID) {
        TakeOutInfoBean bean;
        if (mInfoBeanMap != null) {
            bean = mInfoBeanMap.get(objectID);
            if (bean != null) {
                return bean;
            }
        }

        bean = mRealm.where(TakeOutInfoBean.class)
                .equalTo("objectId", objectID)
                .findFirst();
        return bean;
    }

    private void addToList(TakeOutInfoBean takeOutInfoBean) {
        for (int i = 0; i < mTakeOutInfoList.size(); i++) {
            if (mTakeOutInfoList.get(i).getObjectId().equals(takeOutInfoBean.getObjectId())) {
                mTakeOutInfoList.set(i, takeOutInfoBean);
                return;
            }
        }
    }

    private void loadCache(TakeOutInfoBean takeOutInfoBean) {
        TakeOutInfoBean hasBeen = getTakeOutInfoBeanById(takeOutInfoBean.getObjectId());
        if (hasBeen != null && hasBeen.getUpdatedAt().equals(takeOutInfoBean.getUpdatedAt())) {
            takeOutInfoBean.setTakeOutBuyBean(hasBeen.getTakeOutBuyBean());
        }
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

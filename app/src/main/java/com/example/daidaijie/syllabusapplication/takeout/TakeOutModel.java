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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/8.
 */

public class TakeOutModel implements ITakeOutModel {

    private Realm mRealm;
    private TakeOutInfoApi mTakeOutInfoApi;
    private Gson mGson;

    private Map<String, TakeOutInfoBean> mInfoBeanMap;


    public TakeOutModel(Realm realm, TakeOutInfoApi takeOutInfoApi, Gson gson) {
        mRealm = realm;
        mTakeOutInfoApi = takeOutInfoApi;
        mGson = gson;
        mInfoBeanMap = new LinkedHashMap<>();
    }

    @Override
    public void loadDataFromNet(final OnLoadListener onLoadListener) {
        /**
         * 获取除了详细菜单之外的信息
         */
        mTakeOutInfoApi.getTakeOutInfo("name,long_number,short_number,condition")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BmobResult<TakeOutInfoBean>>() {

                    List<TakeOutInfoBean> takeOutInfoBeen;

                    @Override
                    public void onCompleted() {
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                for (TakeOutInfoBean takeOutInfoBean : takeOutInfoBeen) {
                                    TakeOutInfoBean hasBean = getTakeOutInfoBeanById(takeOutInfoBean.getObjectId());
                                    if (hasBean != null) {
                                        takeOutInfoBean.setMenu(hasBean.getMenu());
                                    }
                                    realm.copyToRealmOrUpdate(takeOutInfoBean);
                                }
                            }
                        });
                        loadToMemory(takeOutInfoBeen);
                        onLoadListener.onLoadSuccess(takeOutInfoBeen);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadListener.onLoadFail(e.getMessage());
                    }

                    @Override
                    public void onNext(BmobResult<TakeOutInfoBean> bmobResult) {
                        takeOutInfoBeen = bmobResult.getResults();
                    }
                });
    }

    @Override
    public void loadDataFromDist(final OnLoadListener onLoadListener) {
        try {
            RealmResults<TakeOutInfoBean> takeOutInfoBeanResults =
                    mRealm.where(TakeOutInfoBean.class).findAll();
            List<TakeOutInfoBean> takeOutInfoBeen = takeOutInfoBeanResults.subList(0,
                    takeOutInfoBeanResults.size());
            loadToMemory(takeOutInfoBeen);
            onLoadListener.onLoadSuccess(takeOutInfoBeen);
        } catch (Throwable e) {
            onLoadListener.onLoadFail(e.getMessage());
        }
    }

    @Override
    public void loadDataFromMemory(OnLoadListener onLoadListener) {
        if (mInfoBeanMap == null || mInfoBeanMap.values().size() == 0) {
            onLoadListener.onLoadFail("NULL");
        } else {
            onLoadListener.onLoadSuccess(new ArrayList<>(mInfoBeanMap.values()));
        }
    }

    @Override
    public void loadItemFromNet(String objectID, final OnLoadItemListener onLoadItemListener) {
        mTakeOutInfoApi.getTakeOutDetailInfo(objectID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TakeOutInfoBean>() {

                    TakeOutInfoBean mTakeOutInfoBean;

                    @Override
                    public void onCompleted() {
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
        TakeOutInfoBean bean = mRealm.where(TakeOutInfoBean.class)
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

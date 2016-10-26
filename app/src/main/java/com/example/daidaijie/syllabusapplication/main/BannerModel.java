package com.example.daidaijie.syllabusapplication.main;

import com.example.daidaijie.syllabusapplication.bean.BannerBeen;
import com.example.daidaijie.syllabusapplication.bean.BannerInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.retrofitApi.BannerApi;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class BannerModel implements IBannerModel {

    BannerBeen mBannerBeen;

    Realm mRealm;

    BannerApi mBannerApi;

    public BannerModel(Realm realm, BannerApi bannerApi) {
        mRealm = realm;
        mBannerApi = bannerApi;
    }

    @Override
    public Observable<BannerBeen> getBannerFromMemory() {
        return Observable.create(new Observable.OnSubscribe<BannerBeen>() {
            @Override
            public void call(Subscriber<? super BannerBeen> subscriber) {
                subscriber.onNext(mBannerBeen);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BannerBeen> getBannerFromDisk() {
        return Observable.create(new Observable.OnSubscribe<BannerBeen>() {
            @Override
            public void call(Subscriber<? super BannerBeen> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<BannerBeen> results = realm.where(BannerBeen.class).findAll();
                        if (results.size() != 0) {
                            mBannerBeen = realm.copyFromRealm(results.first());
                            mBannerBeen.convertBanners();
                        }
                    }
                });
                subscriber.onNext(mBannerBeen);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<BannerBeen> getBannerFromNet() {
        return mBannerApi.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<BannerInfo>, Observable<BannerBeen>>() {
                    @Override
                    public Observable<BannerBeen> call(HttpResult<BannerInfo> bannerInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(bannerInfoHttpResult)) {
                            final BannerBeen bannerBeen = bannerInfoHttpResult.getData().getBannerBeen();
                            bannerBeen.convertBannersString();
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(BannerBeen.class).findAll().deleteAllFromRealm();
                                    realm.copyToRealm(bannerBeen);
                                }
                            });
                            return Observable.just(bannerBeen);
                        }
                        return Observable.error(new Throwable(bannerInfoHttpResult.getMessage()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<BannerBeen> getBannerFromCache() {
        return Observable.concat(getBannerFromMemory(), getBannerFromDisk())
                .takeFirst(new Func1<BannerBeen, Boolean>() {
                    @Override
                    public Boolean call(BannerBeen bannerBeen) {
                        return mBannerBeen != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getBannerNormal(OnGetSuccessCallBack<BannerBeen> onGetCallBack) {
        if (mBannerBeen != null && mBannerBeen.getBanners() != null) {
            onGetCallBack.onGetSuccess(mBannerBeen);
            return;
        }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<BannerBeen> results = realm.where(BannerBeen.class).findAll();
                if (results.size() != 0) {
                    mBannerBeen = realm.copyFromRealm(results.first());
                    mBannerBeen.convertBanners();
                }
            }
        });

        if (mBannerBeen != null && mBannerBeen.getBanners() != null) {
            onGetCallBack.onGetSuccess(mBannerBeen);
        }

    }
}

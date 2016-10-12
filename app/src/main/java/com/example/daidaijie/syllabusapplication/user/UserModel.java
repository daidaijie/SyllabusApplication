package com.example.daidaijie.syllabusapplication.user;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.GetUserBaseApi;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/12.
 */

public class UserModel implements IUserModel {

    private UserInfo mUserInfo;

    private UserBaseBean mUserBaseBean;

    private Realm mRealm;

    private String mUsername;

    private GetUserBaseApi mGetUserBaseApi;


    public UserModel(String username, Realm realm, Retrofit retrofit) {
        mUsername = username;
        mRealm = realm;
        mGetUserBaseApi = retrofit.create(GetUserBaseApi.class);
    }

    @Override
    public Observable<UserBaseBean> getUserBaseBeanFromMemory() {
        return Observable.create(new Observable.OnSubscribe<UserBaseBean>() {
            @Override
            public void call(Subscriber<? super UserBaseBean> subscriber) {
                subscriber.onNext(mUserBaseBean);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserBaseBean> getUserBaseBeanFromDisk() {
        return Observable.create(new Observable.OnSubscribe<UserBaseBean>() {
            @Override
            public void call(Subscriber<? super UserBaseBean> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<UserBaseBean> results = realm.where(UserBaseBean.class)
                                .equalTo("account", mUsername).findAll();
                        if (results.size() != 0) {
                            mUserBaseBean = realm.copyFromRealm(results.first());
                        }
                    }
                });
                subscriber.onNext(mUserBaseBean);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserBaseBean> getUserBaseBeanFromNet() {
        return mGetUserBaseApi.get_user(mUsername)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<UserBaseBean>, Observable<UserBaseBean>>() {
                    @Override
                    public Observable<UserBaseBean> call(HttpResult<UserBaseBean> userBaseBeanHttpResult) {
                        if (RetrofitUtil.isSuccessful(userBaseBeanHttpResult)) {
                            mUserBaseBean = userBaseBeanHttpResult.getData();
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(mUserBaseBean);
                                }
                            });
                            return Observable.just(userBaseBeanHttpResult.getData());
                        } else {
                            return Observable.error(new Throwable(userBaseBeanHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserBaseBean> getUserBaseBeanFromCache() {
        return Observable.concat(getUserBaseBeanFromMemory(), getUserBaseBeanFromDisk(), getUserBaseBeanFromNet())
                .takeFirst(new Func1<UserBaseBean, Boolean>() {
                    @Override
                    public Boolean call(UserBaseBean userBaseBean) {
                        return userBaseBean != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public UserBaseBean getUserBaseBean() {
        return mUserBaseBean;
    }

    @Override
    public Observable<UserInfo> getUserInfoFromMemory() {
        return Observable.create(new Observable.OnSubscribe<UserInfo>() {
            @Override
            public void call(Subscriber<? super UserInfo> subscriber) {
                subscriber.onNext(mUserInfo);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserInfo> getUserInfoFromDisk() {
        return Observable.create(new Observable.OnSubscribe<UserInfo>() {
            @Override
            public void call(Subscriber<? super UserInfo> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<UserInfo> results = realm.where(UserInfo.class)
                                .equalTo("username", mUsername)
                                .findAll();
                        if (results.size() != 0) {
                            mUserInfo = mRealm.copyFromRealm(results.first());
                        }
                    }
                });
                subscriber.onNext(mUserInfo);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<UserInfo> getUserInfoFromNet() {
        return null;
    }

    @Override
    public Observable<UserInfo> getUserInfo() {
        return Observable.concat();
    }
}

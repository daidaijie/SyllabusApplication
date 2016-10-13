package com.example.daidaijie.syllabusapplication.user;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.retrofitApi.GetUserBaseApi;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    private ILoginModel mILoginModel;

    private GetUserBaseApi mGetUserBaseApi;

    private boolean isLogin;

    public UserModel(ILoginModel loginModel, Retrofit retrofit) {
        isLogin = false;
        mILoginModel = loginModel;
        mGetUserBaseApi = retrofit.create(GetUserBaseApi.class);
    }


    public UserModel(ILoginModel loginModel, Realm realm, Retrofit retrofit) {
        isLogin = true;
        mILoginModel = loginModel;
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
                                .equalTo("account", mILoginModel.getUserLogin().getUsername()).findAll();
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
        return mGetUserBaseApi.get_user(mILoginModel.getUserLogin().getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<UserBaseBean>, Observable<UserBaseBean>>() {
                    @Override
                    public Observable<UserBaseBean> call(HttpResult<UserBaseBean> userBaseBeanHttpResult) {
                        if (RetrofitUtil.isSuccessful(userBaseBeanHttpResult)) {
                            mUserBaseBean = userBaseBeanHttpResult.getData();
                            Realm realm = getRealm();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(mUserBaseBean);
                                }
                            });
                            if (!isLogin) {
                                realm.close();
                            }
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
                                .equalTo("username", mILoginModel.getUserLogin().getUsername())
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
        return Observable.concat(getUserInfoFromMemory(), getUserInfoFromDisk(), getUserInfoFromNet())
                .takeFirst(new Func1<UserInfo, Boolean>() {
                    @Override
                    public Boolean call(UserInfo userInfo) {
                        return userInfo != null;
                    }
                });
    }

    private Realm getRealm() {
        if (isLogin) {
            return mRealm;
        } else {
            RealmConfiguration configuration = new RealmConfiguration
                    .Builder(App.getContext())
                    .schemaVersion(1)
                    .name(mILoginModel.getUserLogin().getUsername() + ".realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            return Realm.getInstance(configuration);
        }

    }
}

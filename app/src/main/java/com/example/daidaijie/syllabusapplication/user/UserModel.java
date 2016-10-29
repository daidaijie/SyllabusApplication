package com.example.daidaijie.syllabusapplication.user;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.event.UpdateUserInfoEvent;
import com.example.daidaijie.syllabusapplication.retrofitApi.GetUserBaseApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.UserInfoApi;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.greenrobot.eventbus.EventBus;

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

    private UserInfoApi mUserInfoApi;

    private boolean isLogin;

    public UserModel(ILoginModel loginModel, Retrofit retrofit) {
        isLogin = false;
        mILoginModel = loginModel;
        mGetUserBaseApi = retrofit.create(GetUserBaseApi.class);
        mUserInfoApi = retrofit.create(UserInfoApi.class);
    }


    public UserModel(ILoginModel loginModel, Realm realm, Retrofit retrofit) {
        this(loginModel, retrofit);
        mRealm = realm;
        isLogin = true;
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
                            return Observable.just(mUserBaseBean);
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
    public UserBaseBean getUserBaseBeanNormal() {
        if (mUserBaseBean != null) {
            return mUserBaseBean;
        }

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
        return mUserInfoApi.getUserInfo(
                mILoginModel.getUserLogin().getUsername(),
                mILoginModel.getUserLogin().getPassword(),
                "query",
                mILoginModel.getCurrentSemester().getYearString(),
                mILoginModel.getCurrentSemester().getSeason() + ""
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<UserInfo>, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(HttpResult<UserInfo> userInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(userInfoHttpResult)) {
                            mUserInfo = userInfoHttpResult.getData();
                            mUserInfo.setUsername(mILoginModel.getUserLogin().getUsername());
                            Realm realm = getRealm();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(mUserInfo);
                                }
                            });

                            RealmResults<Syllabus> results = realm.where(Syllabus.class)
                                    .equalTo("mSemester.season", mILoginModel.getCurrentSemester().getSeason())
                                    .equalTo("mSemester.startYear", mILoginModel.getCurrentSemester().getStartYear())
                                    .findAll();
                            Syllabus syllabus;
                            if (results.size() > 0) {
                                syllabus = realm.copyFromRealm(results.first());
                            } else {
                                syllabus = new Syllabus();
                            }

                            syllabus.convertSyllabus(realm, mUserInfo.getClasses(), mILoginModel.getCurrentSemester());

                            if (!isLogin) {
                                realm.close();
                            }
                            EventBus.getDefault().post(new UpdateUserInfoEvent());
                            return Observable.just(mUserInfo);
                        } else {
                            return Observable.error(new Throwable(userInfoHttpResult.getMessage()));
                        }
                    }
                });
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

    @Override
    public UserInfo getUserInfoNormal() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
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
        return mUserInfo;
    }

    @Override
    public void updateUserInfo(final UserInfo userInfo) {
        mUserInfo = userInfo;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mUserInfo);
            }
        });
    }

    @Override
    public void updateUserBaseBean(UserBaseBean userBaseBean) {
        mUserBaseBean = userBaseBean;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mUserBaseBean);
            }
        });
    }

    private Realm getRealm() {
        if (isLogin) {
            return mRealm;
        } else {
            RealmConfiguration.Builder builder = new RealmConfiguration.Builder(App.getContext())
                    .schemaVersion(App.userVersion)
                    .name(mILoginModel.getUserLogin().getUsername() + ".realm");
            if (App.isDebug) {
                builder.deleteRealmIfMigrationNeeded();
            }
            return Realm.getInstance(builder.build());
        }

    }
}

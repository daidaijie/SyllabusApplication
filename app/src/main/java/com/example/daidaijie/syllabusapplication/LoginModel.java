package com.example.daidaijie.syllabusapplication;

import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class LoginModel implements ILoginModel {

    UserLogin mUserLogin;

    Realm mRealm;

    public LoginModel(Realm realm) {
        mRealm = realm;
    }

    @Override
    public void setUserLogin(UserLogin userLogin) {
        mUserLogin = userLogin;
    }

    @Override
    public UserLogin getUserLogin() {
        return mUserLogin;
    }

    @Override
    public Observable<UserLogin> getUserLoginFromMemory() {
        return Observable.create(new Observable.OnSubscribe<UserLogin>() {
            @Override
            public void call(Subscriber<? super UserLogin> subscriber) {
                subscriber.onNext(mUserLogin);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserLogin> getUserLoginFromDisk() {
        return Observable.create(new Observable.OnSubscribe<UserLogin>() {
            @Override
            public void call(Subscriber<? super UserLogin> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<UserLogin> results = realm.where(UserLogin.class).findAll();
                        if (results.size() != 0) {
                            mUserLogin = realm.copyFromRealm(results.first());
                        }
                        LoggerUtil.e("userlogin", results.size() + "");

                    }
                });
                subscriber.onNext(mUserLogin);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserLogin> getUserLoginFromCache() {
        return Observable.concat(getUserLoginFromMemory(), getUserLoginFromDisk())
                .takeFirst(new Func1<UserLogin, Boolean>() {
                    @Override
                    public Boolean call(UserLogin userLogin) {
                        return userLogin != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveUserLoginToDisk() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
                realm.copyToRealm(mUserLogin);
            }
        });
    }
}

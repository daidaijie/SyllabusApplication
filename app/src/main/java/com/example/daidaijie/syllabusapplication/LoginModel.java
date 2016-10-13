package com.example.daidaijie.syllabusapplication;

import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import org.joda.time.DateTime;

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

    Semester mCurrentSemester;

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
                realm.where(UserLogin.class).findAll().deleteAllFromRealm();
                realm.copyToRealm(mUserLogin);
            }
        });
    }

    @Override
    public void setCurrentSemester(Semester currentSemester) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Semester.class).findAll().deleteAllFromRealm();
                realm.copyToRealm(mCurrentSemester);
            }
        });
        mCurrentSemester = currentSemester;
    }

    @Override
    public Semester getCurrentSemester() {
        if (mCurrentSemester != null) return mCurrentSemester;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Semester> results = mRealm.where(Semester.class).findAll();
                if (results.size() != 0) {
                    mCurrentSemester = realm.copyFromRealm(results.first());
                }
            }
        });
        if (mCurrentSemester == null) {
            int queryYear;
            int querySem;
            DateTime dateTime = DateTime.now();
            int year = dateTime.getYear();
            int month = dateTime.getMonthOfYear();
            if (month > 1 && month < 8) {
                queryYear = year - 1;
                querySem = 2;
            } else if (month == 8) {
                queryYear = year;
                querySem = 3;
            } else {
                if (month > 8) {
                    queryYear = year;
                } else {
                    queryYear = year - 1;
                }
                querySem = 1;
            }
            mCurrentSemester = new Semester(queryYear, querySem);
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Semester.class).findAll().deleteAllFromRealm();
                    realm.copyToRealm(mCurrentSemester);
                }
            });
        }
        return mCurrentSemester;
    }
}

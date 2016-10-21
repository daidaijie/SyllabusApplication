package com.example.daidaijie.syllabusapplication.syllabus;

import android.support.annotation.Nullable;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by daidaijie on 2016/10/19.
 */

public class SyllabusModel implements ISyllabusModel {

    Syllabus mSyllabus;

    ILoginModel mILoginModel;

    IUserModel mIUserModel;

    Realm mRealm;

    public SyllabusModel(ILoginModel ILoginModel, IUserModel IUserModel, Realm realm) {
        mILoginModel = ILoginModel;
        mIUserModel = IUserModel;
        mRealm = realm;
    }

    @Override
    public Observable<Syllabus> getSyllabusFromMemory() {
        return Observable.create(new Observable.OnSubscribe<Syllabus>() {
            @Override
            public void call(Subscriber<? super Syllabus> subscriber) {
                subscriber.onNext(mSyllabus);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Syllabus> getSyllabusFromDisk() {
        return Observable.create(new Observable.OnSubscribe<Syllabus>() {
            @Override
            public void call(final Subscriber<? super Syllabus> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Syllabus> results =
                                realm.where(Syllabus.class)
                                        .equalTo("mSemester.season", mILoginModel.getCurrentSemester().getSeason())
                                        .equalTo("mSemester.startYear", mILoginModel.getCurrentSemester().getStartYear())
                                        .findAll();
                        if (results.size() > 0) {
                            mSyllabus = realm.copyFromRealm(results.first());
                        }

                    }
                });
                if (mSyllabus != null) {
                    mSyllabus.loadLessonFromDisk(mRealm);
                }
                subscriber.onNext(mSyllabus);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Syllabus> getSyllabusFromNet() {
        return Observable.merge(mIUserModel.getUserInfoFromNet(), mIUserModel.getUserBaseBeanFromNet())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<RealmObject, Boolean>() {
                    @Override
                    public Boolean call(RealmObject realmObject) {
                        return realmObject instanceof UserInfo;
                    }
                })
                .flatMap(new Func1<RealmObject, Observable<Syllabus>>() {
                    @Override
                    public Observable<Syllabus> call(RealmObject realmObject) {
                        return getSyllabusFromDisk();
                    }
                });
    }

    @Override
    public Observable<Syllabus> getSyllabusFromCache() {
        return Observable.concat(getSyllabusFromMemory(), getSyllabusFromDisk())
                .takeFirst(new Func1<Syllabus, Boolean>() {
                    @Override
                    public Boolean call(Syllabus syllabus) {
                        return syllabus != null;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getSyllabusNormal(OnGetSuccessCallBack<Syllabus> onGetSuccessCallBack, @Nullable OnGetFailCallBack onGetFailCallBack) {
        if (mSyllabus != null) {
            onGetSuccessCallBack.onGetSuccess(mSyllabus);
            return;
        }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Syllabus> results =
                        realm.where(Syllabus.class)
                                .equalTo("mSemester.season", mILoginModel.getCurrentSemester().getSeason())
                                .equalTo("mSemester.startYear", mILoginModel.getCurrentSemester().getStartYear())
                                .findAll();
                if (results.size() > 0) {
                    mSyllabus = realm.copyFromRealm(results.first());
                }

            }
        });
        if (mSyllabus != null) {
            mSyllabus.loadLessonFromDisk(mRealm);
            onGetSuccessCallBack.onGetSuccess(mSyllabus);
        }
        if (onGetFailCallBack != null) {
            onGetFailCallBack.onGetFail();
        }
    }

}
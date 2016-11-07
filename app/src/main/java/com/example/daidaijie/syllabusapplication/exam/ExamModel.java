package com.example.daidaijie.syllabusapplication.exam;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.bean.ExamInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.retrofitApi.ExamInfoApi;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/14.
 */

public class ExamModel implements IExamModel {

    ExamInfoApi mExamInfoApi;
    Realm mRealm;
    ILoginModel mILoginModel;

    List<Exam> mExams;

    public ExamModel(ExamInfoApi examInfoApi, Realm realm, ILoginModel ILoginModel) {
        mExamInfoApi = examInfoApi;
        mRealm = realm;
        mILoginModel = ILoginModel;
    }

    @Override
    public Observable<List<Exam>> getExamFromMemory() {
        return Observable.create(new Observable.OnSubscribe<List<Exam>>() {
            @Override
            public void call(Subscriber<? super List<Exam>> subscriber) {
                subscriber.onNext(mExams);
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Exam>> getExamFromDisk() {
        return Observable.create(new Observable.OnSubscribe<List<Exam>>() {
            @Override
            public void call(Subscriber<? super List<Exam>> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Exam> results = realm.where(Exam.class)
                                .equalTo("mSemester.startYear", mILoginModel.getCurrentSemester().getStartYear())
                                .equalTo("mSemester.season", mILoginModel.getCurrentSemester().getSeason())
                                .findAll();
                        if (results.size() != 0) {
                            mExams = realm.copyFromRealm(results);
                        }
                    }
                });
                subscriber.onNext(mExams);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Exam>> getExamFromNet() {

        return mExamInfoApi.getExamInfo(
                mILoginModel.getUserLogin().getUsername(),
                mILoginModel.getUserLogin().getPassword(),
                mILoginModel.getCurrentSemester().getYearString(),
                mILoginModel.getCurrentSemester().getSeason() + ""
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<ExamInfo>, Observable<List<Exam>>>() {
                    @Override
                    public Observable<List<Exam>> call(HttpResult<ExamInfo> examInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(examInfoHttpResult)) {
                            mExams = examInfoHttpResult.getData().getExams();
                            saveToDisk();
                            return Observable.just(mExams);
                        } else if (examInfoHttpResult.getMessage().equals("no exams")) {
                            List<Exam> exams = new ArrayList<>();
                            return Observable.just(exams);
                        }
                        return Observable.error(new Throwable(examInfoHttpResult.getMessage()));
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Exam>> getExamFromCache() {
        return Observable.concat(getExamFromMemory(), getExamFromDisk())
                .takeFirst(new Func1<List<Exam>, Boolean>() {
                    @Override
                    public Boolean call(List<Exam> exams) {
                        return exams != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Exam getExamInList(int position) {
        if (mExams != null) {
            return mExams.get(position);
        }

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Exam> results = realm.where(Exam.class)
                        .equalTo("mSemester.startYear", mILoginModel.getCurrentSemester().getStartYear())
                        .equalTo("mSemester.season", mILoginModel.getCurrentSemester().getSeason())
                        .findAll();
                if (results.size() != 0) {
                    mExams = realm.copyFromRealm(results);
                }
            }
        });

        return mExams.get(position);
    }

    private void saveToDisk() {
        if (mExams == null) return;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Exam.class).equalTo("mSemester.startYear", mILoginModel.getCurrentSemester().getStartYear())
                        .equalTo("mSemester.season", mILoginModel.getCurrentSemester().getSeason())
                        .findAll().deleteAllFromRealm();
                for (Exam exam : mExams) {
                    exam.setSemester(mILoginModel.getCurrentSemester());
                    realm.copyToRealm(exam);
                }
            }
        });
    }
}

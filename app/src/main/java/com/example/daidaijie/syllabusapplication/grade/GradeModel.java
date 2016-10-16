package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.GradeBean;
import com.example.daidaijie.syllabusapplication.bean.GradeInfo;
import com.example.daidaijie.syllabusapplication.bean.GradeStore;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;
import com.example.daidaijie.syllabusapplication.retrofitApi.GradeApi;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class GradeModel implements IGradeModel {

    GradeApi mGradeApi;

    GradeStore mGradeStore;

    ILoginModel mILoginModel;

    Realm mRealm;

    public GradeModel(GradeApi gradeApi, ILoginModel ILoginModel, Realm realm) {
        mGradeApi = gradeApi;
        mILoginModel = ILoginModel;
        mRealm = realm;
    }

    @Override
    public Observable<GradeStore> getGradeStoreListFromMemory() {
        return Observable.create(new Observable.OnSubscribe<GradeStore>() {
            @Override
            public void call(Subscriber<? super GradeStore> subscriber) {
                subscriber.onNext(mGradeStore);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<GradeStore> getGradeStoreListFromDisk() {
        return Observable.create(new Observable.OnSubscribe<GradeStore>() {
            @Override
            public void call(Subscriber<? super GradeStore> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<GradeStore> results = realm.where(GradeStore.class).findAll();
                        if (results.size() != 0) {
                            mGradeStore = realm.copyFromRealm(results.first());
                        }
                    }
                });
                subscriber.onNext(mGradeStore);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<GradeStore> getGradeStoreListFromNet() {
        return mGradeApi.getGrade(mILoginModel.getUserLogin().getUsername(),
                mILoginModel.getUserLogin().getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<GradeInfo>, Observable<GradeStore>>() {
                    @Override
                    public Observable<GradeStore> call(HttpResult<GradeInfo> gradeInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(gradeInfoHttpResult)) {
                            convertGradeInfo(gradeInfoHttpResult.getData());
                            return Observable.just(mGradeStore);
                        } else if (gradeInfoHttpResult.getCode() == 200) {
                            return Observable.just(null);
                        }
                        return Observable.error(new Throwable(gradeInfoHttpResult.getMessage()));
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<GradeStore> getGradeStoreListFromCache() {
        return Observable.concat(getGradeStoreListFromMemory(), getGradeStoreListFromDisk())
                .takeFirst(new Func1<GradeStore, Boolean>() {
                    @Override
                    public Boolean call(GradeStore gradeStore) {
                        if (gradeStore != null) {
                            setExpand(gradeStore);
                            gradeStore.converMap();
                        }
                        return gradeStore != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    private void convertGradeInfo(GradeInfo gradeInfo) {
        GradeStore cacheGradeStore = mGradeStore;
        mGradeStore = new GradeStore();
        List<List<GradeBean>> gLists = gradeInfo.getGRADES();
        RealmList<SemesterGrade> mSemesterGrades = new RealmList<>();
        for (int i = gLists.size() - 1; i >= 0; i--) {
            List<GradeBean> gradeBeen = gLists.get(i);
            if (gradeBeen.size() == 0) continue;
            SemesterGrade semesterGrade = new SemesterGrade();
            Semester semester = new Semester(gradeBeen.get(0).getYears(), gradeBeen.get(0).getSemester());
            semesterGrade.setSemester(semester);
            semesterGrade.setGpa(gradeInfo.getSumGpa(i));
            semesterGrade.setCredit(gradeInfo.getSumCredit(i));
            RealmList gradeList = new RealmList();
            gradeList.addAll(gradeBeen);
            semesterGrade.setGradeBeen(gradeList);
            mSemesterGrades.add(semesterGrade);
        }
        mGradeStore.setSemesterGrades(mSemesterGrades);
        mGradeStore.setSize(gradeInfo.getAllSize());
        mGradeStore.setGpa(gradeInfo.getGPA());
        mGradeStore.setCredit(gradeInfo.getAllCredit());

        if (cacheGradeStore != null && cacheGradeStore.getSemesterGradeMap() != null) {
            for (SemesterGrade grades : mGradeStore.getSemesterGrades()) {
                SemesterGrade cacheGrade = cacheGradeStore.getSemesterGradeMap().get(grades.getSemester());
                if (cacheGrade != null) {
                    grades.setExpand(cacheGrade.getExpand());
                }
            }
        }
        setExpand(mGradeStore);
        mGradeStore.converMap();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(GradeStore.class).findAll().deleteAllFromRealm();
                realm.copyToRealm(mGradeStore);
            }
        });
    }

    private void setExpand(GradeStore gradeStore) {
        for (SemesterGrade grades : gradeStore.getSemesterGrades()) {
            if (grades.getExpand() == null) {
                grades.setExpand(false);
            }
        }
    }
}

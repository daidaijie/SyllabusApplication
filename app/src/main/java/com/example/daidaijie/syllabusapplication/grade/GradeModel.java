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
    public Observable<List<SemesterGrade>> getGradeStoreListFromMemory() {
        return Observable.create(new Observable.OnSubscribe<List<SemesterGrade>>() {
            @Override
            public void call(Subscriber<? super List<SemesterGrade>> subscriber) {
                if (mGradeStore == null) {
                    subscriber.onNext(null);
                } else {
                    subscriber.onNext(mGradeStore.getSemesterGrades().subList(0,
                            mGradeStore.getSemesterGrades().size()));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<SemesterGrade>> getGradeStoreListFromDisk() {
        return Observable.create(new Observable.OnSubscribe<List<SemesterGrade>>() {
            @Override
            public void call(Subscriber<? super List<SemesterGrade>> subscriber) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<GradeStore> results = realm.where(GradeStore.class).findAll();
                        if (results.size() != 0) {
                            mGradeStore = realm.copyFromRealm(results.first());
                        }
                    }
                });
                if (mGradeStore == null) {
                    subscriber.onNext(null);
                } else {
                    subscriber.onNext(mGradeStore.getSemesterGrades().subList(0,
                            mGradeStore.getSemesterGrades().size()));
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<SemesterGrade>> getGradeStoreListFromNet() {
        return mGradeApi.getGrade(mILoginModel.getUserLogin().getUsername(),
                mILoginModel.getUserLogin().getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<GradeInfo>, Observable<List<SemesterGrade>>>() {
                    @Override
                    public Observable<List<SemesterGrade>> call(HttpResult<GradeInfo> gradeInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(gradeInfoHttpResult)) {
                            convertGradeInfo(gradeInfoHttpResult.getData());
                            return Observable.just(mGradeStore.getSemesterGrades().subList(0,
                                    mGradeStore.getSemesterGrades().size()));
                        } else if (gradeInfoHttpResult.getCode() == 200) {
                            return Observable.just(null);
                        }
                        return Observable.error(new Throwable(gradeInfoHttpResult.getMessage()));
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<SemesterGrade>> getGradeStoreListFromCache() {
        return Observable.concat(getGradeStoreListFromMemory(), getGradeStoreListFromDisk())
                .takeFirst(new Func1<List<SemesterGrade>, Boolean>() {
                    @Override
                    public Boolean call(List<SemesterGrade> semesterGrades) {
                        return semesterGrades != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    private void convertGradeInfo(GradeInfo gradeInfo) {
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
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(GradeStore.class).findAll().deleteAllFromRealm();
                realm.copyToRealm(mGradeStore);
            }
        });
    }
}

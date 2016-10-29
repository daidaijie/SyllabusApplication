package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.LessonDetailInfo;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.retrofitApi.LessonDetailApi;
import com.example.daidaijie.syllabusapplication.syllabus.ISyllabusModel;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.StringUtil;

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
 * Created by daidaijie on 2016/10/22.
 */

public class ClassmateModel implements IClassmateModel {

    long mLessonID;

    List<StudentInfo> mStudentInfos;

    Realm mRealm;

    LessonDetailApi mLessonDetailApi;

    ISyllabusModel mISyllabusModel;

    public ClassmateModel(ISyllabusModel ISyllabusModel, Realm realm,
                          LessonDetailApi lessonDetailApi, final long lessonID) {
        mISyllabusModel = ISyllabusModel;
        mRealm = realm;
        mLessonDetailApi = lessonDetailApi;
        mLessonID = lessonID;
    }

    @Override
    public void getStuDentInfoNormal(IBaseModel.OnGetSuccessCallBack<List<StudentInfo>> getSuccessCallBack) {
        if (mStudentInfos != null) {
            getSuccessCallBack.onGetSuccess(mStudentInfos);
            return;
        }

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<StudentInfo> results = realm.where(StudentInfo.class)
                        .equalTo("lessonId", mLessonID)
                        .findAll();
                if (results.size() != 0) {
                    mStudentInfos = realm.copyFromRealm(results.sort("number"));
                }
            }
        });
        if (mStudentInfos != null) {
            getSuccessCallBack.onGetSuccess(mStudentInfos);
        }
    }

    @Override
    public void getLessonNormal(final IBaseModel.OnGetSuccessCallBack<Lesson> getLessonSuccessCallBack) {
        mISyllabusModel.getSyllabusNormal(new IBaseModel.OnGetSuccessCallBack<Syllabus>() {
            @Override
            public void onGetSuccess(Syllabus syllabus) {
                getLessonSuccessCallBack.onGetSuccess(syllabus.getLessonByID(mLessonID));
            }
        }, null);
    }

    @Override
    public Observable<List<StudentInfo>> getStudentsFromNet() {
        return mLessonDetailApi.getLessonDetail(mLessonID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<LessonDetailInfo>, Observable<List<StudentInfo>>>() {
                    @Override
                    public Observable<List<StudentInfo>> call(HttpResult<LessonDetailInfo> lessonDetailInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(lessonDetailInfoHttpResult)) {
                            mStudentInfos = lessonDetailInfoHttpResult.getData().getClass_info().getStudent();
                            for (StudentInfo studentInfo : mStudentInfos) {
                                studentInfo.setLessonId(mLessonID);
                            }
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(StudentInfo.class)
                                            .equalTo("lessonId", mLessonID)
                                            .findAll().deleteAllFromRealm();
                                    realm.copyToRealm(mStudentInfos);
                                }
                            });
                            return Observable.just(mStudentInfos);
                        } else {
                            return Observable.error(new Throwable(lessonDetailInfoHttpResult.getMessage()));
                        }
                    }
                });
    }

    @Override
    public void searchStudentsList(final String keyword,
                                   final IBaseModel.OnGetSuccessCallBack<List<StudentInfo>> getSuccessCallBack,
                                   final IBaseModel.OnGetFailCallBack getFailCallBack) {
        if (mStudentInfos == null) {
            getFailCallBack.onGetFail();
            return;
        }

        Observable.from(mStudentInfos)
                .subscribeOn(Schedulers.computation())
                .filter(new Func1<StudentInfo, Boolean>() {
                    @Override
                    public Boolean call(StudentInfo studentInfo) {
                        if (keyword.length() == 0) {
                            return true;
                        }
                        //判断为学号
                        if (StringUtil.isNumberic(keyword)) {
                            if (studentInfo.getNumber().contains(keyword)) {
                                return true;
                            }
                        }
                        //判断性别
                        if (keyword.length() == 1) {
                            if (studentInfo.getGender().equals(keyword)) {
                                return true;
                            }
                        }
                        if (studentInfo.getName().contains(keyword) ||
                                studentInfo.getMajor().contains(keyword)) {
                            return true;
                        }

                        return false;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentInfo>() {

                    List<StudentInfo> mQueryInfo;

                    @Override
                    public void onStart() {
                        super.onStart();
                        mQueryInfo = new ArrayList<>();
                    }

                    @Override
                    public void onCompleted() {
                        getSuccessCallBack.onGetSuccess(mQueryInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getFailCallBack.onGetFail();
                    }

                    @Override
                    public void onNext(StudentInfo studentInfo) {
                        mQueryInfo.add(studentInfo);
                    }
                });
    }
}

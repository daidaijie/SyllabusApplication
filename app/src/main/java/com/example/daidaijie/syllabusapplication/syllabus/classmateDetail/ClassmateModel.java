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

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
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
                        .equalTo("id", mLessonID)
                        .findAll();
                if (results.size() != 0) {
                    mStudentInfos = realm.copyFromRealm(results);
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
                                studentInfo.setId(mLessonID);
                            }
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(StudentInfo.class)
                                            .equalTo("id", mLessonID)
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
}

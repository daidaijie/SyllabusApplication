package com.example.daidaijie.syllabusapplication.grade;

import io.realm.Realm;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class GradePresenter implements GradeContract.presenter {

    Realm mRealm;

    GradeContract.view mView;

    IGradeModel mIGradeModel;
    

    @Override
    public void start() {

    }
}

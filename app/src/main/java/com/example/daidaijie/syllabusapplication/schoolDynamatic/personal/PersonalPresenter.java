package com.example.daidaijie.syllabusapplication.schoolDynamatic.personal;

import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/10/22.
 */

public class PersonalPresenter implements PersonalContract.presenter {

    IUserModel mIUserModel;

    PersonalContract.view mView;

    @Inject
    @PerActivity
    public PersonalPresenter(@LoginUser IUserModel IUserModel, PersonalContract.view view) {
        mIUserModel = IUserModel;
        mView = view;
    }

    @Override
    public void start() {
        UserBaseBean userBaseBean = mIUserModel.getUserBaseBeanNormal();
        mView.showUserBase(userBaseBean);
    }
}

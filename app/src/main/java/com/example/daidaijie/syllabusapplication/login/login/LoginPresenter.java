package com.example.daidaijie.syllabusapplication.login.login;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.UnLoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class LoginPresenter implements LoginContract.presenter {

    LoginContract.view mView;

    ILoginModel mILoginModel;

    IUserModel mIUserModel;

    @Inject
    @PerActivity
    public LoginPresenter(LoginContract.view view, ILoginModel loginModel, @UnLoginUser IUserModel userModel) {
        mView = view;
        mILoginModel = loginModel;
        mIUserModel = userModel;
    }

    @Override
    public void start() {
        mILoginModel.getUserLoginFromCache()
                .subscribe(new Action1<UserLogin>() {
                    @Override
                    public void call(UserLogin userLogin) {
                        if (userLogin != null) {
                            mView.setLogin(userLogin);
                        }
                    }
                });
    }

    @Override
    public void login(String username, String password) {
        UserLogin userLogin = new UserLogin(username, password);
        mILoginModel.setUserLogin(userLogin);
        mIUserModel.getUserBaseBeanFromNet()
                .subscribe(new Subscriber<UserBaseBean>() {
                    @Override
                    public void onCompleted() {
                        mView.toMainView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showFailMessage(e.getMessage().toUpperCase());
                    }

                    @Override
                    public void onNext(UserBaseBean userBaseBean) {
                        mILoginModel.saveUserLoginToDisk();
                    }
                });
    }
}

package com.example.daidaijie.syllabusapplication.login.splash;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class SplashPresenter implements SplashContract.presenter {

    SplashContract.view mView;

    ILoginModel mILoginModel;

    @Inject
    @PerActivity
    public SplashPresenter(SplashContract.view view, ILoginModel ILoginModel) {
        mView = view;
        mILoginModel = ILoginModel;
    }

    @Override
    public void start() {
        mILoginModel.getUserLoginFromCache()
                .subscribe(new Subscriber<UserLogin>() {
                    UserLogin mUserLogin;

                    @Override
                    public void onCompleted() {
                        if (mUserLogin == null) {
                            mView.toLoginView();
                        } else {
                            mView.toMainView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.toLoginView();
                    }

                    @Override
                    public void onNext(UserLogin userLogin) {
                        mUserLogin = userLogin;
                        mILoginModel.setUserLogin(userLogin);
                    }
                });
    }
}

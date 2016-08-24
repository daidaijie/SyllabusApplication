package com.example.daidaijie.syllabusapplication.view;

/**
 * Created by daidaijie on 2016/8/24.
 */
public interface ILoginView extends MvpView, ILoadingView {

    void showLoginSuccess();

    void showLoginFail();
}

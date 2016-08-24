package com.example.daidaijie.syllabusapplication.presenter;

import com.example.daidaijie.syllabusapplication.view.ILoginView;

/**
 * Created by daidaijie on 2016/8/24.
 */
public abstract class ILoginPresenter extends BasePresenter<ILoginView> {

    public abstract void login(String username, String password);

}

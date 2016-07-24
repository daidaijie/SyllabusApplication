package com.example.daidaijie.syllabusapplication.presenter;

import com.example.daidaijie.syllabusapplication.view.MvpView;

/**
 * Created by daidaijie on 2016/7/25.
 * 定义一个含有关联，取消关联View角色的Presenter
 */
public abstract class BasePresenter<T extends MvpView> {

    T mView;

    public void attach(T view){
        mView = view;
    }

    public void detach(){
        mView = null;
    }
}

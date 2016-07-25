package com.example.daidaijie.syllabusapplication.presenter;

import com.example.daidaijie.syllabusapplication.view.ISyllabusMainView;

/**
 * Created by daidaijie on 2016/7/25.
 */
public abstract class ISyllabusMainPresenter extends BasePresenter<ISyllabusMainView>{

    //获取用户信息
    public abstract void getUserInfo();
}

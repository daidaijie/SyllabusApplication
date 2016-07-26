package com.example.daidaijie.syllabusapplication.presenter;

import com.example.daidaijie.syllabusapplication.view.ISyllabusFragmentView;

/**
 * Created by daidaijie on 2016/7/26.
 */
public abstract class ISyllabusFragmentPresenter extends BasePresenter<ISyllabusFragmentView> {

    //展示课表
    public abstract void showSyllabus();

    //获取课表
    public abstract void updateSyllabus();

    //更新用户信息
    public abstract void updateUserInfo();

    //从gson中重新加载课表
    public abstract void reloadSyllabus();

}

package com.example.daidaijie.syllabusapplication.presenter;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.view.IExamListView;

/**
 * Created by daidaijie on 2016/8/4.
 */
public abstract class IExamListPresenter extends BasePresenter<IExamListView>{

    //获取考试列表
    public abstract void getExamList(Context context);
}

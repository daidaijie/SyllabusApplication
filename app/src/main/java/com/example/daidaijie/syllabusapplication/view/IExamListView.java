package com.example.daidaijie.syllabusapplication.view;

import android.content.Context;

/**
 * Created by daidaijie on 2016/8/4.
 */
public interface IExamListView extends MvpView{

    //获取考试列表
    public abstract void getExamList(Context context);
}

package com.example.daidaijie.syllabusapplication.bean;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/7/19.
 * 一个课程格子类,每天每个时间段表示一个格子
 */
public class SyllabusGrid extends RealmObject {

    //储存当前格子上的课程列表
    private RealmList<LessonID> mLessons;

    public SyllabusGrid() {
        mLessons = new RealmList<>();
    }

    public RealmList<LessonID> getLessons() {
        return mLessons;
    }

    public void setLessons(RealmList<LessonID> lessons) {
        mLessons = lessons;
    }
}

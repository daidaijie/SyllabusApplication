package com.example.daidaijie.syllabusapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/7/19.
 * 一个课程格子类,每天每个时间段表示一个格子
 */
public class SyllabusGrid {

    //储存当前格子上的课程列表
    private List<Integer> mLessons;

    public SyllabusGrid(){
        mLessons = new ArrayList<>();
    }

    public List<Integer> getLessons() {
        return mLessons;
    }

    public void setLessons(List<Integer> lessons) {
        mLessons = lessons;
    }
}

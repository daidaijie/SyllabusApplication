package com.example.daidaijie.syllabusapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/7/19.
 * 课表类，储存一个课表
 */
public class Syllabus {

    private List<List<SyllabusGrid>> mSyllabusGrids;

    public Syllabus(){
        mSyllabusGrids = new ArrayList<>(new ArrayList());
        for (int i = 0; i < 7; i++) {
            List<SyllabusGrid> daySyllabusGrids = new ArrayList<>();
            for (int j = 0; j < 13; j++) {
                daySyllabusGrids.add(new SyllabusGrid());
            }
            mSyllabusGrids.add(daySyllabusGrids);
        }
    }

    public List<List<SyllabusGrid>> getSyllabusGrids() {
        return mSyllabusGrids;
    }

    public void setSyllabusGrids(List<List<SyllabusGrid>> syllabusGrids) {
        mSyllabusGrids = syllabusGrids;
    }
}

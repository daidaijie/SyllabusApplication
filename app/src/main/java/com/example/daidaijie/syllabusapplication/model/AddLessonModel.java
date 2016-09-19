package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.bean.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/9/19.
 */
public class AddLessonModel {

    public Lesson mLesson;

    public List<SelectTime> mTimes;

    private static AddLessonModel ourInstance = new AddLessonModel();

    public static AddLessonModel getInstance() {
        return ourInstance;
    }

    private AddLessonModel() {
    }

    public static class SelectTime {
        public List<Boolean> selectWeeks;
        public List<List<Boolean>> mSelectTimes;

        public SelectTime() {
            selectWeeks = new ArrayList<>();
            mSelectTimes = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                selectWeeks.add(false);
            }

            for (int i = 0; i < 7; ++i) {
                List<Boolean> selectTimeList = new ArrayList<>();
                for (int j = 0; j < 13; j++) {
                    selectTimeList.add(false);
                }
                mSelectTimes.add(selectTimeList);
            }

        }
    }
}

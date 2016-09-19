package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;

import java.lang.annotation.ElementType;
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

        public String toHtmlSelectTimeString() {
            String[] weekString = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                boolean haveTimeOfWeek = false;
                StringBuilder weekSb = new StringBuilder();
                for (int j = 0; j < 13; j++) {
                    if (mSelectTimes.get(i).get(j)) {
                        haveTimeOfWeek = true;
                        weekSb.append(Syllabus.time2char(j + 1));
                    }
                }
                if (haveTimeOfWeek) {
                    sb.append("<b>" + weekString[i] + "</b>");
                    sb.append(" ");
                    sb.append(weekSb);
                    sb.append("<br/>");
                }
            }
            return sb.toString();
        }

        public String toHtmlString() {
            StringBuilder sb = new StringBuilder();
            boolean isFound = false;
            for (int i = 0; i < 16; i++) {
                if (selectWeeks.get(i)) {
                    if (!isFound) {
                        sb.append("第");
                        sb.append("" + (i + 1));
                        isFound = true;
                    } else {
                        sb.append(", " + (i + 1));
                    }
                }
            }
            if (isFound) {
                sb.append("周<br/>");
            }

            sb.append(toHtmlSelectTimeString());
            return sb.toString();
        }

    }
}

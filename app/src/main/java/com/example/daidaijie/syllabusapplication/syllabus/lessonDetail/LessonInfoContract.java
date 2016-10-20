package com.example.daidaijie.syllabusapplication.syllabus.lessonDetail;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Lesson;

/**
 * Created by daidaijie on 2016/10/20.
 */

public interface LessonInfoContract {

    interface presenter extends BasePresenter {

    }

    interface view extends BaseView<presenter> {
        void showData(Lesson lesson);
    }
}

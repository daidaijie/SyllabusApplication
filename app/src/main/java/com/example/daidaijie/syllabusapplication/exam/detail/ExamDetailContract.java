package com.example.daidaijie.syllabusapplication.exam.detail;

import android.text.SpannableStringBuilder;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Exam;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ExamDetailContract {

    interface presenter extends BasePresenter {
        void setUpstate();
    }

    interface view extends BaseView<presenter> {
        void showData(Exam exam);

        void showState(SpannableStringBuilder state);
    }
}

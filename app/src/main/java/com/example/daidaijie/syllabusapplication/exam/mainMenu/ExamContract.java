package com.example.daidaijie.syllabusapplication.exam.mainMenu;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ExamContract {

    interface presenter extends BasePresenter {
        void loadData();

    }

    interface view extends BaseView<presenter> {

        void showData(List<Exam> exams);

        void showSuccessMessage(String msg);

        void showInfoMessage(String msg);

        void showFailMessage(String msg);

        void showLoading(boolean isShow);
    }
}

package com.example.daidaijie.syllabusapplication.exam.mainMenu;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Exam;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ExamContract {

    interface presenter extends BasePresenter {
        void loadData();

        void setIsLoaded(boolean isLoaded);
    }

    interface view extends BaseView<presenter> {

        void showData(List<Exam> exams);

        void showSuccessMessage(String msg);

        void showInfoMessage(String msg);

        void showFailMessage(String msg);

        void showFresh(boolean isShow);

        void setIsLoaded(boolean isLoaded);

    }
}

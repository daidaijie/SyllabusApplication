package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.GradeStore;
import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface GradeContract {

    interface presenter extends BasePresenter {
        void loadData();
    }

    interface view extends BaseView<presenter> {
        void showSuccessMessage(String msg);

        void showInfoMessage(String msg);

        void showFailMessage(String msg);

        void showFresh(boolean isShow);

        void setData(List<SemesterGrade> semesterGrades);

        void setHeader(GradeStore gradeStore);
    }

}

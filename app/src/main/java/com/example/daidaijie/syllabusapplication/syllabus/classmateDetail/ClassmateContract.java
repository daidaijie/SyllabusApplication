package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/22.
 */

public interface ClassmateContract {

    interface presenter extends BasePresenter {
        void loadData();

        void search(String keyword);
    }

    interface view extends BaseView<presenter> {
        void setTitleBg(int color);

        void showData(List<StudentInfo> mStudentInfos);

        void showLoading(boolean isShow);

        void showFailMessage(String msg);

        void showWarningMessage(String msg);
    }

}

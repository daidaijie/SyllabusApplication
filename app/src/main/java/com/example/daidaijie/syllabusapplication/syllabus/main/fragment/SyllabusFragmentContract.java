package com.example.daidaijie.syllabusapplication.syllabus.main.fragment;

import android.graphics.Bitmap;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

import org.joda.time.LocalDate;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface SyllabusFragmentContract {

    interface presenter extends BasePresenter {
        void loadData();

        void saveSyllabus(Bitmap syllabusBitmap, Bitmap timeBitmap, Bitmap dayBitmap);
    }

    interface view extends BaseView<presenter> {

        void showSyllabus(Syllabus syllabus);

        void showLoading(boolean isShow);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);
    }
}

package com.example.daidaijie.syllabusapplication.syllabus.main.activity;

import android.graphics.Bitmap;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;

import org.joda.time.LocalDate;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface SyllabusContract {

    interface presenter extends BasePresenter {

        void loadUserInfo();

        void loadWallpaper();

        void setWallpaper(int deviceWidth, int deviceHeight);

        void moveToNowWeek(long startTime);

        void settingWeek(LocalDate date, int week);

    }

    interface view extends BaseView<presenter> {
        void showUserInfo(UserInfo userInfo);

        void showFailMessage(String msg);

        void showInfoMessage(String msg);

        void showSuccessMessage(String msg);

        void setBackground(Bitmap bitmap);

        void showSelectWeekLayout(boolean isShow);

        void showSemester(Semester semester);

        void moveToWeek(int week);
    }
}

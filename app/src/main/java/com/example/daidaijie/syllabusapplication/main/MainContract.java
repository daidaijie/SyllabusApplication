package com.example.daidaijie.syllabusapplication.main;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Banner;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.other.update.IDownloadView;
import com.example.daidaijie.syllabusapplication.other.update.UpdateInstaller;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface MainContract {

    interface presenter extends BasePresenter {

        void showUserInfo();

        void setCurrentSemester(Semester semester);

        void showSemesterSelect();
    }

    interface view extends BaseView<presenter>, IDownloadView, UpdateInstaller {

        interface OnUpdateClickCallBack {
            void onUpdate();
        }

        void setBannerPage(List<Banner> banners);

        void showUserInfo(UserInfo mUserInfo);

        void showSemester(Semester semester);

        void showInfoMessage(String msg);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);

        void setCurrentSemester(Semester semester);

        void showUpdateInfo(String updateInfo, OnUpdateClickCallBack onUpdateClickCallBack);
    }

}

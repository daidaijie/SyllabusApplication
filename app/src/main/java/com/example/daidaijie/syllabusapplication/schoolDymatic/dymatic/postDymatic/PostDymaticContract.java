package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface PostDymaticContract {

    interface presenter extends BasePresenter {
        void selectPhoto();

        void unSelectPhoto(int position);

        boolean isNonePhoto();

        void postContent(String msg, String source, String url, String locate, boolean hasTime);

        void setTime(boolean isStart, int year, int month, int day, int hour, int minute);

        void selectTime(boolean isStart);

    }

    interface view extends BaseView<presenter> {

        void selectTime(final boolean isStart, int year, int month, int day, int hour, int minute);

        void showLoading(boolean isShow);

        void setUpFlow(List<String> PhotoImgs);

        void showFailMessage(String msg);

        void showWarningMessage(String msg);

        void setStartTimeString(String timeString);

        void setEndTimeString(String timeString);

        void onPostFinishCallBack();
    }
}

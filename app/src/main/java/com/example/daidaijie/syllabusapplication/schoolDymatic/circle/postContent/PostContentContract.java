package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.postContent;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface PostContentContract {

    interface presenter extends BasePresenter {
        void selectPhoto();

        void unSelectPhoto(int position);

        boolean isNonePhoto();

        void postContent(String msg, String source);

    }

    interface view extends BaseView<presenter> {
        void showLoading(boolean isShow);

        void setUpFlow(List<String> PhotoImgs);

        void showFailMessage(String msg);

        void showWarningMessage(String msg);

        void onPostFinishCallBack();
    }
}

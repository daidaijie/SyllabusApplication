package com.example.daidaijie.syllabusapplication.officeAutomation.oaDetail;

import android.graphics.Bitmap;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.OAFileBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface OADetailContract {

    interface presenter extends BasePresenter {
        void loadData();

        void screenShot();
    }

    interface view extends BaseView<presenter> {
        void showView(String webViewString);

        void showOAFile(List<OAFileBean> oaFileBeen);

        Bitmap captureScreen();

        void showFileLoadFailMessage(String msg);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);
    }

}

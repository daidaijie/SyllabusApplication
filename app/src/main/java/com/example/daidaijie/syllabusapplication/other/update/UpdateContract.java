package com.example.daidaijie.syllabusapplication.other.update;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;

/**
 * Created by daidaijie on 2016/10/23.
 */

public interface UpdateContract {

    interface presenter extends BasePresenter {
    }


    interface view extends BaseView<presenter>, IDownloadView, UpdateInstaller{
        void showFailMessage(String msg);

        void showInfoMessage(String msg);

        void showLoading(boolean isShow);

        void showInfo(UpdateInfoBean updateInfoBean);

    }

}

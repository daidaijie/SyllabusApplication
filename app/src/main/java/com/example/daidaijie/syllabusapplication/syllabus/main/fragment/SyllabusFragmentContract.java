package com.example.daidaijie.syllabusapplication.syllabus.main.fragment;

import android.graphics.Bitmap;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface SyllabusFragmentContract {

    interface presenter extends BasePresenter {
        void loadData();

        void saveSyllabus(Bitmap syllabusBitmap, Bitmap timeBitmap, Bitmap dayBitmap);
    }

    interface view extends BaseView<presenter>, OnSyllabusFragmentCallBack {

        void showSyllabus(Syllabus syllabus);

        void showLoading(boolean isShow);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);

        void loadData();
    }

    interface OnSyllabusFragmentCallBack {
        void onLoadStart();

        void onLoadEnd(boolean success);
    }
}

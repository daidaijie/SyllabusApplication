package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface LibraryContract {

    interface presenter extends BasePresenter {
        void loadData();

    }

    interface view extends BaseView<presenter> {

        void showRefresh(boolean isShow);

        void showFailMessage(String msg);

        void showData(List<LibraryBean> libraryBeen);
    }
}

package com.example.daidaijie.syllabusapplication.takeout.searchDish;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface SearchTakeOutContract {

    interface presenter extends BasePresenter {
        void search(String keyWord);
    }

    interface view extends BaseView<presenter> {
        void showFailMessage(String msg);
    }
}

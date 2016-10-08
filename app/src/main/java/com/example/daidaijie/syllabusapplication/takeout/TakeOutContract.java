package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface TakeOutContract {

    public interface TakeOutPresenter extends BasePresenter {
        void loadData();
    }

    public interface TakeOutView extends BaseView<TakeOutPresenter> {
        void showFailMessage(String msg);
    }
}

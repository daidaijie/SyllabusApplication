package com.example.daidaijie.syllabusapplication.login.splash;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface SplashContract {

    interface presenter extends BasePresenter {
    }

    interface view extends BaseView<presenter> {
        void toLoginView();

        void toMainView();
    }

}

package com.example.daidaijie.syllabusapplication.login.login;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface LoginContract {

    interface presenter extends BasePresenter {
        void login(String username,String password);
    }

    interface view extends BaseView<presenter> {
        void showLoading(boolean isShow);

        void setLogin(UserLogin userLogin);

        void toMainView();

        void showFailMessage(String msg);
    }

}

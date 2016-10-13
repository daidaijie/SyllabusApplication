package com.example.daidaijie.syllabusapplication.login.login;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

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

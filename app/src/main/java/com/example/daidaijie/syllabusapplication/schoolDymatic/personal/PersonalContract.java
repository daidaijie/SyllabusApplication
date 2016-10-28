package com.example.daidaijie.syllabusapplication.schoolDymatic.personal;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;

/**
 * Created by daidaijie on 2016/10/22.
 */

public interface PersonalContract {

    interface presenter extends BasePresenter {

        void selectHeadImg();

        void pushData(String nickName,String profile);
    }

    interface view extends BaseView<presenter> {

        void showUserBase(UserBaseBean userBaseBean);

        void showHead(String uri);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);
    }

}

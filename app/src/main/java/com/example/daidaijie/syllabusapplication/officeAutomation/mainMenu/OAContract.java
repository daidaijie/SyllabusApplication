package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.OABean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface OAContract {

    interface presenter extends BasePresenter {
        void loadData();

        void setRead(OABean oaBean, boolean isRead);
    }

    interface view extends BaseView<presenter> {
        void showRefresh(boolean isShow);

        void showFailMessage(String msg);

        void showData(List<OABean> oaBeen);
    }

}

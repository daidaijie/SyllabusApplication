package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
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

package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface TakeOutDetailContract {

    interface presenter extends BasePresenter {
        void loadData();
    }

    interface view extends BaseView<presenter> {
        void showFailMessage(String msg);

        void showRefresh(boolean isShow);

        void setUpTakeOutInfo(TakeOutInfoBean takeOutInfoBean);
    }
}

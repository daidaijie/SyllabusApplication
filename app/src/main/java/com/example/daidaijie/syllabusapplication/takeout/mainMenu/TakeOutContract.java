package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface TakeOutContract {

    interface presenter extends BasePresenter {
        void loadDataFromNet();
    }

    interface view extends BaseView<presenter> {
        void showFailMessage(String msg);

        void showData(List<TakeOutInfoBean> mTakeOutInfoBeen);

        void showRefresh(boolean isShow);
    }
}

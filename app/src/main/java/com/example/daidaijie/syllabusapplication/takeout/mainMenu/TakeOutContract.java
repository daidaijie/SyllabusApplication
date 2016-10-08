package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface TakeOutContract {

    interface TakeOutPresenter extends BasePresenter {
        void loadData();
    }

    interface TakeOutView extends BaseView<TakeOutPresenter> {
        void showFailMessage(String msg);

        void showData(List<TakeOutInfoBean> mTakeOutInfoBeen);

        void showRefresh(boolean isShow);
    }
}

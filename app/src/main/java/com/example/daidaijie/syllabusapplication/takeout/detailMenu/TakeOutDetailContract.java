package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import android.content.Intent;

import com.example.daidaijie.syllabusapplication.BasePresenter;
import com.example.daidaijie.syllabusapplication.BaseView;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface TakeOutDetailContract {

    interface presenter extends BasePresenter {
        void loadData();

        void addDish(int position);

        void reduceDish(int position);

        void showPopWindows();

        void toSearch();

        void showPrice();
    }

    interface view extends BaseView<presenter> {
        void showFailMessage(String msg);

        void showRefresh(boolean isShow);

        void setUpTakeOutInfo(TakeOutInfoBean takeOutInfoBean);

        void setMenuList(TakeOutInfoBean takeOutInfoBean);

        void showPrice(TakeOutBuyBean takeOutBuyBean);

        void showPopWindows(TakeOutInfoBean takeOutInfoBean);

        void toSearch(String objectID);

    }
}

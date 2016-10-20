package com.example.daidaijie.syllabusapplication.takeout.searchMenu;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface SearchTakeOutContract {

    interface presenter extends BasePresenter {
        void search(String keyWord);

        void addDish(int position);

        void reduceDish(int position);

        void showPopWindows();
    }

    interface view extends BaseView<presenter> {
        void showFailMessage(String msg);

        void showInput();

        void hideInput();

        void setUpTakeOutInfo(final TakeOutInfoBean takeOutInfoBean);

        void showSearchResult(TakeOutBuyBean takeOutBuyBean, List<Dishes> dishes, String keyword);

        void showPopWindows(TakeOutInfoBean takeOutInfoBean);

        void showPrice(TakeOutBuyBean takeOutBuyBean);
    }
}

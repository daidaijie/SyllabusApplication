package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ITakeOutModel {

    public interface OnLoadListener {
        void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen);

        void onLoadFail();
    }

    void loadDataFromNet(OnLoadListener onLoadListener);


    void loadDataFromDist(OnLoadListener onLoadListener);
}

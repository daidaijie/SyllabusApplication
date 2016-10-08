package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ITakeOutModel {

    interface OnLoadListener {
        void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen);

        void onLoadFail(String msg);
    }

    void loadDataFromNet(OnLoadListener onLoadListener);


    void loadDataFromDist(OnLoadListener onLoadListener);
}

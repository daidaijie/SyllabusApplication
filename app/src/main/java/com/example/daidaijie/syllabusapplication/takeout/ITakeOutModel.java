package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ITakeOutModel {


    /**
     * 多个的获取
     */
    interface OnLoadListener {
        void onLoadSuccess(List<TakeOutInfoBean> takeOutInfoBeen);

        void onLoadFail(String msg);
    }

    void loadDataFromNet(OnLoadListener onLoadListener);

    void loadDataFromDist(OnLoadListener onLoadListener);

    void loadDataFromMemory(OnLoadListener onLoadListener);

    /**
     * 单个的获取
     */
    interface OnLoadItemListener {
        void onLoadSuccess(TakeOutInfoBean takeOutInfoBean);

        void onLoadFail(String msg);
    }

    void loadItemFromNet(String objectID, OnLoadItemListener onLoadItemListener);

    void loadItemFromDist(String objectID, OnLoadItemListener onLoadItemListener);

    void loadItemFromMemory(String objectID, OnLoadItemListener onLoadItemListener);

}

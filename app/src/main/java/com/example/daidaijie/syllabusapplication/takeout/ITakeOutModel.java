package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;
import java.util.PriorityQueue;

import rx.Observable;

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

    Observable<List<TakeOutInfoBean>> getDataFromMemory();

    Observable<List<TakeOutInfoBean>> getDataFromDist();

    Observable<List<TakeOutInfoBean>> getDataFromNet();

    Observable<List<TakeOutInfoBean>> getData();


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
